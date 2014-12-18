package otelRezervasyonSistemi;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.Color;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;

import com.mysql.jdbc.Statement;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JPasswordField;
import java.awt.Toolkit;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	private final JLabel lblOtelRezervasyonSistemiBaslik;
	private final JLabel lblKullaniciAdi;
	private final JLabel lblSifre;
	private final JButton btnYeniKullanici;
	private JButton btnSifremiUnuttum;
	private JButton btnUye;
	private JPanel contentPane;
	private JTextField txtKullaniciAdi;
	private final JButton btnGiris;
	private final String dbUrl = "jdbc:mysql://localhost:3306/otelrezervasyonsistemi";
	private final String username = "ahmet";
	private final String pwd = "ahmet";
	private Connection conDb; /* Veri Tabanina Baðlantýsý */
	private Statement selectStatement;
	private ResultSet resultSet;
	private boolean connection;/* Veritabanýna Baðlantý Saðlandýðýnda */
	private boolean connectionSet = false; /* Kullanýc Bilgileri Doðru ise */
	private JPasswordField txtSifre;
	private String sql = "select * from kullanicilar";

	private boolean availableInternetConnection() {
		boolean result = false;
		Socket socketObject = new Socket();
		InetSocketAddress inetObject = new InetSocketAddress("www.google.com",
				80);// Port 80 den google'a girmeye çalýþýyoruz.
		try {
			socketObject.connect(inetObject, 3000);/*
													 * 3 ms kadar google.com a
													 * baðlanmaya çalýþýyorum.
													 */
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(contentPane,
					"Ýntenet Baðlantýnýzýn Olduðuna Emin Olunuz",
					"Baðlantý Hatasý", JOptionPane.ERROR_MESSAGE);
		} finally {
			try {
				socketObject.close();/*
									 * Baðlandýktan sonra hata meydana gelmezse
									 * kapatýyorum.
									 */
				result = true;
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(contentPane,
						"Ýnternet Baðlantýsý Test Edilirken Bir Hata Meydana Geldi. \n\nNedeni : "
								+ ex.getMessage(), "Baðlantý Hatasý",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		return result;
	}/* end method availableInternetConnection */

	private boolean DatabaseConnection(JLabel setPicture) {
		try {
			conDb = DriverManager.getConnection(dbUrl, username, pwd);
			setPicture.setIcon(new ImageIcon(Main.class
					.getResource("/otelRezervasyonSistemi/Resimler/ok.jpg")));
			return true;
		} catch (Exception ex) {
			try {
				setPicture.setIcon(new ImageIcon(getClass().getResource(
						"cancel.png")));

			} catch (Exception ex1) {
				JOptionPane.showMessageDialog(this,
						"Veritabanýna Baðlanýrken Hata Oluþtu.\n\n Nedeni : "
								+ ex.getMessage() + "\n\n", "Hata",
						JOptionPane.ERROR_MESSAGE);
			}
			return false;
		}
	}// end method DatabaseConnection

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		setIconImage(Toolkit
				.getDefaultToolkit()
				.getImage(
						Main.class
								.getResource("/otelRezervasyonSistemi/Resimler/mainIcon.png")));
		setTitle("Otel Rezervasyon Sistemi Yapan Ahmet Tavli");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 464, 162);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				int Answer = JOptionPane
						.showConfirmDialog(contentPane,
								"Çýkmak Ýstediðinizden Emin Misiniz?", "Çýkýþ",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				if (Answer == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});

		final MailGonderme mailObject = new MailGonderme();

		lblOtelRezervasyonSistemiBaslik = new JLabel("Otel Rezervasyon Sistemi");
		lblOtelRezervasyonSistemiBaslik
				.setHorizontalAlignment(SwingConstants.CENTER);
		lblOtelRezervasyonSistemiBaslik.setForeground(Color.RED);
		lblOtelRezervasyonSistemiBaslik.setFont(new Font("Arial Black",
				Font.BOLD, 16));
		lblOtelRezervasyonSistemiBaslik.setBounds(10, 11, 440, 41);
		contentPane.add(lblOtelRezervasyonSistemiBaslik);

		btnYeniKullanici = new JButton("Yeni Kullan\u0131c\u0131y\u0131m");
		btnYeniKullanici.setIcon(new ImageIcon(Main.class
				.getResource("/otelRezervasyonSistemi/Resimler/newUser.png")));
		btnYeniKullanici.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				lblOtelRezervasyonSistemiBaslik.setEnabled(false);
				lblKullaniciAdi.setEnabled(false);
				lblSifre.setEnabled(false);
				txtKullaniciAdi.setEnabled(false);
				txtSifre.setEnabled(false);
				btnGiris.setEnabled(false);
				btnYeniKullanici.setEnabled(false);

				final YeniKullanici yeniObject = new YeniKullanici(conDb,
						resultSet);
				yeniObject.setVisible(true);
				yeniObject.lblGorev.setText("insert");
				yeniObject.lblYeniKullanici.setText("Yeni Kullanici");

				dispose();
			}
		});
		btnYeniKullanici.setFont(new Font("Calibri Light", Font.PLAIN, 14));
		btnYeniKullanici.setBounds(10, 63, 205, 57);
		contentPane.add(btnYeniKullanici);

		final JPanel pnlGiris = new JPanel();
		pnlGiris.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(255,
				0, 0), Color.YELLOW, null, null));
		pnlGiris.setBackground(Color.LIGHT_GRAY);
		pnlGiris.setBounds(10, 131, 440, 142);
		pnlGiris.setVisible(false);
		contentPane.add(pnlGiris);
		pnlGiris.setLayout(null);

		lblKullaniciAdi = new JLabel("Email :");
		lblKullaniciAdi.setHorizontalAlignment(SwingConstants.CENTER);
		lblKullaniciAdi.setBackground(Color.ORANGE);
		lblKullaniciAdi.setForeground(Color.BLACK);
		lblKullaniciAdi.setBounds(10, 11, 50, 23);
		pnlGiris.add(lblKullaniciAdi);
		lblKullaniciAdi.setFont(new Font("Calibri Light", Font.BOLD, 15));

		txtKullaniciAdi = new JTextField();
		txtKullaniciAdi.setBounds(70, 12, 222, 20);
		pnlGiris.add(txtKullaniciAdi);
		txtKullaniciAdi.setColumns(10);

		lblSifre = new JLabel("\u015Eifre :");
		lblSifre.setForeground(Color.BLACK);
		lblSifre.setBounds(10, 46, 50, 23);
		pnlGiris.add(lblSifre);
		lblSifre.setFont(new Font("Calibri Light", Font.BOLD, 15));

		txtSifre = new JPasswordField();
		txtSifre.setBounds(70, 43, 222, 21);
		pnlGiris.add(txtSifre);

		btnGiris = new JButton("Giri\u015F");
		btnGiris.setIcon(new ImageIcon(Main.class
				.getResource("/otelRezervasyonSistemi/Resimler/login.png")));
		btnGiris.setBounds(161, 69, 130, 62);
		pnlGiris.add(btnGiris);
		btnGiris.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {

					selectStatement = (Statement) conDb.createStatement();
					resultSet = selectStatement.executeQuery(sql);

					String kullaniciAdi = txtKullaniciAdi.getText();
					String sifre = new String(txtSifre.getPassword());

					while (resultSet.next()) {
						if (kullaniciAdi.equals(resultSet.getString("Email"))
								&& sifre.equals(resultSet.getString("Sifre"))) {
							connectionSet = true;
							break;
						}
					}// end while

					if (connectionSet) {
						/*
						 * JOptionPane.showMessageDialog(contentPane,
						 * "Sisteme Giriþiniz Baþarýyla Saðlandý.", "Baþarýlý",
						 * JOptionPane.INFORMATION_MESSAGE);
						 */

						KullaniciPaneli panelObject = new KullaniciPaneli(
								conDb, resultSet);
						panelObject.setVisible(true);

						dispose();

					} else {
						JOptionPane
								.showMessageDialog(
										contentPane,
										"Email Adresinizi ve Þifrenizi kontrol ediniz.",
										"Hata", JOptionPane.ERROR_MESSAGE);

						if (kullaniciAdi == null || kullaniciAdi.isEmpty()) {
							txtKullaniciAdi.requestFocus();
						} else if (sifre == null || sifre.isEmpty()) {
							txtSifre.requestFocus();
						}
					}

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(contentPane,
							"Sorgu Sýrasýnda Hata Oluþtu. Nedeni :  " + "\n\n"
									+ e.getMessage(), "Veritabaný Hatasý",
							JOptionPane.ERROR_MESSAGE);

				}
			}
		});
		btnGiris.setFont(new Font("Calibri Light", Font.PLAIN, 14));

		JPanel pnlVeriTabaniBaglantisi = new JPanel();
		pnlVeriTabaniBaglantisi.setBounds(302, 11, 130, 121);
		pnlGiris.add(pnlVeriTabaniBaglantisi);
		pnlVeriTabaniBaglantisi.setBackground(Color.WHITE);
		pnlVeriTabaniBaglantisi.setBorder(new BevelBorder(BevelBorder.LOWERED,
				null, null, null, null));
		pnlVeriTabaniBaglantisi.setForeground(new Color(0, 0, 0));
		pnlVeriTabaniBaglantisi.setLayout(null);

		JLabel lblVeriTabanBalants = new JLabel("Veri Taban\u0131");
		lblVeriTabanBalants.setForeground(new Color(255, 0, 0));
		lblVeriTabanBalants.setFont(new Font("Calibri Light", Font.BOLD, 15));
		lblVeriTabanBalants.setHorizontalAlignment(SwingConstants.CENTER);
		lblVeriTabanBalants.setBounds(10, 11, 110, 19);
		pnlVeriTabaniBaglantisi.add(lblVeriTabanBalants);

		JLabel lblBaglantiDurumu = new JLabel("");
		lblBaglantiDurumu.setBounds(32, 41, 77, 75);
		pnlVeriTabaniBaglantisi.add(lblBaglantiDurumu);

		connection = DatabaseConnection(lblBaglantiDurumu);

		JLabel lblBalants = new JLabel("Ba\u011Flant\u0131s\u0131");
		lblBalants.setForeground(Color.RED);
		lblBalants.setFont(new Font("Calibri Light", Font.BOLD, 15));
		lblBalants.setHorizontalAlignment(SwingConstants.CENTER);
		lblBalants.setBounds(10, 25, 110, 19);
		pnlVeriTabaniBaglantisi.add(lblBalants);

		btnSifremiUnuttum = new JButton("\u015Eifremi Unuttum");
		btnSifremiUnuttum.setIcon(new ImageIcon(Main.class
				.getResource("/otelRezervasyonSistemi/Resimler/lostPwd.png")));
		btnSifremiUnuttum.setBounds(280, 284, 170, 39);
		btnSifremiUnuttum.setVisible(false);
		contentPane.add(btnSifremiUnuttum);
		btnSifremiUnuttum.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/* Önce Ýnternet Baðlantýsýnýn Olup Olmadýðýný Kontrol Et */
				if (availableInternetConnection()) {
					/*
					 * Þifre Email ile Kullanýcýya Gönderilirken Sistemi
					 * kullanýlamaz hale getir.
					 */
					ConnectionBasedEnabling(false);
					try {
						/* Kullanýcýnýn adýný ekrandan al. */
						String kullaniciAdi = txtKullaniciAdi.getText();

						selectStatement = (Statement) conDb.createStatement();
						/* Bu sql Bütün kullanýcýlarý seçiyor. */
						resultSet = selectStatement.executeQuery(sql);

						/*
						 * Ekranda Girilmiþ olan Email Veritabnýnda Olup
						 * Olmadýðýný Kontrol et
						 */
						while (resultSet.next()) {
							if (kullaniciAdi.equals(resultSet
									.getString("Email"))) {
								connectionSet = true;
								break;
							}
						}

						/* Ekranda Girilmiþ Email Veritabanýnda mevcut ise */
						if (connectionSet) {

							mailObject.mailGonder(resultSet.getString("Email"),
									resultSet.getString("Sifre"));

						} else {
							JOptionPane.showMessageDialog(contentPane,
									"Email Adresiniz Sistemde Bulunamadý.",
									"Hata", JOptionPane.ERROR_MESSAGE);
						}

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(contentPane,
								"Emailiniz Araþtýrýlýrken Hata Oluþtu. Nedeni :  "
										+ "\n\n" + e.getMessage(),
								"Veritabaný Hatasý", JOptionPane.ERROR_MESSAGE);
					}

					ConnectionBasedEnabling(true);
				} else {
					JOptionPane.showMessageDialog(contentPane,
							"Lütfen Internet Baðlantýnýzý kontrol ediniz.",
							"Baðlantý Hatasý", JOptionPane.ERROR_MESSAGE);

				}

			}/* end action method */
		});

		btnSifremiUnuttum.setFont(new Font("Calibri Light", Font.PLAIN, 14));

		btnUye = new JButton("\u00DCyeyim");
		btnUye.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pnlGiris.setVisible(true);
				btnSifremiUnuttum.setVisible(true);
				setBounds(100, 100, 464, 362);
				txtKullaniciAdi.requestFocus();
			}
		});
		btnUye.setIcon(new ImageIcon(Main.class
				.getResource("/otelRezervasyonSistemi/Resimler/member.png")));
		btnUye.setFont(new Font("Calibri Light", Font.PLAIN, 14));
		btnUye.setBounds(225, 63, 225, 57);
		contentPane.add(btnUye);

		ConnectionBasedEnabling(connection);

	}

	/**
	 * Veri Tabaný Baðlantýsý yoksa veya önemli bir iþlem yapýlýrken, Sorun
	 * çýkmasýn diye sistemi Geçici Disable Eder.
	 * 
	 * @param conObject
	 *            : true olduðunda sistem çalýþýr durumdadýr. : false Olduðunda
	 *            sistem kulanýlamaz durumdadýr.
	 */
	private void ConnectionBasedEnabling(boolean conObject) {
		lblOtelRezervasyonSistemiBaslik.setEnabled(conObject);
		lblKullaniciAdi.setEnabled(conObject);
		lblSifre.setEnabled(conObject);
		txtSifre.setEnabled(conObject);
		btnGiris.setEnabled(conObject);
		btnYeniKullanici.setEnabled(conObject);
		btnSifremiUnuttum.setEnabled(conObject);
		btnUye.setEnabled(conObject);
	}
}
