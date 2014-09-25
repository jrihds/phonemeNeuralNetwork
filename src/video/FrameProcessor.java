package video;

import speech.*;

public class FrameProcessor {
	
	NeuralNet neuralNet;
	int outputs;
	
	public void FrameProcessor(NeuralNet neuralNet, int outputs) {
		this.neuralNet = neuralNet;
		this.outputs = outputs;
	}
	
	public double[] forwardPass(double[][] red, double[][] green ,double[][] blue) {
		// Perform a forward neural net pass of the input image
		// Input image needs to be XX by XX pixels
		
		double neuralNetOutputs[] = new double[outputs];
		double networkInputs[] = new double[(red.length * red.length)*3];
		
		int count = 0;
		for (int i=0; i<red.length; i++) {
			for (int j=0; j<red.length; j++) {
				networkInputs[count] = red[i][j];
				count++;
				networkInputs[count] = green[i][j];
				count++;
				networkInputs[count] = blue[i][j];
				count++;
			}
		}
		
		neuralNetOutputs = neuralNet.forwardPass(networkInputs);
		
		return neuralNetOutputs;
		
	}
	
}
