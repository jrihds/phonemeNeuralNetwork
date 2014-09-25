import java.awt.*;
import java.awt.event.KeyEvent;

import javax.swing.*;

public class JR_drawGraph extends JPanel {

	double graph_data[][] = new double[200][5];
	int x_array[] = new int[202];
	int y_array[] = new int[202];
	int speed;
	boolean trans;
	String text[] = new String[200];
	
	public JR_drawGraph() {
		x_array[200]=1000;
		y_array[200]=600;
		x_array[201]=0;
		y_array[201]=600;
		speed=1;
		trans = false;
		for (int i=0; i<text.length; i++) {
			text[i]="";
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		g2.drawString("Pause (Space)     Faster (+)     Slower (-)    Transparent (T)", 10, 15);
		g2.drawString("Warp Factor: "+speed, 10, 35);
		if (trans) g2.drawString("Transparent On", 10, 50);
		else g2.drawString("Transparent Off", 10, 50);
		
		for (int i=0; i<5; i++) {
			if (i==0) g2.setColor(new Color(255, 130, 71));
			if (i==1) g2.setColor(new Color(20, 128, 20));
			if (i==2) g2.setColor(new Color(180, 40, 40));
			if (i==3) g2.setColor(new Color(215, 215, 40));
			if (i==4) g2.setColor(new Color(20, 20, 128));
			if (trans) {g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.35f));}
			for (int j=0; j<200; j++) {
				x_array[j] = speed*j*5;
				y_array[j] = (int)graph_data[j][i];
			}

			g2.fillPolygon(x_array, y_array, 202);
			
			for (int j=0; j<200; j++) {
				g2.setColor(new Color(0, 0, 0));
				g2.setStroke(new BasicStroke(0.7f));
				g2.drawLine(x_array[j], 600, x_array[j], y_array[j]);
			}

			if (trans) g2.setStroke(new BasicStroke(0.3f));
			else g2.setStroke(new BasicStroke(2.0f));
			g2.drawPolygon(x_array, y_array, 202);

			}
		
		for (int j=0; j<200; j++) {
			g2.setColor(new Color(0, 0, 0));
			g2.setStroke(new BasicStroke(0.7f));
			g2.drawString(text[j], x_array[j]+(2*speed)-2, 80);
		}
		
		/*g2.setStroke(new BasicStroke( 5.0f ));
		int count=0;
		for (int j=0; j<200; j++) {
			int x1 = (i*3)+count;
			int y1 = 580;							// Draw Histogram instead of shaded polygon
			int x2 = (i*3)+count;
			int y2 = (int)graph_data[j][i];
			count+=20;
			g2.drawLine(x1, y1, x2, y2);
		}*/
		
	}
	
	public void updateGraph(double[][] in_values, int speed_in, boolean trans_in, boolean play, String text_in, double[][] diph_outputs) {
		if (play) {
			for (int i=0; i<5; i++) {
				int count=199;
				for (int j=0; j<199; j++) {
					graph_data[count][i]=graph_data[count-1][i];
					count-=1;
				}
			}
			for (int i=0; i<5; i++) {
				graph_data[0][i]=(int)500*(1.2-in_values[i][2]);
			}
			int count=199;
			for (int j=0; j<199; j++) {
				text[count]=text[count-1];
				count-=1;
			}
		}
		
		speed = speed_in;
		trans = trans_in;
		text[0]=text_in;
		   
		repaint();
	}

}