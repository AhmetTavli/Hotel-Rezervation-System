package otelRezervasyonSistemi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public class KrediKartiKontrolu {

	public static final int MASTERCARD = 0, VISA = 1, AMEX = 2, DISCOVER = 3,
			DINERS = 4;

	public boolean uygunKrediKarti(String number, int type) {

		if (number.equals("")) {
			JOptionPane.showMessageDialog(null,
					"Kredi Kartý Kýsmý Boþ Kalamaz", "Kredi Kartý Alaný Boþ",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		Matcher m = Pattern.compile("[^\\d\\s.-]").matcher(number);

		if (m.find()) {
			JOptionPane
					.showMessageDialog(
							null,
							"Kredi Kartý Sadece Rakam, Boþluk, - ve . 'dan oluþmalýdýr.",
							"Yanlýþ Girlimiþ Kredi Kartý",
							JOptionPane.ERROR_MESSAGE);
			return false;
		}

		Matcher matcher = Pattern.compile("[\\s.-]").matcher(number);
		number = matcher.replaceAll("");

		return validate(number, type);
	}// end method uygunKrediKarti

	private boolean validate(String number, int type) {

		if (null == number || number.length() < 12) {
			JOptionPane.showMessageDialog(null,
					"Kredi Kartý Alaný 12 haneden küçük Olamaz",
					"Hatalý Kredi Kartý", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		switch (type) {

		case MASTERCARD:
			if (number.length() != 16
					|| Integer.parseInt(number.substring(0, 2)) < 51
					|| Integer.parseInt(number.substring(0, 2)) > 55) {
				return false;
			}
			break;

		case VISA:
			if ((number.length() != 13 && number.length() != 16)
					|| Integer.parseInt(number.substring(0, 1)) != 4) {
				return false;
			}
			break;

		case AMEX:
			if (number.length() != 15
					|| (Integer.parseInt(number.substring(0, 2)) != 34 && Integer
							.parseInt(number.substring(0, 2)) != 37)) {
				return false;
			}
			break;

		case DISCOVER:
			if (number.length() != 16
					|| Integer.parseInt(number.substring(0, 4)) != 6011) {
				return false;
			}
			break;

		case DINERS:
			if (number.length() != 14
					|| ((Integer.parseInt(number.substring(0, 2)) != 36 && Integer
							.parseInt(number.substring(0, 2)) != 38)
							&& Integer.parseInt(number.substring(0, 3)) < 300 || Integer
							.parseInt(number.substring(0, 3)) > 305)) {
				return false;
			}
			break;
		}

		if (type == DISCOVER) { // no luhn validate for DISCOVER
			return true;
		}

		// Luhn Algoritmasý ile kontrol edicez.
		return luhnValidate(number);
	}

	private boolean luhnValidate(String numberString) {

		char[] charArray = numberString.toCharArray();
		int[] number = new int[charArray.length];
		int total = 0;

		for (int i = 0; i < charArray.length; i++) {
			number[i] = Character.getNumericValue(charArray[i]);
		}

		for (int i = number.length - 2; i > -1; i -= 2) {
			number[i] *= 2;

			if (number[i] > 9) {
				number[i] -= 9;
			}
		}

		for (int i = 0; i < number.length; i++) {
			total += number[i];
		}

		if (total % 10 != 0) {
			return false;
		}

		return true;
	}

}
