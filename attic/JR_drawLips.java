import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class JR_drawLips extends JPanel {

	public int posx[] = new int[51];
	public int posy[] = new int[51];
	public int posx2[] = new int[51];
	public int posy2[] = new int[51];
	
	public void paintComponent(Graphics g) {
		Image img = null;
		try{
			img = ImageIO.read(new File("imagefiles/face.bmp"));
		} catch (IOException e) {}
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(img, 0, 0, null);
		g2.setColor(Color.BLACK);
		g2.fillPolygon(posx, posy, 17);
		java.awt.Color rgb = new java.awt.Color(232, 204, 186);
		g2.setColor(rgb);
		g2.fillPolygon(posx, posy, 51);
		java.awt.Color rgb2 = new java.awt.Color(255,120,120);
		g2.setColor(rgb2);
		g2.fillPolygon(posx2, posy2, 50);
	}
	
	public void vectorMean(double points[][][], double points2[][][], double outputs[][]) {
		double sum_x, sum_y, sum, diffx, diffy;
		for (int i=0; i<51; i++)  {
			sum_x=0.0;
			sum_y=0.0;
			sum=0.0;
			for (int k=0; k<5; k++) {
				sum_x+=points[i][0][k]*outputs[k][2]*0.4;
				sum_y+=points[i][1][k]*outputs[k][2]*0.25;
				sum+=outputs[k][2];
			}
			diffx=posx[i]-(int)(sum_x/(sum));
			diffy=posy[i]-(int)(sum_y/(sum));
			posx[i]=(int)(178.0+(sum_x/(sum)));
			posy[i]=(int)(358.0+(sum_y/(sum)));
		}
		
		for (int i=0; i<51; i++)  {
			sum_x=0.0;
			sum_y=0.0;
			sum=0.0;
			for (int k=0; k<5; k++) {
				sum_x+=points2[i][0][k]*outputs[k][2]*0.4;
				sum_y+=points2[i][1][k]*outputs[k][2]*0.25;
				sum+=outputs[k][2];
			}
			diffx=posx2[i]-(int)(sum_x/(sum));
			diffy=posy2[i]-(int)(sum_y/(sum));
			posx2[i]=(int)(178.0+(sum_x/(sum)));
			posy2[i]=(int)(358.0+(sum_y/(sum)));
		}
		
		repaint();
	}
}