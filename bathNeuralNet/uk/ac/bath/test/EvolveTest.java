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
import uk.ac.bath.ai.backprop.TrainingData;

public class EvolveTest {

	public static void randomTrain(NeuralNetEvolvableWrapper evo) {
	
		double thresh=.5;
		double fit;
		long cnt=0;
		do {
			evo.randomGuess();
			fit=evo.fitness();
			if ((cnt++) % 100000 == 0) System.out.println(cnt+" Fitness: "+fit);
		} while( fit > thresh);
		
		

	
		/*if (i == num_iter) {
			System.out.println(i + " iterations completed..." + "MSE: "
					+ bp.mse(target[(i - 1) % 8]));
		}*/
	}

	public static void walkerTrain(NeuralNetEvolvableWrapper evo) {
		
		double thresh=.5;
		double fit;
		long cnt=0;
		do {
			evo.randomGuess();
			fit=evo.fitness();
			if ((cnt++) % 100000 == 0) System.out.println(cnt+" Fitness: "+fit);
		} while( fit > thresh);
		
		

	
		/*if (i == num_iter) {
			System.out.println(i + " iterations completed..." + "MSE: "
					+ bp.mse(target[(i - 1) % 8]));
		}*/
	}
	public static void testNet(NeuralNet bp, TestData testData) {
		
		System.out
				.println("Now using the trained network to make predctions on test data....");
		TraingDataIterator iter=testData.iter();
		
		while(true) {
			TrainingData d=iter.next();
			if (d == null) break;
			double out[]=bp.forwardPass(d.in);
			
			System.out.println(d.in[0] + "  " + d.in[1] + "  "
					+ d.in[2] + "  " + d.out[0] + "  " + out[0]
					+ "  " + d.out[1] + "  " + out[1]);
		}
	}

	public static void main(String arg[]) {

		
		TestData testData=new TestData();
		
		// Yes well .....
		int lSz[] = { 3, 6, 2 };

		
		
		double beta = .01, alpha = 1000.;
		// Creating the net
		NeuralNet net = new BackProp(lSz, beta, alpha, new Random());

		NeuralNetEvolvableWrapper evo=new NeuralNetEvolvableWrapper(testData, net);
		randomTrain(evo);
		testNet(net,testData);	
		
		
		File file = new File("PJLBackprop.net");

		try {
			FileOutputStream istr = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(istr);
			out.writeObject(net);
			out.close();
			net=null;
			FileInputStream ostr = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(ostr);
			net=(NeuralNet)in.readObject();
			in.close();
			System.out.println(" Loaded from file .......... ");
			testNet(net,testData);
			

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
