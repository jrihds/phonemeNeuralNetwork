package video;

import java.awt.*;
import javax.swing.*;

public class DrawWC extends JPanel {
	
	public int red[][] = new int[640][480];
	public int green[][] = new int[640][480];
	public int blue[][] = new int[640][480];
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		for (int i=0; i<640; i++) {
			for (int j=0; j<480; j++) {
				java.awt.Color rgb = new java.awt.Color(red[i][j], green[i][j], blue[i][j]);
				g2.setColor(rgb);
				g2.drawLine(i, j, i, j);
			}
		}
		
	}
	
	public void update(int colors[][]) {
		
		for (int i=0; i<640; i++) {
			for (int j=0; j<480; j++) {
				red[i][j] = ((colors[i][j] >> 16) & 0xff);
				green[i][j] = (colors[i][j] >> 8) & 0xff;
				blue[i][j] = (colors[i][j]) & 0xff;
			}
		}
		
		repaint();
	}
}