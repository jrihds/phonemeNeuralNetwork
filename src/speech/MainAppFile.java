package speech;

//@author JER
//
//  For use when using the system with pre-recorded wav files
//

import speech.dataAcquisition.ReadWav;
import speech.frontendGfx.MainApplicationWindow;
import speech.neuralNetwork.NeuralNetClient;

public class MainAppFile {
	
	int frequencyBins = 128;
	int fftSize = 1024;
	int numberOfPhonemes = 6;
	
	double neuralNetworkOutputs[];
	double audioSpectrum[];
	double testWav[][];
	String phonemeText;
	double spectrum[] = new double[fftSize];
	
	ReadWav readWav;
	MainApplicationWindow mainApplicationWindow;
	NeuralNetClient neuralNetworkClient;
	
	String fileName = "male patient e vowel.wav";
	
	public static void main(String args[]) throws Exception {
		MainAppFile appFile = new MainAppFile();
		appFile.start();
	}
	
	MainAppFile() throws Exception {
		
		mainApplicationWindow = new MainApplicationWindow(numberOfPhonemes, frequencyBins);
		mainApplicationWindow.makeMaster();
		
		readWav = new ReadWav(numberOfPhonemes);
		testWav = readWav.getPatientWavs(fftSize, numberOfPhonemes, 44100, fileName);
		
		neuralNetworkClient = new NeuralNetClient(fftSize, frequencyBins);
		
	}
	
	void start() throws Exception {
		for (int k = 0; k < readWav.getPateintFileLength(); k++) {
			for (int j = 0; j < fftSize; j++) {
				spectrum[j] = testWav[k][j];
			}
			
			neuralNetworkClient.audioChunkReciever(spectrum);
			
			phonemeText = neuralNetworkClient.getStrongestPhoneme();
			neuralNetworkOutputs = neuralNetworkClient.getNeuralNetworkOutputs();
			audioSpectrum = neuralNetworkClient.getAudioSpectrum();
			
			mainApplicationWindow.updateGfx(
					phonemeText,
					neuralNetworkOutputs,
					audioSpectrum);

			Thread.sleep(40);

		}
	}

}