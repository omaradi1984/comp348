package assignment4;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;

public class EmailAttachementSender {

    public static void main(String[] args) {
        String filePath = "path/to/email/config.txt"; // Adjust the file path
        sendEmail(filePath, "path/to/attachment/file");
    }

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
