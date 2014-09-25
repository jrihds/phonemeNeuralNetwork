import java.awt.event.*;

import javax.swing.*;

import java.util.Arrays;

public class JR_MainApp {
	
   public static int onscreen_bins = 128;
   public static int fftsize = 1024;
   public static double spectrum[] = new double[fftsize];
   
   public static double magn_log[] = new double[onscreen_bins];
   public static double smoothed[] = new double[onscreen_bins];
   public static double outputs[][] = new double[onscreen_bins][2];
   public static double outputs_sort[] = new double[5];
   public static double vocal_tract[][][] = new double[405][600][5];
   public static double lips_1[][][] = new double[405][600][5];
   public static double lips_2[][][] = new double[405][600][5];


   public static void main(String args[]) throws Exception {
	   
	   int channel=1; 							// for JR sound card !!!!
	   
       final JR_AudioProcess JR_Proc = new JR_AudioProcess(fftsize,(float)44100.0,channel);
       final JR_makeFrames frames = new JR_makeFrames();
       //final JR_NeuralNet JR_NN = new JR_NeuralNet();
       final JR_RecurrentNeuralNet JR_recNN = new JR_RecurrentNeuralNet();
       final JR_SpectrumAdjust SpecAdj = new JR_SpectrumAdjust();
       final JR_ReadImage ri = new JR_ReadImage();
       
	   vocal_tract=ri.read();								// Read Vocal Tract Images from file into an array
	   lips_1=ri.read_lips_1();								// Read Outer Lips from file into an array
	   lips_2=ri.read_lips_2();								// Read Inner Lips from file into an array
       
       frames.makeAnalyser(onscreen_bins);					// Construct Master MeterPane	l Spectrum Analyser
       frames.makeTract();									// Construct Vocal Tract Image
       frames.makeLips();									// Construct Lips Image
       frames.makeGraph();									// Construct Graph
       
       JR_recNN.readFile();									// Load Neural Network from file
       
       Timer timer = new Timer(40, new ActionListener() {
    	   public void actionPerformed(ActionEvent ae) {
    		   
    		   double values[][] = JR_Proc.getValues();								// Grab FFT outputs
    		   for (int i=0; i<spectrum.length; i++) {
    			   spectrum[i]=values[i][0];										// Load values we want to see into an array
    		   }
    		   
    		   SpecAdj.autoGain(spectrum);											// Do some gain control on the FFT values
        	   magn_log=SpecAdj.linear_log(onscreen_bins,fftsize,spectrum);			// Convert scale from linear to log
        	   smoothed=SpecAdj.run_average(onscreen_bins, magn_log);				// Smooth FFT in time with 3 point running average
        	   
        	   outputs=JR_recNN.recforwardPass(smoothed);							// Perform forward pass of Neural Net on FFT data
        	   
        	   frames.updateMeters(smoothed, onscreen_bins, outputs);				// Display all of the information on the meters
        	   
        	   for (int i=0; i<outputs_sort.length; i++) {							// Move Neural Net outputs into new array
        		   outputs_sort[i]=outputs[i][2];
        		   }
        	   Arrays.sort(outputs_sort);											// Sort said array
        	   
        	   String text="";
        	   if (outputs_sort[4]>0.4) {
		           	if (outputs_sort[4]==outputs[0][2]) {text="AH";}
		           	if (outputs_sort[4]==outputs[1][2]) {text="EE";}
		           	if (outputs_sort[4]==outputs[2][2]) {text="EH";}				// Find which is the largest magnitude Neural Net output
		           	if (outputs_sort[4]==outputs[3][2]) {text="OH";}
		           	if (outputs_sort[4]==outputs[4][2]) {text="OO";}
        	   }
        	   frames.updateFace(vocal_tract, outputs, text, lips_1, lips_2, outputs);		// Send a whole load of data to plot onto the screen
           }
       });
       String inName="default [default]";
	   String outName="default [default]";
       JR_Proc.startAudio(inName,outName); 										// Start Audio Capture
       timer.start();
   }

}