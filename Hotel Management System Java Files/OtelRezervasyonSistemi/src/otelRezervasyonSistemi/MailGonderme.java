package otelRezervasyonSistemi;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MailGonderme {

	private final JFrame contentPane = new JFrame();

	/* Alttaki veriler Þifremi Unuttum a týklayýnca gelcek olan veriler. */
	private String host = "smtp.gmail.com";
	private String port = "465";
	private String Gonderen = "otel.rezervasyon.sistemi@gmail.com";
	private String Sifre = "OtelRezervasyon";
	private String Kimden = "admin@otelRezervasyonSistemi.com";

	private String SistemdeKalmaSuresiniBaslat = "true";
	private String debug = "true";
	private String SOCKET_FACTORY = "javax.net.ssl.SSLSocketFactory";

	public synchronized void mailGonder(String getUserEmail,
			String getUserPassword) {
		// Use Properties object to set environment properties
		Properties props = new Properties();

		props.put("mail.smtp.host", host);/* gmail Host */
		props.put("mail.smtp.port", port);/* Port */
		props.put("mail.smtp.user", Gonderen);

		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", SistemdeKalmaSuresiniBaslat);
		props.put("mail.smtp.debug", debug);

		props.put("mail.smtp.socketFactory.port", port);/* gmail port */
		props.put("mail.smtp.socketFactory.class", SOCKET_FACTORY);
		props.put("mail.smtp.socketFactory.fallback", "false");

		try {

			Session session = Session.getDefaultInstance(props, null);
			session.setDebug(true);

			// Construct the mail message
			MimeMessage message = new MimeMessage(session);
			message.setText("Email :" + getUserEmail + "\nÞifreniz : "
					+ getUserPassword);
			message.setSubject("Otel Rezervasyon Sistemi Þifreniz");
			message.setFrom(new InternetAddress(Kimden));
			message.addRecipient(RecipientType.TO, new InternetAddress(
					getUserEmail));
			message.saveChanges();

			// Use Transport to deliver the message
			Transport transport = session.getTransport("smtp");
			transport.connect(host, Gonderen, Sifre);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();

			JOptionPane.showMessageDialog(contentPane,
					"Þifreniz Email Adresinize Gönderildi.", "Baþarýlý",
					JOptionPane.INFORMATION_MESSAGE);

		} catch (Exception e) {

			JOptionPane
					.showMessageDialog(contentPane,
							"Þifreniz Mailinize Gönderilirken Hata Oluþtu. \n\nNedeni : "
									+ e.getMessage(), "Hata",
							JOptionPane.ERROR_MESSAGE);
		}

	}
}
