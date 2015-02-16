# EasyBulkMailer

EasyBulkMailer is an easy-to-configure bulk mailer for single-user usage on a client.

Features are:

- Command line interface (later I will add a gui)
- Import mail template including attachments in standard format (EML)
- Import receipients in standard format (vCard)
- Configuration of mail server and sending options as standard preferences (using of Java-Preferences API)
- Platform independent (implemented with Java 8).

**Usage**

This is just a first attempt, only the "mailing engine" is more or less implemented. Later in the progress I will add gui's
for configuration and start and watch the mailing process, logging, error handling etc.

You will need a Java runtime environment (at least version 8) (free downloadable from here: http://www.oracle.com/technetwork/java/javase/downloads/index.html)

You will also need the JavaMail API (free downloadable from here: http://www.oracle.com/technetwork/java/javamail/index.html).

Finally you will need Card Me (free downloadable from here: http://sourceforge.net/projects/cardme/) and Apache Commons (http://commons.apache.org/proper/commons-codec/download_codec.cgi).

A more detailed usage instruction will follow...