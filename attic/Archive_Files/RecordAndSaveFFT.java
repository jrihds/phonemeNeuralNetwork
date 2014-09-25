package Archive_Files;

import MeterPanel;

import com.frinika.audio.toot.AudioPeakMonitor;
import com.frinika.sequencer.model.audio.AudioWriter;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.Timer;

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
public class RecordAndSaveFFT {

    private MultiIOJavaSoundAudioServer audioServer;
    private AudioBuffer chunk;
    private JFrame frame;
    private AudioPeakMonitor peakIn;
    private MeterPanel meterPanel;
    private AudioWriter audioWriter;
    private boolean writing = false;
    JToggleButton button;
    IOAudioProcess input;

    public static void main(String args[]) throws Exception {
        new RecordAndSaveFFT().invoke();


    }

    void invoke() throws Exception {

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

        list = audioServer.getAvailableInputNames();
        a = new Object[list.size()];
        a = list.toArray(a);

        selectedValue = JOptionPane.showInputDialog(frame,
                "audio_input", "INPUT", JOptionPane.INFORMATION_MESSAGE,
                null, a, a[0]);

        input = audioServer.openAudioInput((String) selectedValue,
                "output");

        chunk = audioServer.createAudioBuffer("default");
        chunk.setRealTime(true);





        audioServer.setClient(new AudioClient() {

            public void work(int arg0) {
                chunk.makeSilence();
                output.processAudio(chunk);
                input.processAudio(chunk);
                peakIn.processAudio(chunk);
                if (writing) {
                    audioWriter.processAudio(chunk);
                }
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

    private void updateMeters() {
        double val = peakIn.getPeak();
        if (val > .99) {
            meterPanel.updateMeter(val, Color.RED);
        } else {
            meterPanel.updateMeter(val, Color.GREEN);
        }

    }

    public void configure() {



        frame = new JFrame();
        final JPanel content = new JPanel();
        button = new JToggleButton("RECORD FFT");

        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Thread t=new Thread(new SaveAndFFT());
                t.start();
            }
        });

        meterPanel = new MeterPanel();
        content.add(meterPanel);
        content.add(button);
        
        frame.setAlwaysOnTop(
                true);
        frame.setContentPane(content);
        frame.pack();
        frame.setVisible(
                true);
    }

    class SaveAndFFT implements Runnable {

        public void run() {

            System.out.println("HEllo 1");

            File file = new File("test.wav");

            AudioFormat format = new AudioFormat(
                    audioServer.getSampleRate(),
                    16,
                    ((IOAudioProcess) input).getChannelFormat().getCount(),
                    true, false);
            try {
                audioWriter = new AudioWriter(file, format);
                writing=true;
            } catch (IOException ex) {
                Logger.getLogger(RecordAndSaveFFT.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RecordAndSaveFFT.class.getName()).log(Level.SEVERE, null, ex);
            }

            writing=false;
            audioWriter.close();
            
            System.out.println(" Do othter stuff now");

            /// do other stuff here

        }
    }
}

//class MeterPanel2 extends JPanel {
//
//    double val = 0.0;
//    Color color = null;
//    int redcount = 0;
//
//    @Override
//    public Dimension getMaximumSize() {
//        return new Dimension(10, 100);
//    }
//
//    @Override
//    public Dimension getPreferredSize() {
//        return new Dimension(10, 100);
//    }
//
//    void updateMeter(double val, Color col) {
//        this.val = val;
//        if (color == null || col == Color.RED) {
//            color = col;
//        }
//        repaint();
//
//    }
//
//    @Override
//    public void paintComponent(Graphics g) {
//
//        int w = getWidth();
//        int h = getHeight();
//
//        if (val <= 0.0) {
//            g.setColor(Color.DARK_GRAY);
//            g.fillRect(0, 0, w, h);
//        } else {
//            int h2 = (int) ((1.0 - val) * h);
//            g.setColor(Color.DARK_GRAY);
//            g.fillRect(0, 0, w, h2);
//
//            if ((redcount + 1) % 4 != 0) {
//                g.setColor(color);
//            } else {
//                g.setColor(Color.WHITE);
//            }
//            g.fillRect(0, h2, w, h);
//        }
//        if (color == Color.RED) {
//            redcount++;
//            if (redcount > 20) {
//                color = null;
//                redcount = 0;
//            }
//        } else {
//            color = null;
//        }
//        g.setColor(Color.BLACK);
//        g.drawRect(0, 0, w - 1, h - 1);
//    }
//}
