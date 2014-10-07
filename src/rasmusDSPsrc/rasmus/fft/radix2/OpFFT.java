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

public final class OpFFT {
	
	public final static double[] computeTwiddleFactors(int fftFrameSize, int sign)
	{
		
		int imax = (int)(Math.log(fftFrameSize)/Math.log(2.));
				
		double[] warray = new double[(fftFrameSize-1)*4];
		int w_index = 0;
			
		for (int i = 0, nstep = 2; i < imax; i++) {
			
			int jmax = nstep;
			nstep <<= 1;
			
			double wr = 1.0;
			double wi = 0.0;			
			
			double arg = Math.PI / (jmax>>1);			
			double wfr = Math.cos(arg);
			double wfi = sign*Math.sin(arg); 
			
			for (int j = 0; j < jmax; j += 2) {			
				
				warray[w_index++] = wr;
				warray[w_index++] = wi;
			
				double tempr = wr;
				wr = tempr*wfr - wi*wfi;
				wi = tempr*wfi + wi*wfr;
			}
		}		
		
		
		 //  PRECOMPUTATION of  wwr1, wwi1 for factor 4 Decomposition (3 * complex operators and 8 +/- complex operators)
		{
			
			w_index = 0;
			int w_index2 = warray.length >> 1;
			for (int i = 0, nstep = 2; i < (imax-1); i++) {
				
				int jmax = nstep;
				nstep *= 2;
				
				int ii = w_index + jmax;
				for (int j = 0; j < jmax; j += 2) {			
					
					double wr = warray[w_index++];
					double wi = warray[w_index++];
					double wr1 = warray[ii++];
					double wi1 = warray[ii++];
					warray[w_index2++] = wr * wr1 - wi * wi1;  
					warray[w_index2++] = wr * wi1 + wi * wr1;
					
				}
			}					
			
		} 
		
		return warray;
		
	}

	public final static void calc(int fftFrameSize, double[] data, int sign, double[] w)

	// Sign = -1 is FFT, 1 is IFFT (inverse FFT)
	// Data = Interlaced double array to be transformed.
	// The order is: real (sin), complex (cos)
	// Framesize must be power of 2
	{

		final int fftFrameSize2 = fftFrameSize << 1;

		if (fftFrameSize < 64) {
			if (sign == -1) {
				switch (fftFrameSize) {
				case 1:
					return;
				case 2:
					OpFFT2.calc(data, 0);
					return;
				case 4:
					OpFFT4.calc(data, 0);
					return;
				case 8:
					OpFFT8.calc(data, 0);
					return;
				case 16:
					OpFFT16.calc(data, 0);
					return;
				case 32:
					OpFFT32.calc(data, 0);
					return;
				}
			}
			switch (fftFrameSize) {
			case 1:
				return;
			case 2:
				OpFFTI2.calc(data, 0);
				return;
			case 4:
				OpFFTI4.calc(data, 0);
				return;
			case 8:
				OpFFTI8.calc(data, 0);
				return;
			case 16:
				OpFFTI16.calc(data, 0);
				return;
			case 32:
				OpFFTI32.calc(data, 0);
				return;
			}
		}

		
		//int nstep = 2;
		/*
		if (sign == -1) {
			for (int j = 0; j < fftFrameSize2; j += 8)
				OpFFT4.calc(data, j);
		} else {
			for (int j = 0; j < fftFrameSize2; j += 8)
				OpFFTI4.calc(data, j);
		}
		int nstep = 2 << 2; 		*/
		
		/*
		if (sign == -1) {
			for (int j = 0; j < fftFrameSize2; j += 32)
				OpFFT16f4.calc(data, j);
		} else {
			for (int j = 0; j < fftFrameSize2; j += 32)
				OpFFTI16f4.calc(data, j);
		}
		int nstep = 2 << 4; 

		*/
	
		if (sign == -1) {
			for (int j = 0; j < fftFrameSize2; j += 64)
				OpFFT32f4.calc(data, j);
		} else {
			for (int j = 0; j < fftFrameSize2; j += 64)
				OpFFTI32f4.calc(data, j);
		}
		
		int nstep = 2 << 5;  
		
		/*
		if (sign == -1) {
			for (int j = 0; j < fftFrameSize2; j += 128)
				OpFFT64f4.calc(data, j);
		} else {
			for (int j = 0; j < fftFrameSize2; j += 128)
				OpFFTI64f4.calc(data, j);
		}
		int nstep = 2 << 6;*/ 

		if (nstep >= fftFrameSize2)
			return;
		int i = nstep - 2;
		if(sign == -1)
			calcF4F(fftFrameSize, data, i, nstep, w);
		else
			calcF4I(fftFrameSize, data, i, nstep, w);

	}

