# EasyBulkMailer

EasyBulkMailer is an easy-to-configure bulk mailer for single-user usage on a client.

Features are:

- GUI (very simple yet)
- Command line interface (also simple)
- Import mail template including attachments in standard format (EML)
- Import receipients in standard format (vCard)
- Configuration of mail server and sending options as standard preferences (using of Java-Preferences API)
- Platform independent (implemented with Java 8).

The latest release can be downloaded from [here](https://github.com/joergflorin/EasyBulkMailer/releases).

Required resources/libraries (included in [lib](https://github.com/joergflorin/EasyBulkMailer/tree/master/lib)):

- Java runtime environment (version 8): http://www.oracle.com/technetwork/java/javase/downloads/index.html
- JavaMail API (https://java.net/projects/javamail/pages/Home).
- Card me library (http://sourceforge.net/projects/cardme/)
- Apache Commons-Codec library (http://commons.apache.org/proper/commons-codec/download_codec.cgi)

**Usage**

This is just a first attempt, only the "mailing engine" is more or less implemented. You can start a very simple gui tool or use a command line tool. Later in the progress I will add a more complex gui with contacts preview start, interrupt and checkpoint/restart features.

You need to configure at least mail server, user and password. On mac you can create a plist-file `~/Library/Preferences/de.casablu.pmlog.plist`like the [sample file]( https://github.com/joergflorin/EasyBulkMailer/blob/master/de.casablu.ebm.plist).

*GUI tool*

- Download release jar from [here](https://github.com/joergflorin/EasyBulkMailer/releases).
- Start the "jar" by double clicking the jar file or with the command `java -jar ebm.jar`

The activities are logged to the console window. You will find information about how many contacts are found and how many and what messages are successfully sent or not.

You may do a dry-run with just analyzing the contacts by click "Cancel" on the selection dialog for the EML file. The contacts (vcards) and the recipients (name, email address) will be listed on the console window.

*Preparation of command line tool*

- Download release jar from [here](https://github.com/joergflorin/EasyBulkMailer/releases).
- Download bash script from [here](https://github.com/joergflorin/EasyBulkMailer/tree/master/bash), this script is tested with Mac OS X, maybe it will run unchanged on Linux. A command script for Windows will be provided later.
- Put both files in the same directory.

*Starting the command line tool*

- Open shell (e.g. Mac OS X Terminal).
- Change to the directory with the jar und bash-file.
- Type following command

```bash
. ./ebm.sh --eml=/path-to-testxml/Test.eml --vcards=/path-to-contactsvcf/Contacts.vcf
```

The activities are logged to stdout. You will find information about how many contacts are found and how many and what messages are successfully sent or not.

The --eml argument is optional. If not set, only the contacts will be analyzed and listed
like in the gui tool (see above).
