package assignment4;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;
/**
 * The {@code EmailAttachementSender} class extends email sending capabilities to include attachments.
 * This class allows sending emails with multiple recipients (To, CC, BCC), subject, body, and attachments
 * by reading configuration from a specified file or directly through method parameters.
 * 
 * It supports connecting to any SMTP server specified in the configuration, including authentication,
 * and handles MIME types to attach files to the email.
 * 
 * Usage involves creating an instance of the class, setting the necessary properties, and calling the
 * {@code EmailAttachementSender} method with appropriate parameters.
 */
public class EmailAttachementSender {
	/**
     * Main method to demonstrate the usage of {@code EnhancedEmailSenderWithAttachment}.
     * 
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        String filePath = "path/to/email/config.txt"; // Adjust the file path
        sendEmail(filePath, "path/to/attachment/file");
    }
    /**
     * Sends an email with the specified parameters including attachments.
     * This method sets up the mail session, configures SMTP properties, and sends the email to the specified recipients
     * with the provided subject, body, and attachments.
     *
     * @param host The SMTP server to connect to.
     * @param port The SMTP server port.
     * @param user The user's email address for SMTP authentication.
     * @param password The password for SMTP authentication.
     * @param to Array of primary recipient email addresses.
     * @param cc Array of CC recipient email addresses, can be {@code null}.
     * @param bcc Array of BCC recipient email addresses, can be {@code null}.
     * @param subject The subject of the email.
     * @param body The body text of the email.
     * @param attachments Array of file paths for attachments, can be {@code null}.
     * @throws MessagingException If there is an error during email composition or sending.
     */
    private static void sendEmail(String filePath, String attachmentPath) {
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

            // Create a MimeBodyPart for the email body
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body.toString());

            // Create another MimeBodyPart for the attachment
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(attachmentPath);

            // Create a Multipart object and add the parts to it
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);

            // Set the complete message parts
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailProps.getProperty("mail.smtp.user")));
            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(emailProps.getProperty("mail.smtp.to")));
            message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(emailProps.getProperty("mail.smtp.cc")));
            message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(emailProps.getProperty("mail.smtp.bcc")));
            message.setSubject(subject);
            message.setContent(multipart);

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully with attachment....");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
