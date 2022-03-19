package com.insite.userhistory.service;

import com.insite.userhistory.dto.MessageDto;

import java.util.List;

public interface MessageService {
    List<String> addMessage (String auth, MessageDto message);
}
