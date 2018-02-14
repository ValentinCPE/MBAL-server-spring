package com.worldgether.mbal.service.android;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

import com.worldgether.mbal.model.Evt_message;
import com.worldgether.mbal.model.Family;
import com.worldgether.mbal.model.Message;
import com.worldgether.mbal.model.User;
import com.worldgether.mbal.repository.Evt_messageRepository;
import com.worldgether.mbal.repository.FamilyRepository;
import com.worldgether.mbal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AndroidPushNotificationsService {

    private static final String FIREBASE_SERVER_KEY = "AAAAEqBmUcs:APA91bE56_mqUXAhGYSZiLIx8TbHNGmx6-lHglrZ-ZgY4FYbCKKCwwcW4hOyLSgra1TXDdspK7wJPOWcs6ke5bgQefo0e0HMLhm2BI-n4qdq93XfR3TG6oYd2wbAtI8-0Qq55AWCsm-p";
    private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";

    @Autowired
    private Evt_messageRepository evt_messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FamilyRepository familyRepository;

    @Async
    public CompletableFuture<String> send(HttpEntity<String> entity) {

        RestTemplate restTemplate = new RestTemplate();

        /**
         https://fcm.googleapis.com/fcm/send
         Content-Type:application/json
         Authorization:key=FIREBASE_SERVER_KEY*/

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FIREBASE_SERVER_KEY));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
        restTemplate.setInterceptors(interceptors);

        String firebaseResponse = restTemplate.postForObject(FIREBASE_API_URL, entity, String.class);

        return CompletableFuture.completedFuture(firebaseResponse);
    }

    public String saveInDb(Integer id_user, Message message_sent){

        if(id_user == null || message_sent == null){
            return null;
        }

        User user = userRepository.findById(id_user);

        if(user == null){
            return null;
        }

        Evt_message evt_message = new Evt_message();

        evt_message.setDateUTC(new Date());
        evt_message.setFamily(user.getFamily());
        evt_message.setMessage(message_sent);
        evt_message.setUser(user);

        evt_messageRepository.save(evt_message);

        return "Saved";

    }

}
