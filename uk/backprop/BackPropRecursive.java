package uk.ac.bath.ai.backprop;

// converted to java by p.j.leonard
// based  on C++ code found at 
// http://www.codeproject.com/KB/recipes/BP.aspx?msg=2809798#xx2809798xx
//

import java.io.Serializable;
import java.util.Random;
import java.util.Vector;

import speech.NeuralNet;
import uk.ac.bath.tweaks.Tweakable;

public class BackPropRecursive  implements Serializable,NeuralNet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int inSize;
	public BackProp bp;
	private int hidSize;
	private double[] in;
	
	
    
    public BackPropRecursive(int sz[], double b, double a,Random rand) {
    	this.inSize=sz[0];
    	this.hidSize=sz[1];
   
    	sz[0]=sz[0]+sz[1];    //  increase size to include hidden layer feedback
    	this.in = new double[sz[0]];
    	bp=new BackProp(sz,b,a,rand);
    }

    public Vector<Tweakable> getTweaks() {
        return bp.tweaks;
    }
 
 
    // feed forward one set of input
    private void makeInput(double in1[]) {
    	System.arraycopy(in1, 0, this.in, 0, this.inSize);
    	System.arraycopy(bp.out[1], 0, this.in, this.inSize, this.hidSize);
    }
    

   @Override
    public double [] backPropTrain(double in1[], double tgt[]) {
        makeInput(in1);
        return bp.backPropTrain(in,tgt);
      }

	@Override
	public double[] forwardPass(double[] in1) {
	   	makeInput(in1);
    	return bp.forwardPass(in);
 	}

	@Override
	public void randomWeights() {
		bp.randomWeights();		
	}

	@Override
	public void randomWeights(double low, double high) {
		bp.randomWeights(low,high);
		
	}

	
    
      
    
  }


