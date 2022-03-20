package com.insite.userhistory.service.impl;

import com.insite.userhistory.dto.ClientDto;
import com.insite.userhistory.dto.TokenDto;
import com.insite.userhistory.exception.NotFoundException;
import com.insite.userhistory.exception.PermissionDeniedException;
import com.insite.userhistory.model.Client;
import com.insite.userhistory.repository.ClientRepository;
import com.insite.userhistory.service.AuthService;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
@AllArgsConstructor
class AuthServiceImpl implements AuthService {
    private final JwtComponent jwtComponent;
    private final ClientRepository userRepository;

    @Override
    public TokenDto createToken(ClientDto userDto) {
        Client user = userRepository.findByName(userDto.getName())
                .orElseThrow(NotFoundException::new);

        return new TokenDto()
                .setToken(jwtComponent.createJWT(user.getName()));
    }

    @Override
    public void validate(String bearerToken, String userName) {
        String token = bearerToken.split(" ")[1];
        Claims claims = jwtComponent.decodeJWT(token);
        String subject = claims.getSubject();
        if (!Objects.equals(subject, userName)) {
            throw new PermissionDeniedException();
        }
    }
}
