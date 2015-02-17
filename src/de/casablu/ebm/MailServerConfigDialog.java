/*
 * Created on 17.02.2015 by joerg
 */
package de.casablu.ebm;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import de.casablu.ebm.engine.MailServerConfiguration;

public class MailServerConfigDialog extends JDialog {

    private MailServerConfiguration conf;

    private JTextField smtpHost = new JTextField(20);

    private JFormattedTextField smtpPort = new JFormattedTextField(
            new DecimalFormat());

    private JTextField sslSocketFactoryClass = new JTextField(20);

    private JCheckBox smtpAuth = new JCheckBox();

    private JTextField smtpUserId = new JTextField(15);

    private JPasswordField smtpPassword = new JPasswordField(15);

    private JTextField mailSenderAddress = new JTextField(20);

    public MailServerConfigDialog() {
        super((Frame) null, String.format(
                "Easy Bulk Mailer %s: Mailserver configuration",
                Version.getVersionString()), true);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setContentPane(mainPanel);

        conf = new MailServerConfiguration();

        JLabel smtpHostLabel = new JLabel("SMTP-Host:");
        smtpHost.setText(conf.getSmtpHost());

        JLabel smtpPortLabel = new JLabel("SMTP-Port:");
        smtpPort.setValue(conf.getSmtpPort());

        JLabel sslSocketFactoryClassLabel = new JLabel(
                "SSLSocketFactory-Class:");
        sslSocketFactoryClass.setText(conf.getSslSocketFactory());

        JLabel smtpAuthLabel = new JLabel("SMTP-Authentication:");
        smtpAuth.setSelected(conf.getSmtpAuth());

        JLabel smtpUserIdLabel = new JLabel("SMTP-User-ID:");
        smtpUserId.setText(conf.getSmtpUserId());

        JLabel smtpPasswordLabel = new JLabel("SMTP-Password:");
        smtpPassword.setText(conf.getSmtpPassword());

        JLabel mailSenderAddressLabel = new JLabel("Mail-Sender-Address:");
        mailSenderAddress.setText(conf.getMailSenderAddress());

        JPanel confPanel = new JPanel();
        final GroupLayout layout = new GroupLayout(confPanel);
        confPanel.setLayout(layout);

        layout.setHorizontalGroup(layout
                .createSequentialGroup()
                .addGroup(
                        layout.createParallelGroup()
                                .addComponent(smtpHostLabel)
                                .addComponent(smtpPortLabel)
                                .addComponent(sslSocketFactoryClassLabel)
                                .addComponent(smtpAuthLabel)
                                .addComponent(smtpUserIdLabel)
                                .addComponent(smtpPasswordLabel)
                                .addComponent(mailSenderAddressLabel))
                .addGroup(
                        layout.createParallelGroup().addComponent(smtpHost)
                                .addComponent(smtpPort)
                                .addComponent(sslSocketFactoryClass)
                                .addComponent(smtpAuth)
                                .addComponent(smtpUserId)
                                .addComponent(smtpPassword)
                                .addComponent(mailSenderAddress)));
        layout.setVerticalGroup(layout
                .createSequentialGroup()
                .addGroup(
                        layout.createParallelGroup(
                                GroupLayout.Alignment.BASELINE)
                                .addComponent(smtpHostLabel)
                                .addComponent(smtpHost))
                .addGroup(
                        layout.createParallelGroup(
                                GroupLayout.Alignment.BASELINE)
                                .addComponent(smtpPortLabel)
                                .addComponent(smtpPort))
                .addGroup(
                        layout.createParallelGroup(
                                GroupLayout.Alignment.BASELINE)
                                .addComponent(sslSocketFactoryClassLabel)
                                .addComponent(sslSocketFactoryClass))
                .addGroup(
                        layout.createParallelGroup(
                                GroupLayout.Alignment.BASELINE)
                                .addComponent(smtpAuthLabel)
                                .addComponent(smtpAuth))
                .addGroup(
                        layout.createParallelGroup(
                                GroupLayout.Alignment.BASELINE)
                                .addComponent(smtpUserIdLabel)
                                .addComponent(smtpUserId))
                .addGroup(
                        layout.createParallelGroup(
                                GroupLayout.Alignment.BASELINE)
                                .addComponent(smtpPasswordLabel)
                                .addComponent(smtpPassword))
                .addGroup(
                        layout.createParallelGroup(
                                GroupLayout.Alignment.BASELINE)
                                .addComponent(mailSenderAddressLabel)
                                .addComponent(mailSenderAddress)));

        mainPanel.add(confPanel, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                MailServerConfigDialog.this.dispose();
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(okButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.Window#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();

        conf.setSmtpHost(smtpHost.getText());
        conf.setSmtpPort((Integer) smtpPort.getValue());
        conf.setSslSocketFactory(sslSocketFactoryClass.getText());
        conf.setSmtpAuth(smtpAuth.isSelected());
        conf.setSmtpUserId(smtpUserId.getText());
        conf.setSmtpPassword(smtpPassword.getText());
        conf.setMailSenderAddress(mailSenderAddress.getText());

    }

}
