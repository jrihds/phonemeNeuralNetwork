package speech;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.Timer;

import speech.AudioProcessing.RealTimeSpectralSource;
import speech.AudioProcessing.SpectralAnalysisProcess;
import speech.DataAcquisition.ReadImage;
import speech.FrontendGfx.MainApplicationWindow;

public class MainApp {
	
	public int frequencySpectrum = 128;
	public int fftSize = 1024;
	public int numberOfPhonemes = 6;
	
	public double neuralNetworkOutputs[];
	public double vocalTractContour[][][];
	public double innerLipContour[][][];
	public double outerLipsContour[][][];
	public double strongestPhoneme;
	public double amplitude;
	public String phonemeText;
	
	MainApplicationWindow mainApplicationWindow;
	Timer timer;
	NeuralNetClient neuralNetwork;
	
	public static void main(String args[]) throws Exception {
		MainApp app = new MainApp();
		app.start();
	}
	
	MainApp() {
		
		mainApplicationWindow = new MainApplicationWindow(numberOfPhonemes, frequencySpectrum); 	
		mainApplicationWindow.makeMaster();
		
		ReadImage readImage = new ReadImage();
		try {
			vocalTractContour = readImage.readTract(); 				// Read in data from images of lip
			innerLipContour = readImage.readLips1(); 					//		and vocal tract shapes
			outerLipsContour = readImage.readLips2();					// 		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		timer = new Timer(40, new ActionListener() {					// Create a timer object that runs at 25 frames 
																		//		per second, i.e. every 40ms
			public void actionPerformed(ActionEvent ae) {				//
				
				neuralNetworkOutputs = neuralNetwork.getNeuralNetworkOutputs();
				strongestPhoneme = neuralNetwork.getMaxPhonemeValue();
				
				phonemeText = "";
				amplitude = 0.3;
				if (strongestPhoneme > amplitude) {
					if (strongestPhoneme == neuralNetworkOutputs[0]) {phonemeText = "EEE";}
					if (strongestPhoneme == neuralNetworkOutputs[1]) {phonemeText = "EHH";}
					if (strongestPhoneme == neuralNetworkOutputs[2]) {phonemeText = "ERR";}
					if (strongestPhoneme == neuralNetworkOutputs[3]) {phonemeText = "AHH";}
					if (strongestPhoneme == neuralNetworkOutputs[4]) {phonemeText = "OOH";}
					if (strongestPhoneme == neuralNetworkOutputs[5]) {phonemeText = "UHH";}
				}
				
				mainApplicationWindow.updateGfx(
						vocalTractContour,
						phonemeText,
						innerLipContour,
						outerLipsContour,
						neuralNetworkOutputs,
						neuralNetwork.getSmoothedFrequencySpectrum());
			}
		});
	}
	
	void start() throws InterruptedException {
		
		SpectralAnalysisProcess spectralAnalysis = new SpectralAnalysisProcess(
				fftSize, (float) 44100.0);
		
		RealTimeSpectralSource rtSource = new RealTimeSpectralSource(
				spectralAnalysis);
		
		neuralNetwork = new NeuralNetClient(fftSize, frequencySpectrum, mainApplicationWindow.drawScroll);
		
		// Setup input from soundcard
		String inName = "default [default]";
		String outName = "default [default]";
		
		try {
			rtSource.startAudio(inName, outName, frequencySpectrum, neuralNetwork);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		timer.start();
	}
}