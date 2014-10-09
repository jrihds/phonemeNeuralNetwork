package speech.FileAudioInput;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;

import javax.swing.Timer;

import speech.NeuralNet;
import speech.NeuralNetClient;
import speech.AudioProcessing.SpectrumAdjust;
import speech.DataAcquisition.ReadImage;
import speech.FrontendGfx.MainApplicationWindow;

public class ValidationMainApp {
	
	public static int onscreenBins = 128;
	public static int fftSize = 1024;
	public static int phonemes = 6;
	public static int Fs = 44100;
	public static int maxAudioLength = 1000;
	
	public static double spectrum[] = new double[fftSize];
	public static double outputSort[] = new double[phonemes];
	public static double magnLog[];
	public static double smoothed[];
	public static double outputs[];
	public static double vocalTract[][][];
	public static double lipsInner[][][];
	public static double lipsOuter[][][];
	
	static ValidationReadWav readTestWav;
	static NeuralNet neuralNet;
	static SpectrumAdjust specAdj;
	static MainApplicationWindow frames;
	static ReadImage ri;
	static NeuralNetClient client;
	
	public static void main(String args[]) throws Exception {
	
		frames = new MainApplicationWindow(phonemes, onscreenBins); 		// Create gfx for output
		
		final ReadImage ri = new ReadImage();
		try {
			vocalTract = ri.readTract(); 			// Read in data from images
			lipsInner = ri.readLipContour1(); 			// of lip and tract shapes
			lipsOuter = ri.readLipContour2();
		} catch (IOException e) {
			e.printStackTrace();
		}
		frames.makeMaster();
		
		readTestWav = new ValidationReadWav(phonemes, 11);
		specAdj = new SpectrumAdjust();
		double testWav[][] = readTestWav.getPatientWavs(fftSize, phonemes, Fs,
				maxAudioLength);
		
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
		
		for (int k = 0; k < readTestWav.file_length_patient; k++) {
			for (int j = 0; j < fftSize; j++) {
				spectrum[j] = 2.0*testWav[k][j];
			}

			magnLog = specAdj.linearToLog(onscreenBins, fftSize, spectrum);
			smoothed = specAdj.smoothSpectrumRunningAverageOf3(onscreenBins, magnLog);
			outputs = neuralNet.forwardPass(smoothed);
			
			for (int l = 0; l < outputs.length; l++) {
				outputSort[l] = outputs[l];
			}
			Arrays.sort(outputSort);
			
			String text = "";
			if (outputSort[5] > 0.3) {
				if (outputSort[5] == outputs[0]) {text = "EEE";}
				if (outputSort[5] == outputs[1]) {text = "EHH";}
				if (outputSort[5] == outputs[2]) {text = "ERR";}
				if (outputSort[5] == outputs[3]) {text = "AHH";}
				if (outputSort[5] == outputs[4]) {text = "OOH";}
				if (outputSort[5] == outputs[5]) {text = "UHH";}
			}
			
			frames.updateGfx(vocalTract, text, lipsInner, lipsOuter, outputs, smoothed);

			Thread.sleep(40);

		}
		}
	
}