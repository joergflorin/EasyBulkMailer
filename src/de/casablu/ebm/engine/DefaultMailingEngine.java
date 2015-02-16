/*
 * Created on 16.02.2015 by joerg
 */
package de.casablu.ebm.engine;

import java.util.Arrays;
import java.util.List;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Default Implementation of a MailingEngine.
 * @author Joerg Florin (git@casa-blu.de)
 */
class DefaultMailingEngine implements MailingEngine {

    @Override
    public void runJob(MailingJob job) {
        try {
        //TODO use loggin mechanism instead System.outs
        MailServerConfiguration conf = new MailServerConfiguration();
        System.out.println("Use mail configuration:\n" + conf);
        List<InternetAddress> receipients = VCardImporter.importReceipients(job.getContactData());
//        List<InternetAddress> receipients;
//            receipients = Arrays.asList(new InternetAddress("Joerg Florin <joerg@casa-blu.de>"));

        Session session = Session.getInstance(conf.getMailServerProperties(), conf.getMailAuthenticator());
        
        MimeMessage message = new MimeMessage(session, job.getMailData());
        message.setFrom(conf.getSender());
        message.setSender(conf.getSender());
        
        for (InternetAddress receipient : receipients) {
            System.out.println("send to " + receipient + " ...");
            message.setRecipients(Message.RecipientType.TO, new InternetAddress[] {receipient});
            Transport.send(message);
            System.out.println("done");
       }

        } catch (Exception e) {
            // TODO Better Error-Handling required.
            throw new RuntimeException(e);
        }
    }

}
