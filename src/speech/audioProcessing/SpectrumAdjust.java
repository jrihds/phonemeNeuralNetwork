package speech.audioProcessing;

//
//@author JER
//
/*
 * Contains a number of functions for adjusting a spectrum of information passed to it
 */

public class SpectrumAdjust {

	public double[] linearToLog(int onscreenBins, int fftsize, double[] linearAudio) {
		
		// pass a linear spectrum of audio data, convert it into a logarithmic spectrum
		// 		and return the value

		int triangular = 0;

		for (int i = 0; i < onscreenBins; i++) {
			triangular += i;
		}

		double factor = (double) fftsize / triangular;
		double[] logarithmicAudio = new double[onscreenBins];
		int count = 0;
		int count2 = 0;
		while (count != onscreenBins) {
			for (int j = 0; j < Math.round(count * factor); j++) {
				logarithmicAudio[count] += linearAudio[count2];
				count2++;
			}
			count++;
		}
		return logarithmicAudio;

	}

	public double[] smoothSpectrumRunningAverageOf3(double[] audioSpectrum) {

		double[] smoothed = new double[audioSpectrum.length];
		double left, right, middle;
		
		for (int i = 0; i < audioSpectrum.length; i++) {
			
			left = right = 0;
			middle = audioSpectrum[i];

			try{ 
				left = audioSpectrum[i - 1];
				right = audioSpectrum[i + 1];
			}
			catch(Exception e){
				continue;
			}
			smoothed[i] = (left+middle+right) / 3;
		}
		return smoothed;

	}
	
	public double[] changeVolume(int factor, double[] audioSpectrum) {
		for (int i=0; i<audioSpectrum.length; i++) {
			audioSpectrum[i]*=factor;
		}
		return audioSpectrum;
	}

}
