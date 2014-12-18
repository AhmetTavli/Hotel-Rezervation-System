package otelRezervasyonSistemi;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JPasswordField;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Toolkit;

import javax.swing.border.EtchedBorder;
import javax.swing.JComboBox;

public class YeniKullanici extends JFrame {

	private static final long serialVersionUID = 1933122569336607532L;
	private JPanel contentPane;
	public JTextField txtAd;
	public JTextField txtSoyad;
	public JTextField txtEmail;
	public JTextField txtGsm;
	public JTextField txtKrediKarti;
	public JButton btnOlustur;
	private PreparedStatement preparedStatement;
	public JPasswordField txtPwd;
	private final String emailregex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
	private boolean validEmailAddress;
	private boolean validCreditCard;
	public JLabel lblGorev;
	public JLabel lblYeniKullanici;
	public JLabel lblResim;
	private File file;
	private ProfilResmiDuzenleme resimObject = new ProfilResmiDuzenleme();
	private KrediKartiKontrolu kartKontrol = new KrediKartiKontrolu();
	private JLabel lblKrediKartiNumarasi;
	private final String[] KrediKartlari = { "Seçiniz...", "Master Card",
			"Visa", "Amex", "Discover", "Diners" };
	private ResimliCombo resim;
	@SuppressWarnings("rawtypes")
	private JComboBox cmbKrediKartiTipi;
	@SuppressWarnings("rawtypes")
	public JComboBox cmbSehirler;

