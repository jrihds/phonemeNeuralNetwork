package uk.ac.bath.test;

import java.util.Random;

import uk.ac.bath.ai.backprop.TrainingData;

public class TestSequence {
	

	final int nIn=3;
	final int nOut=9;
	
	Random rand=new Random();
	
	//final float [][]inToVec={{0,0},{0,1},{0,1},{1,0},{1,1}};
	int iLast=0;
	
	// matrix to classify output
	final int[][]seqTable={
			{0,3,4},
			{5,1,6},
			{7,8,2}};
	
	// matrix to classify output
	//final int[][]seqTable={
	//		{0,0,0},
	//		{0,1,0},
	//		{0,0,2}};
		
	public TrainingData getNext(){
		
		int iIn=rand.nextInt(3);
		TrainingData data=new TrainingData();
		data.in=new double[nIn];
		data.in[iIn]=1.0f;
		int iOut=seqTable[iIn][iLast];   // transition detect
		int iOut1=seqTable[iIn][iIn];    // normal output as well
			
		data.out=new double[nOut];
		data.out[iOut]=1;
		data.out[iOut1]=1;
		
		iLast=iIn;
		return data;
	}
	
	

}
