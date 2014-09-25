package speech;

import java.util.*;

public class JR_RecNeuralNet implements NeuralNet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	double recnetwork[];
    int recinputs;
    int bias=1;
    int recoutputs;
	double reclearning;
	double recnode[];
	double input_vals[] = new double[10];

	public JR_RecNeuralNet(int inputs_in, int outputs_in, double learning_in) {
		recinputs = inputs_in;
		recoutputs = outputs_in;
		reclearning = learning_in;
		recnetwork = new double[(recinputs+bias)*recoutputs];
		recnode = new double[recoutputs];
	}
	
	public double recbackPropTrain(double[] train_invals, double[] train_outvals) {
		
		double test_outvals[] = new double[recoutputs];
		double error_vals[] = new double[recoutputs];
		double error_return = 0.0;
	        
        test_outvals=recforwardPass(train_invals);
        
    	// Output layer error calculations
    	for (int i=0; i<recoutputs; i++) {
        	error_vals[i]=(train_outvals[i]-test_outvals[i])*(1-test_outvals[i])*test_outvals[i];
        	error_return+=error_vals[i];
    	}
    	
    	int count2=0;
        for (int i=0; i<recinputs; i++) {
        	for (int j=0; j<recoutputs; j++) {
        		recnetwork[count2]+=(reclearning*(error_vals[j]*input_vals[i]));
                count2++;
        	}
        }
        
        // Output Bias Node update
        for (int j=0; j<recoutputs; j++) {
        	recnetwork[count2]+=(reclearning*(error_vals[j]));
            count2++;
    	}

		return Math.sqrt(error_return*error_return);
	}
	
	public void randomWeights() {
		
		Random generator = new Random();
		int count;

	    count=0;
	    for (int i=0; i<recinputs+bias; i++){
	    	for (int j=0; j<recoutputs; j++){
	    		recnetwork[count]=((double)generator.nextInt(100)/10000);
	    		count++;
	    	}
	    }
		    
	}
	
	
	public double[] recforwardPass(double input_node[]) {
		
        int count=0;
        
        for (int i=0; i<5; i++) {
        	input_vals[i] = input_node[i];
        	input_vals[i+5] = recnode[i];
        }

        count=0;
        for (int i=0; i<input_vals.length; i++) {								// Output Layer Neuron Calc
        	for (int j=0; j<recoutputs; j++) {
        		recnode[j]+=(input_vals[i]*recnetwork[count]);
        		count++;
        	}
        }
        for (int j=0; j<recoutputs; j++) {
        	recnode[j]+=(1*recnetwork[count]);						// Output Layer Bias Calc
    		count++;
    	}
        
        for (int j=0; j<recoutputs; j++) {							
        	recnode[j]=1/(1+Math.exp(recnode[j]*-1));				// Output Layer Sigmoid (Non-bipolar)
        }
        
        return recnode;
        
	}

	@Override
	public double backPropTrain(double[] phonemeSmoothed, double[] trainOutvals) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double[] forwardPass(double[] smoothed) {
		// TODO Auto-generated method stub
		return null;
	}
	
}