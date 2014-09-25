package speech;

import java.io.*;
import java.util.*;

//
//@author JER
//
/*
 * 
 * Work in progress - a neural network
 * 
* */

public class JR_NeuralNet implements NeuralNet {
	
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

	public JR_NeuralNet(int inputs_in, int hidden_in, int outputs_in, double learning_in) {
		inputs = inputs_in;
		hidden = hidden_in;
		outputs = outputs_in;
		learning = learning_in;
		network = new double[(inputs+bias)*hidden][3];
		node = new double[hidden][3];
	}
	
	public double [] backPropTrain(double[] train_invals, double[] train_outvals) {
		
		double test_outvals[][] = new double[hidden][3];
		double error_vals[][] = new double[hidden][3];
		double output_vals[]=forwardPass(train_invals);
        
        for (int i=0; i<3; i++) {
	        for (int j=0; j<hidden; j++) {
	        	test_outvals[j][i] =  node[j][i];
	        }
        }
        
    	// Output layer error calculations
    	for (int i=0; i<outputs; i++) {
        	error_vals[i][2]=(train_outvals[i]-test_outvals[i][2])*(1-test_outvals[i][2])*test_outvals[i][2];
    	}
    	
    	int count2=0;
        for (int i=0; i<hidden; i++) {
        	for (int j=0; j<outputs; j++) {
                network[count2][2]+=(learning*(error_vals[j][2]*test_outvals[i][1]));
                count2++;
        	}
        }
        
        // Output Bias Node update
        for (int j=0; j<outputs; j++) {
            network[count2][2]+=(learning*(error_vals[j][2]));
            count2++;
    	}
        
        //-----------------------------------------------------------------------------------------------//
        
        // Hidden layer error calculations
    	count2=0;
    	for (int i=0; i<hidden; i++) {
    		for (int j=0; j<outputs; j++) {
    			error_vals[i][1]+=(network[count2][2]*error_vals[j][2]);
    			count2++;
    		}
    		error_vals[i][1]=error_vals[i][1]*(1-test_outvals[i][1])*test_outvals[i][1];
    	}
    	
    	// Hidden layer weight update
    	count2=0;
    	for (int i=0; i<inputs; i++) {
    		for (int j=0; j<hidden; j++) {
    			network[count2][1]+=(learning*(error_vals[j][1]*train_invals[i]));
    			count2++;
    		}
    	}
    	
    	// Output Bias Node update
    	for (int j=0; j<hidden; j++) {
			network[count2][1]+=(learning*error_vals[j][1]);
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
		    		network[count][1]=((double)generator.nextInt(100)/10000);
		    		count++;
		    	}
		    }
		    
		    count=0;
		    for (int i=0; i<hidden+bias; i++){
		    	for (int j=0; j<outputs; j++){
		    		network[count][2]=((double)generator.nextInt(100)/10000);
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
        for (int i=0; i<hidden; i++) {								// Hidden Layer Neuron Calc
        	for (int j=0; j<hidden; j++) {
        		node[j][1]+=(input_node[i]*network[count][1]);
        		count++;
        	}
        }
        
        for (int j=0; j<hidden; j++) {
    		node[j][1]+=(1*network[count][1]);						// Hidden Layer Bias Calc
    		count++;
    	}
        
        for (int j=0; j<hidden; j++) {
        	node[j][1]=1/(1+Math.exp(node[j][1]*-1));				// Hidden Layer Sigmoid (Non-bipolar)
        }
        
        //-----------------------------------------------------------------------------------------------//
        
        count=0;
        for (int i=0; i<hidden; i++) {								// Output Layer Neuron Calc
        	for (int j=0; j<outputs; j++) {
        		node[j][2]+=(node[i][1]*network[count][2]);
        		count++;
        	}
        }
        for (int j=0; j<outputs; j++) {
    		node[j][2]+=(1*network[count][2]);						// Output Layer Bias Calc
    		count++;
    	}
        
        for (int j=0; j<outputs; j++) {							
        	node[j][2]=1/(1+Math.exp(node[j][2]*-1));				// Output Layer Sigmoid (Non-bipolar)
        	output_node[j]=node[j][2];
        }
        
        return output_node;
        
	}

	@Override
	public void randomWeights(double low, double high) {
		// TODO Auto-generated method stub
		
	}
	
}