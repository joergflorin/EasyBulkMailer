/*
 * Created on 16.02.2015 by joerg
 */
package de.casablu.ebm.engine;

import java.util.Properties;
import java.util.prefs.Preferences;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import de.casablu.ebm.CommandLineMailer;

/**
 * MailServerConfiguration configured by Preferences.
 * 
 * @author Joerg Florin (git@casa-blu.de)
 */
class MailServerConfiguration {

    private final Preferences prefs = Preferences
            .userNodeForPackage(CommandLineMailer.class);

    private static final String SMTP_HOST_PREF = "SmtpHost";

    private static final String SMTP_PORT_PREF = "SmtpPort";

    private static final String SOCKET_FACTORY_CLASS = "SocketFactoryClass";

    private static final String SMTP_AUTH = "SmtpAuth";

    private static final String USER_ID = "UserId";

    private static final String PASSWORD = "Password";

    private static final String MAIL_SENDER_ADDRESS = "MailSenderAddress";

    /**
     * @return Properties for the mail server.
     */
    Properties getMailServerProperties() {
        Properties props = new Properties();

        props.put("mail.smtp.host", prefs.get(SMTP_HOST_PREF, ""));
        props.put("mail.smtp.socketFactory.port",
                prefs.get(SMTP_PORT_PREF, "465"));
        props.put("mail.smtp.socketFactory.class", prefs.get(
                SOCKET_FACTORY_CLASS, "javax.net.ssl.SSLSocketFactory"));
        props.put("mail.smtp.auth", prefs.get(SMTP_AUTH, "true"));
        props.put("mail.smtp.port", prefs.get(SMTP_PORT_PREF, "465"));

        return props;
    }

    /**
     * @return Authenticator for mail server login.
     */
    Authenticator getMailAuthenticator() {
        return new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(prefs.get(USER_ID, ""),
                        prefs.get(PASSWORD, ""));
            }
        };
    }

    /**
     * @return Adress for sender and from-field.
     */
    InternetAddress getSender() {
        try {
            return new InternetAddress(prefs.get(MAIL_SENDER_ADDRESS, ""));
        } catch (AddressException e) {
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
        builder.append("mail_user_id=" + prefs.get(USER_ID, ""));
        builder.append('\n');
        return builder.toString();
    }
}
