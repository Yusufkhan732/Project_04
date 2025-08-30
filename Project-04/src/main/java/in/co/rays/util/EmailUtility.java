package in.co.rays.util;

import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import in.co.rays.exception.ApplicationException;

/**
 * Utility class for sending email messages using SMTP. Supports both HTML and
 * plain text emails. Configurations are loaded from a ResourceBundle.
 * 
 * Author: Yusuf Khan Version: 1.0
 */
public class EmailUtility {

	/** Resource bundle for system properties */
	static ResourceBundle rb = ResourceBundle.getBundle("in.co.rays.bundle.system");

	/** SMTP server host name */
	private static final String SMTP_HOST_NAME = rb.getString("smtp.server");

	/** SMTP server port */
	private static final String SMTP_PORT = rb.getString("smtp.port");

	/** Sender email address */
	private static final String emailFromAddress = rb.getString("email.login");

	/** Sender email password */
	private static final String emailPassword = rb.getString("email.pwd");

	/** Properties object for mail configuration */
	private static Properties props = new Properties();

	/** Static block to initialize mail properties */
	static {
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");
		props.put("mail.debug", "true");
		props.put("mail.smtp.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");
	}

	/**
	 * Sends an email message using SMTP server.
	 *
	 * @param emailMessageDTO the EmailMessage object containing recipient, subject,
	 *                        content, and type
	 * @throws ApplicationException if there is an error sending the email
	 */
	public static void sendMail(EmailMessage emailMessageDTO) throws ApplicationException {
		try {
			// Setup mail session
			Session session = Session.getDefaultInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(emailFromAddress, emailPassword);
				}
			});

			// Create and setup the email message
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(emailFromAddress));
			msg.setRecipients(Message.RecipientType.TO, getInternetAddresses(emailMessageDTO.getTo()));
			msg.setSubject(emailMessageDTO.getSubject());

			// Set content type based on message type
			String contentType = emailMessageDTO.getMessageType() == EmailMessage.HTML_MSG ? "text/html" : "text/plain";
			msg.setContent(emailMessageDTO.getMessage(), contentType);

			// Send the email
			Transport.send(msg);

		} catch (Exception ex) {
			throw new ApplicationException("Email Error: " + ex.getMessage());
		}
	}

	/**
	 * Converts a comma-separated string of emails into an array of InternetAddress
	 * objects.
	 *
	 * @param emails comma-separated email addresses
	 * @return array of InternetAddress objects
	 * @throws Exception if any email address is invalid
	 */
	private static InternetAddress[] getInternetAddresses(String emails) throws Exception {
		if (emails == null || emails.isEmpty()) {
			return new InternetAddress[0];
		}
		String[] emailArray = emails.split(",");
		InternetAddress[] addresses = new InternetAddress[emailArray.length];
		for (int i = 0; i < emailArray.length; i++) {
			addresses[i] = new InternetAddress(emailArray[i].trim());
		}
		return addresses;
	}
}