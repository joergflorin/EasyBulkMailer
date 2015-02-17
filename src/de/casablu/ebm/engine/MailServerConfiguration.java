/*
 * Created on 16.02.2015 by joerg
 */
package de.casablu.ebm.engine;

import java.util.Properties;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.sun.istack.internal.logging.Logger;

import de.casablu.ebm.CommandLineMailer;

/**
 * MailServerConfiguration configured by Preferences.
 * 
 * @author Joerg Florin (git@casa-blu.de)
 */
public class MailServerConfiguration {

    private static final Logger LOGGER = Logger
            .getLogger(MailServerConfiguration.class);

    private final Preferences prefs = Preferences
            .userNodeForPackage(CommandLineMailer.class);

    private static final String SMTP_HOST_PREF = "SmtpHost";

    private static final String SMTP_PORT_PREF = "SmtpPort";

    private static final String SOCKET_FACTORY_CLASS_PREF = "SocketFactoryClass";

    private static final String SMTP_AUTH_PREF = "SmtpAuth";

    private static final String SMTP_USER_ID_PREF = "UserId";

    private static final String SMTP_PASSWORD_PREF = "Password";

    private static final String MAIL_SENDER_ADDRESS_PREF = "MailSenderAddress";

    /**
     * @return Properties for the mail server.
     */
    Properties getMailServerProperties() {
        Properties props = new Properties();

        props.put("mail.smtp.host", getSmtpHost());
        props.put("mail.smtp.socketFactory.port",
                Integer.toString(getSmtpPort()));
        props.put("mail.smtp.socketFactory.class", getSslSocketFactory());
        props.put("mail.smtp.auth", Boolean.toString(getSmtpAuth()));
        props.put("mail.smtp.port", getSmtpPort());

        return props;
    }

    public boolean getSmtpAuth() {
        return prefs.getBoolean(SMTP_AUTH_PREF, true);
    }

    public void setSmtpAuth(boolean smtpAuth) {
        prefs.putBoolean(SMTP_AUTH_PREF, smtpAuth);
    }

    public String getSslSocketFactory() {
        return prefs.get(SOCKET_FACTORY_CLASS_PREF,
                "javax.net.ssl.SSLSocketFactory");
    }

    public void setSslSocketFactory(String sslSocketFactory) {
        prefs.put(SOCKET_FACTORY_CLASS_PREF, sslSocketFactory);
    }

    public int getSmtpPort() {
        return prefs.getInt(SMTP_PORT_PREF, 465);
    }

    public void setSmtpPort(int smtpPort) {
        prefs.putInt(SMTP_PORT_PREF, smtpPort);
    }

    public String getSmtpHost() {
        return prefs.get(SMTP_HOST_PREF, "");
    }

    public void setSmtpHost(String smtpHost) {
        prefs.put(SMTP_HOST_PREF, smtpHost);
    }

    public String getSmtpUserId() {
        return prefs.get(SMTP_USER_ID_PREF, "");
    }

    public void setSmtpUserId(String smtpUserId) {
        prefs.put(SMTP_USER_ID_PREF, smtpUserId);
    }

    public String getSmtpPassword() {
        return prefs.get(SMTP_PASSWORD_PREF, "");
    }

    public void setSmtpPassword(String smtpPassword) {
        prefs.put(SMTP_PASSWORD_PREF, smtpPassword);
    }

    public String getMailSenderAddress() {
        return prefs.get(MAIL_SENDER_ADDRESS_PREF, "");
    }

    public void setMailSenderAddress(String mailSenderAddress) {
        prefs.put(MAIL_SENDER_ADDRESS_PREF, mailSenderAddress);
    }

    public void flushPrefs() {
        try {
            prefs.flush();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return Authenticator for mail server login.
     */
    Authenticator getMailAuthenticator() {
        return new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(getSmtpUserId(),
                        getSmtpPassword());
            }

        };
    }

    /**
     * @return Adress for sender and from-field.
     */
    InternetAddress getSender() {
        try {
            return new InternetAddress(getMailSenderAddress());
        } catch (AddressException e) {
            LOGGER.severe("Exception in creating sender Address", e);
            // TODO better Error-Handling.
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("mail_server_properties=" + getMailServerProperties());
        builder.append('\n');
        builder.append("sender=" + getSender());
        builder.append('\n');
        builder.append("mail_user_id=" + prefs.get(SMTP_USER_ID_PREF, ""));
        builder.append('\n');
        return builder.toString();
    }
}
