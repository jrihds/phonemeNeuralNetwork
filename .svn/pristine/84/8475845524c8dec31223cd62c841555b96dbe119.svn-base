package speech;

// Arghhhhhhhhhhhhhhhhhhh

import java.io.*;
import java.util.*;

public class NeuralNetwork implements NeuralNet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	double network[][];
    int inputs;
    int bias=1;
    int hidden;
    int outputs;
	double learning;
	double node[][];

	public NeuralNetwork(int inputs_in, int hidden_in, int outputs_in, double learning_in) {
		inputs = inputs_in;
		hidden = hidden_in;
		outputs = outputs_in;
		learning = learning_in;
		network = new double[3][];
		node = new double[3][];
	}
	
	public double [] backPropTrain(double[] train_invals, double[] train_outvals) {
		
		double test_outvals[][] = new double[3][];
		double error_vals[][] = new double[3][];
		double output_vals[]=forwardPass(train_invals);
        
        for (int i=0; i<3; i++) {
	        for (int j=0; j<hidden; j++) {
	        	test_outvals[i][j] =  node[i][j];
	        }
        }
        
    	// Output layer error calculations
    	for (int i=0; i<outputs; i++) {
        	error_vals[2][i]=(train_outvals[i]-test_outvals[2][i])*(1-test_outvals[2][i])*test_outvals[2][i];
    	}
    	
    	int count2=0;
        for (int i=0; i<hidden; i++) {
        	for (int j=0; j<outputs; j++) {
                network[2][count2] +=(learning*(error_vals[2][j]*test_outvals[1][i]));
                count2++;
        	}
        }
        
        // Output Bias Node update
        for (int j=0; j<outputs; j++) {
            network[2][count2]+=(learning*(error_vals[2][j]));
            count2++;
    	}
        
        //-----------------------------------------------------------------------------------------------//
        
        // Hidden layer error calculations
    	count2=0;
    	for (int i=0; i<hidden; i++) {
    		for (int j=0; j<outputs; j++) {
    			error_vals[1][i]+=(network[2][count2]*error_vals[2][j]);
    			count2++;
    		}
    		error_vals[1][i]=error_vals[1][i]*(1-test_outvals[1][i])*test_outvals[1][i];
    	}
    	
    	// Hidden layer weight update
    	count2=0;
    	for (int i=0; i<inputs; i++) {
    		for (int j=0; j<hidden; j++) {
    			network[1][count2]+=(learning*(error_vals[1][j]*train_invals[i]));
    			count2++;
    		}
    	}
    	
    	// Output Bias Node update
    	for (int j=0; j<hidden; j++) {
			network[1][count2]+=(learning*error_vals[1][j]);
            count2++;
    	}
    	
    	//-----------------------------------------------------------------------------------------------//
    	/*
    	// Input layer error calculations
    	count2=0;
    	for (int i=0; i<hidden; i++) {
    		for (int j=0; j<outputs; j++) {
    			error_vals[i][0]+=(network[count2][1]*error_vals[j][1]);
    			count2++;
    		}
    		error_vals[i][0]=error_vals[i][0]*(1-test_outvals[i][0])*test_outvals[i][0];
    	}

    	// Input layer weight update
    	count2=0;
    	for (int i=0; i<inputs; i++) {
    		for (int j=0; j<hidden; j++) {
    			network[count2][0]+=(learning*(error_vals[j][0]*train_invals[i]));
    			count2++;
    		}
    	}
    	
    	// Output Bias Node update
    	for (int j=0; j<hidden; j++) {
			network[count2][0]+=(learning*error_vals[j][0]);
            count2++;
    	}*/
    	
    	return output_vals;

	}
	
	public void randomWeights() {
		
		Random generator = new Random();
		int count;
		
		//try {
		    
			//BufferedWriter out = new BufferedWriter(new FileWriter("src/textfiles/network.txt"));
		    
		    count=0;
		    for (int i=0; i<inputs+bias; i++){
		    	for (int j=0; j<hidden; j++){
		    		network[count][0]=((double)generator.nextInt(100)/10000);
		    		count++;
		    	}
		    }
		    
		    count=0;
		    for (int i=0; i<inputs+bias; i++){
		    	for (int j=0; j<hidden; j++){
		    		network[1][count]=((double)generator.nextInt(100)/10000);
		    		count++;
		    	}
		    }
		    
		    count=0;
		    for (int i=0; i<hidden+bias; i++){
		    	for (int j=0; j<outputs; j++){
		    		network[2][count]=((double)generator.nextInt(100)/10000);
		    		count++;
		    	}
		    }
		   // out.close();
		//} catch (IOException e) {
		//}
	}
	
	public double[] forwardPass(double input_node[]) {
		
		double output_node[] = new double[outputs];
		
        int count=0;
        /*for (int i=0; i<inputs; i++) {								// Input Layer Neuron Calc
        	for (int j=0; j<hidden; j++) {
        		node[j][0]+=(input_node[i]*network[count][0]);
        		count++;
        	}
        }
        
        for (int j=0; j<hidden; j++) {
    		node[j][0]+=(1*network[count][0]);						// Input Layer Bias Calc
    		count++;
    	}
        
        for (int j=0; j<hidden; j++) {
        	node[j][0]=1/(1+Math.exp(node[j][0]*-1));				// Input Layer Sigmoid (Non-bipolar)
        }*/
        
    	//-----------------------------------------------------------------------------------------------//
        
        count=0;
        for (int i=0; i<inputs; i++) {								// Hidden Layer Neuron Calc
        	for (int j=0; j<hidden; j++) {
        		node[1][j]+=(input_node[i]*network[1][count]);
        		count++;
        	}
        }
        
        for (int j=0; j<hidden; j++) {
    		node[1][j]+=(1*network[1][count]);						// Hidden Layer Bias Calc
    		count++;
    	}
        
        for (int j=0; j<hidden; j++) {
        	node[1][j]=1/(1+Math.exp(node[1][j]*-1));				// Hidden Layer Sigmoid (Non-bipolar)
        }
        
        //-----------------------------------------------------------------------------------------------//
        
        count=0;
        for (int i=0; i<hidden; i++) {								// Output Layer Neuron Calc
        	for (int j=0; j<outputs; j++) {
        		node[2][j]+=(node[1][i]*network[2][count]);
        		count++;
        	}
        }
        for (int j=0; j<outputs; j++) {
    		node[2][j]+=(1*network[2][count]);						// Output Layer Bias Calc
    		count++;
    	}
        
        for (int j=0; j<outputs; j++) {							
        	node[2][j]=1/(1+Math.exp(node[2][j]*-1));				// Output Layer Sigmoid (Non-bipolar)
        	output_node[j]=node[2][j];
        }
        
        return output_node;
        
	}

	@Override
	public void randomWeights(double low, double high) {
		// TODO Auto-generated method stub
		
	}
	
}