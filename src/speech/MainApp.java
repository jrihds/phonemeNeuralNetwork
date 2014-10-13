package speech;

//@author JER and PJL
//
//  For use when using the system with a microphone in real-time
//

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import speech.audioProcessing.RealTimeSpectralSource;
import speech.audioProcessing.SpectralAnalysisProcess;
import speech.frontendGfx.MainApplicationWindow;
import speech.neuralNetwork.NeuralNetClient;

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
				audioSpectrum = neuralNetworkClient.getAudioSpectrum();
				
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