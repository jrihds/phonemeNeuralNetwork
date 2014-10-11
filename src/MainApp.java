

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

import speech.NeuralNetClient;
import speech.AudioProcessing.RealTimeSpectralSource;
import speech.AudioProcessing.SpectralAnalysisProcess;
import speech.FrontendGfx.MainApplicationWindow;

public class MainApp {
	
	double neuralNetworkOutputs[];
	double audioSpectrum[];
	String phonemeText;
	
	int frequencyBins = 128;
	int fftSize = 1024;
	int numberOfPhonemes = 6;

	MainApplicationWindow mainApplicationWindow;
	Timer timer;
	NeuralNetClient neuralNetworkClient;
	
	public static void main(String args[]) throws Exception {
		MainApp app = new MainApp();
		app.start();
	}
	
	MainApp() {
		
		// create audio processes and neural network
		SpectralAnalysisProcess spectralAnalysis = new SpectralAnalysisProcess(fftSize, (float) 44100.0);
		RealTimeSpectralSource rtSource = new RealTimeSpectralSource(spectralAnalysis);
		neuralNetworkClient = new NeuralNetClient(fftSize, frequencyBins);
		
		// Setup input from soundcard
		String inName = "default [default]";
		String outName = "default [default]";
		try {
			rtSource.startAudio(inName, outName, frequencyBins, neuralNetworkClient);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mainApplicationWindow = new MainApplicationWindow(numberOfPhonemes, frequencyBins);
		mainApplicationWindow.makeMaster();
		
		timer = new Timer(40, new ActionListener() {					// Create a timer object that runs at 25 frames
			public void actionPerformed(ActionEvent ae) {				//		per second, i.e. every 40ms
				
				neuralNetworkOutputs = neuralNetworkClient.getNeuralNetworkOutputs();
				phonemeText = neuralNetworkClient.getStrongestPhoneme();
				audioSpectrum = neuralNetworkClient.getSmoothedFrequencySpectrum();
				
				mainApplicationWindow.updateGfx(
						phonemeText,
						neuralNetworkOutputs,
						audioSpectrum);
			}
		});
	}
	
	void start() throws InterruptedException {
		timer.start();
	}
}