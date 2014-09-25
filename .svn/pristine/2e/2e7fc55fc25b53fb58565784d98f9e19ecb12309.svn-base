package Archive_Files;

import MeterPanel;

import com.frinika.sequencer.model.audio.AudioReader;

import com.frinika.sequencer.model.audio.VanillaRandomAccessFile;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import uk.ac.bath.audio.AudioPeakMonitor;
import uk.org.toot.audio.core.AudioBuffer;
import uk.org.toot.audio.server.AudioClient;
import uk.org.toot.audio.server.IOAudioProcess;
import uk.org.toot.audio.server.MultiIOJavaSoundAudioServer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author pjl
 */
public class ReadAndPlay {

    private static MultiIOJavaSoundAudioServer audioServer;
    private static AudioBuffer chunk;
    private static JFrame frame;
    private static AudioPeakMonitor peakIn;
    private static MeterPanel meterPanel;
    private static AudioReader audioReader;

    public static void main(String args[]) throws Exception {

        audioServer = new MultiIOJavaSoundAudioServer();

        peakIn = new AudioPeakMonitor();
        List<String> list = audioServer.getAvailableOutputNames();
        Object a[] = new Object[list.size()];
        a = list.toArray(a);

        frame = new JFrame();
        Object selectedValue = JOptionPane.showInputDialog(frame,
                "audio_output", "OUTPUT", JOptionPane.INFORMATION_MESSAGE,
                null, a, a[0]);

        final IOAudioProcess output = audioServer.openAudioOutput((String) selectedValue,
                "output");


        chunk = audioServer.createAudioBuffer("default");
        chunk.setRealTime(true);

        File file = new File("/home/pjl/Sassy9.wav");

        RandomAccessFile rafG = new RandomAccessFile(file, "r");

        audioReader = new AudioReader(new VanillaRandomAccessFile(rafG));

        audioServer.setClient(new AudioClient() {

            public void work(int arg0) {
                chunk.makeSilence();
                if (audioReader.eof()) {
                } else {
                    audioReader.processAudio(chunk);
                }
                output.processAudio(chunk);
                peakIn.processAudio(chunk);
            }

            public void setEnabled(boolean arg0) {
                //  throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        audioServer.start();
        configure();


        Timer timer = new Timer(50, new ActionListener() {

            public void actionPerformed(ActionEvent ae) {

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

class MeterPanel2 extends JPanel {

    double val = 0.0;
    Color color = null;
    int redcount = 0;

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(10, 100);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(10, 100);
    }

    void updateMeter(double val, Color col) {
        this.val = val;
        if (color == null || col == Color.RED) {
            color = col;
        }
        repaint();

    }

    @Override
    public void paintComponent(Graphics g) {

        int w = getWidth();
        int h = getHeight();

        if (val <= 0.0) {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, w, h);
        } else {
            int h2 = (int) ((1.0 - val) * h);
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, w, h2);

            if ((redcount + 1) % 4 != 0) {
                g.setColor(color);
            } else {
                g.setColor(Color.WHITE);
            }
            g.fillRect(0, h2, w, h);
        }
        if (color == Color.RED) {
            redcount++;
            if (redcount > 20) {
                color = null;
                redcount = 0;
            }
        } else {
            color = null;
        }
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, w - 1, h - 1);
    }
}
