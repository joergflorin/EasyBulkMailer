# EasyBulkMailer

EasyBulkMailer is an easy-to-configure bulk mailer for single-user usage on a client.

Features are:

- Command line interface (later I will add a gui)
- Import mail template including attachments in standard format (EML)
- Import receipients in standard format (vCard)
- Configuration of mail server and sending options as standard preferences (using of Java-Preferences API)
- Platform independent (implemented with Java 8).

The latest release can be downloaded from [here](https://github.com/joergflorin/EasyBulkMailer/releases).

Required resources/libraries:

- Java runtime environment (version 8): http://www.oracle.com/technetwork/java/javase/downloads/index.html
- Card me library (http://sourceforge.net/projects/cardme/)
- Apache Commons-Codec library (http://commons.apache.org/proper/commons-codec/download_codec.cgi)

**Usage**

This is just a first attempt, only the "mailing engine" is more or less implemented. Later in the progress I will add gui's
for configuration and start and watch the mailing process, logging, error handling etc.


Start the command line application like follows:

```bash
java -classpath ebm-0.1.jar:lib/javax.mail-1.5.2.jar:lib/cardme-0.4.0.jar:lib/commons-codec-1.10.jar de.casablu.ebm.CommandLineMailer --eml=~/Test.eml --vcards=~/Contacts.vcf
```

You need to configure at least mail server, user and password. On mac you can create a plist-file `~/Library/Preferences/de.casablu.pmlog.plist`like the [sample file]( https://github.com/joergflorin/EasyBulkMailer/blob/master/de.casablu.ebm.plist).
