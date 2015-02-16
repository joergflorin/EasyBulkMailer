/*
 * Created on 16.02.2015 by joerg
 */
package de.casablu.ebm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;

import net.sourceforge.cardme.vcard.VCard;
import de.casablu.ebm.engine.MailingEngine;
import de.casablu.ebm.engine.MailingEngineFactory;
import de.casablu.ebm.engine.MailingJob;

public class Gui {

    public static void main(String[] args) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException {

        // Set System L&F
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                // Open ConsoleWindow.
                new ConsoleWindow();

                // Open FileDialog for mail template.
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileFilter() {

                    @Override
                    public boolean accept(File f) {
                        return f.isDirectory() || f.getName().endsWith(".eml")
                                || f.getName().endsWith(".EML");
                    }

                    @Override
                    public String getDescription() {
                        return "EML Files";
                    }

                });
                fileChooser
                        .setDialogTitle("EasyBulkMailer: Select Mail Template (EML)");
                fileChooser.setMultiSelectionEnabled(false);
                int result = fileChooser.showOpenDialog(null);
                File emlFile = fileChooser.getSelectedFile();
                if (result != JFileChooser.APPROVE_OPTION || emlFile == null) {
                    // User cancelled.
                    System.exit(0);
                }

                // Open FileDialog for vcard files.
                fileChooser.setFileFilter(new FileFilter() {

                    @Override
                    public boolean accept(File f) {
                        return f.isFile() && f.getName().endsWith(".vcf")
                                || f.getName().endsWith(".VCF");
                    }

                    @Override
                    public String getDescription() {
                        return "vCard Files";
                    }

                });
                fileChooser
                        .setDialogTitle("EasyBulkMailer: Select Contacts file (vCard)");
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
                this.emlFile = new FileInputStream(emlFile);
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
