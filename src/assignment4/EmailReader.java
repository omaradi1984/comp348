package assignment4;

import javax.mail.*;
import javax.mail.Flags.Flag;
import javax.mail.search.FlagTerm;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * I declare that this assignment is my own work and that all material
 * previously written or published in any source by any other person has been
 * duly acknowledged in the assignment. I have not submitted this work, or a
 * significant part thereof, previously as part of any academic program. In
 * submitting this assignment I give permission to copy it for assessment
 * purposes only.
 * 
 * The {@code EmailReader} class provides functionalities to connect to an email
 * server using IMAP, list unread emails, and retrieve specific emails based on
 * a provided reference number. It supports command-line arguments to specify
 * the email server, user credentials, and optionally the email number to
 * retrieve.
 *
 * Usage: To list unread emails:
 * {@code java EmailReader <mail server> <email> <password>}
 * 
 * To retrieve a specific email by its number:
 * {@code java EmailReader <mail server> <email> <password> <email number>}
 * 
 * * <h2>Test Scenarios:</h2>
 * <ul>
 *   <li><strong>Scenario 1:</strong> List all unread emails when no email number is provided. This can be tested by providing only the server, email, and password as arguments.</li>
 *   <li><strongScenario 2:</strong> Retrieve a specific email when an email number is provided. This requires providing all four arguments, with the last being the number of the email to retrieve.</li>
 *   <li><strong>Scenario 3:</strong> Handle incorrect login information gracefully, ensuring the program provides a meaningful error message without crashing.</li>
 *   <li><strong>Scenario 4:</strong> Verify that the program can handle an invalid email number, such as one that is out of range, by providing an appropriate error message or fallback behavior.</li>
 * </ul>
 */
public class EmailReader {
	/**
     * Connects to the email server and either lists all unread emails or retrieves
     * a specific email, depending on whether a reference number is provided.
     *
     * @param host        The email server address.
     * @param storeType   The store type, typically "imaps" for secure IMAP.
     * @param user        The user's email address.
     * @param password    The user's password.
     * @param emailNumber The optional reference number of a specific email to
     *                    retrieve. If null, all unread emails are listed.
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
	 * Connects to the email server and either lists all unread emails or retrieves
	 * a specific email, depending on whether a reference number is provided.
	 *
	 * @param host        The email server address.
	 * @param storeType   The store type, typically "imaps" for secure IMAP.
	 * @param user        The user's email address.
	 * @param password    The user's password.
	 * @param emailNumber The optional reference number of a specific email to
	 *                    retrieve. If null, all unread emails are listed.
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
				// Fetch and display details of the specific email if the email number is
				// provided
				Message[] messages = emailFolder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

				Message message = emailFolder.getMessage(messages[emailNumber - 1].getMessageNumber());
				System.out.println("From: " + message.getFrom()[0]);
				System.out.println("Subject: " + message.getSubject());
				if (message.isMimeType("multipart/mixed")) {
					Multipart multipart = (Multipart) message.getContent();
					//System.out.println("Body: " + multipart.getBodyPart(1).getContent().toString());
					for (int i = 0; i < multipart.getCount(); i++) {
						BodyPart bodyPart = multipart.getBodyPart(i);
						if((bodyPart.isMimeType("text/plain")||bodyPart.isMimeType("text/html")) && (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()))==false)
							System.out.println("Body:\n" + bodyPart.getContent().toString());
						else if(bodyPart.isMimeType("multipart/alternative")) {
							Multipart subPart = (Multipart) bodyPart.getContent();
							for (int z = 0; z < subPart.getCount(); z++) {
								BodyPart part = subPart.getBodyPart(z);
								if(part.isMimeType("text/plain")||part.isMimeType("text/html")){
									System.out.println("Body:\n" + part.getContent().toString());
								}
							}
						}
						else if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
						InputStream stream = bodyPart.getInputStream();
						byte[] buffer = new byte[bodyPart.getSize()];
						int read;
						String fileName = bodyPart.getFileName();
						String filePath = "./" + fileName;
						while ((read = stream.read(buffer)) != -1) {
							try (FileOutputStream fout = new FileOutputStream(filePath)) {
								fout.write(buffer);
								System.out.println("File downloaded successfully: " + filePath);
								fout.flush();
								fout.close();
							}
						}
						}
					}
				} else {
					System.out.println("Body:\n" + message.getContent().toString());
				}
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
