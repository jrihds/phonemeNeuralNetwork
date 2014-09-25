import java.io.*;
import java.util.*;

import java.net.URL;

public class JR_RecurrentNeuralNet {
	
	double network[][];
    int inputs=128;
    int bias=1;
    int hidden=128;
    int outputs=5;
    int k=0;
	double learning=0.5;
	double node[][][] = new double[hidden][3][2];
	
	public JR_RecurrentNeuralNet() {
		network = new double[(inputs+bias)*hidden][3];
	}
	
	public double recbackPropTrain(double[] train_invals, double[] train_outvals) {
		
		double test_outvals[][] = new double[hidden][3];
		double error_vals[][] = new double[hidden][3];
		double error_return = 0.0;
	        
        test_outvals=recforwardPass(train_invals);
        
    	// Output layer error calculations
    	for (int i=0; i<outputs; i++) {
        	error_vals[i][2]=(train_outvals[i]-test_outvals[i][2])*(1-test_outvals[i][2])*test_outvals[i][2];
        	error_return+=error_vals[i][2];
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
    	}
    	    	
		return Math.sqrt(error_return*error_return);
	}
	
	public void random_weights() {
		
		Random generator = new Random();
		int count=0;
		
		try {
		    BufferedWriter out = new BufferedWriter(new FileWriter("textfiles/network.txt"));
		    for (int i=0; i<inputs+bias; i++){
		    	for (int j=0; j<hidden; j++){
		    		out.write((double)generator.nextInt(100)/10000+"\n");
		    		count++;
		    	}
		    }
		    for (int i=0; i<inputs+bias; i++){
		    	for (int j=0; j<hidden; j++){
		    		out.write((double)generator.nextInt(100)/10000+"\n");
		    		count++;
		    	}
		    }
		    for (int i=0; i<hidden+bias; i++){
		    	for (int j=0; j<outputs; j++){
		    		out.write((double)generator.nextInt(100)/10000+"\n");
		    		count++;
		    	}
		    }
		    out.close();
		} catch (IOException e) {
		}
	}
    
	public double[][] recforwardPass(double input_node[]) {
		
		double node[][] = new double[hidden][3];
		
        int count=0;
        for (int i=0; i<inputs; i++) {								// Input Layer Neuron Calc
        	for (int j=0; j<hidden; j++) {
        		node[j][0]+=(input_node[i]*network[count][0]);
        		node[j][0]+=node[j][1];								// Recurrent Sum layer 2 --> 1
        		count++;
        	}
        }
        
        for (int j=0; j<hidden; j++) {
    		node[j][0]+=(1*network[count][0]);						// Input Layer Bias Calc
    		count++;
    	}
        
        for (int j=0; j<hidden; j++) {
        	node[j][0]=1/(1+Math.exp(node[j][0]*-1));				// Input Layer Sigmoid (Non-bipolar)
        }
        
    	//-----------------------------------------------------------------------------------------------//
        
        count=0;
        for (int i=0; i<hidden; i++) {								// Hidden Layer Neuron Calc
        	for (int j=0; j<hidden; j++) {
        		node[j][1]+=(input_node[i]*network[count][1]);
        		count++;
        	}
        	for (int j=0; j<outputs; j++) {
        		node[i][1]+=node[j][2];								// Recurrent Sum layer 3 --> 2
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
        
        for (int j=0; j<hidden; j++) {							
        	node[j][2]=1/(1+Math.exp(node[j][2]*-1));				// Output Layer Sigmoid (Non-bipolar)
        }
        
        return node;
        
	}
	
	public void writeFile() {
		try {
        	int count=0;
		    BufferedWriter out = new BufferedWriter(new FileWriter("textfiles/network.txt"));
		    for (int i=0; i<inputs+bias; i++){
		    	for (int j=0; j<hidden; j++){
		    		out.write(String.valueOf(network[count][0])+"\n");
		    		count++;
		    	}
		    }
		    count=0;
		    for (int i=0; i<inputs+bias; i++){
		    	for (int j=0; j<hidden; j++){
		    		out.write(String.valueOf(network[count][1])+"\n");
		    		count++;
		    	}
		    }
		    count=0;
		    for (int i=0; i<hidden+bias; i++){
		    	for (int j=0; j<outputs; j++){
		    		out.write(String.valueOf(network[count][2])+"\n");
		    		count++;
		    	}
		    }
		    out.close();
		} catch (IOException e) {
		}
	}
	
	public void readFile() {
		BufferedReader reader = null;
        String input_weights[] = new String[((inputs+bias)*hidden)+((inputs+bias)*hidden)+((hidden+bias)*outputs)];
        
        try {
        	
        	String resource="textfiles/network.txt";
        	InputStream stream=this.getClass().getResourceAsStream(resource);
        	
            
        	reader = new BufferedReader(new InputStreamReader(stream));
    		
//        	reader = new BufferedReader(new FileReader("textfiles/network.txt"));
        	
        	for (int j=0; j<input_weights.length; j++){
    			input_weights[j] = reader.readLine();
    			System.out.println(input_weights[j]);
    		}		
    		reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        int count=0;
        for (int i=0; i<(inputs+bias)*hidden; i++) {
        	network[i][0]=Double.parseDouble(input_weights[count]);
        	count++;
        }
        for (int i=0; i<(inputs+bias)*hidden; i++) {
        	network[i][1]=Double.parseDouble(input_weights[count]);
        	count++;
        }
        for (int i=0; i<(hidden+bias)*outputs; i++) {
        	network[i][2]=Double.parseDouble(input_weights[count]);
        	count++;
        }
        
	}
	
}