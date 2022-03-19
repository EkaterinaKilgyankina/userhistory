package com.insite.userhistory.controller;

import com.insite.userhistory.dto.ClientDto;
import com.insite.userhistory.dto.TokenDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.insite.userhistory.service.AuthService;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/authentications")
public class AuthenticateController {
    private final AuthService authService;

    @PostMapping
    public TokenDto createToken(@RequestBody @Valid ClientDto clientDto) {
        return authService.createToken(clientDto);
    }
}