	public final static void calcF2(int fftFrameSize, double[] data, int i, int nstep, double[] w) {
		final int fftFrameSize2 = fftFrameSize << 1; // 2*fftFrameSize;
		// Factor-2 Decomposition
		while (nstep < fftFrameSize2) {

			if (nstep << 1 == fftFrameSize2) {
				calcF2E(fftFrameSize, data, i, nstep, w);
				return;
			}

			int jmax = nstep;
			nstep <<= 1;
			for (int j = 0; j < jmax; j += 2) {
				double wr = w[i++];
				double wi = w[i++];
				for (int n = j; n < fftFrameSize2; n += nstep) {
					int m = n + jmax;
					double datam_r = data[m];
					double datam_i = data[m + 1];
					double datan_r = data[n];
					double datan_i = data[n + 1];
					double tempr = datam_r * wr - datam_i * wi;
					double tempi = datam_r * wi + datam_i * wr;
					data[m] = datan_r - tempr;
					data[m + 1] = datan_i - tempi;
					data[n] = datan_r + tempr;
					data[n + 1] = datan_i + tempi;
				}
			}
		}

	}

	public final static void calcF2E(int fftFrameSize, double[] data, int i, int nstep, double[] w) {
		int jmax = nstep;
		nstep <<= 1;
		for (int n = 0; n < jmax; n += 2) {
			double wr = w[i++];
			double wi = w[i++];
			int m = n + jmax;
			double datam_r = data[m];
			double datam_i = data[m + 1];
			double datan_r = data[n];
			double datan_i = data[n + 1];
			double tempr = datam_r * wr - datam_i * wi;
			double tempi = datam_r * wi + datam_i * wr;
			data[m] = datan_r - tempr;
			data[m + 1] = datan_i - tempi;
			data[n] = datan_r + tempr;
			data[n + 1] = datan_i + tempi;
		}
		return;

	}

	// Perform Factor-4 Decomposition with  3 * complex operators and 8 +/- complex operators

