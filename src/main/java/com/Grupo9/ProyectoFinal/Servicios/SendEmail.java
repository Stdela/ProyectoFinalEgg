package com.Grupo9.ProyectoFinal.Servicios;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service
public class SendEmail {

@Autowired
private JavaMailSender mailSender;

public void sendEmail(String correo) throws IOException {
    
    
    SimpleMailMessage message = new SimpleMailMessage();

    message.setFrom("springchallengealkemy@gmail.com");
    message.setTo(correo);
    message.setText("Bienvenido/a a nuestra apps de trabajos. Esperamos que la disfrute y no dude en consultar sobre cualquier cuestión!");
    message.setSubject("TrabajAR");

    mailSender.send(message);    
   
    
        }
}
