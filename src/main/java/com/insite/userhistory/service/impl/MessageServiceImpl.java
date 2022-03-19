package com.insite.userhistory.service.impl;

import com.insite.userhistory.dto.MessageDto;
import com.insite.userhistory.exception.NotFoundException;
import com.insite.userhistory.model.Client;
import lombok.AllArgsConstructor;
import com.insite.userhistory.model.Message;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import com.insite.userhistory.repository.MessageRepository;
import com.insite.userhistory.repository.ClientRepository;
import com.insite.userhistory.service.MessageService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final AuthServiceImpl authService;
    private final ClientRepository userRepository;
    private final MessageRepository messageRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<String> addMessage(String auth, MessageDto messageDto) {
        Client user = userRepository.findByName(messageDto.getUserName())
                .orElseThrow(NotFoundException::new);

        authService.validate(auth, messageDto.getUserName());

        String text = messageDto.getText();

        if (text.matches("^history [0-9]+$")) {
            int limit = Integer.parseInt(text.split(" ")[1]);
            return messageRepository.findAllByClientId(user.getId(),
                    PageRequest.of(0, limit, Sort.Direction.ASC, "text"))
                    .stream()
                    .map(Message::getText)
                    .collect(Collectors.toList());
        }

        Message message = new Message()
                .setText(text)
                .setClientId(user.getId());
        messageRepository.save(message);

        return null;
    }
}
