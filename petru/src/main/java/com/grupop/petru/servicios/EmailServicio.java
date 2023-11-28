package com.grupop.petru.servicios;


import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class EmailServicio {
    private JavaMailSender mailSender;
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private int port;
    @Value("${spring.mail.email}")
    private String email;
    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;

    public void sendEmail(String to, String subject, String body) {
        JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();

        mailSenderImpl.setHost(host);
        mailSenderImpl.setPort(port);

        mailSenderImpl.setUsername(username);
        mailSenderImpl.setPassword(password);

        Properties props = mailSenderImpl.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false");

        mailSenderImpl.setJavaMailProperties(props);

        mailSender = mailSenderImpl;

        MimeMessage message = mailSender.createMimeMessage();

        try {
            message.setRecipients(MimeMessage.RecipientType.TO, to);
            message.setSubject(subject);
            message.setContent(body, "text/html; charset=utf-8");
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }

        mailSender.send(message);
    }

    public void sendEmail(String subject, String body) {
        JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();

        mailSenderImpl.setHost(host);
        mailSenderImpl.setPort(port);

        mailSenderImpl.setUsername(username);
        mailSenderImpl.setPassword(password);

        Properties props = mailSenderImpl.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false");

        mailSenderImpl.setJavaMailProperties(props);

        mailSender = mailSenderImpl;

        MimeMessage message = mailSender.createMimeMessage();

        try {
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject(subject);
            message.setContent(body, "text/html; charset=utf-8");
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }

        mailSender.send(message);
    }
}
