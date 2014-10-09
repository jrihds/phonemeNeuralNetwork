package speech;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import speech.AudioProcessing.SpectralAnalysisProcess;
import speech.AudioProcessing.SpectrumAdjust;
import speech.FrontendGfx.DrawScrollingSpectrum;
import uk.ac.bath.ai.backprop.BackProp;

public class NeuralNetClient {
	
	NeuralNet neuralNet;
	SpectrumAdjust spectrumAdjust;
	SpectralAnalysisProcess sprectralAnalysis;
	DrawScrollingSpectrum drawScrollingSpectrum;
	private double outputs[];
	double smoothed[];
	double magnLog[];
	int fftSize;
	int frequencyBins;
	
	public NeuralNetClient(int fftsize, int frequencyBins, DrawScrollingSpectrum scrollingSpect) {
		
		this.frequencyBins = frequencyBins;
		this.fftSize=fftsize;
		spectrumAdjust = new SpectrumAdjust();
		this.drawScrollingSpectrum=scrollingSpect;
		outputs = new double[6];
		
		FileInputStream ostr;
		try {
			ostr = new FileInputStream("src/textfiles/network.txt");
			ObjectInputStream in = new ObjectInputStream(ostr);
			neuralNet = (NeuralNet) in.readObject();
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	public void forwardPass(double[] spectrum) {
		
		magnLog = spectrumAdjust.linearToLog(frequencyBins, fftSize, spectrum);
		
		smoothed = spectrumAdjust.smoothSpectrumRunningAverageOf3(frequencyBins, magnLog);
		
		for (int i=0; i<smoothed.length; i++) {
			smoothed[i]*=2;									// This is adding volume to the input signal.
		}													// the USB audio interface isn't 'hot' enough
		
		if (drawScrollingSpectrum != null) drawScrollingSpectrum.notifyMoreDataReady(magnLog);
		// the line of code above is bad and needs fixing
		
		outputs = neuralNet.forwardPass(smoothed);
	}
	
	public double[] getNeuralNetworkOutputs() {
		return outputs;
	}
	
	public double[] getSmoothedFrequencySpectrum() {
		return smoothed;
	}
	
	public double getMaxPhonemeValue() {
		double max = 0;
		for (int i = 0; i < outputs.length; i ++) {
			if (outputs[i] > max) { max = outputs[i]; }
		}
		return max;
	}
	
}
