/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.*;

/**
 *
 * @author Caleb Fonyuy Asheri
 */
public class EmailSender {
    private static final String HOST="smtp.gmail.com";
    private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    private static final String sender = "fonyuycaleb@gmail.com";
    private static final String subject = "Invitation to ";
    private static final String password = "JEEZY2WAYN3";
    
    public static boolean sendMail(String receiver,String title,String message){
        Properties mail = System.getProperties();
        mail.setProperty("mail.smtp.host",HOST);
        mail.setProperty("mail.smtp.socketFactory.class",SSL_FACTORY);
        
        mail.setProperty("mail.smtp.socketFactory.fallback","false");
        mail.setProperty("mail.smtp.socketFactory.port","465");
        mail.setProperty("mail.smtp.auth", "true");
        mail.setProperty("mail.debug","false");
        mail.setProperty("mail.store.protocol","pop3");
        mail.setProperty("mail.transport.protocol","smtp");
        
        
        Session session = Session.getDefaultInstance(mail, new Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(sender,password);
            }
        });
        try{
           MimeMessage mess = new MimeMessage(session);
           
           mess.setFrom(new InternetAddress(sender));
           
           mess.addRecipient(Message.RecipientType.TO,new InternetAddress(receiver));
           
           mess.setSubject(subject+title);
           
           mess.setText(message);
           
           mess.setSentDate(new Date());
           
           Transport.send(mess);
           return true;
        } catch (MessagingException ex) {
            Logger.getLogger(EmailSender.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