	/**
	 * Create the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public YeniKullanici(final Connection conDb, final ResultSet resultObject) {
		setIconImage(Toolkit
				.getDefaultToolkit()
				.getImage(
						YeniKullanici.class
								.getResource("/otelRezervasyonSistemi/Resimler/newUser.png")));

		setTitle("Kullanici Profili");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 523, 389);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblYeniKullanici = new JLabel("Yeni Kullanici");
		lblYeniKullanici.setForeground(new Color(255, 0, 0));
		lblYeniKullanici.setFont(new Font("Arial Black", Font.BOLD, 14));
		lblYeniKullanici.setHorizontalAlignment(SwingConstants.CENTER);
		lblYeniKullanici.setBounds(10, 0, 442, 30);
		contentPane.add(lblYeniKullanici);

		JPanel pnlKullaniciVerileri = new JPanel();
		pnlKullaniciVerileri.setBorder(new EtchedBorder(EtchedBorder.LOWERED,
				Color.DARK_GRAY, new Color(255, 200, 0)));
		pnlKullaniciVerileri.setBounds(175, 41, 332, 316);
		contentPane.add(pnlKullaniciVerileri);
		pnlKullaniciVerileri.setLayout(null);

		txtAd = new JTextField();
		txtAd.setBounds(93, 11, 229, 20);
		pnlKullaniciVerileri.add(txtAd);
		txtAd.setColumns(10);

		JLabel lblAd = new JLabel("Ad\u0131n\u0131z :");
		lblAd.setBounds(10, 12, 73, 19);
		pnlKullaniciVerileri.add(lblAd);
		lblAd.setFont(new Font("Calibri Light", Font.PLAIN, 15));

		txtSoyad = new JTextField();
		txtSoyad.setBounds(93, 42, 229, 20);
		pnlKullaniciVerileri.add(txtSoyad);
		txtSoyad.setColumns(10);

		txtEmail = new JTextField();
		txtEmail.setBounds(93, 73, 229, 20);
		pnlKullaniciVerileri.add(txtEmail);
		txtEmail.setColumns(10);

		txtPwd = new JPasswordField();
		txtPwd.setBounds(93, 104, 229, 23);
		pnlKullaniciVerileri.add(txtPwd);

		JLabel lblSoyad = new JLabel("Soyadiniz :");
		lblSoyad.setBounds(10, 42, 73, 19);
		pnlKullaniciVerileri.add(lblSoyad);
		lblSoyad.setFont(new Font("Calibri Light", Font.PLAIN, 15));

		JLabel lblEmail = new JLabel("Email :");
		lblEmail.setBounds(10, 74, 73, 19);
		pnlKullaniciVerileri.add(lblEmail);
		lblEmail.setFont(new Font("Calibri Light", Font.PLAIN, 15));

		JLabel lblSifre = new JLabel("\u015Eifre :");
		lblSifre.setBounds(10, 106, 73, 19);
		pnlKullaniciVerileri.add(lblSifre);
		lblSifre.setFont(new Font("Calibri Light", Font.PLAIN, 15));

		txtGsm = new JTextField();
		txtGsm.setBounds(93, 135, 229, 20);
		pnlKullaniciVerileri.add(txtGsm);
		txtGsm.setColumns(10);

		JLabel lblGsm = new JLabel("GSM :");
		lblGsm.setBounds(10, 136, 73, 19);
		pnlKullaniciVerileri.add(lblGsm);
		lblGsm.setFont(new Font("Calibri Light", Font.PLAIN, 15));

		JLabel lblSehir = new JLabel("\u015Eehir :");
		lblSehir.setBounds(10, 166, 73, 19);
		pnlKullaniciVerileri.add(lblSehir);
		lblSehir.setFont(new Font("Calibri Light", Font.PLAIN, 15));

		JLabel lblKrediKart = new JLabel("Kredi Kart\u0131 :");
		lblKrediKart.setBounds(10, 196, 79, 20);
		pnlKullaniciVerileri.add(lblKrediKart);
		lblKrediKart.setFont(new Font("Calibri Light", Font.PLAIN, 15));

		txtKrediKarti = new JTextField();
		txtKrediKarti.setBounds(93, 254, 229, 20);
		pnlKullaniciVerileri.add(txtKrediKarti);
		txtKrediKarti.setColumns(10);

		btnOlustur = new JButton("Olu\u015Ftur");
		btnOlustur
				.setIcon(new ImageIcon(
						YeniKullanici.class
								.getResource("/otelRezervasyonSistemi/Resimler/createUser.png")));
		btnOlustur.setBounds(214, 285, 106, 23);
		pnlKullaniciVerileri.add(btnOlustur);
		btnOlustur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (lblGorev.getText().contains("insert")) {
					try {

						String userPwd = new String(txtPwd.getPassword());

						validEmailAddress = txtEmail.getText().matches(
								emailregex);

						int type = cmbKrediKartiTipi.getSelectedIndex();

						validCreditCard = kartKontrol.uygunKrediKarti(
								txtKrediKarti.getText(), type - 1);

						if (validEmailAddress && validCreditCard) {

							if (file != null) {
								String sqlInsert = "insert into kullanicilar values (default,?,?,?,?,?,?,?,?)";
								preparedStatement = (PreparedStatement) conDb
										.prepareStatement(sqlInsert);
								preparedStatement.setString(1, txtAd.getText());
								preparedStatement.setString(2,
										txtSoyad.getText());
								preparedStatement.setString(3, file.getPath());
								preparedStatement.setString(4,
										txtEmail.getText());
								preparedStatement.setString(5, userPwd);
								preparedStatement.setString(6, txtGsm.getText());
								preparedStatement.setString(7, String
										.valueOf(cmbSehirler.getSelectedItem()));
								preparedStatement.setString(8,
										txtKrediKarti.getText());
								preparedStatement.executeUpdate();
							} else {
								String sqlInsert = "insert into kullanicilar values (default,?,?,null,?,?,?,?,?)";
								preparedStatement = (PreparedStatement) conDb
										.prepareStatement(sqlInsert);
								preparedStatement.setString(1, txtAd.getText());
								preparedStatement.setString(2,
										txtSoyad.getText());
								preparedStatement.setString(3,
										txtEmail.getText());
								preparedStatement.setString(4, userPwd);
								preparedStatement.setString(5, txtGsm.getText());
								preparedStatement.setString(6, String
										.valueOf(cmbSehirler.getSelectedItem()));
								preparedStatement.setString(7,
										txtKrediKarti.getText());
								preparedStatement.executeUpdate();
							}
							JOptionPane
									.showMessageDialog(
											contentPane,
											"Kullanici Baþarýlý Bir Þekilde Oluþturuldu.",
											"Baþarýlý",
											JOptionPane.INFORMATION_MESSAGE);

						} else {
							JOptionPane
									.showMessageDialog(
											contentPane,
											"Emailinizi ve Kredi Kartýnýzý Doðru Biçimde Yazdýðýnýzdan Emin Olunuz!! ",
											"Email Hatasý",
											JOptionPane.ERROR_MESSAGE);

							return;
						}

					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(contentPane,
								"Oluþturma Sýrasýnda Hata Oluþtu. Nedeni :  "
										+ "\n\n" + e1.getMessage(),
								"Veritabaný Hatasý", JOptionPane.ERROR_MESSAGE);
					}// end catch
				} else if (lblGorev.getText().contains("update")) {

					try {

						String userPwd = new String(txtPwd.getPassword());

						validEmailAddress = txtEmail.getText().matches(
								emailregex);

						int type = cmbKrediKartiTipi.getSelectedIndex();

						if (type - 1 < 0) {
							JOptionPane.showMessageDialog(contentPane,
									"Lütfen Kredi kartý Tipini Seçiniz",
									"Kredi Kartý Hatasý",
									JOptionPane.ERROR_MESSAGE);
							return;
						}

						validCreditCard = kartKontrol.uygunKrediKarti(
								txtKrediKarti.getText(), type - 1);

						if (validEmailAddress && validCreditCard) {

							if (file != null) {
								String sqlUpdate = "update kullanicilar set Adi = ?, Soyadi = ?, ProfilResmi=?, Email = ?, Sifre = ?, GSM = ?, Sehir = ?, KrediKartNo  = ? where KullaniciID = "
										+ resultObject.getString("KullaniciID");
								preparedStatement = (PreparedStatement) conDb
										.prepareStatement(sqlUpdate);
								preparedStatement.setString(1, txtAd.getText());
								preparedStatement.setString(2,
										txtSoyad.getText());
								preparedStatement.setString(3, file.getPath());
								preparedStatement.setString(4,
										txtEmail.getText());
								preparedStatement.setString(5, userPwd);
								preparedStatement.setString(6, txtGsm.getText());
								preparedStatement.setString(7, String
										.valueOf(cmbSehirler.getSelectedItem()));
								preparedStatement.setString(8,
										txtKrediKarti.getText());
								preparedStatement.executeUpdate();
							} else {
								String sqlUpdate = "update kullanicilar set Adi = ?, Soyadi = ?, Email = ?, Sifre = ?, GSM = ?, Sehir = ?, KrediKartNo  = ? where KullaniciID = "
										+ resultObject.getString("KullaniciID");
								preparedStatement = (PreparedStatement) conDb
										.prepareStatement(sqlUpdate);
								preparedStatement.setString(1, txtAd.getText());
								preparedStatement.setString(2,
										txtSoyad.getText());
								preparedStatement.setString(3,
										txtEmail.getText());
								preparedStatement.setString(4, userPwd);
								preparedStatement.setString(5, txtGsm.getText());
								preparedStatement.setString(6, String
										.valueOf(cmbSehirler.getSelectedItem()));
								preparedStatement.setString(7,
										txtKrediKarti.getText());
								preparedStatement.executeUpdate();
							}

							JOptionPane
									.showMessageDialog(
											contentPane,
											"Kullanici Baþarýlý Bir Þekilde Güncellendi.",
											"Baþarýlý",
											JOptionPane.INFORMATION_MESSAGE);
						} else {
							JOptionPane
									.showMessageDialog(
											contentPane,
											"Emailinizi Doðru Biçimde Yazdýðýnýzdan Emin Olunuz!! ",
											"Email Hatasý",
											JOptionPane.ERROR_MESSAGE);
							return;
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(contentPane,
								"Oluþturma Sýrasýnda Hata Oluþtu. Nedeni :  "
										+ "\n\n" + e1.getMessage(),
								"Veritabaný Hatasý", JOptionPane.ERROR_MESSAGE);

						return;
					}
				}// end else

				Main mainObject = new Main();
				mainObject.setVisible(true);
				dispose();

			}// end action performed-method
		});
		btnOlustur.setFont(new Font("Calibri Light", Font.PLAIN, 15));

		lblGorev = new JLabel("");
		lblGorev.setHorizontalAlignment(SwingConstants.CENTER);
		lblGorev.setBounds(10, 285, 73, 23);
		pnlKullaniciVerileri.add(lblGorev);
		lblGorev.setEnabled(false);

		JButton btnVazgec = new JButton("Vazgec");
		btnVazgec.setBounds(93, 285, 111, 23);
		pnlKullaniciVerileri.add(btnVazgec);
		btnVazgec.setIcon(new ImageIcon(YeniKullanici.class
				.getResource("/otelRezervasyonSistemi/Resimler/return.png")));
		btnVazgec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (lblGorev.getText().contains("insert")) {
					Main mainObject = new Main();
					mainObject.setVisible(true);
					dispose();
				} else if (lblGorev.getText().contains("update")) {
					KullaniciPaneli panelObject = new KullaniciPaneli(conDb,
							resultObject);
					panelObject.setVisible(true);
					dispose();
				}
			}
		});
		btnVazgec.setFont(new Font("Calibri Light", Font.PLAIN, 15));

		lblKrediKartiNumarasi = new JLabel("Numaras\u0131 :");
		lblKrediKartiNumarasi
				.setFont(new Font("Calibri Light", Font.PLAIN, 15));
		lblKrediKartiNumarasi.setBounds(10, 254, 73, 20);
		pnlKullaniciVerileri.add(lblKrediKartiNumarasi);

		cmbKrediKartiTipi = new JComboBox();
		resim = new ResimliCombo(KrediKartlari);
		cmbKrediKartiTipi = resim.resimliComboBox(KrediKartlari);
		cmbKrediKartiTipi.setBounds(93, 197, 229, 45);
		pnlKullaniciVerileri.add(cmbKrediKartiTipi);

		cmbSehirler = new JComboBox();
		cmbSehirler.setBounds(93, 165, 229, 20);

		String selectStatement = "select * from sehirler";
		try {
			Statement statement = (Statement) conDb.createStatement();
			ResultSet sehirlerSet = statement.executeQuery(selectStatement);
			while (sehirlerSet.next()) {
				cmbSehirler.addItem(sehirlerSet.getString("name"));
			}/* end while */
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(contentPane,
					"Sehirler Doldurulurken Bir Hata Meydana Geldi. \n\nNedeni : "
							+ e.getMessage(), "Veritabaný Hatasý",
					JOptionPane.ERROR_MESSAGE);
		}

		pnlKullaniciVerileri.add(cmbSehirler);

		JButton btnProfilResmi = new JButton("Profil Resmi");
		btnProfilResmi
				.setIcon(new ImageIcon(
						YeniKullanici.class
								.getResource("/otelRezervasyonSistemi/Resimler/profilePicture.png")));
		btnProfilResmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileObject = new JFileChooser();

				/* All Files opsiyonunu kaldýrýcak. */
				fileObject.setAcceptAllFileFilterUsed(false);
				/* Sadece Resim Dosyalarýný Göstericek */
				fileObject.setFileSelectionMode(JFileChooser.FILES_ONLY);

				/* Sistemde tanýmlý tüm resim dosyalarýný getir. */
				String[] suffices = ImageIO.getReaderFileSuffixes();
				for (int i = 0; i < suffices.length; i++) {
					FileFilter filter = new FileNameExtensionFilter(suffices[i]
							+ " dosyalarý", suffices[i]);
					fileObject.addChoosableFileFilter(filter);
				}

				int response = fileObject.showOpenDialog(null);

				if (response == JFileChooser.APPROVE_OPTION) {
					try {

						file = fileObject.getSelectedFile();
						lblResim.setIcon(new ImageIcon(resimObject
								.designImage(file)));

					} catch (Exception ex) {
						JOptionPane.showMessageDialog(contentPane,
								"Resim Seçilirken Hata Oluþtu. \n\nHatanýn Nedeni : "
										+ ex.getMessage(), "Hata",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		btnProfilResmi.setFont(new Font("Calibri Light", Font.PLAIN, 15));
		btnProfilResmi.setBounds(20, 195, 145, 41);
		contentPane.add(btnProfilResmi);

		JPanel pnlResim = new JPanel();
		pnlResim.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(64,
				64, 64), new Color(255, 175, 175), Color.CYAN, Color.GREEN));
		pnlResim.setBounds(20, 41, 145, 143);
		contentPane.add(pnlResim);
		pnlResim.setLayout(null);

		lblResim = new JLabel("");
		lblResim.setHorizontalAlignment(SwingConstants.CENTER);
		lblResim.setIcon(new ImageIcon(
				YeniKullanici.class
						.getResource("/otelRezervasyonSistemi/Resimler/defaultProfilePicture.jpg")));
		lblResim.setBounds(10, 11, 125, 125);
		pnlResim.add(lblResim);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				int Answer = JOptionPane
						.showConfirmDialog(contentPane,
								"Çýkmak Ýstediðinizden Emin Misiniz?", "Çýkýþ",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				if (Answer == JOptionPane.YES_OPTION
						&& lblGorev.getText().contains("insert")) {
					Main mainObject = new Main();
					mainObject.setVisible(true);
					dispose();
				} else if (Answer == JOptionPane.YES_OPTION
						&& lblGorev.getText().contains("update")) {
					KullaniciPaneli kullaniciObject = new KullaniciPaneli(
							conDb, resultObject);
					kullaniciObject.setVisible(true);
					dispose();
				}
			}
		});
	}
}
