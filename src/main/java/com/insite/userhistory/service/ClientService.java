package com.insite.userhistory.service;

import com.insite.userhistory.dto.ClientDto;
import com.insite.userhistory.model.Client;

public interface ClientService {
    Client registerNewUserAccount(ClientDto user);

}
