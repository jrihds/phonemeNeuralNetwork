package speech.frontendGfx;


import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

public class DrawScrollingPhonemeGraph extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Image offScreenImage;
	private Dimension screenSize;
	private Graphics offScreenGraphics;
	private AffineTransform at;

	int in_value_last[], speed, phonemes;
	boolean scrollTransparent;

	public DrawScrollingPhonemeGraph(int phonemes) {
		this.phonemes = phonemes;
		speed = 15;
		scrollTransparent = false;
		at = new AffineTransform();
		in_value_last = new int[phonemes];
	//	setDoubleBuffered(false);
	}

	@Override
	public void paint(Graphics g) {
	
		if (offScreenImage != null) {
			g.drawImage(offScreenImage, 0, 0, null);
		}
		
		// Draw comments on screen
		g.drawString(
				"Pause (Space) Faster (+),Slower (-) Transparent (T)",
				10, 15);
		g.drawString("Playback Speed: " + speed, 10, 35);
		
		if (scrollTransparent)
			g.drawString("Transparent On", 10, 50);
		else
			g.drawString("Transparent Off", 10, 50);
		
	}
	
	public void updateGraph(double[] in_values, int speed, 
			boolean trans_in, boolean play, String text) {

		this.speed = speed;
		
		if (offScreenGraphics == null || !getSize().equals(screenSize)) {
			if (getSize().width == 0) return;
			screenSize = new Dimension(getSize());
			offScreenImage = createImage(getSize().width, getSize().height);
			offScreenGraphics = offScreenImage.getGraphics();
		}
		
		if (play) {
			
			((Graphics2D) offScreenGraphics).setComposite(AlphaComposite.getInstance(
					AlphaComposite.SRC_ATOP, 1.0f));
			offScreenGraphics.copyArea(0, 0, screenSize.width - speed,
					screenSize.height, speed, 0);
			offScreenGraphics.clearRect(0, 0, speed, screenSize.height);
			
			for (int i=0; i<6; i++) {
				if (offScreenGraphics != null) {
					
					int y1 = (int) (500 * (1.2 - in_values[i]));
					int y2 = in_value_last[i];
					
					if (i == 0) offScreenGraphics.setColor(new Color(20, 128, 20));
					if (i == 1) offScreenGraphics.setColor(new Color(180, 40, 40));
					if (i == 2) offScreenGraphics.setColor(new Color(255, 130, 71));
					if (i == 3) offScreenGraphics.setColor(new Color(250, 20, 128));
					if (i == 4) offScreenGraphics.setColor(new Color(20, 20, 128));
					if (i == 5) offScreenGraphics.setColor(new Color(215, 215, 40));
					
					// If we want it transparent, make it transparent
					if (trans_in) {
						((Graphics2D) offScreenGraphics)
								.setComposite(AlphaComposite.getInstance(
										AlphaComposite.SRC_ATOP, 0.5f));
					} else {
						((Graphics2D) offScreenGraphics)
								.setComposite(AlphaComposite.getInstance(
										AlphaComposite.SRC_ATOP, 1.0f));
					}
					
					int x_singlePolygon[] = {0, speed, speed, 0};
					int y_singlePolygon[] = {400, 400, y2, y1};
					offScreenGraphics.fillPolygon(x_singlePolygon, y_singlePolygon, 4);
					
					// Draw a line around the polygon to give it definition,
					// if we have transparent on then make it a thin line
					offScreenGraphics.setColor(Color.BLACK);
					if (scrollTransparent)
						((Graphics2D) offScreenGraphics)
								.setStroke(new BasicStroke(0.3f));
					else
						((Graphics2D) offScreenGraphics)
								.setStroke(new BasicStroke(1.0f));

					offScreenGraphics.drawPolygon(x_singlePolygon,
							y_singlePolygon, 4);
					offScreenGraphics.setColor(Color.BLACK);

					at.setToRotation(-Math.PI / 2.0, 1.0, 1.0);
					((Graphics2D) offScreenGraphics).setTransform(at);

					offScreenGraphics.drawString(text, -90, 10);

					at.setToRotation(0.0, 1.0, 1.0);
					((Graphics2D) offScreenGraphics).setTransform(at);

					in_value_last[i] = (int) ((int) 500 * (1.2 - in_values[i]));
					
				}
			}
			
		}
	}
}
