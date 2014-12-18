package otelRezervasyonSistemi;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JSeparator;
import javax.swing.JComboBox;

import java.awt.Toolkit;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class KullaniciPaneli extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private final String[] Secenekler = { "Seçiniz...", "Profil Güncelle",
			"Rezervasyon Yap", "Geçmiþ Rezervasyon Bilgilerimi Göster",
			"Rezervasyon Ýsteklerimi Göster" };
	private final String[] Admin = { "Seçiniz...", "Profil Güncelle",
			"Rezervasyon Yap", "Geçmiþ Rezervasyon Bilgilerimi Göster",
			"Rezervasyon Ýsteklerimi Göster", "Tüm Kullanýcýlarý Göster" };
	@SuppressWarnings("rawtypes")
	private JComboBox cmbKullanicininYapabilcekleri;
	private ResimliCombo resim;
	private ProfilResmiDuzenleme resimObject = new ProfilResmiDuzenleme();

	/**
	 * Create the frame.
	 */
	public KullaniciPaneli(final Connection conDb, final ResultSet resultObject) {
		setIconImage(Toolkit
				.getDefaultToolkit()
				.getImage(
						KullaniciPaneli.class
								.getResource("/otelRezervasyonSistemi/Resimler/userPanel.png")));
		setTitle("Kullan\u0131c\u0131 Paneli");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 450, 280);
		contentPane = new JPanel();
		contentPane.setOpaque(true);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblOtelRezervasyonSistemine = new JLabel(
				"Otel Rezervasyon Sistemine Ho\u015F Geldiniz");
		lblOtelRezervasyonSistemine.setBackground(Color.DARK_GRAY);
		lblOtelRezervasyonSistemine.setForeground(Color.RED);
		lblOtelRezervasyonSistemine
				.setHorizontalAlignment(SwingConstants.CENTER);
		lblOtelRezervasyonSistemine.setFont(new Font("Arial Black", Font.BOLD,
				14));
		lblOtelRezervasyonSistemine.setBounds(41, 10, 356, 21);
		contentPane.add(lblOtelRezervasyonSistemine);

		JSeparator sprtrBaslik = new JSeparator();
		sprtrBaslik.setBackground(Color.RED);
		sprtrBaslik.setBounds(402, 19, 0, 2);
		contentPane.add(sprtrBaslik);

		try {
			if (resultObject.getString("Email").equals("compeng89@hotmail.com")
					&& resultObject.getString("Sifre").equals("Blitz!89")) {
				resim = new ResimliCombo(Admin);
				cmbKullanicininYapabilcekleri = resim.resimliComboBox(Admin);
			} else {
				resim = new ResimliCombo(Secenekler);
				cmbKullanicininYapabilcekleri = resim
						.resimliComboBox(Secenekler);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			resim = new ResimliCombo(Secenekler);
			cmbKullanicininYapabilcekleri = resim.resimliComboBox(Secenekler);
		}

		cmbKullanicininYapabilcekleri.setBounds(10, 63, 414, 45);
		cmbKullanicininYapabilcekleri.setMaximumRowCount(5);

		cmbKullanicininYapabilcekleri.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent event) {
				// TODO Auto-generated method stub
				if (event.getStateChange() == ItemEvent.SELECTED) {
					switch (cmbKullanicininYapabilcekleri.getSelectedIndex()) {
					case 1: {
						try {
							YeniKullanici yeniObject = new YeniKullanici(conDb,
									resultObject);
							yeniObject.setVisible(true);
							yeniObject.lblGorev.setText("update");
							yeniObject.btnOlustur.setText("Güncelle");
							yeniObject.lblYeniKullanici
									.setText("Profil Güncelleme");

							// Kullanýcýnýn Þu anki Verilerini Sitemde
							// Görüntüleme try {
							yeniObject.txtAd.setText(resultObject
									.getString("Adi"));

							yeniObject.txtSoyad.setText(resultObject
									.getString("Soyadi"));

							yeniObject.txtEmail.setText(resultObject
									.getString("Email"));

							yeniObject.txtPwd.setText(resultObject
									.getString("Sifre"));

							yeniObject.txtGsm.setText(resultObject
									.getString("GSM"));

							/*
							 * yeniObject.txtSehir.setText(resultObject
							 * .getString("Sehir"));
							 */

							yeniObject.cmbSehirler.setSelectedItem(resultObject
									.getString("Sehir"));

							yeniObject.txtKrediKarti.setText(resultObject
									.getString("KrediKartNo"));

							if (resultObject.getString("ProfilResmi") != null) {
								File file = new File(resultObject
										.getString("ProfilResmi"));

								yeniObject.lblResim.setIcon(new ImageIcon(
										resimObject.designImage(file)));
							}

							dispose();

						} catch (SQLException e) { // TODO Auto-generated catch
													// block
							JOptionPane.showMessageDialog(contentPane,
									"Veriler Profile Getirilirken Bir Hata Oluþtu. \n\nNedeni : "
											+ e.getMessage(),
									"Veritabaný Hatasý",
									JOptionPane.ERROR_MESSAGE);
						}

					}
						break;// end case 1

					case 2: {
						Rezervasyon rezervasyonObject = new Rezervasyon(conDb,
								resultObject);

						File file = null;
						try {
							if (resultObject.getString("ProfilResmi") != null) {
								file = new File(resultObject
										.getString("ProfilResmi"));
							}
						} catch (SQLException e) { // TODO Auto-generated catch
													// block
							JOptionPane.showMessageDialog(contentPane,
									"Profil Resmi Yüklenirken Hatayla Karþýlaþýldý. \n\nNedeni"
											+ e.getMessage(),
									"VeriTabaný Hatasý",
									JOptionPane.ERROR_MESSAGE);

							dispose();
						}

						if (file != null) {
							rezervasyonObject.lblResim.setIcon(new ImageIcon(
									resimObject.designImage(file)));

						}

						rezervasyonObject.setVisible(true);

						dispose();

					}

						break;// end case 2

					case 3: {
						GecmisRezervasyonBilgileri gecmisObject = new GecmisRezervasyonBilgileri(
								conDb, resultObject);
						gecmisObject.setVisible(true);

						dispose();
					}
						break;

					case 4: {
						RezervasyonIsteklerimiGoster istekObject = new RezervasyonIsteklerimiGoster(
								conDb, resultObject);
						istekObject.setVisible(true);

						dispose();
					}
						break;

					case 5: {

						TumKullanicilar tumObject = new TumKullanicilar(conDb,
								resultObject);
						tumObject.setVisible(true);

						dispose();
					}
						break;

					}// end switch

				}
			}
		});

		contentPane.add(cmbKullanicininYapabilcekleri);

		JLabel lblOption = new JLabel("");
		lblOption.setHorizontalAlignment(SwingConstants.CENTER);
		lblOption.setIcon(new ImageIcon(KullaniciPaneli.class
				.getResource("/otelRezervasyonSistemi/Resimler/options.png")));
		lblOption.setBounds(41, 113, 218, 128);
		contentPane.add(lblOption);

		JButton btnNewButton = new JButton("Geri D\u00F6n");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Main mainObject = new Main();
				mainObject.setVisible(true);
				dispose();
			}
		});
		btnNewButton.setIcon(new ImageIcon(KullaniciPaneli.class
				.getResource("/otelRezervasyonSistemi/Resimler/return.png")));
		btnNewButton.setFont(new Font("Calibri Light", Font.PLAIN, 15));
		btnNewButton.setBounds(269, 190, 128, 33);
		contentPane.add(btnNewButton);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				int Answer = JOptionPane
						.showConfirmDialog(contentPane,
								"Çýkmak Ýstediðinizden Emin Misiniz?", "Çýkýþ",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				if (Answer == JOptionPane.YES_OPTION) {
					dispose();
				}
			}
		});
	}
}