	public final static void calcF4F(int fftFrameSize, double[] data, int i, int nstep, double[] w) {
		final int fftFrameSize2 = fftFrameSize << 1; // 2*fftFrameSize;
		// Factor-4 Decomposition

		int w_len = w.length >> 1;
		while (nstep < fftFrameSize2) {

			if (nstep << 2 == fftFrameSize2) {
				// Goto Factor-4 Final Decomposition
				//calcF4E(data, i, nstep, -1, w);
				calcF4FE(fftFrameSize, data, i, nstep, w);
				return;
			}
			int jmax = nstep;
			int nnstep = nstep << 1;
			if (nnstep == fftFrameSize2)
			{
				// Factor-4 Decomposition not possible
				calcF2E(fftFrameSize, data, i, nstep, w);
				return;
			}
			nstep <<= 2;
			int ii = i + jmax;
			int iii = i + w_len;
			
			{
				i+=2;
				ii+=2;
				iii+=2;
				
				for (int n = 0; n < fftFrameSize2; n += nstep) {
					int m = n + jmax;

					double datam1_r = data[m];
					double datam1_i = data[m + 1];
					double datan1_r = data[n];
					double datan1_i = data[n + 1];

					n += nnstep;
					m += nnstep;
					double datam2_r = data[m];
					double datam2_i = data[m + 1];
					double datan2_r = data[n];
					double datan2_i = data[n + 1];

					double tempr   = datam1_r ;
					double tempi   = datam1_i;
					
					datam1_r = datan1_r - tempr;
					datam1_i = datan1_i - tempi;
					datan1_r = datan1_r + tempr;
					datan1_i = datan1_i + tempi;
					
					double n2w1r  = datan2_r;
					double n2w1i  = datan2_i;
					double m2ww1r = datam2_r;
					double m2ww1i = datam2_i;					
					
					tempr = m2ww1r - n2w1r;
					tempi = m2ww1i - n2w1i;
					
					datam2_r = datam1_r + tempi; 
					datam2_i = datam1_i - tempr; 
					datam1_r = datam1_r - tempi; 
					datam1_i = datam1_i + tempr; 

					tempr = n2w1r + m2ww1r;
					tempi = n2w1i + m2ww1i;					
					
					datan2_r = datan1_r - tempr; 
					datan2_i = datan1_i - tempi; 
					datan1_r = datan1_r + tempr; 
					datan1_i = datan1_i + tempi; 
										
					data[m] = datam2_r;
					data[m + 1] = datam2_i;
					data[n] = datan2_r;
					data[n + 1] = datan2_i;
					
					n -= nnstep;
					m -= nnstep;
					data[m] = datam1_r;
					data[m + 1] = datam1_i;
					data[n] = datan1_r;
					data[n + 1] = datan1_i;

				}
				
			}			
			for (int j = 2; j < jmax; j += 2) {
				double wr = w[i++];
				double wi = w[i++];
				double wr1 = w[ii++];
				double wi1 = w[ii++];
				double wwr1 = w[iii++];
				double wwi1 = w[iii++];				
				//double wwr1 = wr * wr1 - wi * wi1;  // these numbers can be precomputed!!! 
				//double wwi1 = wr * wi1 + wi * wr1;
				
				for (int n = j; n < fftFrameSize2; n += nstep) {
					int m = n + jmax;

					double datam1_r = data[m];
					double datam1_i = data[m + 1];
					double datan1_r = data[n];
					double datan1_i = data[n + 1];

					n += nnstep;
					m += nnstep;
					double datam2_r = data[m];
					double datam2_i = data[m + 1];
					double datan2_r = data[n];
					double datan2_i = data[n + 1];

					double tempr   = datam1_r * wr - datam1_i * wi;
					double tempi   = datam1_r * wi + datam1_i * wr;
					
					datam1_r = datan1_r - tempr;
					datam1_i = datan1_i - tempi;
					datan1_r = datan1_r + tempr;
					datan1_i = datan1_i + tempi;
					
					double n2w1r  = datan2_r * wr1 - datan2_i * wi1;
					double n2w1i  = datan2_r * wi1 + datan2_i * wr1;
					double m2ww1r = datam2_r * wwr1 - datam2_i * wwi1;
					double m2ww1i = datam2_r * wwi1 + datam2_i * wwr1;					
					
					tempr = m2ww1r - n2w1r;
					tempi = m2ww1i - n2w1i;
					
					datam2_r = datam1_r + tempi; 
					datam2_i = datam1_i - tempr; 
					datam1_r = datam1_r - tempi; 
					datam1_i = datam1_i + tempr; 

					tempr = n2w1r + m2ww1r;
					tempi = n2w1i + m2ww1i;					
					
					datan2_r = datan1_r - tempr; 
					datan2_i = datan1_i - tempi; 
					datan1_r = datan1_r + tempr; 
					datan1_i = datan1_i + tempi; 
										
					data[m] = datam2_r;
					data[m + 1] = datam2_i;
					data[n] = datan2_r;
					data[n + 1] = datan2_i;
					
					n -= nnstep;
					m -= nnstep;
					data[m] = datam1_r;
					data[m + 1] = datam1_i;
					data[n] = datan1_r;
					data[n + 1] = datan1_i;

				}
			}

			i += jmax << 1;

		}

		calcF2E(fftFrameSize, data, i, nstep, w);

	}
	
	// Perform Factor-4 Decomposition with  3 * complex operators and 8 +/- complex operators
	
