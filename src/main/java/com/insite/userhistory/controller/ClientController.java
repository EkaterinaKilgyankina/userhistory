package com.insite.userhistory.controller;

import com.insite.userhistory.dto.ClientDto;
import com.insite.userhistory.mapper.ClientDtoMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.insite.userhistory.service.impl.ClientServiceImpl;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/registrations")
public class ClientController {
    private final ClientServiceImpl userService;
    private final ClientDtoMapper mapper;

    @PostMapping
    public ClientDto create(@RequestBody @Valid ClientDto userDto) {
        return mapper.toDto(userService.registerNewUserAccount(userDto));
    }
}