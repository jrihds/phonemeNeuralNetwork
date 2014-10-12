package speech.neuralNetwork;

//@author PJL
//
// This interface is implemented by the neural network developed
//   by Paul Leonard which can be found in 'bathNeuralNet' directory
//	 as part of the uk.ac.bath package

import java.io.Serializable;

public interface NeuralNet extends Serializable{

	double[] forwardPass(double[] smoothed);

	void randomWeights();

	double [] backPropTrain(double[] phonemeSmoothed, double[] trainOutvals);

	void randomWeights(double low, double high);

}