/* 
 * Copyright (c) 2006, Karl Helgason
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 *    1. Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    2. Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *    3. The name of the author may not be used to endorse or promote
 *       products derived from this software without specific prior
 *       written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package rasmus.fft.radix2;

public class BitReversal {

	public int[] bitm_array;
	public int[] bitm_array2;
	public int fftFrameSize;
	public int fftFrameSize2;
	
	public BitReversal(int fftFrameSize)
	{
		this.fftFrameSize = fftFrameSize;
		fftFrameSize2 = fftFrameSize << 1;
		
		// Pre-process Bit-Reversal
		bitm_array = new int[fftFrameSize2];
		for (int i = 2; i < fftFrameSize2; i += 2) {
			int j;
			int bitm;
			for (bitm = 2, j = 0; bitm < fftFrameSize2; bitm <<= 1) {
				if ((i & bitm) != 0) j++;
				j <<= 1;
			}
			bitm_array[i] = j;
		}
		
		// Performing Bit-Reversal
		int bcount = 0;
		int size = fftFrameSize2-2;
		for (int i = 2; i < size; i += 2) {
			int j = bitm_array[i];
			if (i < j) {
				bcount++;
			}
		}		
		
		bitm_array2 = new int[bcount*2];
		int k = 0;
		for (int i = 2; i < size; i += 2) {
			int j = bitm_array[i];
			if (i < j) {		
				bitm_array2[k++] = i;
				bitm_array2[k++] = j;
			}
		}				
		
	}
	
	
	public final void transform(double[] data)  
	{		
		if(fftFrameSize < 4) return;			
					
		int inverse = fftFrameSize2-2;
		for (int i = 0; i < fftFrameSize; i += 4) {
			int j = bitm_array[i];
			
			// Performing Bit-Reversal, even v.s. even, O(2N)
			if (i < j) {
				
				int n = i; 
				int m = j;

				// COMPLEX: SWAP(data[n], data[m])				
				// Real Part				
				double tempr = data[n]; 
				data[n] = data[m];
				data[m] = tempr; 					
				// Imagery Part
				n++;
				m++;
				double tempi = data[n];
				data[n] = data[m];
				data[m] = tempi;

				n = inverse - i; 
				m = inverse - j;
				
				// COMPLEX: SWAP(data[n], data[m])				
				// Real Part
				tempr = data[n]; 
				data[n] = data[m];
				data[m] = tempr; 					
				// Imagery Part
				n++;
				m++;
				tempi = data[n];
				data[n] = data[m];
				data[m] = tempi;
			}
			
			// Performing Bit-Reversal, odd v.s. even, O(N)
			
			int m = j + fftFrameSize; //bitm_array[i+2];
			// COMPLEX: SWAP(data[n], data[m])				
			// Real Part
			int n = i+2; 
			double tempr = data[n]; 
			data[n] = data[m];
			data[m] = tempr; 					
			// Imagery Part
			n++;
			m++;
			double tempi = data[n];
			data[n] = data[m];
			data[m] = tempi; 	
		}				
		
		
	}
	
	
	public final void transform1(double[] data)
	{		
		final int size = bitm_array2.length;
		for (int i = 0; i < size; i++) {
			int n = bitm_array2[i++]; 
			int m = bitm_array2[i];
			double tempr = data[n]; 
			data[n] = data[m];
			data[m] = tempr; 	
			double tempi = data[++n];
			data[n] = data[++m];
			data[m] = tempi;	
		}		
	}
		
	public final void transform2(double[] data)
	{
		int[] bitm_array = this.bitm_array;
		// Performing Bit-Reversal
		int size = fftFrameSize2-2;
		for (int i = 2; i < size; i += 2) {
			int j = bitm_array[i];
			if (i < j) {				
				// COMPLEX: SWAP(data[n], data[m])				
				// Real Part
				int n = i; 
				int m = j;
				double tempr = data[n]; 
				data[n] = data[m];
				data[m] = tempr; 					
				// Imagery Part
				n++;
				m++;
				double tempi = data[n];
				data[n] = data[m];
				data[m] = tempi;
			}
		}				
	}


}
