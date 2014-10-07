package uk.ac.bath.test;

import javax.swing.text.html.HTMLDocument.Iterator;

import speech.NeuralNet;
import uk.ac.bath.ai.backprop.TrainingData;

public class NeuralNetEvolvableWrapper {

	
	private TestData data;
	private NeuralNet net;


	public NeuralNetEvolvableWrapper(TestData data,NeuralNet net) {
		this.data=data;
		this.net=net;
	}
	
	public void randomGuess(){
		net.randomWeights();	
	}
	 
	public double fitness() {
		double maxError = 0;

		TraingDataIterator iter=data.iter();
		
		while(true) {
			TrainingData d=iter.next();
			if (d == null) break;
			double out[]= net.forwardPass(d.in);
			double err=0;
			for (int i=0;i<out.length;i++){
				err += Math.sqrt((d.out[i]-out[i])*(d.out[i]-out[i]));	
				maxError = Math.max(err, maxError);
			}
		}
		return maxError;
	}

	
}
