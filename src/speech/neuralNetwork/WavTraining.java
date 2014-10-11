package speech.neuralNetwork;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import speech.audioProcessing.SpectrumAdjust;
import speech.dataAcquisition.ReadWav;
import uk.ac.bath.ai.backprop.BackProp;

//
//@author JER
//
/*
 * Trains a neural network for a given set of audio data acquired from
 * a variety of sound sources
 */

public class WavTraining {
	
	public static NeuralNet neuralNet;
	public static SpectrumAdjust spectrumAdjust;
	public static ReadWav readWav;
	
	public static int sampleFreq = 44100;
	public static int maxAudioLength = 1000;

	public static int neuralNetInputNodeQty = 128;
	public static int neuralNetHiddenNodeQty = 30;
	public static int neuralNetOutputNodeQty = 6;
	
	public static int fftSize = 1024;
	public static int onscreenBins = 128;
	
	public static double alpha = 300000.0;
	public static double beta = .000001;
	
	public static double maxError = 0.01;
	
	private static int i_max;

	public static void main(String args[]) throws Exception {

		int neuralNetworkSize[] = { neuralNetInputNodeQty, neuralNetHiddenNodeQty, neuralNetOutputNodeQty };

		neuralNet = new BackProp(neuralNetworkSize, beta, alpha, null);
		spectrumAdjust = new SpectrumAdjust();
		readWav = new ReadWav(neuralNetOutputNodeQty);

		neuralNet.randomWeights(0.0, 0.01);

		double error = 1.0;
		double[] phonemeRaw = new double[fftSize];
		double[] phonemeLogarthmic = new double[onscreenBins];
		double[] phonemeSmoothed = new double[onscreenBins];

		int count = 0;
		
		// -------- Train Network------------------ //

		// Read wavs from file
		double[][][] wavs = readWav.getMonoThongWavs(fftSize, neuralNetOutputNodeQty, sampleFreq, maxAudioLength);
		
		while (error > maxError) {
			
			error = 0.0;
			
			i_max = readWav.file_length[0] - 1;

			for (int i = 1; i < i_max; i++) { 							// Cycle through instances of FFT
				for (int p = 0; p < neuralNetOutputNodeQty+1; p++) { 	// Cycle through phonemes
					
					for (int j = 0; j < fftSize; j++) {
						phonemeRaw[j] = wavs[i][j][p];
					}
					
					phonemeLogarthmic = spectrumAdjust.linearToLog(onscreenBins, fftSize, phonemeRaw); 
					phonemeSmoothed = spectrumAdjust.smoothSpectrumRunningAverageOf3(onscreenBins, phonemeLogarthmic); 

					double[] train_outvals = new double[neuralNetOutputNodeQty+1];
					if (p != neuralNetOutputNodeQty) train_outvals[p] = 1.0;

					neuralNet.backPropTrain(phonemeSmoothed, train_outvals); 		// Do a back prop

					double[] output_vals = neuralNet.forwardPass(phonemeSmoothed);

					for (int j = 0; j < neuralNetOutputNodeQty; j++) {
						error += (train_outvals[j] - output_vals[j])
								* (train_outvals[j] - output_vals[j]);
					}

				}
			}
			
			if (count % 10 == 0) System.out.println("Total Error: " + error);	// print training progress
			count++;
			
		}

		FileOutputStream istr = new FileOutputStream(
				"src/neuralNetworkStore/network.txt");
		ObjectOutputStream out = new ObjectOutputStream(istr);
		out.writeObject(neuralNet);
		out.close();

		System.out.println("Finished training! \n It took "
				+ count + " back props");

	}

}