	public final static void calcF4I(int fftFrameSize, double[] data, int i, int nstep, double[] w) {
		final int fftFrameSize2 = fftFrameSize << 1; // 2*fftFrameSize;
		// Factor-4 Decomposition
		
		int w_len = w.length >> 1;
		while (nstep < fftFrameSize2) {

			if (nstep << 2 == fftFrameSize2) {
				// Goto Factor-4 Final Decomposition
				//calcF4E(data, i, nstep, 1, w);
				calcF4IE(fftFrameSize, data, i, nstep, w);
				return;
			}
			int jmax = nstep;
			int nnstep = nstep << 1;
			if (nnstep == fftFrameSize2)
			{
				// Factor-4 Decomposition not possible
				calcF2E(fftFrameSize, data, i, nstep, w);
				return;
			}
			nstep <<= 2;
			int ii = i + jmax;
			int iii = i + w_len;
			{
				i+=2;
				ii+=2;
				iii+=2;
				
				for (int n = 0; n < fftFrameSize2; n += nstep) {
					int m = n + jmax;

					double datam1_r = data[m];
					double datam1_i = data[m + 1];
					double datan1_r = data[n];
					double datan1_i = data[n + 1];

					n += nnstep;
					m += nnstep;
					double datam2_r = data[m];
					double datam2_i = data[m + 1];
					double datan2_r = data[n];
					double datan2_i = data[n + 1];

					double tempr   = datam1_r ;
					double tempi   = datam1_i;
					
					datam1_r = datan1_r - tempr;
					datam1_i = datan1_i - tempi;
					datan1_r = datan1_r + tempr;
					datan1_i = datan1_i + tempi;
					
					double n2w1r  = datan2_r;
					double n2w1i  = datan2_i;
					double m2ww1r = datam2_r;
					double m2ww1i = datam2_i;					
					
					tempr = n2w1r - m2ww1r;
					tempi = n2w1i - m2ww1i;
					
					datam2_r = datam1_r + tempi; 
					datam2_i = datam1_i - tempr; 
					datam1_r = datam1_r - tempi; 
					datam1_i = datam1_i + tempr; 

					tempr = n2w1r + m2ww1r;
					tempi = n2w1i + m2ww1i;					
					
					datan2_r = datan1_r - tempr; 
					datan2_i = datan1_i - tempi; 
					datan1_r = datan1_r + tempr; 
					datan1_i = datan1_i + tempi; 
										
					data[m] = datam2_r;
					data[m + 1] = datam2_i;
					data[n] = datan2_r;
					data[n + 1] = datan2_i;
					
					n -= nnstep;
					m -= nnstep;
					data[m] = datam1_r;
					data[m + 1] = datam1_i;
					data[n] = datan1_r;
					data[n + 1] = datan1_i;

				}
				
			}					
			for (int j = 2; j < jmax; j += 2) {
				double wr = w[i++];
				double wi = w[i++];
				double wr1 = w[ii++];
				double wi1 = w[ii++];
				double wwr1 = w[iii++];
				double wwi1 = w[iii++];						
				//double wwr1 = wr * wr1 - wi * wi1;  // these numbers can be precomputed!!! 
				//double wwi1 = wr * wi1 + wi * wr1;
				
				for (int n = j; n < fftFrameSize2; n += nstep) {
					int m = n + jmax;

					double datam1_r = data[m];
					double datam1_i = data[m + 1];
					double datan1_r = data[n];
					double datan1_i = data[n + 1];

					n += nnstep;
					m += nnstep;
					double datam2_r = data[m];
					double datam2_i = data[m + 1];
					double datan2_r = data[n];
					double datan2_i = data[n + 1];

					double tempr = datam1_r * wr - datam1_i * wi;
					double tempi = datam1_r * wi + datam1_i * wr;
					
					datam1_r = datan1_r - tempr;
					datam1_i = datan1_i - tempi;
					datan1_r = datan1_r + tempr;
					datan1_i = datan1_i + tempi;
					
					double n2w1r  = datan2_r * wr1 - datan2_i * wi1;
					double n2w1i  = datan2_r * wi1 + datan2_i * wr1;
					double m2ww1r = datam2_r * wwr1 - datam2_i * wwi1;
					double m2ww1i = datam2_r * wwi1 + datam2_i * wwr1;					

					tempr = n2w1r - m2ww1r;
					tempi = n2w1i - m2ww1i;
					
					datam2_r = datam1_r + tempi; 
					datam2_i = datam1_i - tempr; 
					datam1_r = datam1_r - tempi; 
					datam1_i = datam1_i + tempr; 

					tempr = n2w1r + m2ww1r;
					tempi = n2w1i + m2ww1i;					
					
					datan2_r = datan1_r - tempr; 
					datan2_i = datan1_i - tempi; 
					datan1_r = datan1_r + tempr; 
					datan1_i = datan1_i + tempi; 
										
					data[m] = datam2_r;
					data[m + 1] = datam2_i;
					data[n] = datan2_r;
					data[n + 1] = datan2_i;
					
					n -= nnstep;
					m -= nnstep;
					data[m] = datam1_r;
					data[m + 1] = datam1_i;
					data[n] = datan1_r;
					data[n + 1] = datan1_i;

				}
			}

			i += jmax << 1;

		}

		calcF2E(fftFrameSize, data, i, nstep, w);

	}
	
	
	
