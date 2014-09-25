package Archive_Files;

import com.frinika.sequencer.model.audio.AudioReader;
import com.frinika.sequencer.model.audio.VanillaRandomAccessFile;
import java.io.File;
import java.io.RandomAccessFile;
import uk.ac.bath.audio.FFTWorker;
import uk.org.toot.audio.core.AudioBuffer;

/**
 *
 * @author pjl
 */
public class ReadAndFFT {

    // private static MultiIOJavaSoundAudioServer audioServer;
    private static AudioBuffer chunk;
  
    private static AudioReader audioReader;

    public static void main(String args[]) throws Exception {

        //      audioServer = new MultiIOJavaSoundAudioServer();


        File file = new File("test.wav");

        RandomAccessFile rafG = new RandomAccessFile(file, "r");



        audioReader = new AudioReader(new VanillaRandomAccessFile(rafG));

        double Fs = 44100.0;
        boolean doHanning = true;


        int fftsize = 128;

        double input[] = new double[fftsize];

        double out[] = new double[2 * fftsize];

        int chunksize = fftsize / 2;

        chunk = new AudioBuffer("Dans buffer", 2, fftsize, 44100);

        chunk.setRealTime(true);

        FFTWorker fft = new FFTWorker(Fs, doHanning);
        double magn[] = new double[fftsize];

        fft.resize(fftsize);

        while (!audioReader.eof()) {

            chunk.makeSilence();
            audioReader.processAudio(chunk);

            float left[] = chunk.getChannel(0);

            // System.out.println(" mm =" + left[0]);

            for (int i = 0; i < fftsize; i++) {
                input[i] = left[i];
            }

            fft.process(input, out);

            for (int i = 0; i < input.length; i++) {
                double real = out[2 * i];
                double imag = out[2 * i + 1];
                magn[i] = (float) Math.sqrt(real * real + imag * imag);
            }

            System.out.println(magn[5]);

        }
    }

   
}
