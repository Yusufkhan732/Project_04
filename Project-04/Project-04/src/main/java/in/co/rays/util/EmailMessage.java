package in.co.rays.util;

/**
 * A class to represent an email message with details such as recipient,
 * subject, content, and message type (HTML or Text). Provides getter and setter
 * methods for all fields.
 * 
 * Author: Yusuf Khan Version: 1.0
 */
public class EmailMessage {

	/** Recipient email address */
	private String to;

	/** Subject of the email */
	private String subject;

	/** Content of the email */
	private String message;

	/** Type of the message: HTML or Text */
	private int messageType = TEXT_MSG;

	/** Constant representing HTML message type */
	public static final int HTML_MSG = 1;

	/** Constant representing plain text message type */
	public static final int TEXT_MSG = 2;

	/** Default constructor */
	public EmailMessage() {
	}

	/**
	 * Parameterized constructor to initialize an email message.
	 *
	 * @param to      recipient email address
	 * @param subject subject of the email
	 * @param message content of the email
	 */
	public EmailMessage(String to, String subject, String message) {
		this.to = to;
		this.subject = subject;
		this.message = message;
	}

	/**
	 * Sets the recipient email address.
	 *
	 * @param to recipient email
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * Returns the recipient email address.
	 *
	 * @return recipient email
	 */
	public String getTo() {
		return to;
	}

	/**
	 * Sets the subject of the email.
	 *
	 * @param subject email subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * Returns the subject of the email.
	 *
	 * @return email subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Sets the content of the email.
	 *
	 * @param message email content
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Returns the content of the email.
	 *
	 * @return email content
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the type of the email message.
	 *
	 * @param messageType HTML_MSG or TEXT_MSG
	 */
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	/**
	 * Returns the type of the email message.
	 *
	 * @return HTML_MSG or TEXT_MSG
	 */
	public int getMessageType() {
		return messageType;
	}
}
