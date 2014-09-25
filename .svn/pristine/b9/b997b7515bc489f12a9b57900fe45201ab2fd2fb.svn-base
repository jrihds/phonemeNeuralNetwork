package speech;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

//
// @author JER
//
/*
 * Paints a pair of lips onscreen, the position of which can be updated
 *  by calling vectorMean
 */


public class DrawLips extends JPanel {

	public int posx[] = new int[28];
	public int posy[] = new int[28];
	public int posx2[] = new int[26];
	public int posy2[] = new int[26];
	int phonemes;
	Image img = null;
	public DrawLips(int phonemes) {
		this.phonemes = phonemes;
		
		try{
			img = ImageIO.read(new File("src/speech/imagefiles/face.bmp"));
		} catch (IOException e) {}
	}

	public void paint(Graphics g) {

		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(img, 0, 0, null);
		g2.setColor(Color.BLACK);
		g2.fillPolygon(posx, posy, 8);
		java.awt.Color rgb = new java.awt.Color(232, 204, 186);
		g2.setColor(rgb);
		g2.fillPolygon(posx, posy, 28);
		java.awt.Color rgb2 = new java.awt.Color(255,120,120);
		g2.setColor(rgb2);
		g2.fillPolygon(posx2, posy2, 26);
	}
	
	public void vectorMean(double points[][][], double points2[][][],
			double outputs[]) {

		double sum_x, sum_y, sum;

		for (int i = 0; i < 28; i++) {
			sum_x = 0.0;
			sum_y = 0.0;
			sum = 0.0;
			for (int k = 0; k < phonemes; k++) {
				sum_x += points[i][0][k] * outputs[k] * 0.6;
				sum_y += points[i][1][k] * outputs[k] * 0.47;
				sum += outputs[k];
			}
			posx[i] = (int) (-48.0 + (sum_x / (sum)));		// Locate the lips on the
			posy[i] = (int) (100.0 + (sum_y / (sum)));		// screen (178 & 358)
		}

		for (int i = 0; i < 26; i++) {
			sum_x = 0.0;
			sum_y = 0.0;
			sum = 0.0;
			for (int k = 0; k < phonemes; k++) {
				sum_x += points2[i][0][k] * outputs[k] * 0.6;
				sum_y += points2[i][1][k] * outputs[k] * 0.47;
				sum += outputs[k];
			}
			posx2[i] = (int) (-48.0 + (sum_x / (sum)));
			posy2[i] = (int) (100.0 + (sum_y / (sum)));
		}

		//repaint();
	}
}