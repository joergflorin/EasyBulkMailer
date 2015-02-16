/*
 * Created on 16.02.2015 by joerg
 */
package de.casablu.ebm.engine;

/**
 * MailingEngine with following requirements:
 * 
 * <ul>
 * <li>Input eml data as a mail template (or any other JavaMail compatible
 * format)</li>
 * <li>Input contact data as vcard files, mails send to the first e-mail address
 * of every contact</li>
 * <li>Handles Mailing requests as jobs with a checkpoint-restart feature.</li>
 * <li>Loging of the activity</li>
 * <li>Listener for progress (gui)</li>
 * <li>Mailserver configuration is made by user preferences.</li>
 * </ul>
 * 
 * @author Joerg Florin (git@casa-blu.de)
 */
public interface MailingEngine {

    void runJob(MailingJob job);
}
