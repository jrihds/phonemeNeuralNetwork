package uk.ac.bath.test;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class ScrollingGraph extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image offScreenImage;
	private Graphics offScreenGraphics;
	private Dimension screenSize;

	public ScrollingGraph() {
	
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//System.out.println(" PAINT" );
		if (offScreenGraphics == null || !getSize().equals(screenSize)) {
			screenSize = new Dimension(getSize());
			offScreenImage = createImage(getSize().width, getSize().height);
			offScreenGraphics = offScreenImage.getGraphics();
		}

		if (offScreenImage != null) {
			g.drawImage(offScreenImage, 0, 0, this);
		}
	}

	int in_value_last = 0;

	public void updateGraph(double in_value,int dx) {
		//int dx = 10;
		if (offScreenGraphics != null) {
			offScreenGraphics.copyArea(0, 0, screenSize.width - dx,
					screenSize.height, dx, 0);
			int y1 = (int) in_value;
			//System.out.println( y1);
			int y2 = in_value_last;
			offScreenGraphics.clearRect(0, 0, dx, screenSize.height);
			offScreenGraphics.setColor(Color.red);

			int x_singlePolygon[] = {0, dx, dx, 0};
			int y_singlePolygon[] = {600, 600, y2, y1};
			
			offScreenGraphics.fillPolygon(x_singlePolygon, y_singlePolygon, 4);
			
			in_value_last = (int) in_value;
		}
		repaint();
	}

	static double t = 0;

	public static void main(String args[]) {
		JFrame frame = new JFrame();
		final ScrollingGraph panel = new ScrollingGraph();
		frame.setContentPane(panel);
		frame.setSize(400, 300);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final int dt=10;
		Timer timer = new Timer(dt, new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				//System.out.println( " XXX");
				t += dt/400.0;
				panel.updateGraph(150 + Math.sin(t)*100,2);

			}

		});
		timer.start();

	}
}
