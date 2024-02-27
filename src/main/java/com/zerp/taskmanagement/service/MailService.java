package com.zerp.taskmanagement.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    private SimpleMailMessage mailMessageInstance;

    public MailService() {
        this.mailMessageInstance = new SimpleMailMessage();
    }

    public String sendMail(String to , String subject, String message) {

        SimpleMailMessage mailMessage = this.mailMessageInstance;
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        javaMailSender.send(mailMessage);

        return "Email sended successfully";
    }

    public String sendMailWithAttachment(File file , String to , String subject , String message) throws MessagingException{

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(message);
        messageHelper.addAttachment(file.getName(), file);
        javaMailSender.send(mimeMessage);        

        return "Email sent successfully";
    }


    
}
