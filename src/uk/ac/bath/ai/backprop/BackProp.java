package uk.ac.bath.ai.backprop;

// converted to java by p.j.leonard
// based  on C++ code found at 
// http://www.codeproject.com/KB/recipes/BP.aspx?msg=2809798#xx2809798xx
//

import java.io.IOException;
import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Vector;

import speech.NeuralNet;
import uk.ac.bath.tweaks.Tweakable;
import uk.ac.bath.tweaks.TweakableDouble;

public class BackProp implements Serializable, NeuralNet {
	/**
	 *
	 *
	 *   weight[layer=0:numlayer-1][out_node=0:layersize[layer]-1][in_node=0:layersize[layer-1]]
	 * 
	 *   weight[layer][out_node][layer_size[layer-1]]  is the bais (connected to unit input)
	 *   
	 *   weight[0]=null   
	 *   
	 */
	private static final long serialVersionUID = 1L;

	// output of each neuron out[layer_no][neuron_index]
	double out[][];
	// delta error value for each neuron
	double delta[][];
	// vector of weights for each neuron
	double weight[][][];
	// no of layers in net
	// including input layer
	int numlayer;
	// vector of numl elements for size
	// of each layer
	int layersizes[];
	// learning rate
	double beta;
	// momentum parameter
	double alpha;
	// storage for weight-change made
	// in previous epoch
	double prevDwt[][][];

	transient Vector<Tweakable> tweaks;
	transient private TweakableDouble betaTweak;
	transient private TweakableDouble alphaTweak;
	transient Random rand;
	
	public BackProp(int sz[], double b, double a, Random rand) {

		this.rand=rand;
		beta = b;
		alpha = a;

		makeTweaks();
		// set no of layers and their sizes
		numlayer = sz.length;
		layersizes = new int[numlayer];

		for (int i = 0; i < numlayer; i++) {
			layersizes[i] = sz[i];
		}

		// allocate memory for output of each neuron
		out = new double[numlayer][];

		for (int i = 0; i < numlayer; i++) {
			out[i] = new double[layersizes[i]];
		}

		// allocate memory for delta
		delta = new double[numlayer][];

		for (int i = 1; i < numlayer; i++) {
			delta[i] = new double[layersizes[i]];
		}

		// allocate memory for weights
		weight = new double[numlayer][][];

		for (int i = 1; i < numlayer; i++) {
			weight[i] = new double[layersizes[i]][];
		}
		for (int i = 1; i < numlayer; i++) {
			for (int j = 0; j < layersizes[i]; j++) {
				weight[i][j] = new double[layersizes[i - 1] + 1];
			}
		}

		// allocate memory for previous weights
		prevDwt = new double[numlayer][][];

		for (int i = 1; i < numlayer; i++) {
			prevDwt[i] = new double[layersizes[i]][];

		}
		for (int i = 1; i < numlayer; i++) {
			for (int j = 0; j < layersizes[i]; j++) {
				prevDwt[i][j] = new double[layersizes[i - 1] + 1];
			}
		}

		// seed and assign random weights
		// srand((unsigned)(time(NULL)));

		if(rand == null){
		 rand = new Random();
		}
		for (int i = 1; i < numlayer; i++) {
			for (int j = 0; j < layersizes[i]; j++) {
				for (int k = 0; k < layersizes[i - 1] + 1; k++) {
					weight[i][j][k] = (double) (rand.nextDouble() - .5);// 32767
					// initialize previous weights to 0 for first iteration
				}
			}
		}

		for (int i = 1; i < numlayer; i++) {
			for (int j = 0; j < layersizes[i]; j++) {
				for (int k = 0; k < layersizes[i - 1] + 1; k++) {
					prevDwt[i][j][k] = 0.0f;// Note that the following variables
											// are unused,
					//
					// delta[0]
					// weight[0]
					// prevDwt[0]

					// I did this intentionaly to maintains consistancy in
					// numbering the layers.
					// Since for a net having n layers, input layer is refered
					// to as 0th layer,
					// first hidden layer as 1st layer and the nth layer as
					// output layer. And
					// first (0th) layer just stores the inputs hence there is
					// no delta or weigth
					// values corresponding to it.
				}
			}
		}
	}

	public Vector<Tweakable> getTweaks() {
		return tweaks;
	}

	// mean square error
//	private double mse(double tgt[]) {
//		double mse = 0;
//		for (int i = 0; i < layersizes[numlayer - 1]; i++) {
//			mse += (tgt[i] - out[numlayer - 1][i])
//					* (tgt[i] - out[numlayer - 1][i]);
//		}
//		return mse / 2;
//	}

	// returns i'th output of the net
//	 double Out(int i) {
//		return out[numlayer - 1][i];
//	}
//
//	double[] output() {
//		return out[numlayer - 1];
//	}

