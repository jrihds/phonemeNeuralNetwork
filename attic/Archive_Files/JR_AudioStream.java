package Archive_Files;
import MeterPanel;

import com.frinika.sequencer.model.audio.AudioReader;
import java.io.File;
import javax.sound.sampled.*;
import com.frinika.sequencer.model.audio.AudioWriter;
import com.frinika.sequencer.model.audio.VanillaRandomAccessFile;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.AudioSystem;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import uk.ac.bath.audio.AudioPeakMonitor;
import uk.org.toot.audio.core.AudioBuffer;
import uk.org.toot.audio.server.AudioClient;
import uk.org.toot.audio.server.IOAudioProcess;
import uk.org.toot.audio.server.MultiIOJavaSoundAudioServer;

/**
 *
 * @author pjl
 * edited by JER
 */
public class JR_AudioStream {

    private static MultiIOJavaSoundAudioServer audioServer;
    private static AudioBuffer chunk;
    private static JFrame frame;
    private static AudioPeakMonitor peakIn;
    private static MeterPanel meterPanel;
    private static AudioWriter audioWriter;
    private static boolean writing=true;
    private static AudioReader audioReader;
    private static AudioClient audioClient;

    public static void main(String args[]) throws Exception {
    	
        audioServer = new MultiIOJavaSoundAudioServer();
        final IOAudioProcess output = audioServer.openAudioOutput("default [default]", "output");
        final IOAudioProcess input = audioServer.openAudioInput("default [default]", "input");
        chunk = audioServer.createAudioBuffer("default");
        chunk.setRealTime(true);
        
        peakIn = new AudioPeakMonitor();
        
        File file = new File("test.wav");
        AudioFormat format = new AudioFormat(audioServer.getSampleRate(), 16, ((IOAudioProcess) input).getChannelFormat().getCount(), true, false);
        audioWriter = new AudioWriter(file, format);
        
        audioClient = new AudioClient() {
            public void work(int arg0) {
                chunk.makeSilence();
                input.processAudio(chunk);
                output.processAudio(chunk);
                peakIn.processAudio(chunk);
                if (writing) audioWriter.processAudio(chunk);
            }
            public void setEnabled(boolean arg0) {}
        };
        
        audioServer.setClient(audioClient);
        audioServer.start();
        configure();

        Timer timer = new Timer(50, new ActionListener() {
            int cnt=0;
            public void actionPerformed(ActionEvent ae) {
                cnt=cnt+1;
                if (cnt == 100) {
                    writing=false;
                    audioWriter.close();
                 }
                updateMeters();
            }
        });
        timer.start();
    }

    private static void updateMeters() {
        double val = peakIn.getPeak();
        if (val > .99) {
            meterPanel.updateMeter(val, Color.RED);
        } else {
            meterPanel.updateMeter(val, Color.GREEN);
        }
    }

    public static void configure() {
        frame = new JFrame();
        JPanel content = new JPanel();
        meterPanel = new MeterPanel();
        content.add(meterPanel);
        frame.setAlwaysOnTop(true);
        frame.setContentPane(content);
        frame.pack();
        frame.setVisible(true);
    }
}