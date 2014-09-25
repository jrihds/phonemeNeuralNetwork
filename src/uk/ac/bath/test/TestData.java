package uk.ac.bath.test;

import uk.ac.bath.ai.backprop.TrainingData;

public class TestData {
	

	double target[][] = { { 0, 1 }, { 1, 1 }, { 1, 0 }, { 0, 0 }, { 1, 1 },
			{ 0, 1 }, { 0, 1 }, { 1, 1 } };

	// prepare test data
	double testData[][] = { { 0, 0, 0 }, { 0, 0, 1 }, { 0, 1, 0 },
			{ 0, 1, 1 }, { 1, 0, 0 }, { 1, 0, 1 }, { 1, 1, 0 }, { 1, 1, 1 } };
	
	int n=target.length;
	
	
	
	TestDataIterator iter() {
		return new TestDataIterator();
	}
	
	class TestDataIterator implements TraingDataIterator {
		
		TrainingData td=new TrainingData();
		int cnt=0;
		
		public TestDataIterator() {
			cnt=0;	
		}
		
		public TrainingData next() {
		
			if (cnt >= n) return null;
			td.in=testData[cnt];
			td.out=target[cnt];
			cnt++;
			return td;
			
		}
		
	}

}
