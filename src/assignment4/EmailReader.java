package assignment4;

import javax.mail.*;
import javax.mail.Flags.Flag;
import javax.mail.search.FlagTerm;
import java.util.Properties;
/**
 * I declare that this assignment is my own work and that all material previously written or published in any source by any other person has been duly acknowledged in the assignment. I have not submitted this work, or a significant part thereof, previously as part of any academic program. In submitting this assignment I give permission to copy it for assessment purposes only.
 * 
 * The {@code EmailReader} class provides functionalities to connect to an email server using IMAP,
 * list unread emails, and retrieve specific emails based on a provided reference number.
 * It supports command-line arguments to specify the email server, user credentials, and optionally
 * the email number to retrieve.
 *
 * Usage:
 * To list unread emails:
 * {@code java GetMail <mail server> <email> <password>}
 * 
 * To retrieve a specific email by its number:
 * {@code java GetMail <mail server> <email> <password> <email number>}
 */
public class EmailReader {
	/**
     * The main method that processes input arguments and either lists unread emails or retrieves
     * a specific email based on the provided reference number.
     *
     * @param args Command line arguments containing the mail server, email, password, and optionally the email number.
     */
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java EmailReader <mail server> <email> <password> [<email number>]");
            return;
        }

        String host = args[0]; // IMAP server address
        String user = args[1]; // Your email ID
        String password = args[2]; // Your password
        Integer emailNumber = args.length == 4 ? Integer.parseInt(args[3]) : null; // Email number to fetch

        checkInbox(host, "imaps", user, password, emailNumber);
    }
    /**
     * Connects to the email server and either lists all unread emails or retrieves a specific email,
     * depending on whether a reference number is provided.
     *
     * @param host The email server address.
     * @param storeType The store type, typically "imaps" for secure IMAP.
     * @param user The user's email address.
     * @param password The user's password.
     * @param emailNumber The optional reference number of a specific email to retrieve. If null, all unread emails are listed.
     */
    public static void checkInbox(String host, String storeType, String user, String password, Integer emailNumber) {
        try {
            // Set up the mail server properties
            Properties properties = new Properties();
            properties.put("mail.imap.host", host);
            properties.put("mail.imap.port", "993");
            properties.put("mail.imap.starttls.enable", "true");
            properties.put("mail.imap.ssl.trust", host);
            Session emailSession = Session.getDefaultInstance(properties);

            // Connect to the email store
            Store store = emailSession.getStore(storeType);
            store.connect(host, user, password);

            // Open the INBOX folder
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_WRITE);

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
            	Message[] messages = emailFolder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            	
                Message message = emailFolder.getMessage(messages[emailNumber-1].getMessageNumber());
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);
                System.out.println("Text: " + message.getContent().toString());
                message.setFlag(Flag.SEEN, true);
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
