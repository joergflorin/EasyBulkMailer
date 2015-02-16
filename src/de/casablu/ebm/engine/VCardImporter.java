/*
 * Created on 16.02.2015 by joerg
 */
package de.casablu.ebm.engine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.InternetAddress;

import net.sourceforge.cardme.engine.VCardEngine;
import net.sourceforge.cardme.vcard.VCard;
import net.sourceforge.cardme.vcard.exceptions.VCardParseException;

/**
 * Imports vCards and converts them to InternetAddresses.
 * 
 * @author Joerg Florin (git@casa-blu.de)
 */
class VCardImporter {

    /**
     * Imports vCards from contactData and extracts InternetAddresses.
     * 
     * @param contactData
     *            InputStream with vCards.
     * @return InternetAddresses from contactData.
     */
    public static List<InternetAddress> importReceipients(
            InputStream contactData) {

        ByteArrayOutputStream contactBytes = new ByteArrayOutputStream(1024);
        try {
            byte[] buffer = new byte[1024];
            int readBytes = 0;

            do {
                readBytes = contactData.read(buffer);
                if (readBytes > 0) {
                    contactBytes.write(buffer, 0, readBytes);
                }
            } while (readBytes > 0);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                contactData.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                contactBytes.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String allContacts = contactBytes.toString();
        String[] contactStringArray = allContacts.split("BEGIN:VCARD");
        List<String> contactStrings = new ArrayList<String>();
        for (String contactString : contactStringArray) {
            if (contactString != null && !contactString.isEmpty()) {
                contactStrings.add("BEGIN:VCARD" + contactString);
            }
        }

        VCardEngine engine = new VCardEngine();

        try {
            VCard[] vcards = engine.parse(contactStrings.toArray(new String[contactStrings.size()]));
            List<InternetAddress> addresses = new ArrayList<InternetAddress>();
            for (VCard vcard : vcards) {
                String name = vcard.getFN().getFormattedName();
                String emailAddress = vcard.getEmails().get(0).getEmail();
                addresses.add(new InternetAddress(emailAddress, name));
            }
            return addresses;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
    }

}
