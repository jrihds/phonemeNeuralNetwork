package video;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

//
//@author JER
//
/*
 * 
 * 
 * */

public class ReadVideoStills {

	public double[][][] read() throws IOException {

		double inputImages[][][] = new double[640][480][15];
		String names[] = { "EEE", "AHH", "OOH", "ERR", "CLEAR" };
		
		int count = 0;
		for (int f = 0; f < 5; f++) {
			
			String resource="imagefiles/"+names[f]+".bmp";
			URL url = this.getClass().getResource(resource);
			BufferedImage img = ImageIO.read(url);

			for (int i = 0; i < 640; i++) {
				for (int j = 0; j < 480; j++) {
					double red = (img.getRGB(i, j) >> 16) & 0xff;
					double green = (img.getRGB(i, j) >> 8) & 0xff;
					double blue = (img.getRGB(i, j)) & 0xff;
					
					inputImages[i][j][count] = red;
					inputImages[i][j][count+1] = green;
					inputImages[i][j][count+2] = blue;
					
					//float HSB[] = Color.RGBtoHSB((int)red, (int)green, (int)blue, null);
					//inputImages[i][j][count] = (double) HSB[0]*255;
					//inputImages[i][j][count+1] = (double) HSB[1]*255;
					//inputImages[i][j][count+2] = (double) HSB[2]*255;
				}
			}
			count+=3;
		}
		
		return inputImages;
	}
}