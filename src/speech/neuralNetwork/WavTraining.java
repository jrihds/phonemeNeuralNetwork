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
	
	public static void main(String args[]) throws Exception {

		SpectrumAdjust spectrumAdjust = new SpectrumAdjust();

		// -------- Setup Neural Network------------------ //
		
		int neuralNetInputNodeQty = 128;   // equal to onscreenBins
		int neuralNetHiddenNodeQty = 30;
		int neuralNetOutputNodeQty = 6;
		
		int fftSize = 1024;
		int frequencyBins = 128;
		
		double alpha = 300000.0;		// training parameter, 300000.0 works ok
		double beta = .000001;			// training parameter, 0.000001 works ok
		double targetFitness = 0.01;	// training parameter, 0.01 works ok

		int neuralNetworkSize[] = { neuralNetInputNodeQty, neuralNetHiddenNodeQty, neuralNetOutputNodeQty };
		
		NeuralNet neuralNet = new BackProp(neuralNetworkSize, beta, alpha, null);
		neuralNet.randomWeights(0.0, 0.01);

		// -------- Read In Wav Files------------------ //
		
		int sampleFreq = 44100;
		int maxAudioLength = 1000;
		
		ReadWav readWav = new ReadWav(neuralNetOutputNodeQty);
		double[][][] wavs = readWav.getMonoThongWavs(fftSize, neuralNetOutputNodeQty, sampleFreq, maxAudioLength);

		double[] phonemeLinear = new double[fftSize];
		double[] phonemeLogarthmic = new double[frequencyBins];
		
		// -------- Train Network------------------ //

		int epochCount = 0;
		double currentFitness = 1.0;
		int maxNumFftSlices = readWav.file_length[0] - 1;
		double[] targetOutputValues;
		
		while (currentFitness > targetFitness) {
			
			currentFitness = 0.0;

			for (int i = 1; i < maxNumFftSlices; i++) { 				// Cycle through instances of FFT
				for (int p = 0; p < neuralNetOutputNodeQty+1; p++) { 	// Cycle through phonemes
					
					for (int j = 0; j < fftSize; j++) {
						phonemeLinear[j] = wavs[i][j][p];
					}
					
					phonemeLogarthmic = spectrumAdjust.linearToLog(frequencyBins, fftSize, phonemeLinear); 
					phonemeLogarthmic = spectrumAdjust.smoothSpectrumRunningAverageOf3(frequencyBins, phonemeLogarthmic); 

					targetOutputValues = new double[neuralNetOutputNodeQty+1];			// create new target array
					if (p != neuralNetOutputNodeQty) targetOutputValues[p] = 1.0;		// only make target phoneme
																						//    output node 'true'

					neuralNet.backPropTrain(phonemeLogarthmic, targetOutputValues); 	// Do a back prop

					double[] currentOutputValues = neuralNet.forwardPass(phonemeLogarthmic);

					for (int j = 0; j < neuralNetOutputNodeQty; j++) {
						currentFitness += (targetOutputValues[j] - currentOutputValues[j])
								* (targetOutputValues[j] - currentOutputValues[j]);
					}

				}
			}
			
			if (epochCount % 10 == 0) System.out.println("Total Error: " + currentFitness);	// print training progress
			epochCount++;
			
		}

		FileOutputStream istr = new FileOutputStream(
				"src/neuralNetworkStore/network.txt");
		ObjectOutputStream out = new ObjectOutputStream(istr);
		out.writeObject(neuralNet);
		out.close();

		System.out.println("Finished training! \n It took "
				+ epochCount + " back props");

	}

}