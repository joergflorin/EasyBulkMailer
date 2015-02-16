/*
 * Created on 16.02.2015 by joerg
 */
package de.casablu.ebm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

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
        if (args.length != 2) {
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

        if (pathToEmlFile == null || pathToVCardFile == null) {
            printUsageAndExit();
        }

        MailingEngine engine = MailingEngineFactory.getInstance()
                .getMailingEngine();
        engine.runJob(new CommandLineMailingJob(pathToEmlFile, pathToVCardFile));
    }

    private static final class CommandLineMailingJob implements MailingJob {

        private final InputStream emlFile;

        private final InputStream vCardFile;

        private CommandLineMailingJob(String pathToEmlFile,
                String pathToVCardFile) {
            try {
                emlFile = new FileInputStream(pathToEmlFile);
                vCardFile = new FileInputStream(pathToVCardFile);
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
        public InputStream getContactData() {
            return vCardFile;
        }
    }

    private static void printUsageAndExit() {
        System.err
                .println("usage: --eml={path-to-eml-file<} --vcards={path-to-vcard-file}");
        System.exit(-1);
    }
}