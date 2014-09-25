import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

// multidimensional array sort taken from: http://realityisimportant.blogspot.com/

public class JR_ReadImage {

	public double[][][] read() throws IOException {
		
		double all_points[][][] = new double[100][3][5];
		double points[][] = new double[100][3];
		double sorted_points[][] = new double[100][3];
		File file = null;
		
		for (int f=0; f<5; f++) {
			if (f==0) {file = new File("imagefiles/ah_wgr.bmp");}
			if (f==1) {file = new File("imagefiles/ee_wgr.bmp");}
			if (f==2) {file = new File("imagefiles/eh_wgr.bmp");}
			if (f==3) {file = new File("imagefiles/oh_wgr.bmp");}
			if (f==4) {file = new File("imagefiles/oo_wgr.bmp");}
			BufferedImage img = ImageIO.read(file);
			int count=0;
			for (int i=0; i<405; i++) {
				for (int j=0; j<600; j++) {
					double red = (img.getRGB(i,j)>>16)&0xff;
					double green = (img.getRGB(i,j)>>8) & 0xff;
					double blue = (img.getRGB(i,j)) & 0xff;
					if (green==255.0 && blue==0.0 && red<150.0) {
						points[count][0]=(double)i;
						points[count][1]=(double)j;
						points[count][2]=(double)((img.getRGB(i, j) >> 16) & 0xff);
						count++;
					}
				}
			}
			sorted_points=bubbleSortMulti(points, 2);
			for (int i=0; i<100; i++) {
				all_points[i][0][f]=sorted_points[i][0];
				all_points[i][1][f]=sorted_points[i][1];
				all_points[i][2][f]=(double)i;
			}
		}
		return all_points;
	}
	
public double[][][] read_lips_1() throws IOException {
		
		double all_points[][][] = new double[51][3][5];
		double points[][] = new double[51][3];
		double sorted_points[][] = new double[51][3];
		File file = null;
		
		for (int f=0; f<5; f++) {
			if (f==0) {file = new File("imagefiles/lips_ai.bmp");}
			if (f==1) {file = new File("imagefiles/lips_e.bmp");}
			if (f==2) {file = new File("imagefiles/lips_ai.bmp");}
			if (f==3) {file = new File("imagefiles/lips_o.bmp");}
			if (f==4) {file = new File("imagefiles/lips_u.bmp");}
			BufferedImage img = ImageIO.read(file);
			int count=0;
			for (int i=0; i<304; i++) {
				for (int j=0; j<273; j++) {
					double red = (img.getRGB(i,j)>>16)&0xff;
					double green = (img.getRGB(i,j)>>8) & 0xff;
					double blue = (img.getRGB(i,j)) & 0xff;
					if (green==100.0 && blue==100.0 && red<100.0) {
						points[count][0]=(double)i;
						points[count][1]=(double)j;
						points[count][2]=(double)((img.getRGB(i, j) >> 16) & 0xff);
						count++;
						}
					}
				}
			sorted_points=bubbleSortMulti(points, 2);
			for (int i=0; i<51; i++) {
				all_points[i][0][f]=(double)sorted_points[i][0];
				all_points[i][1][f]=(double)sorted_points[i][1];
				all_points[i][2][f]=(double)i;
			}
		}
		return all_points;
	}

public double[][][] read_lips_2() throws IOException {
	
	double all_points[][][] = new double[51][3][5];
	double points[][] = new double[51][3];
	double sorted_points[][] = new double[51][3];
	File file = null;
	
	for (int f=0; f<5; f++) {
		if (f==0) {file = new File("imagefiles/lips_ai.bmp");}
		if (f==1) {file = new File("imagefiles/lips_e.bmp");}
		if (f==2) {file = new File("imagefiles/lips_ai.bmp");}
		if (f==3) {file = new File("imagefiles/lips_o.bmp");}
		if (f==4) {file = new File("imagefiles/lips_u.bmp");}
		BufferedImage img = ImageIO.read(file);
		int count=0;
		for (int i=0; i<304; i++) {
			for (int j=0; j<273; j++) {
				double red = (img.getRGB(i,j)>>16)&0xff;
				double green = (img.getRGB(i,j)>>8) & 0xff;
				double blue = (img.getRGB(i,j)) & 0xff;
				if (green<100.0 && blue==100.0 && red==100.0) {
					points[count][0]=(double)i;
					points[count][1]=(double)j;
					points[count][2]=(double)green;
					count++;
					}
				}
			}
		sorted_points=bubbleSortMulti(points, 2);
		for (int i=0; i<51; i++) {
			all_points[i][0][f]=(double)sorted_points[i][0];
			all_points[i][1][f]=(double)sorted_points[i][1];
			all_points[i][2][f]=(double)i;
			all_points[1][0][0]=116.0;
			all_points[1][1][0]=74.0;
			all_points[2][0][0]=92.0;
			all_points[2][1][0]=96.0;
		}
	}
	return all_points;
}
	
	private double[][] bubbleSortMulti(double[][] MultiIn, int compIdx) {  
	    double[][] temp = new double[MultiIn.length][MultiIn[0].length];  
	    boolean finished = false;  
	    while (!finished) {  
	      finished = true;  
	      for (int i = 0; i < MultiIn.length - 1; i++) {  
	         if (MultiIn[i][compIdx] > (MultiIn[i + 1][compIdx])) {  
	           for (int j = 0; j < MultiIn[i].length; j++) {  
	              temp[i][j] = MultiIn[i][j];  
	              MultiIn[i][j] = MultiIn[i + 1][j];  
	              MultiIn[i + 1][j] = temp[i][j];  
	           }  
	           finished = false;  
	         }  
	       }  
	    }  
	    return MultiIn;  
	 }  
	
}
