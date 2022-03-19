package com.insite.userhistory.integration;

import com.insite.userhistory.PostgresContainer;
import com.insite.userhistory.dto.MessageDto;
import com.insite.userhistory.dto.ObjectforMessages;
import com.insite.userhistory.exception.ErrorMessage;
import com.insite.userhistory.model.Client;
import com.insite.userhistory.model.Message;
import com.insite.userhistory.repository.ClientRepository;
import com.insite.userhistory.repository.MessageRepository;
import com.insite.userhistory.service.impl.JwtComponent;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//TODO очистка базы - нужна ли??
//@Sql(
//        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
//        scripts = "classpath:/sql/reset_tables.sql",
//        config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
public class MessageControllerTest {


    @DynamicPropertySource
    public static void properties(final DynamicPropertyRegistry registry) {
        PostgresContainer.properties(registry);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private JwtComponent jwtComponent;

    @Test
    void whenSendAMessageAndClientExistThanStatusIsCreated() {
        //given
        Client client = new Client()
                .setName("testClient")
                .setPassword("testSecret1!");
        clientRepository.save(client);

        String token = "Bearer " + jwtComponent.createJWT(client.getName());

        MessageDto messageDto = new MessageDto()
                .setUserName(client.getName())
                .setText("testText");

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", token);

        HttpEntity<MessageDto> httpEntity = new HttpEntity<>(messageDto, headers);

        //when
        ResponseEntity<Object> response = restTemplate.postForEntity("/messages", httpEntity, null);

        //then
        Assertions.assertThat(response)
                .matches(e->e.getStatusCode().equals(HttpStatus.CREATED));
    }

    @Test
    //TODO если он уже авторизовался через токен - как его может не быть в базе?
    void whenSendAMessageAndClientNotExistThanThrowExc() {
        MessageDto messageDto = new MessageDto()
                .setUserName("noneExistClient")
                .setText("testText");

//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//        headers.add("Authorization", token);

//        HttpEntity<MessageDto> httpEntity = new HttpEntity<>(messageDto, headers);

//        ResponseEntity<Object> response = restTemplate.postForEntity("/messages", httpEntity, null);
//
//        Assertions.assertThat(response)
//                .matches(e->e.getStatusCode().equals(HttpStatus.CREATED));
    }

    @Test
    void whenSendAMessageAndClientExistAndHistoryRequestThanStatusIsOk() {
        //given
        Client client = new Client()
                .setName("testClient")
                .setPassword("testSecret1!");

        Client savedClient = clientRepository.save(client);

        String token = "Bearer " + jwtComponent.createJWT(client.getName());

        Message message1 = new Message()
                .setClientId(savedClient.getId())
                .setText("firstTestMessage");
        messageRepository.save(message1);

        Message message2 = new Message()
                .setClientId(savedClient.getId())
                .setText("secondTextMessage");
        messageRepository.save(message2);

        MessageDto messageDto = new MessageDto()
                .setUserName(client.getName())
                .setText("history 2");

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", token);

        HttpEntity<MessageDto> httpEntity = new HttpEntity<>(messageDto, headers);
        List<String> messages = new ArrayList<>();

        //when
        ObjectforMessages responseObject = restTemplate.postForObject("/messages", httpEntity, ObjectforMessages.class);

        //then
        Assertions.assertThat(responseObject)
                .matches(e->e.getMessages().size() == 2)
                .matches(e -> e.getMessages().get(0).equals(message1.getText()))
                .matches(e -> e.getMessages().get(1).equals(message2.getText()));
    }

    @Test
    void whenSendAMessageAndClientNameIsMissingThrowEsc() {
        //given
        String token = "Bearer " + jwtComponent.createJWT("test");

        MessageDto messageDto = new MessageDto()
                .setText("testText");

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", token);

        HttpEntity<MessageDto> httpEntity = new HttpEntity<>(messageDto, headers);

        //when
        ResponseEntity<ErrorMessage> response = restTemplate.postForEntity("/messages", httpEntity, ErrorMessage.class);

        //then
        Assertions.assertThat(response)
                .matches(e -> e.getStatusCodeValue() == 400)
                .extracting(HttpEntity::getBody)
                .matches(e -> e.getMessage().equals("name is mandatory"));
    }

    @Test
    void whenSendAMessageAndTextIsMissingThrowEsc() {
        //given
        String token = "Bearer " + jwtComponent.createJWT("test");

        MessageDto messageDto = new MessageDto()
                .setUserName("test");

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", token);

        HttpEntity<MessageDto> httpEntity = new HttpEntity<>(messageDto, headers);

        //when
        ResponseEntity<ErrorMessage> response = restTemplate.postForEntity("/messages", httpEntity, ErrorMessage.class);

        //then
        Assertions.assertThat(response)
                .matches(e -> e.getStatusCodeValue() == 400)
                .extracting(HttpEntity::getBody)
                .matches(e -> e.getMessage().equals("message is mandatory"));
    }
}