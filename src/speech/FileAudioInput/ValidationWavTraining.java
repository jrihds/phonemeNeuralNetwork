package speech.FileAudioInput;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import speech.NeuralNet;
import speech.AudioProcessing.SpectrumAdjust;
import uk.ac.bath.ai.backprop.BackProp;

//
//@author JER
//
/*
 * Trains a neural network for a given set of audio data acquired from
 * a variety of sound sources
 */

public class ValidationWavTraining {
	
	public static NeuralNet neuralNet;
	public static SpectrumAdjust specAdjust;
	public static ValidationReadWav readWav;
	
	public static int Fs = 44100;
	public static int maxAudioLength = 1000;

	public static int inputs = 128;
	public static int hidden = 30;
	public static int outputs = 6;
	
	public static int fftSize = 1024;
	public static int onscreenBins = 128;
	
	public static double alpha = 1000.0;
	public static double beta = 0.0001;
	
	public static double maxError = 0.01;
	
	private static int i_max;

	public static void main(String args[]) throws Exception {

		int sz[] = { inputs, hidden, outputs };

		neuralNet = new BackProp(sz, beta, alpha, null);
		specAdjust = new SpectrumAdjust();
		readWav = new ValidationReadWav(outputs, 2);

		neuralNet.randomWeights(0.0, 0.01);

		double error = 1.0;
		double[] phonemeRaw = new double[fftSize];
		double[] phonemeLog = new double[onscreenBins];
		double[] phonemeSmoothed = new double[onscreenBins];

		int count = 0;
		
		// -------- Train Network------------------ //

		// Read wavs from file
		double[][][] wavs = readWav.getMonoThongWavs(fftSize, outputs, Fs, maxAudioLength);
		
		long startTime = System.nanoTime();
		
		while (error > maxError) {
			
			error = 0.0;
			
			i_max = 110;//readWav.file_length[0] - 1;

			// readWav.file_length[0] can be replaced with a number

			for (int i = 1; i < i_max; i++) { // Cycle through instances of FFT
				for (int p = 0; p < outputs+1; p++) { // Cycle through phonemes
					
					for (int j = 0; j < fftSize; j++) {
						phonemeRaw[j] = wavs[i][j][p];
					}
					
					phonemeLog = specAdjust.linearLog(onscreenBins, fftSize, phonemeRaw); 
					phonemeSmoothed = specAdjust.running3Average(onscreenBins, phonemeLog); 

					double[] train_outvals = new double[outputs+1];
					if (p != outputs)
						train_outvals[p] = 1.0;

					neuralNet.backPropTrain(phonemeSmoothed, train_outvals); // Go!

					double[] output_vals = neuralNet
							.forwardPass(phonemeSmoothed);

					for (int j = 0; j < outputs; j++) {
						error += (train_outvals[j] - output_vals[j])
								* (train_outvals[j] - output_vals[j]);
					}

				}
			}
			
			if (count % 10 == 0)
				System.out.println("Total Error: " + error);
			count++;
			
		}
		
		System.out.println("Convergence time: " + ((System.nanoTime()-startTime)/1000000000));

		FileOutputStream istr = new FileOutputStream(
				"src/textfiles/network.txt");
		ObjectOutputStream out = new ObjectOutputStream(istr);
		out.writeObject(neuralNet);
		out.close();

		System.out.println("Whooop finished training! \n It took me "
				+ count + " back props");

	}

}