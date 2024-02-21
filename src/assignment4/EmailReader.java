package assignment4;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.FlagTerm;
import java.util.Properties;

public class EmailReader {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java GetMail <mail server> <email> <password> [<email number>]");
            return;
        }

        String host = args[0]; // IMAP server address
        String user = args[1]; // Your email ID
        String password = args[2]; // Your password
        Integer emailNumber = args.length == 4 ? Integer.parseInt(args[3]) : null; // Email number to fetch

        checkInbox(host, "imaps", user, password, emailNumber);
    }

    public static void checkInbox(String host, String storeType, String user, String password, Integer emailNumber) {
        try {
            // Set up the mail server properties
            Properties properties = new Properties();
            properties.put("mail.imap.host", host);
            properties.put("mail.imap.port", "993");
            properties.put("mail.imap.starttls.enable", "true");
            Session emailSession = Session.getDefaultInstance(properties);

            // Connect to the email store
            Store store = emailSession.getStore(storeType);
            store.connect(host, user, password);

            // Open the INBOX folder
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            if (emailNumber == null) {
                // Fetch and list all unread emails if no specific email number is provided
                Message[] messages = emailFolder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
                System.out.println("You have " + messages.length + " unread messages.");

                for (int i = 0, n = messages.length; i < n; i++) {
                    Message message = messages[i];
                    System.out.println((i + 1) + ". " + message.getSubject() + " (" + message.getFrom()[0] + ")");
                }
            } else {
                // Fetch and display details of the specific email if the email number is provided
                Message message = emailFolder.getMessage(emailNumber);
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);
                System.out.println("Text: " + message.getContent().toString());
            }

            // Close the folder and store
            emailFolder.close(false);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
