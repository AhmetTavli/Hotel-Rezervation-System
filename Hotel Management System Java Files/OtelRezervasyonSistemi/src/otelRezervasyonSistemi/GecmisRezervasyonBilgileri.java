package otelRezervasyonSistemi;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.mysql.jdbc.PreparedStatement;

import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GecmisRezervasyonBilgileri extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private String[] KullaniciKolonlari = { "Ad", "Soyad", "Email", "GSM",
			"KrediKartNo", "Otelin Adý", "Rezervasyon Baþlangýç",
			"Rezervasyon Bitiþ" };
	private JScrollPane scroll;
	private String selectGecmis;

	/**
	 * Create the frame.
	 */
	public GecmisRezervasyonBilgileri(final Connection conDb,
			final ResultSet resultObject) {
		setIconImage(Toolkit
				.getDefaultToolkit()
				.getImage(
						GecmisRezervasyonBilgileri.class
								.getResource("/otelRezervasyonSistemi/Resimler/Ge\u00E7mi\u015F Rezervasyon Bilgilerimi G\u00F6ster.png")));
		setTitle("Ge\u00E7mi\u015F Rezervasyon Bilgileri");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 947, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(KullaniciKolonlari);

		JTable table = new JTable();
		table.setModel(model);

		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setFillsViewportHeight(true);
		scroll = new JScrollPane(table);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		try {
			if (resultObject.getString("Email").equals("compeng89@hotmail.com")) {
				selectGecmis = "select distinct k.Adi, k.Soyadi, k.Email,"
						+ "k.KrediKartNo, k.GSM, o.Ad as OtelAd,"
						+ "date_format(date(o.BaslangicTarih), '%D %M %Y') as BaslangicTarih, date_format(date(o.BitisTarih) , '%D %M %Y') as BitisTarih from rezervasyonsistemi rs inner join otel o on rs.OtelID = o.OtelID"
						+ "     inner join kullanicilar k"
						+ "	 on rs.KullaniciID = k.KullaniciID";
			} else {
				selectGecmis = "select distinct k.Adi, k.Soyadi, k.Email,"
						+ "k.KrediKartNo, k.GSM, o.Ad as OtelAd,"
						+ "date_format(date(o.BaslangicTarih), '%D %M %Y') as BaslangicTarih, date_format(date(o.BitisTarih) , '%D %M %Y') as BitisTarih from rezervasyonsistemi rs inner join otel o on rs.OtelID = o.OtelID"
						+ "     inner join kullanicilar k"
						+ "	 on rs.KullaniciID = k.KullaniciID and k.KullaniciID = "
						+ resultObject.getString("KullaniciID");
			}

			PreparedStatement ps;
			try {
				ps = (PreparedStatement) conDb.prepareStatement(selectGecmis);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					model.addRow(new Object[] { rs.getString("Adi"),
							rs.getString("Soyadi"), rs.getString("Email"),
							rs.getString("KrediKartNo"), rs.getString("GSM"),
							rs.getString("OtelAd"),
							rs.getString("BaslangicTarih"),
							rs.getString("BitisTarih") });
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(contentPane,
						"Kullanýcýlar Listelenirken Hata Meydana Geldi. \n\nHatanýn Nedeni : "
								+ e.getMessage(), "VeriTabaný Hatasý",
						JOptionPane.ERROR_MESSAGE);
			}/* end catch */
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(contentPane,
					"Rezervasyon Geçmiþi Getirilirken Bir Sorun Oluþtu. \n\nNedeni : "
							+ ex.getMessage(), "Veritabaný Hatasý",
					JOptionPane.ERROR_MESSAGE);
		}
		contentPane.add(scroll);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				int Answer = JOptionPane
						.showConfirmDialog(contentPane,
								"Çýkmak Ýstediðinizden Emin Misiniz?", "Çýkýþ",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				if (Answer == JOptionPane.YES_OPTION) {
					KullaniciPaneli panel = new KullaniciPaneli(conDb,
							resultObject);
					panel.setVisible(true);
					dispose();
				}
			}
		});

	}

}
