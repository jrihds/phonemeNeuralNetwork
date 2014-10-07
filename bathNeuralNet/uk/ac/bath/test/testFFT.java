package uk.ac.bath.test;

import rasmus.interpreter.sampled.util.FFT;

public class testFFT {

	
	public static void main(String args[]){

		int fftsize=1024;
		FFT fft = new FFT(fftsize);
		
		double in[]= new double[fftsize*2];
		
		in[0]=1;
		
		int sign=1;
		fft.calcReal(in, sign);
		
		
		for (int  i=0;i<fftsize;i++){
			System.out.println(in[i]+"   "+in[i+fftsize]);
		}
	}

}