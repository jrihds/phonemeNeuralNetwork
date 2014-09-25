/* 
 * Part of this code is Copyright (c) 1996 S.M.Bernsee under The Wide Open License.
 * 
 * Part if this code is Copyright (C) 1993 by Steven Trainoff
 * 
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

package rasmus.fft.providers;

import rasmus.fft.FFT;
import rasmus.fft.FFTTransformer;
import rasmus.fft.spi.FFTProvider;


class ComplexSimple2Radix2Transformer implements FFTTransformer
{

	double[] fft_cos;
	double[] fft_sin;
	int[] bitm_array;
	int direction;
	int fftFrameSize;
	int fftFrameSize2;
	int imax;
	
	public ComplexSimple2Radix2Transformer(int fftFrameSize, int direction) {
		
		/* initialize FFT sin and cos variables */
		imax = (int)(Math.log(fftFrameSize)/Math.log(2.));
		
		fft_cos = new double[imax];
		fft_sin = new double[imax];
		for (int k = 0, le = 2; k < imax; k++) {
			le <<= 1;
			int le2 = le>>1;
			double arg = Math.PI / (le2>>1);			
			fft_cos[k] = Math.cos(arg);
			fft_sin[k] = Math.sin(arg); 
		}
		
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

		this.direction = direction;
		this.fftFrameSize = fftFrameSize;
	}

	public void transform(double[] data) {

		// Sign = -1 is FFT, 1 is IFFT (inverse FFT)
		// Data = Interlaced double array to be transformed.
		//        The order is: real (sin), complex (cos) 
		// Framesize must be power of 2
		
		// Originaly Based On: smbPitchShift.cpp from http://www.dspdimension.com
		// Explaination of the algorithm see: http://qucs.sourceforge.net/tech/node33.html
		// Danielson Lanczos Algorithm: http://beige.ucs.indiana.edu/B673/node14.html
		// More about: Cooley-Tukey FFT: http://en.wikipedia.org/wiki/Cooley-Tukey_FFT_algorithm
		/* 
		 FFT routine, (C)1996 S.M.Bernsee. Sign = -1 is FFT, 1 is iFFT (inverse)
		 Fills data[0...2*fftFrameSize-1] with the Fourier transform of the
		 time domain data in data[0...2*fftFrameSize-1]. The FFT array takes
		 and returns the cosine and sine parts in an interleaved manner, ie.
		 data[0] = cosPart[0], data[1] = sinPart[0], asf. fftFrameSize
		 must be a power of 2. It expects a complex input signal (see footnote 2),
		 ie. when working with 'common' audio signals our input signal has to be
		 passed as {in[0],0.,in[1],0.,in[2],0.,...} asf. In that case, the transform
		 of the frequencies of interest is in data[0...fftFrameSize].
		 
		 COPYRIGHT 1999-2003 Stephan M. Bernsee <smb [AT] dspdimension [DOT] com>
		 *
		 * 						The Wide Open License (WOL)
		 *
		 * Permission to use, copy, modify, distribute and sell this software and its
		 * documentation for any purpose is hereby granted without fee, provided that
		 * the above copyright notice and this license appear in all source copies. 
		 * THIS SOFTWARE IS PROVIDED "AS IS" WITHOUT EXPRESS OR IMPLIED WARRANTY OF
		 * ANY KIND. See http://www.dspguru.com/wol.htm for more information.
		 *
		 */	
		
			
			// PART 1. Bit-Reversal
			// ====================
		
			int sign = direction;
			
			// Pre-Processed Data
			final int[] bitm_array = this.bitm_array;
			
			// Performing Bit-Reversal		
			for (int i = 2; i < fftFrameSize2-2; i += 2) {
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
			
			// PART 2. Danielson Lanczos Algorithm
			// ===================================
			
			// Pre-Processed Data		
			final int fftFrameSize2 = this.fftFrameSize2;
			final double[] fft_cos = this.fft_cos;
			final double[] fft_sin = this.fft_sin;		
			final int imax = this.imax;
			
			// Implementation of Danielson Lanczos Algorithm
			for (int i = 0, nstep = 2; i < imax; i++) {
				
				int jmax = nstep;
				nstep *= 2;
				
				// COMPLEX: W = 1
				double wr = 1.0;
				double wi = 0.0;
				
				// COMPLEX: WF = exp[i * sign * PI / (le2/2)]
				double wfr = fft_cos[i];
				double wfi = sign*fft_sin[i];		
				
				for (int j = 0; j < jmax; j += 2) {			
					
					for (int n = j; n < fftFrameSize2; n += nstep) {
						
						int m = n + jmax;
						
						// COMPLEX: TEMP = W * data[k2]
						double tempr = data[m] * wr - data[m+1] * wi;
						double tempi = data[m] * wi + data[m+1] * wr;
						
						// COMPLEX: data[k2] = data[k1] - TEMP
						data[m] = data[n] - tempr;
						data[m+1] = data[n+1] - tempi;
						
						// COMPLEX: data[k1] = data[k1] + TEMP
						data[n] += tempr;
						data[n+1] += tempi;
						
					}
					
					// COMPLEX: W = W * WF 
					double tempr = wr;
					wr = tempr*wfr - wi*wfi;
					wi = tempr*wfi + wi*wfr;
				}
			}

		
	}

	public void transform(double[] in, double[] out) {
		System.arraycopy(in, 0, out, 0, in.length);
		transform(out);
	}
	
}

public class ComplexSimple2Radix2 extends FFTProvider {

	
	private Info info = new Info("ComplexSimple2Radix2", "RasmusDSP", "Simple Radix 2 Cooley-Tukey FFT, using precalculated sin/cos list", "", Info.PRIORITY_OPTIONAL);	

	public Info getInfo() { return info; }	
	
	public FFTTransformer getTransformer(int fftsize, int direction, boolean inplace)
	{		
		if(!FFT.isRadix2(fftsize)) return null;		
		if(direction == FFT.FORWARD) return new ComplexSimple2Radix2Transformer(fftsize, direction);
		if(direction == FFT.BACKWARD) return new ComplexSimple2Radix2Transformer(fftsize, direction);
		return null;
	}	

}
