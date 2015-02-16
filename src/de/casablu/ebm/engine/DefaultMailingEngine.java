/*
 * Created on 16.02.2015 by joerg
 */
package de.casablu.ebm.engine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import net.sourceforge.cardme.engine.VCardEngine;
import net.sourceforge.cardme.vcard.VCard;

/**
 * Default Implementation of a MailingEngine.
 * 
 * @author Joerg Florin (git@casa-blu.de)
 */
class DefaultMailingEngine implements MailingEngine {

    @Override
    public void runJob(MailingJob job) {
        try {
            // TODO use loggin mechanism instead System.outs
            MailServerConfiguration conf = new MailServerConfiguration();
            System.out.println("Use mail configuration:\n" + conf);
            List<InternetAddress> receipients = createRecipients(job
                    .getRecipients());

            Session session = Session
                    .getInstance(conf.getMailServerProperties(),
                            conf.getMailAuthenticator());

            MimeMessage message = new MimeMessage(session, job.getMailData());
            message.setFrom(conf.getSender());
            message.setSender(conf.getSender());

            for (InternetAddress receipient : receipients) {
                System.out.println("send to " + receipient + " ...");
                message.setRecipients(Message.RecipientType.TO,
                        new InternetAddress[] { receipient });
                Transport.send(message);
                System.out.println("done");
            }

        } catch (Exception e) {
            // TODO Better Error-Handling required.
            throw new RuntimeException(e);
        }
    }

    private List<InternetAddress> createRecipients(List<VCard> contacts) {
        List<InternetAddress> recipients = new ArrayList<InternetAddress>();
        for (VCard contact : contacts) {
            
            // TODO in case of error list contact data.
            if (!contact.hasN()) {
                System.err.println("Contact has no name.");
                continue;
            }
            if (!contact.hasEmails()) {
                System.err.println("Contact has no email addresses.");
                continue;
            }
            String name = contact.getFN().getFormattedName();
            String emailAddress = contact.getEmails().get(0).getEmail();
            try {
                recipients.add(new InternetAddress(emailAddress, name));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace(System.err);
                continue;
            }
        }
        return recipients;
    }

 
}
