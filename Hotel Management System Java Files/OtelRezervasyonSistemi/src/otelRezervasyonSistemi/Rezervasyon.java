package otelRezervasyonSistemi;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.Color;

import javax.swing.SwingConstants;
import javax.swing.JComboBox;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import javax.swing.ImageIcon;

import java.awt.Toolkit;

import javax.swing.border.EtchedBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Rezervasyon extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private String KullaniciAdSoyad;
	@SuppressWarnings("rawtypes")
	private JComboBox cmbSehirler;
	private JTextField txtMinimumFiyat;
	private JTextField txtMaksimumFiyat;
	private JTextField txtBaslangicTarihi;
	public JLabel lblResim;
	private JPanel pnlYildiz;
	private JPanel pnlTarih;
	private JPanel pnlFiyat;
	private JTextField txtBitisTarihi;
	private ResultSet otelIDSet;
	private PreparedStatement preparedStatement;
	@SuppressWarnings("rawtypes")
	private final JComboBox cmbYildiz;
	private String maksimFiyat;

	private void getSelectedOtelID(Connection conDb) {
		try {
			String selectOtelID = "select * from otel ot inner join sehirler s on ot.SehirID = s.ID where s.name = '"
					+ String.valueOf(cmbSehirler.getSelectedItem()) + "'";
			Statement getOtelIDStatement = (Statement) conDb.createStatement();
			otelIDSet = getOtelIDStatement.executeQuery(selectOtelID);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(
					contentPane,
					"Otel Bulunurken Hata Oluþtu. \n\nNedeni : "
							+ ex.getMessage(), "Veritabaný Hatasý",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Sehirler CombobBox ýný doldurmada kullanýlýr.
	 */
	@SuppressWarnings("unchecked")
	private void fillSehirler(Connection conDb, ResultSet resultObject) {
		String selectStatement = "select * from sehirler s inner join otel o on s.ID = o.SehirID";
		try {
			Statement statement = (Statement) conDb.createStatement();
			resultObject = statement.executeQuery(selectStatement);
			while (resultObject.next()) {
				cmbSehirler.addItem(resultObject.getString("name"));
			}/* end while */
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(contentPane,
					"Sehirler Doldurulurken Bir Hata Meydana Geldi. \n\nNedeni : "
							+ e.getMessage(), "Veritabaný Hatasý",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Rezervasyon(final Connection conDb, final ResultSet resultObject) {
		setBackground(new Color(240, 240, 240));
		setIconImage(Toolkit
				.getDefaultToolkit()
				.getImage(
						Rezervasyon.class
								.getResource("/otelRezervasyonSistemi/Resimler/reservationMainIcon.png")));
		setTitle("Rezervasyon");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 688, 387);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		try {
			KullaniciAdSoyad = resultObject.getString("Adi") + " "
					+ resultObject.getString("Soyadi");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(contentPane,
					"Ýsim ve Soyisminiz getirilirken bir hata Oluþtu. \n\nHatanýn Nedeni "
							+ e.getMessage(), "Veritabaný Hatasý",
					JOptionPane.ERROR_MESSAGE);
		}
		JLabel lblKullaniciAdSoyad = new JLabel(KullaniciAdSoyad);
		lblKullaniciAdSoyad.setHorizontalAlignment(SwingConstants.CENTER);
		lblKullaniciAdSoyad.setForeground(Color.BLACK);
		lblKullaniciAdSoyad.setFont(new Font("Arial Black", Font.BOLD, 12));
		lblKullaniciAdSoyad.setBounds(444, 11, 218, 28);
		contentPane.add(lblKullaniciAdSoyad);

		JLabel lblRezervasyonSayfamzaHo = new JLabel(
				"Rezervasyon Sayfam\u0131za Ho\u015F Geldiniz ! Say\u0131n");
		lblRezervasyonSayfamzaHo.setForeground(Color.RED);
		lblRezervasyonSayfamzaHo
				.setFont(new Font("Arial Black", Font.BOLD, 16));
		lblRezervasyonSayfamzaHo.setHorizontalAlignment(SwingConstants.CENTER);
		lblRezervasyonSayfamzaHo.setBounds(10, 11, 429, 28);
		contentPane.add(lblRezervasyonSayfamzaHo);

		final JButton btnRezervasyonYaptir = new JButton(
				"Rezervasyon Yapt\u0131r");
		btnRezervasyonYaptir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {

					String checkOtel = "select * from otel ot where ot.Fiyat between "
							+ txtMinimumFiyat.getText()
							+ " and "
							+ txtMaksimumFiyat.getText();
					Statement checkOtelStatement = (Statement) conDb
							.prepareStatement(checkOtel);
					ResultSet checkSet = checkOtelStatement
							.executeQuery(checkOtel);

					if (checkSet.next()) {

						String insertRezervasyon = "insert into rezervasyonsistemi values(default,?,?)";
						PreparedStatement preparedStatement = (PreparedStatement) conDb
								.prepareStatement(insertRezervasyon);

						preparedStatement.setInt(1,
								Integer.parseInt(resultObject
										.getString("KullaniciID")));
						getSelectedOtelID(conDb);
						if (otelIDSet.next())
							preparedStatement.setInt(2, Integer
									.parseInt(otelIDSet.getString("OtelID")));
						preparedStatement.executeUpdate();

						JOptionPane
								.showMessageDialog(
										contentPane,
										"Rezervasyonunuz Baþarýlý Bir Þekilde Tamamlanmýþtýr.",
										"Baþarýlý",
										JOptionPane.INFORMATION_MESSAGE);

						// Kullanýcýlara Otel Bilgilerini Yollucak.
						@SuppressWarnings("unused")
						ReservazyonBilgileriniGonder gonderObject = new ReservazyonBilgileriniGonder(
								resultObject.getString("Email"), otelIDSet
										.getString("name"), otelIDSet
										.getString("Yildiz"), txtMinimumFiyat
										.getText(), txtMaksimumFiyat.getText(),
								conDb);
					} else {
						JOptionPane
								.showMessageDialog(
										contentPane,
										"Sistemde Aradýðýnýz Fiyat Bulunamadý. Lütfen Fiyat Kriterlerini Gözden Geçiriniz.",
										"Fiyatý Yükseltmelisiniz",
										JOptionPane.ERROR_MESSAGE);
					}

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(contentPane,
							"Rezervasyon Bilgileriniz Alýnýrken Hata Oluþtu. \n\nNedeni : "
									+ ex.getMessage(), "Veritabaný Hatasý",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnRezervasyonYaptir
				.setIcon(new ImageIcon(
						Rezervasyon.class
								.getResource("/otelRezervasyonSistemi/Resimler/reservation.png")));
		btnRezervasyonYaptir.setFont(new Font("Calibri Light", Font.PLAIN, 15));
		btnRezervasyonYaptir.setBounds(318, 298, 189, 41);
		contentPane.add(btnRezervasyonYaptir);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(64,

		64, 64), new Color(255, 175, 175), Color.CYAN, Color.GREEN));
		panel.setBounds(10, 50, 145, 143);
		contentPane.add(panel);

		lblResim = new JLabel("");
		lblResim.setHorizontalAlignment(SwingConstants.CENTER);
		lblResim.setBounds(10, 11, 125, 125);
		panel.add(lblResim);

		JPanel pnlSehir = new JPanel();
		pnlSehir.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.BLUE,
				Color.MAGENTA));
		pnlSehir.setBounds(199, 50, 308, 48);
		contentPane.add(pnlSehir);
		pnlSehir.setLayout(null);

		JLabel lblehirSeiniz = new JLabel("1) \u015Eehir Se\u00E7iniz :");
		lblehirSeiniz.setBounds(10, 11, 118, 28);
		pnlSehir.add(lblehirSeiniz);
		lblehirSeiniz.setForeground(Color.BLUE);
		lblehirSeiniz.setBackground(Color.DARK_GRAY);
		lblehirSeiniz.setFont(new Font("Calibri Light", Font.BOLD, 15));
		lblehirSeiniz.setHorizontalAlignment(SwingConstants.CENTER);

		final JButton btnVazgec = new JButton("Vazge\u00E7");
		btnVazgec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String insertStatement = "insert into rezervasyongecmisi values (default,?,?,?,?,?,?,?)";
					preparedStatement = (PreparedStatement) conDb
							.prepareStatement(insertStatement);
					preparedStatement.setInt(1, cmbSehirler.getSelectedIndex());
					preparedStatement.setInt(2, Integer.parseInt(String
							.valueOf(cmbYildiz.getSelectedItem())));
					preparedStatement.setString(3, txtMinimumFiyat.getText());
					preparedStatement.setInt(4, Integer.parseInt(resultObject
							.getString("KullaniciID")));
					preparedStatement.setString(5, txtBaslangicTarihi.getText());
					preparedStatement.setString(6, txtBitisTarihi.getText());
					preparedStatement.setString(7, txtMaksimumFiyat.getText());
					preparedStatement.executeUpdate();

					JOptionPane
							.showMessageDialog(
									contentPane,
									"Baþarýlý Bir Þekilde Rezervasyon Geçmiþi Kaydedildi.",
									"Baþarýlý", JOptionPane.INFORMATION_MESSAGE);

					KullaniciPaneli panelObject = new KullaniciPaneli(conDb,
							resultObject);
					panelObject.setVisible(true);

					dispose();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(contentPane,
							"Rezervasyon Geçmiþini Kaydederken Bir Hata Oluþtu. \n\nNedeni : "
									+ ex.getMessage(), "Veritabaný Hataný",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnVazgec
				.setIcon(new ImageIcon(
						Rezervasyon.class
								.getResource("/otelRezervasyonSistemi/Resimler/cancelReservation.png")));
		btnVazgec.setFont(new Font("Calibri Light", Font.PLAIN, 15));
		btnVazgec.setBounds(199, 298, 109, 40);
		contentPane.add(btnVazgec);

		pnlYildiz = new JPanel();
		pnlYildiz.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.BLUE,
				Color.MAGENTA));
		pnlYildiz.setBounds(199, 101, 308, 48);
		contentPane.add(pnlYildiz);
		pnlYildiz.setLayout(null);

		// Sistemdeki Otellerin Yýldýzlarýný Göstercek.
		cmbYildiz = new JComboBox();
		cmbYildiz.setBounds(138, 15, 160, 20);
		cmbYildiz.addItemListener(new ItemListener() {
			// Yýldýz Seçildiðinde Fiyat ve Tarih Bilgileri Ekranda gözükçek
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				pnlFiyat.setVisible(true);
				pnlTarih.setVisible(true);
				try {
					String selectTarih = "select min(date_format(date(BaslangicTarih), '%D %M %Y')) as BaslangicTarih ,max(date_format(date(BitisTarih), '%D %M %Y')) as BitisTarih from otel ot inner join sehirler s on ot.SehirID = s.ID  where s.name = '"
							+ String.valueOf(cmbSehirler.getSelectedItem())
							+ "'";
					Statement tarih = (Statement) conDb.createStatement();
					ResultSet tarihSet = tarih.executeQuery(selectTarih);
					if (tarihSet.next()) {
						txtBaslangicTarihi.setText(tarihSet
								.getString("BaslangicTarih"));

						txtBitisTarihi.setText(tarihSet.getString("BitisTarih"));
					}

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(contentPane,
							"Tarih Aralýðý Getirilirken Hata Oluþtu. \n\nNedeni : "
									+ ex.getMessage(), "Veritabaný Hatasý",
							JOptionPane.ERROR_MESSAGE);
					dispose();
				}

				btnRezervasyonYaptir.setVisible(true);
				btnVazgec.setVisible(true);
			}

		});

		cmbSehirler = new JComboBox();
		cmbSehirler.setModel(new DefaultComboBoxModel(
				new String[] { "Se\u00E7iniz..." }));
		cmbSehirler.setBounds(138, 15, 160, 20);

		cmbSehirler.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				// TODO Auto-generated method stub
				if (arg0.getStateChange() == ItemEvent.SELECTED
						&& cmbSehirler.getSelectedIndex() != 0) {
					try {
						String selectOtel = "select * from otel ot inner join sehirler s on ot.SehirID = s.ID where s.name = '"
								+ String.valueOf(cmbSehirler.getSelectedItem())
								+ "'";// Sehrin ID
						// si;

						Statement otelstatement = (Statement) conDb
								.createStatement();

						ResultSet otelSet = otelstatement
								.executeQuery(selectOtel);

						if (otelSet.next()) {
							// Þehirler seçildiðinde Yýldýz Panelini Göstersin

							try {
								String selectYildiz = "select distinct Yildiz from otel ot inner join sehirler s on ot.SehirID = s.ID where s.name = '"
										+ String.valueOf(cmbSehirler
												.getSelectedItem()) + "'";
								Statement yildizStatement = (Statement) conDb
										.createStatement();

								ResultSet yildizSet = yildizStatement
										.executeQuery(selectYildiz);

								ArrayList<String> otelYildizDizin = new ArrayList<String>();
								otelYildizDizin.add("Seçiniz...");
								while (yildizSet.next()) {
									otelYildizDizin.add(yildizSet
											.getString("Yildiz"));
								}

								cmbYildiz.setModel(new DefaultComboBoxModel(
										otelYildizDizin.toArray()));

								cmbYildiz.setFont(new Font("Calibri Light",
										Font.PLAIN, 15));

								pnlYildiz.add(cmbYildiz);

								pnlYildiz.setVisible(true);

							} catch (Exception ex) {
								JOptionPane.showMessageDialog(contentPane,
										"Otelin Yýldýzlarýný Getirirken Hata Oluþtu. \n\nNedeni : "
												+ ex.getMessage(),
										"Veritabaný Hatasý",
										JOptionPane.ERROR_MESSAGE);
							}

						} else {
							JOptionPane
									.showMessageDialog(
											contentPane,
											"Seçtiðiniz Þehirdeki Oteller Daha Sistemimize Eklenmemiþtir.",
											"Sistemde Otel Yok",
											JOptionPane.ERROR_MESSAGE);

							pnlYildiz.setVisible(false);
						}
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(contentPane,
								"Seçtiðiniz Þehir ile ilgili Otel Ararken Hata Oluþtu. \n\nNedeni : "
										+ ex.getMessage(), "Veritabaný Hatasý",
								JOptionPane.ERROR_MESSAGE);
					}// end catch
				}

			}
		});

		pnlSehir.add(cmbSehirler);
		cmbSehirler.setBackground(Color.WHITE);
		cmbSehirler.setFont(new Font("Calibri Light", Font.PLAIN, 15));

		JLabel lblYldzSeiniz = new JLabel("2) Y\u0131ld\u0131z Se\u00E7iniz :");
		lblYldzSeiniz.setForeground(Color.BLUE);
		lblYldzSeiniz.setBackground(Color.DARK_GRAY);
		lblYldzSeiniz.setBounds(10, 11, 118, 28);
		pnlYildiz.add(lblYldzSeiniz);
		lblYldzSeiniz.setFont(new Font("Calibri Light", Font.BOLD, 15));
		lblYldzSeiniz.setHorizontalAlignment(SwingConstants.CENTER);

		pnlFiyat = new JPanel();
		pnlFiyat.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.BLUE,
				Color.MAGENTA));
		pnlFiyat.setBounds(199, 156, 308, 48);
		contentPane.add(pnlFiyat);
		pnlFiyat.setLayout(null);

		JLabel lblNewLabel = new JLabel("3) Fiyat Aral\u0131\u011F\u0131 :  ");
		lblNewLabel.setBounds(10, 11, 118, 20);
		pnlFiyat.add(lblNewLabel);
		lblNewLabel.setForeground(Color.BLUE);
		lblNewLabel.setFont(new Font("Calibri Light", Font.BOLD, 15));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);

		txtMinimumFiyat = new JTextField();
		txtMinimumFiyat.setHorizontalAlignment(SwingConstants.CENTER);
		txtMinimumFiyat.setBounds(138, 11, 61, 20);
		txtMinimumFiyat.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				try {

					String selectMinimumFiyat = "select min(Fiyat) as Fiyat from otel ot inner join sehirler s on ot.SehirID = s.ID where s.name = '"
							+ String.valueOf(cmbSehirler.getSelectedItem())
							+ "'";// Sehrin ID
					// si;

					Statement fiyatstatement = (Statement) conDb
							.createStatement();

					ResultSet fiyatSet = fiyatstatement
							.executeQuery(selectMinimumFiyat);

					if (fiyatSet.next()) {

						if (Integer.parseInt(txtMinimumFiyat.getText()) > Integer
								.parseInt(fiyatSet.getString("Fiyat"))) {
							JOptionPane.showMessageDialog(contentPane,
									"Minimum Fiyat Deðeri en Büyük : "
											+ fiyatSet.getString("Fiyat")
											+ " olmalýdýr.", "Otel Yok",
									JOptionPane.ERROR_MESSAGE);

							txtMinimumFiyat.setText(fiyatSet.getString("Fiyat"));
						}
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(contentPane,
							"Minimum Otel Fiyatý Getirilirken Bir Hata Oluþtu. \n\nNedeni : "
									+ ex.getMessage(), "Veritabaný Hatasý",
							JOptionPane.ERROR_MESSAGE);
				}
			}

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		pnlFiyat.add(txtMinimumFiyat);
		txtMinimumFiyat.setColumns(10);

		JLabel lblAnotherSlash = new JLabel("-");
		lblAnotherSlash.setBounds(209, 11, 27, 20);
		pnlFiyat.add(lblAnotherSlash);
		lblAnotherSlash.setHorizontalAlignment(SwingConstants.CENTER);

		txtMaksimumFiyat = new JTextField();
		txtMaksimumFiyat.setHorizontalAlignment(SwingConstants.CENTER);
		txtMaksimumFiyat.setBounds(237, 11, 61, 20);
		txtMaksimumFiyat.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub

				try {

					if (Integer.parseInt(txtMinimumFiyat.getText()) > Integer
							.parseInt(txtMaksimumFiyat.getText())) {
						JOptionPane
								.showMessageDialog(
										contentPane,
										"Soldaki Tarafta Minimum fiyat, Sað Tarafta Maksimum Fiyat Olmalýdýr.",
										"Minimum - Maksimum Fiyat Hatasý",
										JOptionPane.ERROR_MESSAGE);

						return;
					}

					String selectMinimumFiyat = "select max(Fiyat) as Fiyat from otel ot inner join sehirler s on ot.SehirID = s.ID where s.name = '"
							+ String.valueOf(cmbSehirler.getSelectedItem())
							+ "'";// Sehrin ID
					// si;

					Statement fiyatstatement = (Statement) conDb
							.createStatement();

					ResultSet fiyatSet = fiyatstatement
							.executeQuery(selectMinimumFiyat);

					while (fiyatSet.next()) {

						maksimFiyat = fiyatSet.getString("Fiyat");

						if (Integer.parseInt(txtMaksimumFiyat.getText()) < Integer
								.parseInt(fiyatSet.getString("Fiyat"))) {
							JOptionPane
									.showMessageDialog(
											contentPane,
											"Maksimum Fiyat Deðeri en Düþük : "
													+ String.valueOf(Integer.parseInt(fiyatSet
															.getString("Fiyat")) + 5)
													+ " olmalýdýr.",
											"Otel Yok",
											JOptionPane.ERROR_MESSAGE);

							txtMaksimumFiyat.setText(String.valueOf(Integer
									.parseInt(fiyatSet.getString("Fiyat")) + 5));
							return;
						}

					}
				} catch (Exception ex) {

					int maksimumText = Integer.valueOf(maksimFiyat) + 5;
					txtMaksimumFiyat.setText(String.valueOf(maksimumText));
				}

			}

			@Override
			public void focusGained(FocusEvent arg0) {

			}
		});
		pnlFiyat.add(txtMaksimumFiyat);
		txtMaksimumFiyat.setColumns(10);

		pnlTarih = new JPanel();
		pnlTarih.setLayout(null);
		pnlTarih.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.BLUE,
				Color.MAGENTA));
		pnlTarih.setBounds(199, 208, 308, 79);
		contentPane.add(pnlTarih);

		JLabel lblTarihAral = new JLabel("4) Ba\u015Flang\u0131\u00E7 Tarihi :");
		lblTarihAral.setForeground(Color.BLUE);
		lblTarihAral.setBounds(10, 10, 140, 27);
		pnlTarih.add(lblTarihAral);
		lblTarihAral.setFont(new Font("Calibri Light", Font.BOLD, 15));
		;

		txtBaslangicTarihi = new JTextField();
		txtBaslangicTarihi.setBackground(Color.WHITE);
		txtBaslangicTarihi.setForeground(Color.BLACK);
		txtBaslangicTarihi.setHorizontalAlignment(SwingConstants.CENTER);
		txtBaslangicTarihi.setFont(new Font("Calibri Light", Font.BOLD, 15));
		txtBaslangicTarihi.setEnabled(false);
		txtBaslangicTarihi.setEditable(false);

		txtBaslangicTarihi.setBounds(160, 11, 138, 26);
		pnlTarih.add(txtBaslangicTarihi);
		txtBaslangicTarihi.setColumns(10);

		txtBitisTarihi = new JTextField();
		txtBitisTarihi.setHorizontalAlignment(SwingConstants.CENTER);
		txtBitisTarihi.setForeground(Color.BLACK);
		txtBitisTarihi.setFont(new Font("Calibri Light", Font.BOLD, 15));
		txtBitisTarihi.setEnabled(false);
		txtBitisTarihi.setEditable(false);
		txtBitisTarihi.setColumns(10);
		txtBitisTarihi.setBackground(Color.WHITE);
		txtBitisTarihi.setBounds(160, 48, 138, 27);
		pnlTarih.add(txtBitisTarihi);

		JLabel lblBitisTarihi = new JLabel("4) Biti\u015F Tarihi :");
		lblBitisTarihi.setForeground(Color.BLUE);
		lblBitisTarihi.setFont(new Font("Calibri Light", Font.BOLD, 15));
		lblBitisTarihi.setBounds(10, 48, 140, 27);
		pnlTarih.add(lblBitisTarihi);

		JLabel lblReservationSymbol = new JLabel("");
		lblReservationSymbol.setHorizontalAlignment(SwingConstants.CENTER);
		lblReservationSymbol
				.setIcon(new ImageIcon(
						Rezervasyon.class
								.getResource("/otelRezervasyonSistemi/Resimler/reservationSymbol.png")));
		lblReservationSymbol.setBounds(517, 50, 145, 289);
		contentPane.add(lblReservationSymbol);

		JLabel lblSymbol2 = new JLabel("");
		lblSymbol2.setHorizontalAlignment(SwingConstants.CENTER);
		lblSymbol2
				.setIcon(new ImageIcon(
						Rezervasyon.class
								.getResource("/otelRezervasyonSistemi/Resimler/reservationSymbol2.png")));
		lblSymbol2.setBounds(10, 204, 145, 79);
		contentPane.add(lblSymbol2);

		JButton btnNewButton = new JButton("Geri D\u00F6n");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				KullaniciPaneli panelObject = new KullaniciPaneli(conDb,
						resultObject);
				panelObject.setVisible(true);
				dispose();
			}
		});
		btnNewButton.setIcon(new ImageIcon(Rezervasyon.class
				.getResource("/otelRezervasyonSistemi/Resimler/return.png")));
		btnNewButton.setFont(new Font("Calibri Light", Font.PLAIN, 15));
		btnNewButton.setBounds(10, 298, 145, 41);
		contentPane.add(btnNewButton);

		fillSehirler(conDb, resultObject);// Türkiyedeki tüm þehirleri
											// göstericek.

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				int Answer = JOptionPane
						.showConfirmDialog(contentPane,
								"Çýkmak Ýstediðinizden Emin Misiniz?", "Çýkýþ",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				if (Answer == JOptionPane.YES_OPTION) {
					KullaniciPaneli kullaniciObject = new KullaniciPaneli(
							conDb, resultObject);
					kullaniciObject.setVisible(true);
					dispose();
				}
			}
		});

		pnlYildiz.setVisible(false);
		pnlFiyat.setVisible(false);
		pnlTarih.setVisible(false);
		btnVazgec.setVisible(false);
		btnRezervasyonYaptir.setVisible(false);

	}
}
