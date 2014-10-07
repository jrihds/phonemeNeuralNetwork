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
import rasmus.fft.radix2.BitReversal;
import rasmus.fft.radix2.OpFFT;
import rasmus.fft.spi.FFTProvider;


class ComplexSimple3Radix2Transformer implements FFTTransformer
{

	double[] fft_cos;
	double[] fft_sin;
	int[] bitm_array;
	int direction;
	int fftFrameSize;
	int fftFrameSize2;
	int imax;
	double[] w;
	BitReversal bitrev;
	
	public ComplexSimple3Radix2Transformer(int fftFrameSize, int direction) {

		
		w = OpFFT.computeTwiddleFactors(fftFrameSize, direction);
		bitrev = new BitReversal(fftFrameSize);
		
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
		
		this.direction = direction;
		this.fftFrameSize = fftFrameSize;
	}

	public void transform(double[] data) 
	
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
	{

		final int[] bitm_array2 = bitrev.bitm_array2; 
		
		// PART 1. Bit-Reversal
		// ====================		
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
		
		// PART 2. Danielson Lanczos Algorithm
		// ===================================
		
		// Pre-Processed Data		
		final int fftFrameSize2 = 2*fftFrameSize;		
		final double[] w = this.w;		
		 		
		// Implementation of Danielson Lanczos Algorithm
		int i = 0;
		int nstep = 2;
		while(nstep < fftFrameSize2) {			
			int jmax = nstep;
			nstep <<= 1;											
			for (int j = 0; j < jmax; j += 2) {				
				double wr = w[i++];
				double wi = w[i++];		
				for (int n = j; n < fftFrameSize2; n += nstep) {					
					int m = n + jmax;					
					// COMPLEX: datam = data_n[k1] 
					double datam_r = data[m];
					double datam_i = data[m+1];		
					// COMPLEX: datan = data_n[k1] 
					double datan_r = data[n];
					double datan_i = data[n+1];										
					// COMPLEX: TEMP = W * datam
					double tempr = datam_r * wr - datam_i * wi;
					double tempi = datam_r * wi + datam_i * wr;
					// COMPLEX: data[k2] = data[k1] - TEMP
					data[m]   = datan_r - tempr;
					data[m+1] = datan_i - tempi;					
					// COMPLEX: data[k1] = data[k1] + TEMP
					data[n]   = datan_r + tempr;
					data[n+1] = datan_i + tempi;					
				}
			}
		}
		
	}

	public void transform(double[] in, double[] out) {
		System.arraycopy(in, 0, out, 0, in.length);
		transform(out);
	}
	
}

public class ComplexSimple3Radix2 extends FFTProvider {

	private Info info = new Info("ComplexSimple3Radix2", "RasmusDSP", "Simple Radix 2 Cooley-Tukey FFT, using precalculated twiddle factors", "", Info.PRIORITY_OPTIONAL);	

	public Info getInfo() { return info; }	
	
	public FFTTransformer getTransformer(int fftsize, int direction, boolean inplace)
	{
		if(!FFT.isRadix2(fftsize)) return null;
		if(direction == FFT.FORWARD) return new ComplexSimple3Radix2Transformer(fftsize, direction);
		if(direction == FFT.BACKWARD) return new ComplexSimple3Radix2Transformer(fftsize, direction);
		return null;
	}	

}