	// feed forward one set of input
	private void ffwd(double in[]) {
		double sum;

		// assign content to input layer
		for (int i = 0; i < layersizes[0]; i++) {
			out[0][i] = in[i]; // output_from_neuron(i,j) Jth neuron in Ith
								// Layer

			// assign output(activation) value
			// to each neuron usng sigmoid func
		}
		for (int i = 1; i < numlayer; i++) { // For each layer
			for (int j = 0; j < layersizes[i]; j++) { // For each neuron in
														// current layer
				sum = 0.0f;
				for (int k = 0; k < layersizes[i - 1]; k++) { // For input from
																// each neuron
																// in preceeding
																// layer
					sum += out[i - 1][k] * weight[i][j][k]; // Apply weight to
															// inputs and add to
															// sum
				}
				sum += weight[i][j][layersizes[i - 1]]; // Apply bias
				out[i][j] = sigmoid(sum); // Apply sigmoid function
			}
		}
	}

	// backpropogate errors from output
	// layer uptill the first hidden layer
	public void bpgt(double in[], double tgt[]) {
		double sum;

		// update output values for each neuron
		ffwd(in);

		// find delta for output layer
		for (int i = 0; i < layersizes[numlayer - 1]; i++) {
			delta[numlayer - 1][i] = out[numlayer - 1][i]
					* (1 - out[numlayer - 1][i])
					* (tgt[i] - out[numlayer - 1][i]);
		}

		// find delta for hidden layers
		for (int i = numlayer - 2; i > 0; i--) {
			for (int j = 0; j < layersizes[i]; j++) {
				sum = 0.0f;
				for (int k = 0; k < layersizes[i + 1]; k++) {
					sum += delta[i + 1][k] * weight[i + 1][k][j];
				}
				delta[i][j] = out[i][j] * (1 - out[i][j]) * sum;
			}
		}

		// apply momentum ( does nothing if alpha=0 )
		for (int i = 1; i < numlayer; i++) {
			for (int j = 0; j < layersizes[i]; j++) {
				for (int k = 0; k < layersizes[i - 1]; k++) {
					weight[i][j][k] += alpha * prevDwt[i][j][k];
				}
				weight[i][j][layersizes[i - 1]] += alpha
						* prevDwt[i][j][layersizes[i - 1]];
			}
		}

		// adjust weights usng steepest descent
		for (int i = 1; i < numlayer; i++) {
			for (int j = 0; j < layersizes[i]; j++) {
				for (int k = 0; k < layersizes[i - 1]; k++) {
					prevDwt[i][j][k] = (double) (beta * delta[i][j] * out[i - 1][k]);
					weight[i][j][k] += prevDwt[i][j][k];
				}
				prevDwt[i][j][layersizes[i - 1]] = (double) (beta * delta[i][j]);
				weight[i][j][layersizes[i - 1]] += prevDwt[i][j][layersizes[i - 1]];
			}
		}
	}

	void makeTweaks() {
		tweaks = new Vector<Tweakable>();
		betaTweak = new TweakableDouble(0., 20., beta, .00001, "beta");
		alphaTweak = new TweakableDouble(0., 1000., alpha, .1, "alpha");

		betaTweak.addObserver(new Observer() {

			public void update(Observable o, Object arg) {
				beta = betaTweak.doubleValue();
			}
		});

		alphaTweak.addObserver(new Observer() {

			public void update(Observable o, Object arg) {
				alpha = alphaTweak.doubleValue();
			}
		});

		tweaks.add(alphaTweak);
		tweaks.add(betaTweak);

	}

	void dump() {
		for (int i = 0; i < numlayer; i++) {
			System.out.println("Layer " + i);

			for (int j = 0; j < layersizes[i]; j++) {
				if (i > 0) {
					System.out.println(" Neuron " + j + " =" + out[i][j] + "  "
							+ delta[i][j]);

					for (int k = 0; k < layersizes[i - 1] + 1; k++) {
						System.out.println("w prev [" + k + "]"
								+ weight[i][j][k] + " " + prevDwt[i][j][k]);
					}
				} else {
					System.out.println(" Neuron " + j + " =" + out[i][j]);
				}
			}
		}
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		makeTweaks();
	}

	// sigmoid function
	public static double sigmoid(double in) {
		return (double) (1.0 / (1.0 + Math.exp(-in)));
	}
	
	// sigmoid function
	public static double sigmoid1(double in) {
		double ee=Math.exp(-in);
		return (double) (1.0-ee / (1.0 + ee));
	}

	@Override
	public double[] forwardPass(double[] input) {
		ffwd(input);
		return out[numlayer-1];
	}

	@Override
	public void randomWeights() {
		if (rand==null) rand=new Random();
		for (int i = 1; i < numlayer; i++) {
			for (int j = 0; j < layersizes[i]; j++) {
				for (int k = 0; k < layersizes[i - 1] + 1; k++) {
					weight[i][j][k] = (double) (rand.nextDouble() - .5);
				}
			}
		}

	}
	@Override
	public void randomWeights(double low,double high) {
		if (rand==null) rand=new Random();
		for (int i = 1; i < numlayer; i++) {
			for (int j = 0; j < layersizes[i]; j++) {
				for (int k = 0; k < layersizes[i - 1] + 1; k++) {
					double x=rand.nextDouble();
					weight[i][j][k] = (double) (low +(high-low)*x);
				}
			}
		}

	}

	@Override
	public double [] backPropTrain(double[] input, double[] target) {
		 bpgt(input, target);
		 return out[numlayer-1];
	}
}
