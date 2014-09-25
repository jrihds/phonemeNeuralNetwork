
package uk.ac.bath.audio;

import rasmus.interpreter.sampled.util.FFT;

/**
 *
 * @author pjl
 */
public class FFTWorker {

    private FFT fft;
    private double[] hanning;
    int fftsize=-1;
    private int nBin;
  //  private double[] fftOut;
    float freqArray[];
    double freq[];
    private double Fs;
    private boolean doHanning;

    public FFTWorker(double Fs, boolean doHanning) {
        this.doHanning = doHanning;
//        this.Fs=Fs;
    }

    public float[] getFreqArray() {
        return freqArray;
    }

    public int getSizeInBins() {
        return nBin;
    }

    public void process(double[] input, double fftOut[]) {

        if (doHanning) {
            for (int i = 0; i < fftsize; i++) {
                fftOut[i] = input[i] * hanning[i];
            }
        }

        fft.calcReal(fftOut, -1);

    }

    public void resize(int fftsize) {
    	
    	// PJL don't resize if we don't need to !!!
    	if (this.fftsize == fftsize) return;
    	
        this.fftsize = fftsize;
        fft = new FFT(fftsize);
        hanning = fft.wHanning();

        nBin = fftsize / 2;

   //     fftOut = new double[fftsize * 2];
        freqArray = new float[nBin];
        freq = new double[nBin];

        for (int i = 0; i < nBin; i++) {
            freq[i] = (i * Fs / nBin);
            freqArray[i] = (float) freq[i];

        // System.out.println(" fftsize/chunkSIze = " + fftsize + "/"
        // + chunksize);


        }


    }
    public int getFFTSize() {
        return fftsize;
    }
}
