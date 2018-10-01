package com.worldgether.mbal.service;

import com.worldgether.mbal.model.Evt_message;
import com.worldgether.mbal.model.Message;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public interface EventService {

    String addEvent(String username, Message message);

    String changeWifiSettings(String username);

    List<Evt_message> getAllEventsByFamily(String family_name);

    Evt_message getLastEventByFamily(String family_name);

    Evt_message getEventAtDateByFamily(String family_name, Timestamp date);

    boolean hasLetterInProgress(String family_name);

}