	// Perform Factor-4 Decomposition with  3 * complex operators and 8 +/- complex operators

	public final static void calcF4FE(int fftFrameSize, double[] data, int i, int nstep, double[] w) {
		final int fftFrameSize2 = fftFrameSize << 1; // 2*fftFrameSize;
		// Factor-4 Decomposition

		int w_len = w.length >> 1;
		while (nstep < fftFrameSize2) {

			int jmax = nstep;
			int nnstep = nstep << 1;
			if (nnstep == fftFrameSize2)
			{
				// Factor-4 Decomposition not possible
				calcF2E(fftFrameSize, data, i, nstep, w);
				return;
			}
			nstep <<= 2;
			int ii = i + jmax;
			int iii = i + w_len;
			for (int n = 0; n < jmax; n += 2) {
				double wr = w[i++];
				double wi = w[i++];
				double wr1 = w[ii++];
				double wi1 = w[ii++];
				double wwr1 = w[iii++];
				double wwi1 = w[iii++];				
				//double wwr1 = wr * wr1 - wi * wi1;  // these numbers can be precomputed!!! 
				//double wwi1 = wr * wi1 + wi * wr1;
				
					int m = n + jmax;

					double datam1_r = data[m];
					double datam1_i = data[m + 1];
					double datan1_r = data[n];
					double datan1_i = data[n + 1];

					n += nnstep;
					m += nnstep;
					double datam2_r = data[m];
					double datam2_i = data[m + 1];
					double datan2_r = data[n];
					double datan2_i = data[n + 1];

					double tempr   = datam1_r * wr - datam1_i * wi;
					double tempi   = datam1_r * wi + datam1_i * wr;
					
					datam1_r = datan1_r - tempr;
					datam1_i = datan1_i - tempi;
					datan1_r = datan1_r + tempr;
					datan1_i = datan1_i + tempi;
					
					double n2w1r  = datan2_r * wr1 - datan2_i * wi1;
					double n2w1i  = datan2_r * wi1 + datan2_i * wr1;
					double m2ww1r = datam2_r * wwr1 - datam2_i * wwi1;
					double m2ww1i = datam2_r * wwi1 + datam2_i * wwr1;					
					
					tempr = m2ww1r - n2w1r;
					tempi = m2ww1i - n2w1i;
					
					datam2_r = datam1_r + tempi; 
					datam2_i = datam1_i - tempr; 
					datam1_r = datam1_r - tempi; 
					datam1_i = datam1_i + tempr; 

					tempr = n2w1r + m2ww1r;
					tempi = n2w1i + m2ww1i;					
					
					datan2_r = datan1_r - tempr; 
					datan2_i = datan1_i - tempi; 
					datan1_r = datan1_r + tempr; 
					datan1_i = datan1_i + tempi; 
										
					data[m] = datam2_r;
					data[m + 1] = datam2_i;
					data[n] = datan2_r;
					data[n + 1] = datan2_i;
					
					n -= nnstep;
					m -= nnstep;
					data[m] = datam1_r;
					data[m + 1] = datam1_i;
					data[n] = datan1_r;
					data[n + 1] = datan1_i;

				
			}

			i += jmax << 1;

		}

	}
	
	// Perform Factor-4 Decomposition with  3 * complex operators and 8 +/- complex operators
	
