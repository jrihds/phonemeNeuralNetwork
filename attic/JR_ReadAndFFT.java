//import MeterPanel;

import com.frinika.sequencer.model.audio.AudioReader;
import com.frinika.sequencer.model.audio.VanillaRandomAccessFile;

import java.awt.Color;
import java.io.File;
import java.io.RandomAccessFile;

import javax.swing.JFrame;
import javax.swing.JPanel;

import uk.ac.bath.audio.FFTWorker;
import uk.org.toot.audio.core.AudioBuffer;

/**
 *
 * @author pjl
 */
public class JR_ReadAndFFT {

    
    private static AudioBuffer chunk;
  
    private static AudioReader audioReader;
    private static JFrame frame;
    private static MeterPanel 	meterPanel0, 
    							meterPanel1, 
    							meterPanel2, 
    							meterPanel3, 
    							meterPanel4, 
    							meterPanel5, 
    							meterPanel6, 
    							meterPanel7, 
    							meterPanel8, 
    							meterPanel9, 
    							meterPanel10, 
    							meterPanel11, 
    							meterPanel12, 
    							meterPanel13, 
    							meterPanel14, 
    							meterPanel15, 
    							meterPanel16, 
    							meterPanel17, 
    							meterPanel18, 
    							meterPanel19,
    							meterPanel20, 
    							meterPanel21, 
    							meterPanel22, 
    							meterPanel23, 
    							meterPanel24, 
    							meterPanel25, 
    							meterPanel26, 
    							meterPanel27, 
    							meterPanel28, 
    							meterPanel29,
    							meterPanel30, 
    							meterPanel31, 
    							meterPanel32, 
    							meterPanel33, 
    							meterPanel34, 
    							meterPanel35, 
    							meterPanel36, 
    							meterPanel37, 
    							meterPanel38, 
    							meterPanel39,
    							meterPanel40, 
    							meterPanel41, 
    							meterPanel42, 
    							meterPanel43, 
    							meterPanel44, 
    							meterPanel45, 
    							meterPanel46, 
    							meterPanel47, 
    							meterPanel48, 
    							meterPanel49,
    							meterPanel50, 
    							meterPanel51, 
    							meterPanel52, 
    							meterPanel53, 
    							meterPanel54, 
    							meterPanel55, 
    							meterPanel56, 
    							meterPanel57, 
    							meterPanel58, 
    							meterPanel59,
    							meterPanel60, 
    							meterPanel61, 
    							meterPanel62,
    							meterPanel63;

    public static void main(String args[]) throws Exception {
    	
        File file = new File("test.wav");
        RandomAccessFile rafG = new RandomAccessFile(file, "r");
        audioReader = new AudioReader(new VanillaRandomAccessFile(rafG));
        
        final double Fs = 44100.0;
        final boolean doHanning = true;
        final int fftsize = 128;
        final double input[] = new double[fftsize];
        final double out[] = new double[2 * fftsize];
        
        chunk = new AudioBuffer("Dans buffer", 2, fftsize, 44100);
        chunk.setRealTime(true);

        FFTWorker fft = new FFTWorker(Fs, doHanning);
        final double magn[] = new double[fftsize];
        fft.resize(fftsize);
        
        configure();
        
        while (!audioReader.eof()) {

            chunk.makeSilence();
            audioReader.processAudio(chunk);

            float left[] = chunk.getChannel(0);
            for (int i = 0; i < fftsize; i++) {
                input[i] = left[i];
            }
            fft.process(input, out);
            for (int i = 0; i < input.length; i++) {
                double real = out[2 * i];
                double imag = out[2 * i + 1];
                magn[i] = (float) 1000*Math.sqrt(real * real + imag * imag);
            }
            updateMeters(magn);
            Thread.sleep(3);
        }

    }

