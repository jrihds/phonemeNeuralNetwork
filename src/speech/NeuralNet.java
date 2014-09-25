package speech;

import java.io.Serializable;

// This is an interface so we can combine neural network classes
// and benchmark them. Primarily, the PJL network is now used.

public interface NeuralNet extends Serializable{

	double[] forwardPass(double[] smoothed);

	// randomize network
	void randomWeights();

	double [] backPropTrain(double[] phonemeSmoothed, double[] trainOutvals);

	void randomWeights(double low, double high);

}
