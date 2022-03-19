package com.insite.userhistory.controller;

import com.insite.userhistory.dto.MessageDto;
import com.insite.userhistory.dto.ObjectforMessages;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.insite.userhistory.service.MessageService;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<ObjectforMessages> addMessage(
            @RequestHeader(name = "Authorization") String auth,
            @RequestBody @Valid MessageDto message
    ) {
        List<String> strings = messageService.addMessage(auth, message);
        if (strings == null) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .build();
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ObjectforMessages().setMessages(strings));
        }
    }
}
