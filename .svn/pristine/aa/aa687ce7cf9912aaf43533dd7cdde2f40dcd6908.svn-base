
public class JR_SpectrumAdjust {
	
	public double average, temp, error;
	
	public double[] linear_log(int onscreen_bins, int fftsize, double[] spectrum) {
			
		int triangular=0;
		for (int i=0; i<onscreen_bins; i++) {
			triangular+=i;
		}
		
	    double factor=(double)fftsize/triangular;
    	double[] magn_log = new double[onscreen_bins];
    	int count=0;
    	int count2=0;
    	while (count!=onscreen_bins) {
           	for (int j=0; j<Math.round(count*factor); j++) {
           		magn_log[count]+=spectrum[count2];
           		count2++;
           	}
   		count++;
    	}
    	
	    return magn_log;
	}
	
	public void autoGain(double[] spectrum) {
		
		// doesn't work
		
		/*temp=0.0;
		for (int i=0; i<spectrum.length; i++) {
			temp+=spectrum[i];
		}
		average=temp/(spectrum.length);
		error=50000*(0.5-average);
		System.out.println("av "+average);
		System.out.println("er "+error);
		multiplier+=error;
		if ((average*average)<0.000000001) {
			System.out.println("Zomg!");
			multiplier = 250.0;
		}
		*/
	}
	
	public double[] run_average(int onscreen_bins, double[]magn_log) {

       	double[] smoothed = new double[onscreen_bins];
       	for (int i=1; i<(onscreen_bins-1); i++) {
       		smoothed[i]=(magn_log[i-1]+magn_log[i]+magn_log[i+1])/3;
       	}
       	return smoothed;
       	
	}

}
