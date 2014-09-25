import com.frinika.sequencer.model.audio.AudioReader;
import com.frinika.sequencer.model.audio.VanillaRandomAccessFile;
import java.io.File;
import java.io.RandomAccessFile;
import uk.ac.bath.audio.FFTWorker;
import uk.org.toot.audio.core.AudioBuffer;

public class JR_ReadWav {
	
	public static double[][][] getWavs() throws Exception {
		
		double wav1[][] =  readWav("ah.wav", 1024, 44100);
		double wav2[][] =  readWav("ee.wav", 1024, 44100);
		double wav3[][] =  readWav("eh.wav", 1024, 44100);
		double wav4[][] =  readWav("oh.wav", 1024, 44100);
		double wav5[][] =  readWav("oo.wav", 1024, 44100);
		double wav6[][] =  readWav("silence.wav", 1024, 44100);
		double all_wavs[][][] = new double[wav1.length][wav1[0].length][6];
		
		for (int i=0; i<wav1.length; i++) {
			for (int j=0; j<wav1[0].length; j++) {
				all_wavs[i][j][0]=wav1[i][j];
				all_wavs[i][j][1]=wav2[i][j];
				all_wavs[i][j][2]=wav3[i][j];
				all_wavs[i][j][3]=wav4[i][j];
				all_wavs[i][j][4]=wav5[i][j];
				all_wavs[i][j][5]=wav6[i][j];
			}
		}
		
		return all_wavs;
		
	}
    
    public static double[][] readWav(String filename, int fftsize, int Fs) throws Exception  {
    	
		JR_AudioProcess JR_Proc = new JR_AudioProcess(fftsize, (float)Fs, 1);
        File file = new File(filename);
        RandomAccessFile rafG = new RandomAccessFile(file, "r");
        AudioReader audioReader = new AudioReader(new VanillaRandomAccessFile(rafG));
        AudioBuffer chunk = new AudioBuffer("James buffer", 2, fftsize, Fs);
        chunk.setRealTime(true);
        
        double[][] output = new double[44][1024];
        
        int i=0;
        
        while (!audioReader.eof()) {
        	
            chunk.makeSilence();
            audioReader.processAudio(chunk);
            double output_buffer[] = JR_Proc.processAudio(chunk, 1024, (float)44100.0);

            for (int j=0; j<output_buffer.length; j++) {
            	output[i][j] = output_buffer[j];
            }
            i++;

        }
        
        return output;
        
    }
   
}
