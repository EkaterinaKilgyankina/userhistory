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
        ClientDto clientDto = testRestTemplate.postForObject("/registrations", userDto, ClientDto.class);

        //then
        assertThat(clientDto)
                .matches(e -> e.getName().equals(userDto.getName()))
                .matches(e -> !e.getPassword().isEmpty());
    }

    @Test
    void whenCreateUserExistsThenThrowExc() {
        //given
        Client client = new Client()
                .setName("testUser2")
                .setPassword("testSecret21!");
        repository.save(client);

        ClientDto userDto = new ClientDto()
                .setName("testUser2")
                .setPassword("testSecret21!");
        //when
        ResponseEntity<ErrorMessage> response = testRestTemplate
                .postForEntity("/registrations", userDto, ErrorMessage.class);
        //then
        Assertions.assertThat(response)
                .matches(e -> e.getStatusCodeValue() == 400)
                .extracting(HttpEntity::getBody)
                .matches(e -> e.getMessage().equals("object already exists"));
    }
}