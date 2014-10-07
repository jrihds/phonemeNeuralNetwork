package uk.ac.bath.test;

import java.util.Random;

import speech.NeuralNet;
import uk.ac.bath.ai.backprop.BackPropRecursive;
import uk.ac.bath.ai.backprop.TrainingData;

public class RecursiveNetTest {

	public static void train(NeuralNet bp, TestSequence seq) {

		double Thresh = 0.001;

		// maximum no of iterations during training
		long num_iter = 2000000;

		System.out.println("Now training the network....");
		int i;

		double error=1.0f;
		double errorDamp=.01f;
		
		for (i = 0; i < num_iter; i++) {

			TrainingData data = seq.getNext();

			double out[]=bp.backPropTrain(data.in, data.out);
		
			
			double err=Util.mse(out,data.out);
			
			error=(double) (error*(1.0f-errorDamp)+errorDamp*err);
			
			if (error < Thresh) {
				System.out
						.println("Network Trained. Threshold value achieved in "
								+ i + " iterations.\n" + "MSE:  " + error);
				break;
			}

			System.out.println("MSE:  " + String.format("%f7.5", error)
					+ "... Training...");
		}

		/*
		 * if (i == num_iter) { System.out.println(i +
		 * " iterations completed..." + "MSE: " + bp.mse(target[(i - 1) % 8]));
		 * }
		 */
	}

	public static void testNet(NeuralNet bp, double[][] testData, double[][] target) {
		int nTest = target.length;
		System.out
				.println("Now using the trained network to make predctions on test data....");
		for (int i = 0; i < nTest; i++) {
			double out[]=bp.forwardPass(testData[i]);
			System.out.println(testData[i][0] + "  " + testData[i][1] + "  "
					+ testData[i][2] + "  " + target[i][0] + "  " + out[0]
					+ "  " + target[i][1] + "  " + out[1]);
		}
	}

	public static void main(String arg[]) {

		TestSequence seq = new TestSequence();

		// Yes well .....
		int lSz[] = { seq.nIn, 20, seq.nOut };

		double beta = .00001, alpha = 10000.;
		// Creating the net
		BackPropRecursive bp = new BackPropRecursive(lSz, beta, alpha,new Random());

		train(bp, seq);

	}

}
