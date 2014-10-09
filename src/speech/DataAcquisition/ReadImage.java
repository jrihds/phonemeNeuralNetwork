package speech.DataAcquisition;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

// multidimensional array sort taken from: http://realityisimportant.blogspot.com/

//
//@author JER
//
/*
* 
* Reads both lip and vocal tract data from a file and stores
* the data in arrays
* 
* */

public class ReadImage {

	public double[][][] readTract() throws IOException {
		
		double all_points[][][] = new double[100][3][6];
		double points[][] = new double[100][3];
		double sorted_points[][] = new double[100][3];
		String names[]=	{"ee_wgr","eh_wgr","er_wgr","ah_wgr","oo_wgr","oh_wgr"};
	
		for (int f=0; f<6; f++) {

			String resource="imagefiles/"+names[f]+".bmp";

			URL url=this.getClass().getResource(resource);
			BufferedImage img = ImageIO.read(url);
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
	
public double[][][] readLipContour1() throws IOException {
		
		double all_points[][][] = new double[28][3][6];
		double points[][] = new double[28][3];
		double sorted_points[][] = new double[28][3];
		
		String names[]=	{"lipsEEE","lipsEHH","lipsERR","lipsAHH","lipsOOH","lipsUHH"};

		for (int f=0; f<6; f++) {
			String resource="imagefiles/"+names[f]+".bmp";
			URL url=this.getClass().getResource(resource);
			BufferedImage img = ImageIO.read(url);
			int count=0;
			for (int i=0; i<715; i++) {
				for (int j=0; j<656; j++) {
					double red = (img.getRGB(i,j)>>16)&0xff;
					double green = (img.getRGB(i,j)>>8) & 0xff;
					double blue = (img.getRGB(i,j)) & 0xff;
					if (green==100.0 && blue==100.0 && red<28.0) {
						points[count][0]=(double)i;
						points[count][1]=(double)j;
						points[count][2]=(double)((img.getRGB(i, j) >> 16) & 0xff);
						count++;
					}
					}
				}
			sorted_points=bubbleSortMulti(points, 2);
			for (int i=0; i<28; i++) {
				all_points[i][0][f]=(double)sorted_points[i][0];
				all_points[i][1][f]=(double)sorted_points[i][1];
				all_points[i][2][f]=(double)i;
			}
		}
		return all_points;
	}

public double[][][] readLipContour2() throws IOException {
	
	double all_points[][][] = new double[26][3][6];
	double points[][] = new double[26][3];
	double sorted_points[][] = new double[26][3];
	for (int f=0; f<6; f++) {

		String names[]=	{"lipsEEE","lipsEHH","lipsERR","lipsAHH","lipsOOH","lipsUHH"};
		String resource="imagefiles/"+names[f]+".bmp";
		URL url=this.getClass().getResource(resource);
		BufferedImage img = ImageIO.read(url);
	
		int count=0;
		for (int i=0; i<715; i++) {
			for (int j=0; j<656; j++) {
				double red = (img.getRGB(i,j)>>16)&0xff;
				double green = (img.getRGB(i,j)>>8) & 0xff;
				double blue = (img.getRGB(i,j)) & 0xff;
				if (green<26.0 && blue==100.0 && red==100.0) {
					points[count][0]=(double)i;
					points[count][1]=(double)j;
					points[count][2]=(double)green;
					count++;
					}
				}
			}
		sorted_points=bubbleSortMulti(points, 2);
		for (int i=0; i<26; i++) {
			all_points[i][0][f]=(double)sorted_points[i][0];
			all_points[i][1][f]=(double)sorted_points[i][1];
			all_points[i][2][f]=(double)i;
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
