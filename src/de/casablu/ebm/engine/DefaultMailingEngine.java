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

import com.sun.istack.internal.logging.Logger;

import de.casablu.ebm.Version;

/**
 * Default Implementation of a MailingEngine.
 * 
 * @author Joerg Florin (git@casa-blu.de)
 */
class DefaultMailingEngine implements MailingEngine {

    private static Logger LOGGER = Logger.getLogger(MailingEngine.class);

    @Override
    public void runJob(MailingJob job) {
        try {
            LOGGER.info(String.format("Easy Bulk Mailer %s",
                    Version.getVersionString()));

            MailServerConfiguration conf = new MailServerConfiguration();
            LOGGER.info("Use mail configuration:\n" + conf);

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
                    LOGGER.info(String.format(
                            "Send message %d of %d to %s ...",
                            sentMessages + 1, recipients.size(),
                            recipient.toUnicodeString()));
                    try {
                        message.setRecipients(Message.RecipientType.TO,
                                new InternetAddress[] { recipient });
                        Transport.send(message);
                        LOGGER.info("done");
                        sentMessages++;
                    } catch (Exception e) {
                        LOGGER.warning(String.format(
                                "Sending of message to %s failed.",
                                recipient.toUnicodeString()), e);
                        // ignore
                        continue;
                    }
                }
                LOGGER.info(String.format(
                        "Totally %d of %d messages sent, %d messages failed.",
                        sentMessages, recipients.size(), recipients.size()
                                - sentMessages));
            } else {
                LOGGER.warning(String
                        .format("No mail template selected, dry-run: List contacts/recipients"));
                int counter = 0;
                for (VCard contact : job.getRecipients()) {
                    LOGGER.info(String.format("Contact %d of %d: %s",
                            ++counter, recipients.size(), contact.toString()));
                }
                counter = 0;
                for (InternetAddress recipient : recipients) {
                    LOGGER.info(String.format("Recipent %d of %d: %s",
                            ++counter, recipients.size(),
                            recipient.toUnicodeString()));
                }
                LOGGER.info("Listing done.");
            }

        } catch (Exception e) {
            LOGGER.severe("Exception in mailing engine", e);
            // TODO Better Error-Handling required.
            throw new RuntimeException(e);
        } finally {
            try {
                if (job.getMailData() != null) {
                    job.getMailData().close();
                }
            } catch (Exception e) {
                LOGGER.severe("Exception in closing maildata", e);
            }
        }
    }

    private List<InternetAddress> createRecipients(List<VCard> contacts) {
        List<InternetAddress> recipients = new ArrayList<InternetAddress>();
        for (VCard contact : contacts) {

            // TODO in case of error list contact data.
            if (!contact.hasN()) {
                LOGGER.warning("Contact has no name.");
                continue;
            }
            if (!contact.hasEmails()) {
                LOGGER.warning("Contact has no email addresses.");
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
        LOGGER.info(String.format("%d recipients found, %d contacts failed.",
                recipients.size(), contacts.size() - recipients.size()));
        return recipients;
    }

}
