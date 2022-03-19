package com.insite.userhistory.integration;

import com.insite.userhistory.PostgresContainer;
import com.insite.userhistory.dto.ClientDto;
import com.insite.userhistory.dto.TokenDto;
import com.insite.userhistory.exception.ErrorMessage;
import com.insite.userhistory.model.Client;
import com.insite.userhistory.repository.ClientRepository;
import com.insite.userhistory.service.impl.JwtComponent;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Sql(
//        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
//        scripts = "classpath:/sql/reset_tables.sql",
//        config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
public class AuthenticateControllerTest {

    @DynamicPropertySource
    public static void properties(final DynamicPropertyRegistry registry) {
        PostgresContainer.properties(registry);
    }

    @Autowired
    ClientRepository repository;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    JwtComponent jwtComponent;

    @Test
    void whenAuthClientThenOk() {
        //given
        Client client = new Client()
                .setName("testAuthName")
                .setPassword("testAuthSecret1!");
        repository.save(client);

        ClientDto clientDto = new ClientDto()
                .setName("testAuthName")
                .setPassword("testAuthSecret1!");

        //when
        TokenDto tokenDto = testRestTemplate.postForObject("/authentications", clientDto, TokenDto.class);

        //then
        Assertions.assertThat(jwtComponent.decodeJWT(tokenDto.getToken()).getSubject()).isEqualTo(client.getName());
    }

    @Test
    void whenAuthNewClientThenThrowExc() {
        //given
        Client client = new Client()
                .setName("testAuthName1")
                .setPassword("testAuthSecret1!1");
        repository.save(client);

        ClientDto clientDto = new ClientDto()
                .setName("testAuthName")
                .setPassword("testAuthSecret1!");

        //when
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .postForEntity("/authentications", clientDto, ErrorMessage.class);

        //then
        Assertions.assertThat(response)
                .matches(e -> e.getStatusCodeValue() == 404)
                .extracting(HttpEntity::getBody)
                .matches(e -> e.getMessage().equals("object not found"));
    }

    @Test
    void whenAuthClientAndPasswordIsNotValidThrowExc() {
       //given
        ClientDto clientDto = new ClientDto()
                .setName("testClient")
                .setPassword("testSecret");

        //when
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .postForEntity("/authentications", clientDto, ErrorMessage.class);

        //then
        Assertions.assertThat(response)
                .matches(e -> e.getStatusCodeValue() == 400)
                .extracting(HttpEntity::getBody)
                .matches(e -> e.getMessage().equals("password must contain letters (upper and lower case), digits and special symbols"));
    }

    @Test
    void whenAuthClientAndNameIsNotMissingThrowExc() {
        //given
        ClientDto clientDto = new ClientDto()
                .setPassword("testSecret31!");

        //when
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .postForEntity("/authentications", clientDto, ErrorMessage.class);

        //then
        Assertions.assertThat(response)
                .matches(e -> e.getStatusCodeValue() == 400)
                .extracting(HttpEntity::getBody)
                .matches(e -> e.getMessage().equals("name is mandatory"));
    }
}
