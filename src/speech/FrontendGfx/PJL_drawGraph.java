package speech.FrontendGfx;


import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

public class PJL_drawGraph extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image offScreenImage;
	private Dimension screenSize;
	private Graphics offScreenGraphics;
	private AffineTransform at;

	double graph_data_previous[] = new double[6];
	double graph_data_new[] = new double[6];
	int in_value_last[] = new int[6];
	int speed;
	int count = 0;
	boolean trans;
	String text;

	public PJL_drawGraph() {
		super();
		speed = 1;
		trans = false;
		text = "";
		setDoubleBuffered(false);
		at = new AffineTransform();
	}

	@Override
	public void paint(Graphics g) {
		//super.paintComponent(g);
		//System.out.println(" PAINT" );
		if (offScreenGraphics == null || !getSize().equals(screenSize)) {
			screenSize = new Dimension(getSize());
			offScreenImage = createImage(getSize().width, getSize().height);
			offScreenGraphics = offScreenImage.getGraphics();
			System.out.println("I just made some gfx");
		}
		System.out.println(speed);
		if (offScreenImage != null) {
			g.drawImage(offScreenImage, 0, 0, null);
		}
		
		// Draw comments on screen
		g.drawString(
				"Pause (Space)     Faster (+)     Slower (-)    Transparent (T)",
				10, 15);
		g.drawString("Warp Factor: " + speed, 10, 35);
		if (trans)
			g.drawString("Transparent On", 10, 50);
		else
			g.drawString("Transparent Off", 10, 50);
	}
	
	public void updateGraph(double[] in_values, int speed_in, 
			boolean trans_in, boolean play, String text_in) {

		speed = speed_in;
		
		if (play) {
			((Graphics2D) offScreenGraphics).setComposite(AlphaComposite.getInstance(
					AlphaComposite.SRC_ATOP, 1.0f));
			offScreenGraphics.copyArea(0, 0, screenSize.width - speed_in,
					screenSize.height, speed_in, 0);
			
			offScreenGraphics.clearRect(0, 0, speed_in, screenSize.height);
			
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
					
					if (true) {
					// If we want it transparent, make it transparent
					if (trans_in) {
						((Graphics2D) offScreenGraphics).setComposite(AlphaComposite.getInstance(
								AlphaComposite.SRC_ATOP, 0.35f));
					} else {
						((Graphics2D) offScreenGraphics).setComposite(AlphaComposite.getInstance(
								AlphaComposite.SRC_ATOP, 1.0f));
					}
					}
					
					int x_singlePolygon[] = {0, speed_in, speed_in, 0};
					int y_singlePolygon[] = {600, 600, y2, y1};
					offScreenGraphics.fillPolygon(x_singlePolygon, y_singlePolygon, 4);
					
					// Draw a line around the polygon to give it definition,
					//  if we have transparent on then make it a thin line
					offScreenGraphics.setColor(Color.BLACK);
					if (trans) ((Graphics2D) offScreenGraphics).setStroke(new BasicStroke(0.3f));
					else ((Graphics2D) offScreenGraphics).setStroke(new BasicStroke(1.0f));
					
					offScreenGraphics.drawPolygon(x_singlePolygon, y_singlePolygon, 4);
					
					offScreenGraphics.setColor(Color.BLACK);
					
					if (count==10/speed) {
 
					    at.setToRotation(-Math.PI/2.0, 1.0, 1.0);
					    ((Graphics2D) offScreenGraphics).setTransform(at);
					    
					    offScreenGraphics.drawString(text_in, -90, 10);
					    
						count=0;
						
						at.setToRotation(0.0, 1.0, 1.0);
					    ((Graphics2D) offScreenGraphics).setTransform(at);
					}
					
					in_value_last[i] = (int) ((int) 500 * (1.2 - in_values[i]));
					
					count++;
				}
			}
			
		}
		repaint();
	}
}
