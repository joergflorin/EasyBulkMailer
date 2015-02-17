/*
 * Created on 16.02.2015 by joerg
 */
package de.casablu.ebm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import net.sourceforge.cardme.vcard.VCard;
import de.casablu.ebm.engine.MailServerConfiguration;
import de.casablu.ebm.engine.MailingEngine;
import de.casablu.ebm.engine.MailingEngineFactory;
import de.casablu.ebm.engine.MailingJob;

/**
 * Command line interface to the Mailer.
 * 
 * @author Joerg Florin (git@casa-blu.de)
 */
public class CommandLineMailer {

    /**
     * Starts a mailing process.
     * <p>
     * Arguments:
     * <ul>
     * <li>--eml={path-to-eml-file<}</li>
     * <li>--vcards={path-to-vcard-file}</li>
     * </ul>
     * 
     * @param args
     *            command line params.
     */
    public static void main(String[] args) {
        if (args.length > 2 || args.length < 1) {
            printUsageAndExit();
        }
        String pathToEmlFile = null;
        String pathToVCardFile = null;

        for (String arg : args) {
            if (arg.startsWith("--eml=")) {
                pathToEmlFile = arg.substring(6);
            } else if (arg.startsWith("--vcards=")) {
                pathToVCardFile = arg.substring(9);
            }
        }

        if (pathToVCardFile == null) {
            printUsageAndExit();
        }

        // Check if Mailserver is configured.
        String smtpHost = new MailServerConfiguration().getSmtpHost();
        if (smtpHost == null || smtpHost.isEmpty()) {
            System.err.println("MailServer not configured.");
            System.exit(-1);
        }

        MailingEngine engine = MailingEngineFactory.getInstance()
                .getMailingEngine();
        engine.runJob(new CommandLineMailingJob(pathToEmlFile, pathToVCardFile));
    }

    private static final class CommandLineMailingJob implements MailingJob {

        private final InputStream emlFile;

        private final List<VCard> vCards;

        private CommandLineMailingJob(String pathToEmlFile,
                String pathToVCardFile) {
            try {
                if (pathToEmlFile != null) {
                    emlFile = new FileInputStream(pathToEmlFile);
                } else {
                    emlFile = null;
                }
                vCards = new VCardImporter().importVCards(new FileInputStream(
                        pathToVCardFile));
            } catch (FileNotFoundException e) {
                // TODO better error handling.
                throw new RuntimeException(e);
            }
        }

        @Override
        public InputStream getMailData() {
            return emlFile;
        }

        @Override
        public List<VCard> getRecipients() {
            return vCards;
        }
    }

    private static void printUsageAndExit() {
        System.err
                .println("usage: --eml={path-to-eml-file<} --vcards={path-to-vcard-file}");
        System.err
                .println("--eml option is optional, if ommitted, just vcards are analyzed.");
        System.exit(-1);
    }
}
