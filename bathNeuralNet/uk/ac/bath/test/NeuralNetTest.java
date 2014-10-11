package uk.ac.bath.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

import speech.neuralNetwork.NeuralNet;
import uk.ac.bath.ai.backprop.BackProp;

public class NeuralNetTest {

	public static void train(NeuralNet bp, double[][] testData, double[][] target) {
		int nTest = target.length;
		double Thresh = 0.00001;

		// maximum no of iterations during training
		long num_iter = 2000000;

		System.out.println("Now training the network....");
		int i;

		for (i = 0; i < num_iter; i++) {
			double maxError = 0;

			for (int j = 0; j < nTest; j++) {
				
				bp.backPropTrain(testData[j], target[j]);
				double out[]=bp.forwardPass(testData[j]);
				maxError = Math.max(Util.mse(out,target[j]),maxError);
			}

			if (maxError < Thresh) {
				System.out
						.println("Network Trained. Threshold value achieved in "
								+ i
								* nTest
								+ " iterations.\n"
								+ "MSE:  "
								+ maxError);
				break;
			}

			System.out.println("MSE:  " + String.format("%f7.5", maxError)
					+ "... Training...");
		}

		/*if (i == num_iter) {
			System.out.println(i + " iterations completed..." + "MSE: "
					+ bp.mse(target[(i - 1) % 8]));
		}*/
	}

	public static void testNet(NeuralNet bp, double[][] testData,
			double[][] target) {
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

		// prepare XOR traing data

		double target[][] = { { 0, 1 }, { 1, 1 }, { 1, 0 }, { 0, 0 }, { 1, 1 },
				{ 0, 1 }, { 0, 1 }, { 1, 1 } };

		// prepare test data
		double testData[][] = { { 0, 0, 0 }, { 0, 0, 1 }, { 0, 1, 0 },
				{ 0, 1, 1 }, { 1, 0, 0 }, { 1, 0, 1 }, { 1, 1, 0 }, { 1, 1, 1 } };

		// defining a net with 4 layers having 3,3,3, and 1 neuron respectively,
		// the first layer is input layer i.e. simply holder for the input
		// parameters
		// and has to be the same size as the no of input parameters, in out
		// example 3
		// int numLayers = 4, lSz[] = {3, 3, 2, 1};

		// Yes well .....
		int lSz[] = { 3, 6, 2 };

		// int numLayers = lSz.length;

		// Learing rate - beta
		// momentum - alpha
		// Threshhold - thresh (value of target mse, training stops once it is
		// achieved)
		double beta = .01, alpha = 1000.;
		// Creating the net
		NeuralNet bp = new BackProp(lSz, beta, alpha, new Random());
		//NeuralNet bp = new JR_NeuralNet(lSz[0],lSz[1],lSz[2],beta);

		train(bp,testData,target);
		testNet(bp,testData,target);
		
		File file = new File("PJLBackprop.net");

		try {
			FileOutputStream istr = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(istr);
			out.writeObject(bp);
			out.close();
			bp=null;
			FileInputStream ostr = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(ostr);
			bp=(NeuralNet)in.readObject();
			in.close();
			System.out.println(" Loaded from file .......... ");
			testNet(bp,testData,target);
			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
