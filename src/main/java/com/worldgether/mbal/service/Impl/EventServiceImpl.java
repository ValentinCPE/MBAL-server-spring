package com.worldgether.mbal.service.Impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.worldgether.mbal.model.Evt_message;
import com.worldgether.mbal.model.Message;
import com.worldgether.mbal.model.Response;
import com.worldgether.mbal.model.User;
import com.worldgether.mbal.repository.Evt_messageRepository;
import com.worldgether.mbal.repository.UserRepository;
import com.worldgether.mbal.service.EventService;
import com.worldgether.mbal.service.android.AndroidPushNotificationsService;
import org.bson.BsonTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private Evt_messageRepository evt_messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AndroidPushNotificationsService androidPushNotificationsService;

    @Override
    public String addEvent(String username, Message message_sent){

        if(username == null || message_sent == null || username.isEmpty()){
            return null;
        }

        User user = userRepository.findByMail(username);

        if(user == null || user.getFamily() == null){
            return null;
        }

        androidPushNotificationsService.sendNotification(user.getFamily().getName(), message_sent.toString());

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

}
