import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import javax.swing.*;

public class JR_drawTract extends JPanel {

	public int posx[] = new int[102];
	public int posy[] = new int[102];
	String text="";
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		java.awt.Color rgb = new java.awt.Color(239, 170, 180);
		g2.setColor(rgb);
		g2.fillPolygon(posx, posy, 102);
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke( 2.0f ));
		g2.drawPolygon(posx, posy, 102);
		g2.setFont(new Font("sansserif", Font.BOLD, 32));
		g2.drawString(text, 300, 80);
	}
	
	public void vectorMean(double points[][][], double outputs[][], String text_in) {
		text=text_in;
		double sum_x, sum_y, sum, diffx, diffy;
		for (int i=0; i<100; i++)  {
			sum_x=0.0;
			sum_y=0.0;
			sum=0.0;
			for (int k=0; k<5; k++) {
				sum_x+=points[i][0][k]*outputs[k][2];
				sum_y+=points[i][1][k]*outputs[k][2];
				sum+=outputs[k][2];
			}
			diffx=posx[i]-(int)(sum_x/(sum));
			diffy=posy[i]-(int)(sum_y/(sum));
			posx[i]-=0.4*diffx;
			posy[i]-=0.4*diffy;
		}
		posx[100]=100;
		posy[100]=1200;
		posx[101]=1500;
		posy[101]=0;
		repaint();
	}
}