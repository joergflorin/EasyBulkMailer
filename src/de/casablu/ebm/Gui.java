/*
 * Created on 16.02.2015 by joerg
 */
package de.casablu.ebm;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;

import net.sourceforge.cardme.vcard.VCard;
import de.casablu.ebm.engine.MailServerConfiguration;
import de.casablu.ebm.engine.MailingEngine;
import de.casablu.ebm.engine.MailingEngineFactory;
import de.casablu.ebm.engine.MailingJob;

public class Gui {

    public static void main(String[] args) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException {

        if (System.getProperty("java.util.logging.config.file") == null) {
            System.setProperty("java.util.logging.config.class",
                    "de.casablu.ebm.LoggingConfig");
        }

        // Set System L&F
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                // Check if Mailserver is configured.
                String smtpHost = new MailServerConfiguration().getSmtpHost();
                if (smtpHost == null
                        || smtpHost.isEmpty()
                        || Toolkit.getDefaultToolkit().getLockingKeyState(
                                KeyEvent.VK_CAPS_LOCK)) {
                    // Open Configuration Dialog.
                    new MailServerConfigDialog();
                }

                // Open ConsoleWindow.
                new ConsoleWindow();

                // Open FileDialog for mail template.
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileFilter() {

                    @Override
                    public boolean accept(File f) {
                        return f.isDirectory()
                                || f.isFile()
                                && (f.getName().endsWith(".eml") || f.getName()
                                        .endsWith(".EML"));
                    }

                    @Override
                    public String getDescription() {
                        return "EML Files";
                    }

                });
                fileChooser.setDialogTitle(String.format(
                        "EasyBulkMailer %s: Select Mail Template (EML)",
                        Version.getVersionString()));
                fileChooser.setMultiSelectionEnabled(false);
                int result = fileChooser.showOpenDialog(null);
                File emlFile;
                if (result == JFileChooser.APPROVE_OPTION) {
                    emlFile = fileChooser.getSelectedFile();
                } else {
                    emlFile = null;
                    JOptionPane
                            .showMessageDialog(
                                    null,
                                    "No EML-file selected, will just analyse vCards (dry-run).",
                                    "EasyBulMailer: No EML file selected",
                                    JOptionPane.WARNING_MESSAGE);
                }

                // Open FileDialog for vcard files.
                fileChooser.setFileFilter(new FileFilter() {

                    @Override
                    public boolean accept(File f) {
                        return f.isDirectory()
                                || f.isFile()
                                && (f.getName().endsWith(".vcf") || f.getName()
                                        .endsWith(".VCF"));
                    }

                    @Override
                    public String getDescription() {
                        return "vCard Files";
                    }

                });
                fileChooser.setDialogTitle(String.format(
                        "EasyBulkMailer %s: Select Contacts file (vCard)",
                        Version.getVersionString()));
                fileChooser.setMultiSelectionEnabled(false);
                result = fileChooser.showOpenDialog(null);
                File vcardFile = fileChooser.getSelectedFile();
                if (result != JFileChooser.APPROVE_OPTION || vcardFile == null) {
                    // User cancelled.
                    System.exit(0);
                }

                final MailingEngine engine = MailingEngineFactory.getInstance()
                        .getMailingEngine();
                final MailingJob job = new GuiMailingJob(emlFile, vcardFile);

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        engine.runJob(job);
                    }

                }).start();
            }
        });
    }

    private static final class GuiMailingJob implements MailingJob {

        private final InputStream emlFile;

        private final List<VCard> vCards;

        private GuiMailingJob(File emlFile, File vCardFile) {
            try {
                if (emlFile != null) {
                    this.emlFile = new FileInputStream(emlFile);
                } else {
                    this.emlFile = null;
                }
                vCards = new VCardImporter().importVCards(new FileInputStream(
                        vCardFile));
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

}
