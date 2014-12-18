package otelRezervasyonSistemi;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.mysql.jdbc.PreparedStatement;

import java.awt.Toolkit;

public class TumKullanicilar extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private String[] KullaniciKolonlari = { "Ad", "Soyad",
			"Email", "Þifre", "GSM", "Þehir" };
	private JScrollPane scroll;

	/**
	 * Create the frame.
	 */
	public TumKullanicilar(final Connection conDb, final ResultSet resultObject) {
		setIconImage(Toolkit
				.getDefaultToolkit()
				.getImage(
						TumKullanicilar.class
								.getResource("/otelRezervasyonSistemi/Resimler/T\u00FCm Kullan\u0131c\u0131lar\u0131 G\u00F6ster.png")));
		setTitle("T\u00FCm Kullan\u0131c\u0131lar");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 1183, 226);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

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

		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(KullaniciKolonlari);

		JTable table = new JTable();
		table.setModel(model);

		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setFillsViewportHeight(true);
		scroll = new JScrollPane(table);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		String sql = "select * from kullanicilar";
		PreparedStatement ps;
		try {
			ps = (PreparedStatement) conDb.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String ad = rs.getString("Adi");
				String soyad = rs.getString("Soyadi");
				String email = rs.getString("Email");
				String sifre = rs.getString("Sifre");
				String gsm = rs.getString("GSM");
				String sehir = rs.getString("Sehir");
				model.addRow(new Object[] { ad, soyad, email, sifre, gsm, sehir });
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(contentPane,
					"Kullanýcýlar Listelenirken Hata Meydana Geldi. \n\nHatanýn Nedeni : "
							+ e.getMessage(), "VeriTabaný Hatasý",
					JOptionPane.ERROR_MESSAGE);
		}/* end catch */
		contentPane.add(scroll);
	}/* end TumKullanicilar method */

}
