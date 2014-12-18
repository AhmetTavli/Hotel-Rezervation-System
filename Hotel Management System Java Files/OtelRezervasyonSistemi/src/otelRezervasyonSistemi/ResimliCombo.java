package otelRezervasyonSistemi;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ResimliCombo {

	private ImageIcon[] resimDizini;
	private String[] isimDizini;
	@SuppressWarnings("rawtypes")
	private JComboBox cmbObject;

	public ResimliCombo(String[] isimler) {
		isimDizini = isimler;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JComboBox resimliComboBox(String[] isimler) {

		resimDizini = new ImageIcon[isimler.length];
		Integer[] sayiDizini = new Integer[isimler.length];
		for (int i = 0; i < isimler.length; i++) {
			sayiDizini[i] = new Integer(i);
			resimDizini[i] = createImageIcon("/otelRezervasyonSistemi/Resimler/"
					+ isimler[i] + ".png");
			if (resimDizini[i] != null) {
				resimDizini[i].setDescription(isimler[i]);
			}
		}/* end for */
		cmbObject = new JComboBox(sayiDizini);
		ComboBoxRenderer renderer = new ComboBoxRenderer();
		renderer.setPreferredSize(new Dimension(50, 75));
		cmbObject.setRenderer(renderer);
		return cmbObject;
	}

	protected ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = ResimliCombo.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}/* end method ImageIcon */

	@SuppressWarnings("rawtypes")
	class ComboBoxRenderer extends JLabel implements ListCellRenderer {
		private static final long serialVersionUID = 1L;

		public ComboBoxRenderer() {
			setOpaque(true);
		}

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			// TODO Auto-generated method stub

			// Get the selected index. (The index param isn't
			// always valid, so just use the value.)
			int selectedIndex = ((Integer) value).intValue();

			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			// Set the icon and text. If icon was null, say so.
			ImageIcon icon = resimDizini[selectedIndex];
			String isim = isimDizini[selectedIndex];
			setIcon(icon);
			if (icon != null) {
				setText(isim);
				setFont(list.getFont());
			}

			return this;
		}/* end getListCellRenderer */

	}

}
