package speech.frontendGfx;

import java.awt.*;

import javax.swing.*;

//
//@author JER
//
/*
 * Paints a vocal tract onscreen, the position of which can be updated
 *  by calling vectorMean
 */

public class DrawScrollingSpectrumHistogram extends JPanel {

	static final long serialVersionUID = 0L;
	int onscreenBins;
	double magn[];

	public DrawScrollingSpectrumHistogram(int onscreenBins) {
		this.onscreenBins = onscreenBins;
		magn = new double[onscreenBins];
	}

	public void paint(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;
		java.awt.Color rgb = new java.awt.Color(0, 0, 255);
		g2.setColor(rgb);
		int xpoints[] = {0, 320, 320, 0};
		int ypoints[] = {0, 0, 400, 400};
		g2.fillPolygon(xpoints, ypoints, 4);
		g2.setStroke(new BasicStroke( 2.0f ));
		
		for (int i = 0; i < onscreenBins; i++) {
			int red = (int)(magn[i]*200);
			int green = (int)(magn[i]*200);
			if (red>255) red=255;
			if (green>255) green=255;
			rgb = new java.awt.Color(red, green, 0);
			g2.setColor(rgb);
			g2.drawLine(0, i*3, (int)(magn[i]*100), i*3);
		}

	}

	public void update(double[] magnIn) {
		int j = onscreenBins - 1;
		for (int i = 0; i < onscreenBins; i++) {
			magn[i] = Math.log10(magnIn[j]+1);
			j--;
		}
	//	repaint();
	}
}