package com.worldgether.mbal.service.Impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.worldgether.mbal.model.*;
import com.worldgether.mbal.repository.Evt_messageRepository;
import com.worldgether.mbal.repository.UserRepository;
import com.worldgether.mbal.service.EventService;
import com.worldgether.mbal.service.android.AndroidPushNotificationsService;
import org.bson.BsonTimestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.worldgether.mbal.model.Message.NEW_COURRIEL;

@Service
public class EventServiceImpl implements EventService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Evt_messageRepository evt_messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AndroidPushNotificationsService androidPushNotificationsService;

    @Autowired
    private SimpMessagingTemplate template;

    @Override
    public String addEvent(String username, Message message_sent){

        if(username == null || message_sent == null || username.isEmpty()){
            return null;
        }

        User user = userRepository.findByMail(username);

        if(user == null || user.getFamily() == null){
            return null;
        }

        if(message_sent == Message.NEW_COURRIEL){
            template.convertAndSend("/alert/event/"+user.getFamily().getId(), message_sent.toString());
        }

        LocalDateTime localDateTime = new Timestamp(new Date().getTime()).toLocalDateTime();
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("HH:mm (dd-MM-yyyy)", Locale.FRENCH);

        androidPushNotificationsService.sendNotification(user.getFamily().getName(), message_sent.toString() + localDateTime.format(formatterDate));

        DBObject dbObject = new BasicDBObject();
        dbObject.put("event", new BsonTimestamp((int) (new Date().getTime()/1000),1));

        Evt_message evt_message = Evt_message.builder()
                .timestamp(new Date().getTime())
                .date(dbObject)
                .family(user.getFamily())
                .message(message_sent)
                .user(user)
                .build();

        evt_messageRepository.save(evt_message);

        return Response.OK.toString();

    }

    @Override
    public List<Evt_message> getAllEventsByFamily(String family_name) {

        if(family_name == null || family_name.isEmpty()){
            return null;
        }

        List<Evt_message> events = evt_messageRepository.findByFamily_Name(family_name);

        if(events == null){
            log.info(LoggerMessage.getLog(LoggerMessage.EVENTS_NOT_EXIST.toString(),"GETALLEVENTSBYFAMILY"));
            return null;
        }

        if(events.size() == 0){
            return new ArrayList<>(Collections.singletonList(new Evt_message()));
        }

        return events;

    }

    @Override
    public Evt_message getLastEventByFamily(String family_name){

        if(family_name == null || family_name.isEmpty()){
            return null;
        }

        Evt_message lastEvent = evt_messageRepository.findTopByOrderByTimestampDesc();

        if(lastEvent == null){
            lastEvent = new Evt_message();
            log.info(LoggerMessage.getLog(LoggerMessage.EVENTS_NOT_EXIST.toString(),"GETLASTEVENTBYFAMILY"));
        }

        return lastEvent;

    }

    @Override
    public Evt_message getEventAtDateByFamily(String family_name, Timestamp date){

        Evt_message courrielReceived = new Evt_message();

        if(family_name == null || family_name.isEmpty() || date == null){
            return null;
        }

        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDateTime dateMidnight = LocalDateTime.of(date.toLocalDateTime().toLocalDate(), midnight);
        Long dateMidnightLong = Timestamp.valueOf(dateMidnight).getTime();
        Long dateTomorrowMidnightLong = Timestamp.valueOf(dateMidnight.plusDays(1)).getTime();

        List<Evt_message> events = evt_messageRepository.findByFamily_NameAndTimestampBetween(family_name,dateMidnightLong,dateTomorrowMidnightLong);

        if(events == null){
            log.error(LoggerMessage.getLog(LoggerMessage.NO_EVENTS_FOR_DATE.toString(),"GETEVENTATDATEBYFAMILY",dateMidnightLong.toString()));
        }else {
            for (Evt_message evt : events) {
                if (evt.getMessage() == Message.NEW_COURRIEL) {
                    courrielReceived = evt;
                }
            }
        }

        return courrielReceived;

    }

    @Override
    public boolean hasLetterInProgress(String family_name){

        if(family_name == null || family_name.isEmpty()){
            return false;
        }

        Evt_message lastEvent = this.getLastEventByFamily(family_name);

        return lastEvent != null && lastEvent.getMessage() == NEW_COURRIEL;

    }

}