    private static void updateMeters(double[] magn) {
    	meterPanel0.updateMeter(magn[0], Color.CYAN);
        meterPanel1.updateMeter(magn[1], Color.CYAN);
        meterPanel2.updateMeter(magn[2], Color.CYAN);
        meterPanel3.updateMeter(magn[3], Color.CYAN);
        meterPanel4.updateMeter(magn[4], Color.CYAN);
        meterPanel5.updateMeter(magn[5], Color.CYAN);
        meterPanel6.updateMeter(magn[6], Color.CYAN);
        meterPanel7.updateMeter(magn[7], Color.CYAN);
        meterPanel8.updateMeter(magn[8], Color.CYAN);
        meterPanel9.updateMeter(magn[9], Color.CYAN);
    	meterPanel10.updateMeter(magn[10], Color.CYAN);
        meterPanel11.updateMeter(magn[11], Color.CYAN);
        meterPanel12.updateMeter(magn[12], Color.CYAN);
        meterPanel13.updateMeter(magn[13], Color.CYAN);
        meterPanel14.updateMeter(magn[14], Color.CYAN);
        meterPanel15.updateMeter(magn[15], Color.CYAN);
        meterPanel16.updateMeter(magn[16], Color.CYAN);
        meterPanel17.updateMeter(magn[17], Color.CYAN);
        meterPanel18.updateMeter(magn[18], Color.CYAN);
        meterPanel19.updateMeter(magn[19], Color.CYAN);
        meterPanel20.updateMeter(magn[20], Color.CYAN);
        meterPanel21.updateMeter(magn[21], Color.CYAN);
        meterPanel22.updateMeter(magn[22], Color.CYAN);
        meterPanel23.updateMeter(magn[23], Color.CYAN);
        meterPanel24.updateMeter(magn[24], Color.CYAN);
        meterPanel25.updateMeter(magn[25], Color.CYAN);
        meterPanel26.updateMeter(magn[26], Color.CYAN);			//Put all the info into the meters
        meterPanel27.updateMeter(magn[27], Color.CYAN);
        meterPanel28.updateMeter(magn[28], Color.CYAN);
        meterPanel29.updateMeter(magn[29], Color.CYAN);
        meterPanel30.updateMeter(magn[30], Color.CYAN);
        meterPanel31.updateMeter(magn[31], Color.CYAN);
        meterPanel32.updateMeter(magn[32], Color.CYAN);
        meterPanel33.updateMeter(magn[33], Color.CYAN);
        meterPanel34.updateMeter(magn[34], Color.CYAN);
        meterPanel35.updateMeter(magn[35], Color.CYAN);
        meterPanel36.updateMeter(magn[36], Color.CYAN);
        meterPanel37.updateMeter(magn[37], Color.CYAN);
        meterPanel38.updateMeter(magn[38], Color.CYAN);
        meterPanel39.updateMeter(magn[39], Color.CYAN);
        meterPanel40.updateMeter(magn[40], Color.CYAN);
        meterPanel41.updateMeter(magn[41], Color.CYAN);
        meterPanel42.updateMeter(magn[42], Color.CYAN);
        meterPanel43.updateMeter(magn[43], Color.CYAN);
        meterPanel44.updateMeter(magn[44], Color.CYAN);
        meterPanel45.updateMeter(magn[45], Color.CYAN);
        meterPanel46.updateMeter(magn[46], Color.CYAN);
        meterPanel47.updateMeter(magn[47], Color.CYAN);
        meterPanel48.updateMeter(magn[48], Color.CYAN);
        meterPanel49.updateMeter(magn[49], Color.CYAN);
        meterPanel50.updateMeter(magn[50], Color.CYAN);
        meterPanel51.updateMeter(magn[51], Color.CYAN);
        meterPanel52.updateMeter(magn[52], Color.CYAN);
        meterPanel53.updateMeter(magn[53], Color.CYAN);
        meterPanel54.updateMeter(magn[54], Color.CYAN);
        meterPanel55.updateMeter(magn[55], Color.CYAN);
        meterPanel56.updateMeter(magn[56], Color.CYAN);
        meterPanel57.updateMeter(magn[57], Color.CYAN);
        meterPanel58.updateMeter(magn[58], Color.CYAN);
        meterPanel59.updateMeter(magn[59], Color.CYAN);
        meterPanel60.updateMeter(magn[60], Color.CYAN);
        meterPanel61.updateMeter(magn[61], Color.CYAN);
        meterPanel62.updateMeter(magn[62], Color.CYAN);
        meterPanel63.updateMeter(magn[63], Color.CYAN);

    }

