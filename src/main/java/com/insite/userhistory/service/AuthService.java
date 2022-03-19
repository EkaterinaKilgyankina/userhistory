package com.insite.userhistory.service;

import com.insite.userhistory.dto.TokenDto;
import com.insite.userhistory.dto.ClientDto;

public interface AuthService {

    TokenDto createToken(ClientDto userDto);

    void validate(String token, String userName);
}
