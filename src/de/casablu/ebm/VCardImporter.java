/*
 * Created on 16.02.2015 by joerg
 */
package de.casablu.ebm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cardme.engine.VCardEngine;
import net.sourceforge.cardme.vcard.VCard;

/**
 * Imports vCards from vCard-List in InputStream.
 * 
 * @author Joerg Florin (git@casa-blu.de)
 */
class VCardImporter {

    /**
     * Imports vCards from contactData input stream.
     * 
     * @param contactData
     *            InputStream with vCards.
     * @return List of vCards.
     */
    public List<VCard> importVCards(InputStream contactData) {

        VCardEngine engine = new VCardEngine();
        List<VCard> vCards = new ArrayList<VCard>();

        BufferedReader vCardReader = new BufferedReader(new InputStreamReader(
                contactData));

        try {
            StringBuilder vCard = new StringBuilder();
            while (true) {
                String vCardLine;
                try {
                    vCardLine = vCardReader.readLine();
                } catch (IOException e) {
                    // TODO better error handling;
                    throw new RuntimeException(e);
                }
                if (vCardLine == null) {
                    if (vCard.length() > 0) {
                        // TODO log
                        System.err.println("extra lines in contacts data.");
                    }
                    // All lines read, finish read loop.
                    break;
                }
                vCard.append(vCardLine);
                vCard.append('\n');
                if ("END:VCARD".equalsIgnoreCase(vCardLine)) {
                    // vCard finished, parse and put to result.
                    try {
                        vCards.add(engine.parse(vCard.toString()));
                    } catch (Exception ex) {
                        // TODO log
                        ex.printStackTrace(System.err);
                    }
                    vCard = new StringBuilder();
                }
            }
        } finally {
            try {
                vCardReader.close();
                contactData.close();
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
                // ignore
            }
        }

        System.out.println("Imported " + vCards.size() + " contacts.");

        return vCards;
    }

}
