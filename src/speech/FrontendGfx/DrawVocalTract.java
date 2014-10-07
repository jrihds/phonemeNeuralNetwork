package speech.FrontendGfx;

import java.awt.*;
import javax.swing.*;

//
//@author JER
//
/*
* Paints a vocal tract onscreen, the position of which can be updated
*  by calling vectorMean
*/

public class DrawVocalTract extends JPanel {

	public int posx[] = new int[102];
	public int posy[] = new int[102];
	String text="";
	int phonemes;
	Font font;
	
	public DrawVocalTract(int phonemes) {
		this.phonemes = phonemes;
		font=new Font("sansserif", Font.BOLD, 32);
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		java.awt.Color rgb = new java.awt.Color(239, 170, 180);
		g2.setColor(rgb);
		g2.fillPolygon(posx, posy, 102);
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke( 2.0f ));
		g2.drawPolygon(posx, posy, 102);
		g2.setFont(font);
		g2.drawString(text, 160, 70);
	}
	
	public void vectorMean(double points[][][], double outputs[], String text) {
		this.text=text;
		double sum_x, sum_y, sum, diffx, diffy;
		for (int i=0; i<100; i++)  {
			sum_x=0.0;
			sum_y=0.0;
			sum=0.0;
			for (int k=0; k<phonemes; k++) {
				sum_x+=points[i][0][k]*outputs[k];
				sum_y+=points[i][1][k]*outputs[k];
				sum+=outputs[k];
			}
			diffx=posx[i]-(int)(sum_x/(sum));
			diffy=posy[i]-(int)(sum_y/(sum));
			posx[i]-=0.4*diffx;
			posy[i]-=0.4*diffy;
			posx[i]*=0.82;
			posy[i]*=0.82;
			posx[i]+=25;
		}
		posx[100]=100;
		posy[100]=1200;
		posx[101]=1500;
		posy[101]=0;
		// repaint();
	}
}