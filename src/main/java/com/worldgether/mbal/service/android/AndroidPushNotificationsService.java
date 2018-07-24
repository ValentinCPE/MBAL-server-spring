package com.worldgether.mbal.service.android;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AndroidPushNotificationsService {

    private static final String FIREBASE_SERVER_KEY = "AAAA0075Hp4:APA91bHC0ypEjcnhJogzIystQ_Ipd_9NjGWIC9PtB5so24wvTVR9kY7fkPmKTEx4sxo10iQTOIaX0xu0DY7i0_UlfD1ge9ZlqzLT-J_Zo8ksAJ_RtyI0yKo2UESniJBOgoG6EqV7VAum";
    private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";

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

    public String sendNotification(String familyName, String message){
        JSONObject body = new JSONObject();
        body.put("to", "/topics/"+familyName);
        body.put("priority", "high");

        JSONObject notification = new JSONObject();
        notification.put("title", "MBAL");
        notification.put("body", message);
        notification.put("icon", "ic_mbal.png");
        notification.put("sound", "cloche.mp3");

        JSONObject data = new JSONObject();
        data.put("Key-1", "JSA Data 1");
        data.put("Key-2", "JSA Data 2");

        body.put("notification", notification);
        body.put("data", data);

/**
 {
 "notification": {
 "title": "JSA Notification",
 "body": "Happy Message!"
 },
 "data": {
 "Key-1": "JSA Data 1",
 "Key-2": "JSA Data 2"
 },
 "to": "/topics/JavaSampleApproach",
 "priority": "high"
 }
 */

        HttpEntity<String> request = new HttpEntity<>(body.toString());

        CompletableFuture<String> pushNotification = send(request);
        CompletableFuture.allOf(pushNotification).join();

        try {
            String firebaseResponse = pushNotification.get();

            return firebaseResponse;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return "Push Notification ERROR!";

    }

}
