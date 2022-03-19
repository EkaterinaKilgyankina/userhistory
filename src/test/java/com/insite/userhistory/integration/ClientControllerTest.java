package com.insite.userhistory.integration;

import com.insite.userhistory.PostgresContainer;
import com.insite.userhistory.dto.ClientDto;
import com.insite.userhistory.exception.ErrorMessage;
import com.insite.userhistory.model.Client;
import com.insite.userhistory.repository.ClientRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Sql(
//        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
//        scripts = "classpath:/sql/reset_tables.sql",
//        config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
public class ClientControllerTest {

    @DynamicPropertySource
    public static void properties(final DynamicPropertyRegistry registry) {
        PostgresContainer.properties(registry);
    }

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ClientRepository repository;

    @Test
    void whenCreateUserThenOk() {
        //given
        ClientDto userDto = new ClientDto()
                .setName("testUser")
                .setPassword("testSecret1!");

        //when
        Client client = testRestTemplate.postForObject("/registrations", userDto, Client.class);

        //then
        assertThat(client)
                .matches(e -> e.getName().equals(userDto.getName()))
                .matches(e -> !e.getPassword().isEmpty());
    }

    @Test
    void whenCreateUserExistsThenThrowExc() {
        //given
        Client client = new Client()
                .setName("testUser")
                .setPassword("testSecret1!");
        repository.save(client);

        ClientDto userDto = new ClientDto()
                .setName("testUser")
                .setPassword("testSecret1!");

        //when
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .postForEntity("/registrations", userDto, ErrorMessage.class);

        //then
        Assertions.assertThat(response)
                .matches(e -> e.getStatusCodeValue() == 400)
                .extracting(HttpEntity::getBody)
                .matches(e -> e.getMessage().equals("object already exists"));
    }

//
//    @Test
//    void whenCreateClientAndPasswordIsNotValidThrowExc() {
//        //given
//        ClientDto userDto = new ClientDto()
//                .setName("testUser")
//                .setPassword("testSecret");
//
//        //when
//        ResponseEntity<ErrorMessage> response = testRestTemplate
//                .postForEntity("/registrations", userDto, ErrorMessage.class);
//
//        //then
//        Assertions.assertThat(response)
//                .matches(e -> e.getStatusCodeValue() == 400)
//                .extracting(HttpEntity::getBody)
//                .matches(e -> e.getMessage()
//                        .equals("password must contain letters (upper and lower case), digits and special symbols"));
//    }

//    @Test
//    void whenCreateClientAndNameIsMissingThrowExc() {
//        //given
//        ClientDto clientDto = new ClientDto()
//                .setPassword("testSecret31!");
//
//        //when
//        ResponseEntity<ErrorMessage> response = testRestTemplate
//                .postForEntity("/registrations", clientDto, ErrorMessage.class);
//
//        //then
//        Assertions.assertThat(response)
//                .matches(e -> e.getStatusCodeValue() == 400)
//                .extracting(HttpEntity::getBody)
//                .matches(e -> e.getMessage().equals("name is mandatory"));
//    }
}