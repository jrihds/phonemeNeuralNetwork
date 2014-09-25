import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class JR_Training {
	
	public static int inputs = 128;
	public static int outputs = 5;
	
	public static void main(String args[]) throws Exception {
		
		//final JR_NeuralNet JR_NN = new JR_NeuralNet();
		final JR_RecurrentNeuralNet JR_recNN = new JR_RecurrentNeuralNet();
		
		JR_recNN.random_weights();
		
		double error=1;

		JR_recNN.readFile();
		
		int count = 0;
    	String filename = null;
    	double[] phoneme;
    	double[] train_outvals;
    	
		while (error>0.00001) {
			for (int v=0; v<6; v++) {
				
	        	if (v==0) {filename="textfiles/ah.txt";}
	        	if (v==1) {filename="textfiles/ee.txt";}
	        	if (v==2) {filename="textfiles/eh.txt";}
	        	if (v==3) {filename="textfiles/oh.txt";}
	        	if (v==4) {filename="textfiles/oo.txt";}
	        	if (v==5) {filename="textfiles/silence.txt";}
	        	phoneme = readPhoneme(filename);

	    		train_outvals = new double[outputs+1];
	            if (v==0) {train_outvals[0]=1.0;}
	        	if (v==1) {train_outvals[1]=1.0;}
	        	if (v==2) {train_outvals[2]=1.0;}
	        	if (v==3) {train_outvals[3]=1.0;}
	        	if (v==4) {train_outvals[4]=1.0;}
	        	
				error=JR_recNN.recbackPropTrain(phoneme, train_outvals);
				System.out.println(filename+" "+error);
			}
			count++;
		}
		System.out.println("It took me "+count+" back props");
		
		JR_recNN.writeFile();
		
		System.out.println("Whooop finished training!");
		
	}
	
	public static double[] readPhoneme(String filename) {
		double train_invals[] = new double[inputs];
		BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filename));   		
    		for (int i=0; i<train_invals.length; i++){
    			train_invals[i]=Double.parseDouble(reader.readLine());
    		}
    		reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }  
       
        return train_invals;
	        
	}
    
}