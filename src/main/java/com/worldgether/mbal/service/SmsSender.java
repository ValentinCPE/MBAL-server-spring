package com.worldgether.mbal.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class SmsSender {

    private RestTemplate restTemplate;
    private HttpEntity<?> entity;

    public SmsSender() {
        restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        this.entity = new HttpEntity<>(headers);
    }

    public String sendSms(String number, String prenom, String codeToCheck) {

        if (!number.isEmpty() && number.matches("[0-9+]+")) {
            if (number.length() == 12) {
                if (number.charAt(0) == '+') {
                    number = number.replace("+", "");
                } else {
                    return null;
                }
            } else if (number.length() == 10) {
                number = "33" + number.substring(1);
            } else if (number.length() == 9) {
                number = "33" + number;
            }
        } else {
            return null;
        }

        UriComponentsBuilder builder = null;

        builder = UriComponentsBuilder.fromHttpUrl("https://rest.nexmo.com/sms/json")
                .queryParam("api_key", "87867708")
                .queryParam("api_secret", "Qo3KmrHYcksKi8K9")
                .queryParam("to", number)
                .queryParam("from", "MBAL-Verif")
                .queryParam("text", codeToCheck);

        HttpEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                this.entity,
                String.class);

        return response.getBody();
    }
}

