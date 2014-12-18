package otelRezervasyonSistemi;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

class ProfilResmiDuzenleme {

	private int type = 0;
	private BufferedImage originalImage;

	private BufferedImage resizeImage(BufferedImage originalImage, int type) {
		BufferedImage resizedImage = new BufferedImage(125, 125, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, 125, 125, null);
		g.dispose();

		return resizedImage;
	}

	protected BufferedImage designImage(File fileObject) {
		try {
			originalImage = ImageIO.read(fileObject);
			type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB
					: originalImage.getType();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resizeImage(originalImage, type);
	}/* end designImage method */
}
