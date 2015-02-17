/*
 * Created on 16.02.2015 by joerg
 */
package de.casablu.ebm.engine;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import net.sourceforge.cardme.vcard.VCard;
import de.casablu.ebm.Version;

/**
 * Default Implementation of a MailingEngine.
 * 
 * @author Joerg Florin (git@casa-blu.de)
 */
class DefaultMailingEngine implements MailingEngine {

    @Override
    public void runJob(MailingJob job) {
        try {
            System.out.printf("Easy Bulk Mailer %s%n",
                    Version.getVersionString());

            // TODO use loggin mechanism instead System.outs
            MailServerConfiguration conf = new MailServerConfiguration();
            System.out.println("Use mail configuration:\n" + conf);
            List<InternetAddress> recipients = createRecipients(job
                    .getRecipients());

            Session session = Session
                    .getInstance(conf.getMailServerProperties(),
                            conf.getMailAuthenticator());
            // session.setDebug(true);
            // session.setDebugOut(System.out);

            if (job.getMailData() != null) {
                MimeMessage message = new MimeMessage(session,
                        job.getMailData());
                message.setFrom(conf.getSender());
                message.setSender(conf.getSender());

                int sentMessages = 0;
                for (InternetAddress recipient : recipients) {
                    System.out.printf("Send message %d of %d to %s ...%n",
                            sentMessages + 1, recipients.size(), recipient);
                    try {
                        message.setRecipients(Message.RecipientType.TO,
                                new InternetAddress[] { recipient });
                        Transport.send(message);
                        System.out.println("done");
                        sentMessages++;
                    } catch (Exception e) {
                        System.out.printf("Sending of message to %s failed.%n",
                                recipient);
                        e.printStackTrace(System.err);
                        // ignore
                        continue;
                    }
                }
                System.out
                        .printf("Totally %d of %d messages sent, %d messages failed.%n",
                                sentMessages, recipients.size(),
                                recipients.size() - sentMessages);
            } else {
                System.out
                        .println("No mail template selected, dry-run: List contacts/recipients");
                int counter = 0;
                for (VCard contact : job.getRecipients()) {
                    System.out.printf("Contact %d of %d: %s%n", ++counter,
                            recipients.size(), contact.toString());
                }
                counter = 0;
                for (InternetAddress recipient : recipients) {
                    System.out.printf("Recipent %d of %d: %s%n", ++counter,
                            recipients.size(), recipient);
                }
                System.out.println("Listing done.");
            }

        } catch (Exception e) {
            // TODO Better Error-Handling required.
            throw new RuntimeException(e);
        } finally {
            try {
                if (job.getMailData() != null) {
                    job.getMailData().close();
                }
            } catch (Exception e) {
                e.printStackTrace(System.err);
                // ignore
            }
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
        System.out.printf("%d recipients found, %d contacts failed.%n",
                recipients.size(), contacts.size() - recipients.size());
        return recipients;
    }

}
