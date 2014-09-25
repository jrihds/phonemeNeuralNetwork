package speech;


import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;

public class PJL_drawGraph extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image offScreenImage;
	private Graphics offScreenGraphicsCtx;
	private Dimension screenSize;

	double graph_data[][] = new double[200][5];
	int x_array[] = new int[202];
	int y_array[] = new int[202];
	int speed;
	boolean trans;
	String text[] = new String[200];

	public PJL_drawGraph() {
		super();
		x_array[200] = 1000;
		y_array[200] = 600;
		x_array[201] = 0;
		y_array[201] = 600;
		speed = 1;
		trans = false;
		for (int i = 0; i < text.length; i++) {
			text[i] = "";
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		paintComponent2(g);
		if (true) return;
		
		if (offScreenGraphicsCtx == null || !getSize().equals(screenSize)) {
			screenSize = new Dimension(getSize());
			offScreenImage = createImage(getSize().width, getSize().height);
			offScreenGraphicsCtx = offScreenImage.getGraphics();
		}

		if (offScreenImage != null) {
			g.drawImage(offScreenImage, 0, 0, this);
		}
	}

	public void paintComponent2(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;			// Initilaise drawing variable

		// Draw comments on screen
		g2.drawString(
				"Pause (Space)     Faster (+)     Slower (-)    Transparent (T)",
				10, 15);
		g2.drawString("Warp Factor: " + speed, 10, 35);
		if (trans)
			g2.drawString("Transparent On", 10, 50);
		else
			g2.drawString("Transparent Off", 10, 50);

		
		for (int i = 0; i < 5; i++) {

			// Give each phoneme a different colour
			if (i == 0) g2.setColor(new Color(255, 130, 71));
			if (i == 1) g2.setColor(new Color(20, 128, 20));
			if (i == 2) g2.setColor(new Color(180, 40, 40));
			if (i == 3) g2.setColor(new Color(215, 215, 40));
			if (i == 4) g2.setColor(new Color(20, 20, 128));
			
			// If we want it transparent, make it transparent
			if (trans) {
				g2.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_ATOP, 0.35f));
			}
			
			// Populate X and Y arrays for polygon with values
			// 'speed' stretches the X axis
			// graph_data contains the magnitudes of the phonemes
			for (int j = 0; j < 200; j++) {
				x_array[j] = speed * j * 5;
				y_array[j] = (int) graph_data[j][i];
			}
			
			// Draw the phoneme polygon
			g2.fillPolygon(x_array, y_array, 202);

			// Draw vertical lines to show instances in time
			for (int j = 0; j < 200; j++) {
				g2.setColor(new Color(0, 0, 0));
				g2.setStroke(new BasicStroke(0.7f));
				g2.drawLine(x_array[j], 600, x_array[j], y_array[j]);
			}

			// Draw a line around the polygon to give it definition,
			//  if we have transparent on then make it a thin line
			if (trans) g2.setStroke(new BasicStroke(0.3f));
			else g2.setStroke(new BasicStroke(2.0f));
			g2.drawPolygon(x_array, y_array, 202);

		}
		
		// Draw all the text on the screen
		for (int j = 0; j < 200; j++) {
			g2.setColor(new Color(0, 0, 0));
			g2.setStroke(new BasicStroke(0.7f));
			g2.drawString(text[j], x_array[j] + (2 * speed) - 2, 80);
		}


		/*
		 * g2.setStroke(new BasicStroke( 5.0f )); int count=0; for (int j=0;
		 * j<200; j++) { int x1 = (i*3)+count; int y1 = 580; // Draw Histogram
		 * instead of shaded polygon int x2 = (i*3)+count; int y2 =
		 * (int)graph_data[j][i]; count+=20; g2.drawLine(x1, y1, x2, y2); }
		 */
	}

	public void updateGraph(double[] in_values, int speed_in, 
			boolean trans_in, boolean play, String text_in) {
			
			// if play is true we want the graph to scroll
			if (play) {
				
			// Shift all values down one layer in the polygon array
			for (int i = 0; i < 5; i++) {
				int count = 199;
				for (int j = 0; j < 199; j++) {
					graph_data[count][i] = graph_data[count - 1][i];
					count -= 1;
				}
			}
			
			// Insert new value at the start of the array
			for (int i = 0; i < 5; i++) {
				graph_data[0][i] = (int) 500 * (1.2 - in_values[i]);
			}
			
			// Shift all values down one layer in the text array
			int count = 199;
			for (int j = 0; j < 199; j++) {
				text[count] = text[count - 1];
				count -= 1;
			}
		}

		speed = speed_in;		// Update Speed Value
		trans = trans_in;		// Update Transparency
		text[0] = text_in;		// Insert new text value at start of text array

		
		
		repaint();				// redraw it all
	}
}