	public final static void calcF4IE(int fftFrameSize, double[] data, int i, int nstep, double[] w) {
		final int fftFrameSize2 = fftFrameSize << 1; // 2*fftFrameSize;
		// Factor-4 Decomposition
		
		int w_len = w.length >> 1;
		while (nstep < fftFrameSize2) {

			int jmax = nstep;
			int nnstep = nstep << 1;
			if (nnstep == fftFrameSize2)
			{
				// Factor-4 Decomposition not possible
				calcF2E(fftFrameSize, data, i, nstep, w);
				return;
			}
			nstep <<= 2;
			int ii = i + jmax;
			int iii = i + w_len;			
			for (int n = 0; n < jmax; n += 2) {
				double wr = w[i++];
				double wi = w[i++];
				double wr1 = w[ii++];
				double wi1 = w[ii++];
				double wwr1 = w[iii++];
				double wwi1 = w[iii++];						
				//double wwr1 = wr * wr1 - wi * wi1;  // these numbers can be precomputed!!! 
				//double wwi1 = wr * wi1 + wi * wr1;
				
				
					int m = n + jmax;

					double datam1_r = data[m];
					double datam1_i = data[m + 1];
					double datan1_r = data[n];
					double datan1_i = data[n + 1];

					n += nnstep;
					m += nnstep;
					double datam2_r = data[m];
					double datam2_i = data[m + 1];
					double datan2_r = data[n];
					double datan2_i = data[n + 1];

					double tempr = datam1_r * wr - datam1_i * wi;
					double tempi = datam1_r * wi + datam1_i * wr;
					
					datam1_r = datan1_r - tempr;
					datam1_i = datan1_i - tempi;
					datan1_r = datan1_r + tempr;
					datan1_i = datan1_i + tempi;
					
					double n2w1r  = datan2_r * wr1 - datan2_i * wi1;
					double n2w1i  = datan2_r * wi1 + datan2_i * wr1;
					double m2ww1r = datam2_r * wwr1 - datam2_i * wwi1;
					double m2ww1i = datam2_r * wwi1 + datam2_i * wwr1;					

					tempr = n2w1r - m2ww1r;
					tempi = n2w1i - m2ww1i;
					
					datam2_r = datam1_r + tempi; 
					datam2_i = datam1_i - tempr; 
					datam1_r = datam1_r - tempi; 
					datam1_i = datam1_i + tempr; 

					tempr = n2w1r + m2ww1r;
					tempi = n2w1i + m2ww1i;					
					
					datan2_r = datan1_r - tempr; 
					datan2_i = datan1_i - tempi; 
					datan1_r = datan1_r + tempr; 
					datan1_i = datan1_i + tempi; 
										
					data[m] = datam2_r;
					data[m + 1] = datam2_i;
					data[n] = datan2_r;
					data[n + 1] = datan2_i;
					
					n -= nnstep;
					m -= nnstep;
					data[m] = datam1_r;
					data[m + 1] = datam1_i;
					data[n] = datan1_r;
					data[n + 1] = datan1_i;

				
			}

			i += jmax << 1;

		}

	}
	
	
	
	// Perform Factor-4 Decomposition with  4 * complex operators and 8 +/- complex operators
	public final static void calcF4(int fftFrameSize, double[] data, int i, int nstep, int sign, double[] w) {
		final int fftFrameSize2 = fftFrameSize << 1; // 2*fftFrameSize;
		// Factor-4 Decomposition

		while (nstep < fftFrameSize2) {

			if (nstep << 2 == fftFrameSize2) {
				// Goto Factor-4 Final Decomposition
				calcF4E(fftFrameSize, data, i, nstep, sign, w);
				return;
			}
			int jmax = nstep;
			int nnstep = nstep << 1;
			if (nnstep == fftFrameSize2)
				break; // Factor-4 Decomposition not possible
			nstep <<= 2;
			int ii = i + jmax;
			for (int j = 0; j < jmax; j += 2) {
				double wr = w[i++];
				double wi = w[i++];
				double wr1 = w[ii++];
				double wi1 = w[ii++];
				double wr2 = -wi1 * sign;
				double wi2 = wr1 * sign;
				
				
				for (int n = j; n < fftFrameSize2; n += nstep) {
					int m = n + jmax;

					double datam1_r = data[m];
					double datam1_i = data[m + 1];
					double datan1_r = data[n];
					double datan1_i = data[n + 1];
					
					double tempr;
					double tempi;

					n += nnstep;
					m += nnstep;
					double datam2_r = data[m];
					double datam2_i = data[m + 1];
					double datan2_r = data[n];
					double datan2_i = data[n + 1];
			
					tempr = datam1_r * wr - datam1_i * wi;
					tempi = datam1_r * wi + datam1_i * wr;

					datam1_r = datan1_r - tempr;
					datam1_i = datan1_i - tempi;
					datan1_r = datan1_r + tempr;
					datan1_i = datan1_i + tempi;

					tempr = datam2_r * wr - datam2_i * wi;
					tempi = datam2_r * wi + datam2_i * wr;

					datam2_r = datan2_r - tempr;
					datam2_i = datan2_i - tempi;
					datan2_r = datan2_r + tempr;
					datan2_i = datan2_i + tempi;					

					tempr = datan2_r * wr1 - datan2_i * wi1;
					tempi = datan2_r * wi1 + datan2_i * wr1;

					datan2_r = datan1_r - tempr;
					datan2_i = datan1_i - tempi;
					datan1_r = datan1_r + tempr;
					datan1_i = datan1_i + tempi;

					tempr = datam2_r * wr2 - datam2_i * wi2;
					tempi = datam2_r * wi2 + datam2_i * wr2;

					datam2_r = datam1_r - tempr;
					datam2_i = datam1_i - tempi;
					datam1_r = datam1_r + tempr;
					datam1_i = datam1_i + tempi;

					data[m] = datam2_r;
					data[m + 1] = datam2_i;
					data[n] = datan2_r;
					data[n + 1] = datan2_i;
					
					n -= nnstep;
					m -= nnstep;
					data[m] = datam1_r;
					data[m + 1] = datam1_i;
					data[n] = datan1_r;
					data[n + 1] = datan1_i;

				}
			}

			i += jmax << 1;

		}

		calcF2E(fftFrameSize, data, i, nstep, w);

	}
	
	
	
	

