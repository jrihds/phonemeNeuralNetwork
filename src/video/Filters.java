package video;

public class Filters {
	
	public int[][] meanFilter (int imageIn[][]) {
		
		int imageOut[][] = new int[imageIn.length][imageIn.length];
		
		int sum=0;
		for (int i=1; i<imageIn.length-1; i++) {
			for (int j=1; j<imageIn.length-1; j++) {
				sum+=imageIn[i-1][j-1] + imageIn[i][j-1] + imageIn[i+1][j-1];
				sum+=imageIn[i-1][j] + imageIn[i][j] + imageIn[i+1][j];
				sum+=imageIn[i-1][j+1] + imageIn[i][j+1] + imageIn[i+1][j+1];
				imageOut[i][j]=(int)sum/9;
			}
		}
		
		return imageOut;
		
	}

}
