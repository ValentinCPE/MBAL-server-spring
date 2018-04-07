package com.worldgether.mbal.service;

import com.worldgether.mbal.model.Message;
import org.springframework.stereotype.Service;

@Service
public interface EventService {

    String addEvent(String username, Message message);

}