	public final static void calcF4E(int fftFrameSize, double[] data, int i, int nstep, int sign, double[] w) {
		int jmax = nstep;
		int nnstep = nstep << 1;
		nstep <<= 2;
		int ii = i + jmax;
		//int iii = ii + jmax;
		for (int n = 0; n < jmax; n += 2) {
			double wr = w[i++];
			double wi = w[i++];
			double wr1 = w[ii++];
			double wi1 = w[ii++];
			//double wr2 = w[iii++];
			//double wi2 = w[iii++];
			double wr2 = -wi1 * sign;
			double wi2 = wr1 * sign;
			

			int m = n + jmax;

			double datam1_r = data[m];
			double datam1_i = data[m + 1];
			double datan1_r = data[n];
			double datan1_i = data[n + 1];

			n += nnstep;
			m += nnstep;
			double datam2_r = data[m];
			double datam2_i = data[m + 1];
			double datan2_r = data[n];
			double datan2_i = data[n + 1];

			double tempr;
			double tempi;

			tempr = datam1_r * wr - datam1_i * wi;
			tempi = datam1_r * wi + datam1_i * wr;

			datam1_r = datan1_r - tempr;
			datam1_i = datan1_i - tempi;
			datan1_r = datan1_r + tempr;
			datan1_i = datan1_i + tempi;

			tempr = datam2_r * wr - datam2_i * wi;
			tempi = datam2_r * wi + datam2_i * wr;

			datam2_r = datan2_r - tempr;
			datam2_i = datan2_i - tempi;
			datan2_r = datan2_r + tempr;
			datan2_i = datan2_i + tempi;

			tempr = datan2_r * wr1 - datan2_i * wi1;
			tempi = datan2_r * wi1 + datan2_i * wr1;

			datan2_r = datan1_r - tempr;
			datan2_i = datan1_i - tempi;
			datan1_r = datan1_r + tempr;
			datan1_i = datan1_i + tempi;

			tempr = datam2_r * wr2 - datam2_i * wi2;
			tempi = datam2_r * wi2 + datam2_i * wr2;

			datam2_r = datam1_r - tempr;
			datam2_i = datam1_i - tempi;
			datam1_r = datam1_r + tempr;
			datam1_i = datam1_i + tempi;

			data[m] = datam2_r;
			data[m + 1] = datam2_i;
			data[n] = datan2_r;
			data[n + 1] = datan2_i;

			n -= nnstep;
			m -= nnstep;
			data[m] = datam1_r;
			data[m + 1] = datam1_i;
			data[n] = datan1_r;
			data[n + 1] = datan1_i;

		}

		i += jmax << 1;

		return;

	}

}
