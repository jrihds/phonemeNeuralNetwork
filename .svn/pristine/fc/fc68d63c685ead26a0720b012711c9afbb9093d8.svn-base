import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;

import config.Config;

import java.io.*;
import java.util.Arrays;
import java.math.*;

public class PJL_MainApp {

	public int onscreen_bins = 128;
	public int fftsize = 1024;
	public double spectrum[] = new double[fftsize];

	public double magn_log[] = new double[onscreen_bins];
	public double smoothed[] = new double[onscreen_bins];
	public double outputs[][] = new double[onscreen_bins][2];
	public double outputs_sort[] = new double[5];
	public double vocal_tract[][][] = new double[405][600][5];
	public double lips_1[][][] = new double[405][600][5];
	public double lips_2[][][] = new double[405][600][5];
	public PJL_makeFrames frames = new PJL_makeFrames();
	JR_AudioProcess JR_Proc;
	JR_NeuralNet JR_NN;
	JR_SpectrumAdjust SpecAdj;
	JR_ReadImage ri;
	Timer timer;
	
	public static void main(String args[]) throws Exception {
		PJL_MainApp app = new PJL_MainApp();
		app.start();
	}

	PJL_MainApp() {

		// Config.load();

		Float Fs = 44100.0f;

		Float sampleRate = Config.getFloatProperty("sampleRate");

		if (sampleRate != null) {
			Fs = sampleRate;
		}

		Integer inChannel = Config.getIntProperty("channel");

		int chn = 1;

		if (inChannel != null) {
			chn = inChannel;
		}

		JR_Proc = new JR_AudioProcess(fftsize, Fs, chn);
		JR_NN = new JR_NeuralNet();
		SpecAdj = new JR_SpectrumAdjust();
		ri = new JR_ReadImage();

		try {
			vocal_tract = ri.read();
			// array
			lips_1 = ri.read_lips_1(); // Read Outer Lips from file into an
										// array
			lips_2 = ri.read_lips_2(); // Read Inner Lips from file into an
										// array
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Read Vocal Tract Images from file into an

		frames.makeAnalyser(onscreen_bins); // Construct Master MeterPane l
											// Spectrum Analyser
		frames.makeTract(); // Construct Vocal Tract Image
		frames.makeLips(); // Construct Lips Image

		JR_NN.readFile(); // Load Neural Network from file
		timer = new Timer(40, new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				double values[][] = JR_Proc.getValues(); // Grab FFT outputs
				for (int i = 0; i < spectrum.length; i++) {
					spectrum[i] = values[i][0]; // Load values we want to see
												// into an array
				}

				SpecAdj.autoGain(spectrum); // Do some gain control on the FFT
											// values
				magn_log = SpecAdj.linear_log(onscreen_bins, fftsize, spectrum); // Convert
																					// scale
																					// from
																					// linear
																					// to
																					// log
				smoothed = SpecAdj.run_average(onscreen_bins, magn_log); // Smooth
																			// FFT
																			// in
																			// time
																			// with
																			// 3
																			// point
																			// running
																			// average

				outputs = JR_NN.forwardPass(smoothed); // Perform forward pass
														// of Neural Net on FFT
														// data
				frames.meterPanel
						.updateMeters(smoothed, onscreen_bins, outputs); // Display
																			// all
																			// of
																			// the
																			// information
																			// on
																			// the
																			// meters

				for (int i = 0; i < outputs_sort.length; i++) { // Move Neural
																// Net outputs
																// into new
																// array
					outputs_sort[i] = outputs[i][1];
				}
				Arrays.sort(outputs_sort); // Sort said array

				String text = "";
				if (outputs[0][1] > 0.3 || outputs[1][1] > 0.3
						|| outputs[2][1] > 0.3 || outputs[3][1] > 0.3
						|| outputs[4][1] > 0.3) {
					if (outputs_sort[4] == outputs[0][1]) {
						text = "AH";
					}
					if (outputs_sort[4] == outputs[1][1]) {
						text = "EE";
					}
					if (outputs_sort[4] == outputs[2][1]) {
						text = "EH";
					} // Find which is the largest magnitude Neural Net output
					if (outputs_sort[4] == outputs[3][1]) {
						text = "OH";
					}
					if (outputs_sort[4] == outputs[4][1]) {
						text = "OO";
					}
				}
				frames.updateFace(vocal_tract, outputs, text, lips_1, lips_2,
						outputs); // Send a whole load of data to plot onto the
									// screen
			}
		});

	}

	void start() {

		String inName = Config.getProperty("input");
		String outName = Config.getProperty("output");

		if (inName == null) {
			inName = "default [default]";
		}

		if (outName == null) {
			outName = "default [default]";
		}

		try {
			JR_Proc.startAudio(inName, outName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Start Audio Capture
		timer.start();
	}
}