/*
 * Created on 16.02.2015 by joerg
 */
package de.casablu.ebm.engine;

import java.io.InputStream;
import java.util.List;

import net.sourceforge.cardme.vcard.VCard;

/**
 * MailingJob, contains:
 * 
 * <ul>
 * <li>Eml data as a mail template (or any other JavaMail compatible format)</li>
 * <li>Contact data as vcard files, mails send to the first e-mail address of
 * every contact</li>
 * </ul>
 * 
 * @author Joerg Florin (git@casa-blu.de)
 */
public interface MailingJob {

    /**
     * @return Stream to eml data as a mail template (or any other JavaMail
     *         compatible format).
     */
    InputStream getMailData();

    /**
     * @return Recipient contacts as vCards.
     */
    List<VCard> getRecipients();
}
