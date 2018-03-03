package com.worldgether.mbal.service;

import com.worldgether.mbal.model.Mail;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;

public interface EmailService {

    void sendMail(String to, String subject, Map<String,String> variables, String template);

}
