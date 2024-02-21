package assignment4;

/**
 * I declare that this assignment is my own work and that all material previously written or published in any source by any other person has been duly acknowledged in the assignment. I have not submitted this work, or a significant part thereof, previously as part of any academic program. In submitting this assignment I give permission to copy it for assessment purposes only.
 * 
 * The {@code EnhancedEmailSender} class provides functionality to send an email with multiple recipients
 * in the To, CC, and BCC fields. It reads the email configuration and content from a specified text file,
 * including server settings, user credentials, recipients, subject, and the body of the email.
 * The format of the input file should specify each of these components clearly, with the body section
 * spanning multiple lines until the end of the file.
 * <p>
 * Example Format of the Input File:
 * <pre>
 * Server: email server
 * User: email account
 * Password: email account password
 * To: primary recipient,primary recipient 2
 * CC: secondary recipient 1,secondary recipient 2
 * BCC: tertiary recipient 1,tertiary recipient 2
 * Subject: Email subject
 * Body:
 * Here is the email body.
 * It can span multiple lines.
 * This is the last line of the email body.
 * </pre>
 * </p>
 * <p>
 * This class utilizes the JavaMail API for email operations, requiring the javax.mail dependency.
 * </p>
 * <H3>TEST CASES</H3>
 * <p>
Correct Configuration File Format

Objective: Verify that the program can read a correctly formatted configuration file and send an email as expected.
Input: A configuration file with valid server details, user credentials, recipient addresses, subject, and body.
Expected Result: The email is sent successfully to the specified recipients with the correct subject and body.
Invalid Server Details

Objective: Test the program's response to invalid SMTP server details.
Input: A configuration file with incorrect server address or port.
Expected Result: The program should report an error indicating that it could not connect to the SMTP server.
Invalid User Credentials

Objective: Test how the program handles invalid user credentials.
Input: A configuration file with incorrect user email or password.
Expected Result: The program should report an authentication failure.
Missing Recipient Information

Objective: Assess the program's behavior when recipient fields (To, CC, BCC) are missing.
Input: Configuration files missing one or more recipient fields.
Expected Result: The program should report an error or send the email only to the provided recipients, depending on the intended behavior for missing fields.
Empty Subject and Body

Objective: Verify the program's ability to handle emails with an empty subject and/or body.
Input: A configuration file with an empty subject line and/or body.
Expected Result: The email is sent with the specified empty fields. The program should handle empty values gracefully.
Large Email Body

Objective: Test the program's capability to send emails with a large body.
Input: A configuration file with a very large email body (several KBs to MBs in size).
Expected Result: The email is sent successfully without truncation or errors.
Invalid Email Addresses

Objective: Determine how the program handles invalid email addresses in the To, CC, and BCC fields.
Input: A configuration file with one or more invalid email addresses.
Expected Result: The program should report an error related to the invalid email addresses.
File Not Found

Objective: Test the program's response when the configuration file is not found.
Input: An invalid file path.
Expected Result: The program should report an error indicating that the file could not be found.
 * </p>
 */

import javax.mail.*;
import javax.mail.internet.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;

public class EmailSender {

    /**
     * The main method that initiates the email sending process by reading the configuration
     * from the specified file path.
     *
     * @param args Command line arguments (not used).
     */

	public static void main(String[] args) {
		String filePath = "C:\\Users\\omara\\eclipse-workspace\\comp348\\src\\assignment4\\sampleEmail.txt"; // Adjust the file path
		sendEmail(filePath);
	}

	private static void sendEmail(String filePath) {
		Properties emailProps = new Properties();
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			String subject = "";
			StringBuilder body = new StringBuilder();
			boolean isBody = false;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("Server: ")) {
					emailProps.put("mail.smtp.host", line.substring(8));
				} else if (line.startsWith("User: ")) {
					emailProps.put("mail.smtp.user", line.substring(6));
				} else if (line.startsWith("Password: ")) {
					emailProps.put("mail.smtp.password", line.substring(10));
				} else if (line.startsWith("To: ")) {
					emailProps.put("mail.smtp.to", line.substring(4));
				} else if (line.startsWith("CC: ")) {
					emailProps.put("mail.smtp.cc", line.substring(4));
				} else if (line.startsWith("BCC: ")) {
					emailProps.put("mail.smtp.bcc", line.substring(5));
				} else if (line.startsWith("Subject: ")) {
					subject = line.substring(9);
				} else if (line.startsWith("Body:")) {
					isBody = true;
				} else if (isBody) {
					body.append(line).append("\n");
				}
			}

			Properties properties = System.getProperties();
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.host", emailProps.getProperty("mail.smtp.host"));

			Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(emailProps.getProperty("mail.smtp.user"),
							emailProps.getProperty("mail.smtp.password"));
				}
			});

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(emailProps.getProperty("mail.smtp.user")));
			message.addRecipients(Message.RecipientType.TO,
					InternetAddress.parse(emailProps.getProperty("mail.smtp.to")));
			message.addRecipients(Message.RecipientType.CC,
					InternetAddress.parse(emailProps.getProperty("mail.smtp.cc")));
			message.addRecipients(Message.RecipientType.BCC,
					InternetAddress.parse(emailProps.getProperty("mail.smtp.bcc")));
			message.setSubject(subject);
			message.setText(body.toString());

			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
