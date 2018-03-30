package com.worldgether.mbal.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsSender {
    // Find your Account Sid and Auth Token at twilio.com/console
    public static final String ACCOUNT_SID =
            "AC958e68596ebd63d1983647bb25c0dc76";
    public static final String AUTH_TOKEN =
            "79b567063b51251ff20f1a95b901ab29";

    public static String sendSms(String number, String prenom, String codeToCheck) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        if(!number.isEmpty() && number.matches("[0-9+]+")){
            if(number.length() == 10){
                number = "+33" + number.substring(1);
            }else if(number.length() == 12){
                if(number.charAt(0) != '+'){
                    return null;
                }
            }else{
                return null;
            }
        }else{
            return null;
        }

        Message message = Message
                .creator(new PhoneNumber(number), // to
                        new PhoneNumber("+33757901306"), // from
                        "MBAL\nBienvenue "+prenom+"\rCode : "+codeToCheck)
                .create();

        return message.getErrorMessage();
    }
}

