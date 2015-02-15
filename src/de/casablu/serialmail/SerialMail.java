/*
 * Created on 15.02.2015 by joerg
 */
package de.casablu.serialmail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import javax.mail.internet.*;
import javax.mail.*;

public class SerialMail {

    public static void main(String[] args) throws Exception {
        String emlPath = args[0];
        String receipientList = args[1];
        
        Properties mailProps = new Properties();

        Properties props = new Properties();
 
        props.put("mail.smtp.host", "smtp.strato.de");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("joerg@casa-blu.de", "");
            }
          });
        
        InputStream emlInput = new FileInputStream(emlPath);
        MimeMessage message = new MimeMessage(session, emlInput);
        message.setFrom(new InternetAddress("joerg@casa-blu.de", "Jörg Florin"));
        message.setSender(new InternetAddress("joerg@casa-blu.de", "Jörg Florin"));
        message.setRecipients(Message.RecipientType.TO, new InternetAddress[] {new InternetAddress(receipientList)});

        Transport.send(message);
        
//        message.setRecipients(Message.RecipientType.TO, new InternetAddress[] {new InternetAddress("webmaster@casa-blu.de", "Jörg Florin")});

//        Transport.send(message);
        
        System.out.println("Done");
    }

}
