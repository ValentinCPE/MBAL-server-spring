package com.worldgether.mbal.service;

import com.worldgether.mbal.model.Evt_message;
import com.worldgether.mbal.model.Message;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventService {

    String addEvent(String username, Message message);

    List<Evt_message> getAllEventsByFamily(String family_name);

}
