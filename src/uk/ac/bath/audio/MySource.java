/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.bath.audio;

import java.util.Random;
import uk.org.toot.audio.core.AudioBuffer;
import uk.org.toot.audio.core.AudioProcess;

/**
 *
 * @author pjl
 */
public class MySource implements AudioProcess {

    double omega;
 //   double omegaNext;
    double amp;
    double phi;
    double TWOPI = Math.PI * 2;
//    double fact1 = 0.9999;
//    double fact2 = 1 - fact1;
    Random rand = new Random();
 
    public enum Wave {

        SIN("sine"), SAW("saw"), SQUARE("square"), NOISE("noise");
        String name;

        Wave(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    };
    
    Wave wave = Wave.SIN;

    public float getFreq() {
        return (float) (omega / TWOPI);
    }

    public void setWave(Wave wave) {
        this.wave = wave;
    }

    public void setAmp(double amp) {
        this.amp = amp;
    }

    public void setFreq(double freq) {
        this.omega = Math.PI * 2 * freq;
    }

    public void open() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int processAudio(AudioBuffer buff) {
        int n = buff.getSampleCount();
        int nChan = buff.getChannelCount();
        float val;
        for (int i = 0; i < n; i++) {
        //    omega = omega * fact1 + omegaNext * fact2;
            double dPhi = omega / buff.getSampleRate();
            switch (wave) {
                case SIN:
                    val = (float) (amp * Math.sin(phi));
                    break;
                case SAW:
                    val = (float) (2 * amp * ((phi % TWOPI) / TWOPI - 0.5));
                    break;
                case SQUARE:
                    if ((phi % TWOPI) / TWOPI > 0.5) {
                        val = (float) amp;
                    } else {
                        val = (float) -amp;
                    }
                    break;
                    
                case NOISE:
                    val = (float) ((rand.nextFloat() - 0.5f) * amp * 2.0f);
                    break;

                default:
                    val = 0;
            }

            for (int c = 0; c < nChan; c++) {
                buff.getChannel(c)[i] += val;
            }


            phi += dPhi;
        }
        return AUDIO_OK;
    }

    public void close() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
