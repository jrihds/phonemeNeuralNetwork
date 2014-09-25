package speech;

//
//@author JER
//
/*
 * Contains a number of functions for adjusting a spectrum of information passed to it
 */

public class SpectrumAdjust {

	public double average, temp, error;

	public double[] linearLog(int onscreenBins, int fftsize, double[] spectrum) {

		int triangular = 0;

		for (int i = 0; i < onscreenBins; i++) {
			triangular += i;
		}

		double factor = (double) fftsize / triangular;
		double[] magn_log = new double[onscreenBins];
		int count = 0;
		int count2 = 0;
		while (count != onscreenBins) {
			for (int j = 0; j < Math.round(count * factor); j++) {
				magn_log[count] += spectrum[count2];
				count2++;
			}
			count++;
		}
		return magn_log;

	}

	public double[] running3Average(int onscreen_bins, double[] magnLog) {

		double[] smoothed = new double[onscreen_bins];
		for (int i = 1; i < (onscreen_bins - 1); i++) {
			smoothed[i] = (magnLog[i - 1] + magnLog[i] + magnLog[i + 1]) / 3;
		}
		return smoothed;

	}

}
