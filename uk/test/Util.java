package uk.ac.bath.test;

public class Util {
	
	static double mse(double x[],double y[]){
		assert(x.length ==y.length);
		double err=0.0;
		for (int i=0;i<x.length;i++){
			err+=(x[i]-y[i])*(x[i]-y[i]);
		}
		return err/2;
	}

}
