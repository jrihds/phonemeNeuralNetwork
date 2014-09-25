package speech;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.Timer;

public class MainApp {
	
	public int onscreenBins = 128;
	public int fftSize = 1024;
	public int phonemes = 6;
	
	public double spectrum[] = new double[fftSize];
	public double outputSort[] = new double[phonemes];
	public double magnLog[];
	public double smoothed[];
	public double outputs[];
	public double vocalTract[][][];
	public double lipsInner[][][];
	public double lipsOuter[][][];
	
	MakeFrames frames;
	ReadImage ri;
	Timer timer;
	NeuralNetClient client;
	
	public boolean isApplet = false; 			// hack hack hack ... eeeek
	
	public static void main(String args[]) throws Exception {
		MainApp app = new MainApp(false);
		app.start();
	}
	
	MainApp(boolean isApplet) {
		
		frames = new MakeFrames(isApplet, phonemes, onscreenBins); 		// Create gfx for output
		
		final ReadImage ri = new ReadImage();
		try {
			vocalTract = ri.readTract(); 			// Read in data from images
			lipsInner = ri.readLips1(); 			// of lip and tract shapes
			lipsOuter = ri.readLips2();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		frames.makeMaster();
		
		timer = new Timer(40, new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				
				outputs = client.outputs;
				
				for (int i = 0; i < outputs.length; i++) {
					outputSort[i] = outputs[i];
				}
				Arrays.sort(outputSort);
				
				String text = "";
				if (outputSort[5] > 0.3) {
					if (outputSort[5] == outputs[0]) {text = "EEE";}
					if (outputSort[5] == outputs[1]) {text = "EHH";}
					if (outputSort[5] == outputs[2]) {text = "ERR";}
					if (outputSort[5] == outputs[3]) {text = "AHH";}
					if (outputSort[5] == outputs[4]) {text = "OOH";}
					if (outputSort[5] == outputs[5]) {text = "UHH";}
				}
				
				frames.updateGfx(vocalTract, text, lipsInner, lipsOuter, outputs, client.smoothed);
			}
		});
		
	}
	
	void start() throws InterruptedException {
		
		SpectralAnalysisProcess spectralAnalysis = new SpectralAnalysisProcess(
				fftSize, (float) 44100.0);
		
		RealTimeSpectralSource rtSource = new RealTimeSpectralSource(
				spectralAnalysis);
		
		client = new NeuralNetClient(fftSize, onscreenBins,frames.drawScroll);
		
		// Setup input from soundcard
		String inName = null;
		String outName = null;
		if (inName == null) {
			inName = "default [default]";
		}
		if (outName == null) {
			outName = "default [default]";
		}
		
		try {
			rtSource.startAudio(inName, outName, onscreenBins, client);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		timer.start();
	}
}