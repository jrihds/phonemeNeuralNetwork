package speech.dataAcquisition;

//
//@author JER
//
/*
 * 
 * Reads a wav file from disk, performs a FFT on it, and stores the information
 * in an array
 *  
 */

import com.frinika.audio.io.AudioReader;
import com.frinika.audio.io.VanillaRandomAccessFile;
import java.io.File;
import java.io.RandomAccessFile;

import speech.audioProcessing.SpectralAnalysisProcess;
import uk.org.toot.audio.core.AudioBuffer; 

public class ReadWav {

	public int file_length[];
	String names[] = { "eee_all", "ehh_all", "err_all", "ahh_all",
			"ooh_all", "uhh_all", "silence_all" };

	public ReadWav(int outputs) {
		file_length = new int[outputs + 1];
	}

	public double[][][] getMonothongTrainingWavs(int fftSize, int outputs,
			int Fs, int maxAudioLength) throws Exception {

		double allWavs[][][] = new double[maxAudioLength][fftSize][21];

		for (int i = 0; i < outputs + 1; i++) {

			String resource = "src/speech/wavFiles/trainingSplice/" + names[i] + ".wav";
			double wav[][] = readWav(resource, fftSize, Fs, i);

			for (int j = 0; j < wav.length; j++) {
				for (int k = 0; k < wav[0].length; k++) {
					allWavs[j][k][i] = wav[j][k];
				}
			}
		}

		return allWavs;

	}
	
	public double[][] getPatientWavs(int fftSize, int outputs, int Fs, String fileName) throws Exception {

		String resource = "src/speech/wavFiles/patients/"+fileName;
		double wav[][] = readWav(resource, fftSize, Fs, 0);
		return wav;

	}
	
	public double getPateintFileLength() {
		return file_length[0];
	}

	private double[][] readWav(String filename, int fftSize, int Fs,
			int num) throws Exception {

		SpectralAnalysisProcess spectralAnalysis = new SpectralAnalysisProcess(
				fftSize, (float) 44100.0);
		File file = new File(filename);
		RandomAccessFile rafG = new RandomAccessFile(file, "r");
		AudioReader audioReader = new AudioReader(new VanillaRandomAccessFile(
				rafG),44100.0f);
		AudioBuffer chunk = new AudioBuffer("James buffer", 2, fftSize, Fs);
		chunk.setRealTime(true);

		double[][] output = new double[1000][fftSize];

		int i = 0;

		while (!audioReader.eof()) {

			chunk.makeSilence();
			audioReader.processAudio(chunk);
			double output_buffer[] = spectralAnalysis.processAudio(chunk);

			for (int j = 0; j < output_buffer.length; j++) {
				output[i][j] = output_buffer[j];
			}
			i++;

		}

		file_length[num] = i;

		return output;

	}

}
