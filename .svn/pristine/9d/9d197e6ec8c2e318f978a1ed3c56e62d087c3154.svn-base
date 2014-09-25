package speech;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class JR_WavTraining {
	
	public static int inputs = 128;
	public static int outputs = 5;
	
	public static void main(String args[]) throws Exception {
		
		final NeuralNet JR_NN = new JR_NeuralNet(128, 128, 5, 0.5);
		final NeuralNet JR_recNN = new JR_RecNeuralNet(10, 5, 0.01);
		final JR_SpectrumAdjust SpecAdj = new JR_SpectrumAdjust();
		final JR_ReadWav readWav = new JR_ReadWav();
		
		JR_NN.randomWeights();
		
		double error=1;
    	double[] phoneme_raw = new double[1024];
    	double[] phoneme_log = new double[128];
    	double[] phoneme_smoothed = new double[128];
    	
    	int count=0;
    	int i_max;
    	
    	// -------- Train Monothongs -------------------------------------------- //
    	
    	double[][][] wavs =  readWav.getMTWavs();
		
    	while (error>0.001) {				// While
    		error=0;
    		
    		i_max = readWav.file_length[0]-2;
    		
    		// readWav.file_length[0] can be replaced with a number

    		for (int i=0; i<i_max; i++) {			// Cycle through instances of FFT
    			
    			for (int p=0; p<6; p++) {					// Cycle through phonemes

	    			for (int j=0; j<1024; j++) {
	    				phoneme_raw[j] = wavs[i][j][p];				// read current phoneme into array
	    			}
	    			
	    			phoneme_log	= SpecAdj.linear_log(128, 1024, phoneme_raw);				// Convert scale from linear to log
	         	   	phoneme_smoothed = SpecAdj.run_average(128, phoneme_log);				// Smooth FFT in time with 3 point running average
	         	   	
	         	    double[] train_outvals = new double[6];
		            if (p==0) {train_outvals[0]=1.0;}
		        	if (p==1) {train_outvals[1]=1.0;}
		        	if (p==2) {train_outvals[2]=1.0;}			// create training values for current phoneme
		        	if (p==3) {train_outvals[3]=1.0;}
		        	if (p==4) {train_outvals[4]=1.0;}
		        	
		        	
		        	error+=JR_NN.backPropTrain(phoneme_smoothed, train_outvals);		// Go!
    			}
    		}
    		if (count%50 == 0) System.out.println("Total Error: "+error);
			count++;
		}
		System.out.println("Finished Monothong training. \n It took me "+count+" back props");
		
		FileOutputStream istr = new FileOutputStream("src/textfiles/network.txt");
		ObjectOutputStream out = new ObjectOutputStream(istr);
		out.writeObject(JR_NN);
		out.close();
		
		
		// -------- Train Diphthongs -------------------------------------------- //
		
		JR_recNN.randomWeights();
		
		wavs =  readWav.getDTWavs();
 	    double[] train_outvals = new double[5];
		
 	    count=0;
 	    
		error=1;
		while (error>0.001) {
			
    		error=0;
    		
    		for (int p=0; p<6; p++) {									
    		
				i_max = readWav.file_length[p];
				// readGTWav.file_length can be replaced with a number
				
				for (int i=1; i<21; i++) {						
					
	    			for (int j=0; j<1024; j++) {
	    				phoneme_raw[j] = wavs[i][j][p];			
	    			}
	    			
	    			phoneme_log	= SpecAdj.linear_log(128, 1024, phoneme_raw);				// Convert scale from linear to log
	         	   	phoneme_smoothed = SpecAdj.run_average(128, phoneme_log);				// Smooth FFT in time with 3 point running average
	         	   	
	         	   	double[] mono_outs = JR_NN.forwardPass(phoneme_smoothed);
	         	   	
	         	    train_outvals[0]=0.0;
	         	    train_outvals[1]=0.02;
	         	    train_outvals[2]=0.0;
	         	    train_outvals[3]=0.0;
	         	    train_outvals[4]=0.0;
		            if (p==0 && i>10) {train_outvals[0]=1.0;}
		        	if (p==1 && i>10) {train_outvals[1]=1.0;}
		        	
					error+=JR_recNN.recbackPropTrain(mono_outs, train_outvals);
					
    			}
    		}
    		if (count%100 == 0) System.out.println("Total Error: "+error+" ");
			count++;
			if (count%50000 == 0) {
				FileOutputStream istr2 = new FileOutputStream("src/textfiles/rec_network.txt");
				ObjectOutputStream out2 = new ObjectOutputStream(istr2);
				out2.writeObject(JR_recNN);
				out.close();
				System.out.println("Saved!");
			}
			
		}
		
		System.out.println("Finished Diphthong training. \n It took me "+count+" back props");
		
		FileOutputStream istr2 = new FileOutputStream("src/textfiles/rec_network.txt");
		ObjectOutputStream out2 = new ObjectOutputStream(istr2);
		out2.writeObject(JR_recNN);
		out.close();
		
		System.out.println("Whooop finished training!");
		
	}
    
}