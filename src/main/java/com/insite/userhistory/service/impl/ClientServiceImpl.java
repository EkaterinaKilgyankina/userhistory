package com.insite.userhistory.service.impl;

import com.insite.userhistory.dto.ClientDto;
import com.insite.userhistory.exception.AlreadyExistsException;
import com.insite.userhistory.model.Client;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.insite.userhistory.repository.ClientRepository;
import com.insite.userhistory.service.ClientService;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Client registerNewUserAccount(ClientDto clientDto) {

        if (userRepository.existsByName(clientDto.getName())) {
            throw new AlreadyExistsException();
        }
        Client userNew = new Client();
        userNew.setPassword(passwordEncoder.encode(clientDto.getPassword()));
        userNew.setName(clientDto.getName());

        return userRepository.save(userNew);
    }
}
