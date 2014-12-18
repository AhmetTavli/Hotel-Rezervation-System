package otelRezervasyonSistemi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.swing.JOptionPane;

import com.mysql.jdbc.Statement;

/**
 * Bu s�n�f Rezervasyon Yapt�ktan sonra m��terinin hesab�na otelleri g�ndericek
 * olan s�n�f
 * 
 * @author ahmet.tavli
 * 
 */
public class ReservazyonBilgileriniGonder {

	private String host = "smtp.gmail.com";
	private String port = "465";
	private String Gonderen = "otel.rezervasyon.sistemi@gmail.com";
	private String Sifre = "OtelRezervasyon";
	private String Kimden = "admin@otelRezervasyonSistemi.com";

	private String SistemdeKalmaSuresiniBaslat = "true";
	private String debug = "true";
	private String SOCKET_FACTORY = "javax.net.ssl.SSLSocketFactory";

	public ReservazyonBilgileriniGonder(String kullaniciEmail, String sehirAdi,
			String yildiz, String minFiyat, String maxFiyat,
			final Connection conDb) {
		try {

			JOptionPane
					.showMessageDialog(
							null,
							"Otel Bilgileri en ge� 15 dk i�in de Mailinize G�nderilcek.",
							"15 dk i�erisinde", JOptionPane.INFORMATION_MESSAGE);

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

			Session session = Session.getDefaultInstance(props, null);
			session.setDebug(false);

			String selectHotel = "select * from otel ot inner join sehirler s on ot.SehirID = s.ID where s.name = '"
					+ sehirAdi
					+ "'"
					+ " and ot.Yildiz = "
					+ yildiz
					+ " and ot.Fiyat >= "
					+ minFiyat
					+ " and ot.Fiyat <= "
					+ maxFiyat;
			Statement selectStatement = (Statement) conDb.createStatement();
			ResultSet selectSet = selectStatement.executeQuery(selectHotel);

			ArrayList<String> otelText = new ArrayList<String>();

			otelText.add("\n\n-----Oteller----\n\n");

			while (selectSet.next()) {
				otelText.add(" Otelin Ad� : " + selectSet.getString("Ad") + " "
						+ "\nOtelin Adresi : " + selectSet.getString("Adres")
						+ " " + "\nOtelin Y�ld�z� : "
						+ selectSet.getString("Yildiz") + " "
						+ "\nOtelin 1 gecelik 1 oda Fiyat� : "
						+ selectSet.getString("Fiyat") + " YTL"
						+ "\nOtelin Web Adresi : "
						+ selectSet.getString("WebAdres")
						+ "\nOtelin �leti�im Bilgileri : "
						+ selectSet.getString("�letisim"));

			}// end while

			if (otelText.isEmpty()) {
				JOptionPane.showMessageDialog(null,
						"Otel Bilgileri G�nderilirken Bir Sorun Olu�tu",
						"Mail Hatas�", JOptionPane.ERROR_MESSAGE);
				return;
			}

			String otellist = "";

			for (String s : otelText) {
				otellist += s;
			}

			// Construct the mail message
			MimeMessage message = new MimeMessage(session);
			message.setText(otellist);
			message.setSubject("Otel Listesi");
			message.setFrom(new InternetAddress(Kimden));
			message.addRecipient(RecipientType.TO, new InternetAddress(
					kullaniciEmail));
			message.saveChanges();

			// Use Transport to deliver the message
			Transport transport = session.getTransport("smtp");
			transport.connect(host, Gonderen, Sifre);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();

			JOptionPane.showMessageDialog(null,
					"Otel Bilgileri Mailinize �letildi", "Ba�ar�l�",
					JOptionPane.INFORMATION_MESSAGE);

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null,
					"Otel Bilgileri Kullan�c�ya G�nderilirken Hata Olu�tu. \n\nNedeni : "
							+ ex.getMessage(), "Veritaban� Hatas�",
					JOptionPane.ERROR_MESSAGE);
		}
	}// end constructor ReservazyonBilgileriniGonder
}