    public static void configure() {
        frame = new JFrame();
        JPanel content = new JPanel();
        meterPanel0 = new MeterPanel();
        meterPanel1 = new MeterPanel();
        meterPanel2 = new MeterPanel();
        meterPanel3 = new MeterPanel();
        meterPanel4 = new MeterPanel();
        meterPanel5 = new MeterPanel();
        meterPanel6 = new MeterPanel();
        meterPanel7 = new MeterPanel();
        meterPanel8 = new MeterPanel();
        meterPanel9 = new MeterPanel();
        meterPanel10 = new MeterPanel();
        meterPanel11 = new MeterPanel();
        meterPanel12 = new MeterPanel();
        meterPanel13 = new MeterPanel();
        meterPanel14 = new MeterPanel();
        meterPanel15 = new MeterPanel();
        meterPanel16 = new MeterPanel();
        meterPanel17 = new MeterPanel();
        meterPanel18 = new MeterPanel();			// Create a load of meterPanels
        meterPanel19 = new MeterPanel();
        meterPanel20 = new MeterPanel();
        meterPanel21 = new MeterPanel();
        meterPanel22 = new MeterPanel();
        meterPanel23 = new MeterPanel();
        meterPanel24 = new MeterPanel();
        meterPanel25 = new MeterPanel();
        meterPanel26 = new MeterPanel();
        meterPanel27 = new MeterPanel();
        meterPanel28 = new MeterPanel();
        meterPanel29 = new MeterPanel();
        meterPanel30 = new MeterPanel();
        meterPanel31 = new MeterPanel();
        meterPanel32 = new MeterPanel();
        meterPanel33 = new MeterPanel();
        meterPanel34 = new MeterPanel();
        meterPanel35 = new MeterPanel();
        meterPanel36 = new MeterPanel();
        meterPanel37 = new MeterPanel();
        meterPanel38 = new MeterPanel();
        meterPanel39 = new MeterPanel();
        meterPanel40 = new MeterPanel();
        meterPanel41 = new MeterPanel();
        meterPanel42 = new MeterPanel();
        meterPanel43 = new MeterPanel();
        meterPanel44 = new MeterPanel();
        meterPanel45 = new MeterPanel();
        meterPanel46 = new MeterPanel();
        meterPanel47 = new MeterPanel();
        meterPanel48 = new MeterPanel();
        meterPanel49 = new MeterPanel();
        meterPanel50 = new MeterPanel();
        meterPanel51 = new MeterPanel();
        meterPanel52 = new MeterPanel();
        meterPanel53 = new MeterPanel();
        meterPanel54 = new MeterPanel();
        meterPanel55 = new MeterPanel();
        meterPanel56 = new MeterPanel();
        meterPanel57 = new MeterPanel();
        meterPanel58 = new MeterPanel();
        meterPanel59 = new MeterPanel();
        meterPanel60 = new MeterPanel();
        meterPanel61 = new MeterPanel();
        meterPanel62 = new MeterPanel();
        meterPanel63 = new MeterPanel();
        content.add(meterPanel0);
        content.add(meterPanel1);
        content.add(meterPanel2);
        content.add(meterPanel3);
        content.add(meterPanel4);
        content.add(meterPanel5);
        content.add(meterPanel6);
        content.add(meterPanel7);
        content.add(meterPanel8);
        content.add(meterPanel9);
        content.add(meterPanel10);
        content.add(meterPanel11);
        content.add(meterPanel12);
        content.add(meterPanel13);
        content.add(meterPanel14);
        content.add(meterPanel15);
        content.add(meterPanel16);
        content.add(meterPanel17);
        content.add(meterPanel18);
        content.add(meterPanel19);				//Put all the meterPanels in the window,
        content.add(meterPanel20);				// I tried to do this with arrays and loops
        content.add(meterPanel21);				// but I can't make a meterPanel array
        content.add(meterPanel22);
        content.add(meterPanel23);
        content.add(meterPanel24);
        content.add(meterPanel25);
        content.add(meterPanel26);
        content.add(meterPanel27);
        content.add(meterPanel28);
        content.add(meterPanel29);
        content.add(meterPanel30);
        content.add(meterPanel31);
        content.add(meterPanel32);
        content.add(meterPanel33);
        content.add(meterPanel34);
        content.add(meterPanel35);
        content.add(meterPanel36);
        content.add(meterPanel37);
        content.add(meterPanel38);
        content.add(meterPanel39);
        content.add(meterPanel40);
        content.add(meterPanel41);
        content.add(meterPanel42);
        content.add(meterPanel43);
        content.add(meterPanel44);
        content.add(meterPanel45);
        content.add(meterPanel46);
        content.add(meterPanel47);
        content.add(meterPanel48);
        content.add(meterPanel49);
        content.add(meterPanel50);
        content.add(meterPanel51);
        content.add(meterPanel52);
        content.add(meterPanel53);
        content.add(meterPanel54);
        content.add(meterPanel55);
        content.add(meterPanel56);
        content.add(meterPanel57);
        content.add(meterPanel58);
        content.add(meterPanel59);
        content.add(meterPanel60);
        content.add(meterPanel61);
        content.add(meterPanel62);
        content.add(meterPanel63);
        frame.setAlwaysOnTop(true);
        frame.setContentPane(content);
        frame.pack();
        frame.setVisible(true);
        frame.setBounds(50, 50, 2, 2);
    	
    }
   
}
