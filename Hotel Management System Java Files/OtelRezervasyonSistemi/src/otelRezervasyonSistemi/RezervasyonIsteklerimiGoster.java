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

public class RezervasyonIsteklerimiGoster extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JScrollPane scroll;
	private String[] KullaniciKolonlari = { "Ad", "Soyad", "Email", "Yildiz",
			"Minimum Fiyat", "Maksimum Fiyat", "Rezervasyon Baþlangýç",
			"Rezervasyon Bitiþ" };
	private String sql;

	/**
	 * Create the frame.
	 */
	public RezervasyonIsteklerimiGoster(final Connection conDb,
			final ResultSet resultObject) {
		setTitle("Rezervasyon \u0130steklerimi G\u00F6ster");
		setIconImage(Toolkit
				.getDefaultToolkit()
				.getImage(
						RezervasyonIsteklerimiGoster.class
								.getResource("/otelRezervasyonSistemi/Resimler/Rezervasyon \u0130steklerimi G\u00F6ster.png")));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);
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
			if (resultObject.getString("Email").equals("compeng89@hotmail.com"))
				sql = "select * from kullanicilar k inner join rezervasyongecmisi rg on k.KullaniciID=rg.KullaniciID ";
			else {
				sql = "select * from kullanicilar k inner join rezervasyongecmisi rg on k.KullaniciID=rg.KullaniciID and k.KullaniciID = "
						+ resultObject.getString("KullaniciID");
			}
			PreparedStatement ps;

			ps = (PreparedStatement) conDb.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				model.addRow(new Object[] { rs.getString("Adi"),
						rs.getString("Soyadi"), rs.getString("Email"),
						rs.getString("Yildiz"), rs.getString("MinimumFiyat"),
						rs.getString("MaksimumFiyat"),
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
