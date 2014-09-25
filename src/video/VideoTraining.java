package video;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import speech.NeuralNet;
import uk.ac.bath.ai.backprop.BackProp;

//
//@author JER
//
/*
 * Trains a neural network for a given set of video
 */

public class VideoTraining {
	
	public static NeuralNet neuralNet;
	public static ReadVideoStills readStills;
	
	public static int xLeft= 275;
	public static int xRight= 365;
	public static int yTop= 220;
	public static int yBottom= 300;

	public static int inputs = (xRight-xLeft)*(yBottom-yTop)*3;
	public static int hidden = 30;
	public static int outputs = 4;
	
	public static double alpha = 300000.0;
	public static double beta = .000001;
	
	public static double maxError = 0.01;

	public static void main(String args[]) throws Exception {

		int sz[] = { inputs, hidden, outputs };
		
		neuralNet = new BackProp(sz, beta, alpha, null);
		readStills = new ReadVideoStills();

		neuralNet.randomWeights(-0.1, 0.1);

		double error = 1.0;

		double[][][] stills = readStills.read();
		
		double[][] networkInputs = new double[(xRight-xLeft)*(yBottom-yTop)*3][5];

		int count=0;
		for (int i=xLeft; i<xRight; i++) {
			for (int j=yTop; j<yBottom; j++) {
				networkInputs[count][0] = stills[i][j][0];
				networkInputs[count][1] = stills[i][j][3];
				networkInputs[count][2] = stills[i][j][6];
				networkInputs[count][3] = stills[i][j][9];
				networkInputs[count][4] = stills[i][j][12];
				count++;
				
				networkInputs[count][0] = stills[i][j][0];
				networkInputs[count][1] = stills[i][j][3];
				networkInputs[count][2] = stills[i][j][6];
				networkInputs[count][3] = stills[i][j][9];
				networkInputs[count][4] = stills[i][j][12];
				count++;
				
				networkInputs[count][0] = stills[i][j][0];
				networkInputs[count][1] = stills[i][j][3];
				networkInputs[count][2] = stills[i][j][6];
				networkInputs[count][3] = stills[i][j][9];
				networkInputs[count][4] = stills[i][j][12];
				count++;
			}
		}
		
		count=0;
		double[] train_outvals = new double[outputs];
		double[] output_vals;
		double[] networkIn = new double[inputs];
		
		while (error > 0.05) {
			
			error = 0.0;
			for (int k=0; k<5; k++) {

				train_outvals = new double[outputs];
				if (k<4) {train_outvals[k] = 1.0;}
				for (int i=0; i<inputs; i++) {
					networkIn[i] = networkInputs[i][k];
				}
				neuralNet.backPropTrain(networkIn, train_outvals); // Go!
				output_vals = neuralNet.forwardPass(networkIn);
				for (int j = 0; j < outputs; j++) {
					error += (train_outvals[j] - output_vals[j])
							* (train_outvals[j] - output_vals[j]);
				}
			}
			count++;
			if (count % 20 == 0) System.out.println("Total Error: " + error);
			
		}

		FileOutputStream istr = new FileOutputStream(
				"src/video/textfiles/videoNetwork.txt");
		ObjectOutputStream out = new ObjectOutputStream(istr);
		out.writeObject(neuralNet);
		out.close();

		System.out.println("Whooop finished training! \n It took me "
				+ count + " back props");

	}

}