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

package rasmus.fft.radix2;

public class OpGenerator {
	
	/*
		{
			String code = OpGenerator.generateFactor2(16,-1);
			FileOutputStream fos = new FileOutputStream("OpFFT16.java");
			fos.write(code.getBytes("Latin1"));
			fos.close();
		}

		{
			String code = OpGenerator.generateFactor4(16,-1);
			FileOutputStream fos = new FileOutputStream("OpFFT16f4.java");
			fos.write(code.getBytes("Latin1"));
			fos.close();
		}
	 */
	
	public final static String generateFactor2(int fftFrameSize, int sign)
	
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
		
		StringBuffer sbuffer = new StringBuffer();
		
		// PART 1. Bit-Reversal
		// ====================
		
		// Pre-Processed Data
		//final int[] bitm_array = this.bitm_array;
		/*
		int[] num_map = new int[2*fftFrameSize];
		for (int i = 0; i < num_map.length; i++) {
			num_map[i] = i;
		}*/
		
		
		sbuffer.append("package rasmus.fft.radix2; \r\n");
		if(sign == 1)
			sbuffer.append("class OpFFTI" + fftFrameSize +" { \r\n");
		else
			sbuffer.append("class OpFFT" + fftFrameSize +" { \r\n");
		sbuffer.append("public static void calc(double[] d, int offset) { \r\n");
		
		// Performing Bit-Reversal		
		/*
		for (int i = 2; i < 2*fftFrameSize-2; i += 2) {
			int j = bitm_array[i];
			if (i < j) {
				
				// COMPLEX: SWAP(data[n], data[m])
				
				// Real Part
				int n = i; 
				int m = j;
				int tempr = num_map[n]; 
				num_map[n] = num_map[m];
				num_map[m] = tempr; 	
								
				// Imagery Part
				n++;
				m++;
				int tempi = num_map[n];
				num_map[n] = num_map[m];
				num_map[m] = tempi;
			}
		}
		*/
		
		for (int i = 0; i < 2*fftFrameSize; i++) {
			sbuffer.append("double d" + i + "=d[" + i + "+offset];\r\n");
		}
		
		sbuffer.append("double tr;\r\n");
		sbuffer.append("double ti;\r\n");
		
		// PART 2. Danielson Lanczos Algorithm
		// ===================================
		
		// Pre-Processed Data		
		final int fftFrameSize2 = 2*fftFrameSize;		
		
		int imax = (int)(Math.log(fftFrameSize)/Math.log(2.));
		
		double[] fft_cos = new double[imax];
		double[] fft_sin = new double[imax];
		for (int k = 0, le = 2; k < imax; k++) {
			le <<= 1;
			int le2 = le>>1;
			double arg = Math.PI / (le2>>1);			
			fft_cos[k] = Math.cos(arg);
			fft_sin[k] = Math.sin(arg); 
		}		
		
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
				
				sbuffer.append("/* i = " + i + ", j = " + j + " ----------------------------------------- */ \r\n");
				
				for (int n = j; n < fftFrameSize2; n += nstep) {
					
					int m = n + jmax;
					
					// COMPLEX: TEMP = W * data[k2]
					//double tempr = data[m] * wr - data[m+1] * wi;
					//double tempi = data[m] * wi + data[m+1] * wr;
					
					if((Math.abs(wr - 1.0) < 0.000001) && (Math.abs(wi) < 0.000001))
					{
						sbuffer.append("tr=d" + m + ";\r\n");
						sbuffer.append("ti=d" + (m + 1) + ";\r\n"); /*												
						sbuffer.append("d" + m + "-=tr;\r\n");
						sbuffer.append("d" + (m+1) + "-=ti;\r\n");
						sbuffer.append("d" + n + "+=tr;\r\n");
						sbuffer.append("d" + (n+1) + "+=ti;\r\n"); */						
					}
					else
					if((Math.abs(wr) < 0.000001) && (Math.abs(wi - 1.0) < 0.000001))
					{
							sbuffer.append("tr=-d" + (m + 1) + ";\r\n");
							sbuffer.append("ti=d" + m + ";\r\n");										
							/*						
							sbuffer.append("d" + m + "-=tr;\r\n");
							sbuffer.append("d" + (m+1) + "-=ti;\r\n");
							sbuffer.append("d" + n + "+=tr;\r\n");
							sbuffer.append("d" + (n+1) + "+=ti;\r\n"); */						
					}
					else
					if((Math.abs(wr) < 0.000001) && (Math.abs(wi + 1.0) < 0.000001))
					{
								sbuffer.append("tr=d" + (m + 1) + ";\r\n");
								sbuffer.append("ti=-d" + m + ";\r\n");										
								/*						
								sbuffer.append("d" + m + "-=tr;\r\n");
								sbuffer.append("d" + (m+1) + "-=ti;\r\n");
								sbuffer.append("d" + n + "+=tr;\r\n");
								sbuffer.append("d" + (n+1) + "+=ti;\r\n"); */						
					}					
					else
					{
						
						if(Math.abs(wi - 1.0) < 0.000001)
						{
							sbuffer.append("tr=d" + m + "*(" + wr + ")-d" + (m + 1) + ";\r\n");
							sbuffer.append("ti=d" + m + "+d" + (m + 1) + "*(" + wr + ");\r\n");										
						}
						else
						if(Math.abs(wi + 1.0) < 0.000001)
						{
							sbuffer.append("tr=d" + m + "*(" + wr + ")+d" + (m + 1) + ";\r\n");
							sbuffer.append("ti=d" + (m + 1) + "*(" + wr + ")-d" + m + ";\r\n");								
						}
						else  
						if(Math.abs(wi - wr) < 0.000001)
						{
							sbuffer.append("tr=(d" + m + "-d" + (m + 1) + ")*(" + wr + ");\r\n");
							sbuffer.append("ti=(d" + m + "+d" + (m + 1) + ")*(" + wr + ");\r\n");
						}
						else
						if(Math.abs(wi + wr) < 0.000001)
						{
							sbuffer.append("tr=(d" + m + "+d" + (m + 1) + ")*(" + wr + ");\r\n");
							sbuffer.append("ti=(d" + (m + 1) + "-d" + m + ")*(" + wr + ");\r\n");
						}
						else						
						{
							sbuffer.append("tr=d" + m + "*(" + wr + ")-d" + (m + 1) + "*(" + wi + ");\r\n");
							sbuffer.append("ti=d" + m + "*(" + wi + ")+d" + (m + 1) + "*(" + wr + ");\r\n");
						}

						
						
						
					}
					

					// COMPLEX: data[k2] = data[k1] - TEMP
					//data[m] = data[n] - tempr;
					//data[m+1] = data[n+1] - tempi;
					sbuffer.append("d" + m + "=d" + n + "-tr;\r\n");
					sbuffer.append("d" + (m+1) + "=d" + (n+1) + "-ti;\r\n");

					// COMPLEX: data[k1] = data[k1] + TEMP
					//data[n] += tempr;
					//data[n+1] += tempi;
					sbuffer.append("d" + n + "+=tr;\r\n");
					sbuffer.append("d" + (n+1) + "+=ti;\r\n");					

					

					
				}
				
				// COMPLEX: W = W * WF 
				double tempr = wr;
				wr = tempr*wfr - wi*wfi;
				wi = tempr*wfi + wi*wfr;
			}
		}
		
		for (int n = 0; n < fftFrameSize2; n++) {
			sbuffer.append("d[" + n + "+offset]=d" + n + ";\r\n");
			
		}
		
		sbuffer.append("}\r\n");
		sbuffer.append("}\r\n");
		
		
		return sbuffer.toString();
	}
	
	
	public final static String generateFactor4(int fftFrameSize, int sign)  
	
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
		
		final double[] w = OpFFT.computeTwiddleFactors(fftFrameSize, sign);

		
		StringBuffer sbuffer = new StringBuffer();
		
		// PART 1. Bit-Reversal
		// ====================
		
		// Pre-Processed Data
		//final int[] bitm_array = this.bitm_array;
		/*
		int[] num_map = new int[2*fftFrameSize];
		for (int i = 0; i < num_map.length; i++) {
			num_map[i] = i;
		}*/
		
		
		sbuffer.append("package rasmus.fft.radix2; \r\n");
		if(sign == 1)
			sbuffer.append("class OpFFTI" + fftFrameSize +"f4 { \r\n");
		else
			sbuffer.append("class OpFFT" + fftFrameSize +"f4 { \r\n");
		sbuffer.append("public static void calc(double[] d, int offset) { \r\n");
		
		// Performing Bit-Reversal		
		/*
		for (int i = 2; i < 2*fftFrameSize-2; i += 2) {
			int j = bitm_array[i];
			if (i < j) {
				
				// COMPLEX: SWAP(data[n], data[m])
				
				// Real Part
				int n = i; 
				int m = j;
				int tempr = num_map[n]; 
				num_map[n] = num_map[m];
				num_map[m] = tempr; 	
								
				// Imagery Part
				n++;
				m++;
				int tempi = num_map[n];
				num_map[n] = num_map[m];
				num_map[m] = tempi;
			}
		}
		*/
		
		for (int i = 0; i < 2*fftFrameSize; i++) {
			sbuffer.append("double d" + i + "=d[" + i + "+offset];\r\n");
		}
		
		sbuffer.append("double tr;\r\n");
		sbuffer.append("double ti;\r\n");	
		sbuffer.append("double n2w1r;\r\n");
		sbuffer.append("double n2w1i;\r\n");
		sbuffer.append("double m2ww1r;\r\n");
		sbuffer.append("double m2ww1i;\r\n");
		
		// PART 2. Danielson Lanczos Algorithm
		// ===================================
		
		// Pre-Processed Data		
		final int fftFrameSize2 = 2*fftFrameSize;		
		//final double[] fft_cos = this.fft_cos;
		//final double[] fft_sin = this.fft_sin;		
		//final int imax = this.imax;
		
				
		int nstep = 2;
		int i = 0;
		int ss = 0;
		while (nstep < fftFrameSize2) {
			
			int jmax = nstep;
			int nnstep = nstep << 1;
			if (nnstep == fftFrameSize2)
				break; // Factor-4 Decomposition not possible
			nstep <<= 2;
			int ii = i + jmax;
			for (int j = 0; j < jmax; j += 2) {
				
				sbuffer.append("/* i = " + ss + ", j = " + j + " ----------------------------------------- */ \r\n");
				
				double wr = w[i++];
				double wi = w[i++];
				double wr1 = w[ii++];
				double wi1 = w[ii++];
				double wwr1 = wr * wr1 - wi * wi1;  // these numbers can be precomputed!!!
				double wwi1 = wr * wi1 + wi * wr1;
				
				for (int n = j; n < fftFrameSize2; n += nstep) {
					int m = n + jmax;

					String datam1_r = "d" + (m);
					String datam1_i = "d" + (m+1);
					String datan1_r = "d" + (n); 
					String datan1_i = "d" + (n+1);

					n += nnstep;
					m += nnstep;
					String datam2_r = "d" + (m);
					String datam2_i = "d" + (m+1);
					String datan2_r = "d" + (n); 
					String datan2_i = "d" + (n+1);
/*
					double m1wr   = datam1_r * wr - datam1_i * wi;
					double m1wi   = datam1_r * wi + datam1_i * wr;
					double n2w1r  = datan2_r * wr1 - datan2_i * wi1;
					double n2w1i  = datan2_r * wi1 + datan2_i * wr1;
					double m2ww1r = datam2_r * wwr1 - datam2_i * wwi1;
					double m2ww1i = datam2_r * wwi1 + datam2_i * wwr1;
	*/				
					if((Math.abs(wr - 1) < 0.0000001) && (Math.abs(wi - 0) < 0.0000001))
					{
						sbuffer.append("tr=" + datam1_r + ";\r\n");
						sbuffer.append("ti=" + datam1_i + ";\r\n");						
					}
					else
					if((Math.abs(wr - 0) < 0.0000001) && (Math.abs(wi + 1) < 0.0000001))
					{
						sbuffer.append("tr=" + datam1_i + ";\r\n");
						sbuffer.append("ti=-" + datam1_r + ";\r\n");
					}
					else
					if((Math.abs(wr - wi) < 0.0000001))
					{
						sbuffer.append("tr=(" + datam1_r + "-" + datam1_i + ")*(" + wi + ");\r\n");
						sbuffer.append("ti=(" + datam1_r + "+" + datam1_i + ")*(" + wr + ");\r\n");						
					}
					else
					if((Math.abs(wr + wi) < 0.0000001))
					{
						sbuffer.append("tr=(" + datam1_r + "+" + datam1_i + ")*(" + wr + ");\r\n");
						sbuffer.append("ti=(" + datam1_r + "-" + datam1_i + ")*(" + wi + ");\r\n");						
					}
					else
					{						
						sbuffer.append("tr=" + datam1_r + "*(" + wr + ")-" + datam1_i + "*(" + wi + ");\r\n");
						sbuffer.append("ti=" + datam1_r + "*(" + wi + ")+" + datam1_i + "*(" + wr + ");\r\n");
					}
					
					/*
					datam1_r = datan1_r - m1wr;
					datam1_i = datan1_i - m1wi;
					datan1_r = datan1_r + m1wr;
					datan1_i = datan1_i + m1wi; */
					
					sbuffer.append(datam1_r + "="  + datan1_r + "-tr;\r\n"); 											
					sbuffer.append(datam1_i + "="  + datan1_i + "-ti;\r\n"); 											
					sbuffer.append(datan1_r + "="  + datan1_r + "+tr;\r\n"); 											
					sbuffer.append(datan1_i + "="  + datan1_i + "+ti;\r\n"); 											
					
					if((Math.abs(wr1 - 1) < 0.0000001) && (Math.abs(wi1 - 0) < 0.0000001))
					{
						sbuffer.append("n2w1r=" + datan2_r + ";\r\n");
						sbuffer.append("n2w1i=" + datan2_i + ";\r\n");						
					}
					else
					if((Math.abs(wr1 - wi1) < 0.0000001))
					{
						sbuffer.append("n2w1r=(" + datan2_r + "-" + datan2_i  + ")*(" + wi1 + ");\r\n");
						sbuffer.append("n2w1i=(" + datan2_r + "+" + datan2_i  + ")*(" + wr1 + ");\r\n");						
					}
					else
					if((Math.abs(wr1 + wi1) < 0.0000001))
					{
						sbuffer.append("n2w1r=(" + datan2_r + "+" + datan2_i  + ")*(" + wr1 + ");\r\n");
						sbuffer.append("n2w1i=(" + datan2_r + "-" + datan2_i  + ")*(" + wi1 + ");\r\n");						
					}
					else
					{
						sbuffer.append("n2w1r=" + datan2_r + "*(" + wr1 + ")-" + datan2_i  + "*(" + wi1 + ");\r\n");
						sbuffer.append("n2w1i=" + datan2_r + "*(" + wi1 + ")+" + datan2_i  + "*(" + wr1 + ");\r\n");
					}
					
					if((Math.abs(wwr1 - 1) < 0.0000001) && (Math.abs(wwi1 - 0) < 0.0000001))
					{
						sbuffer.append("m2ww1r=" + datam2_r + ";\r\n");
						sbuffer.append("m2ww1i=" + datam2_i + ";\r\n");
					}
					else
					if((Math.abs(wwr1 - wwi1) < 0.0000001))
					{
						sbuffer.append("m2ww1r=(" + datam2_r + "-" + datam2_i + ")*(" + wwi1 + ");\r\n");
						sbuffer.append("m2ww1i=(" + datam2_r + "+" + datam2_i + ")*(" + wwr1 + ");\r\n");					
					}
					else
					if((Math.abs(wwr1 + wwi1) < 0.0000001))
					{
						sbuffer.append("m2ww1r=(" + datam2_r + "+" + datam2_i + ")*(" + wwr1 + ");\r\n");
						sbuffer.append("m2ww1i=(" + datam2_r + "-" + datam2_i + ")*(" + wwi1 + ");\r\n");						
					}
					else
					{
						sbuffer.append("m2ww1r=" + datam2_r + "*(" + wwr1 + ")-" + datam2_i + "*(" + wwi1 + ");\r\n");
						sbuffer.append("m2ww1i=" + datam2_r + "*(" + wwi1 + ")+" + datam2_i + "*(" + wwr1 + ");\r\n");
					}
					
					//double tempr = m2ww1r - n2w1r;
					//double tempi = m2ww1i - n2w1i;
					
					if(sign == -1)
					{
						sbuffer.append("tr=m2ww1r-n2w1r;\r\n"); 											
						sbuffer.append("ti=m2ww1i-n2w1i;\r\n"); 											
					}
					else
					{
						sbuffer.append("tr=n2w1r-m2ww1r;\r\n"); 											
						sbuffer.append("ti=n2w1i-m2ww1i;\r\n"); 																	
					}
					/*
					datam2_r = datam1_r + tempi; 
					datam2_i = datam1_i - tempr; 
					datam1_r = datam1_r - tempi; 
					datam1_i = datam1_i + tempr; */
					
					sbuffer.append(datam2_r + "=" + datam1_r + "+ti;\r\n"); 											
					sbuffer.append(datam2_i + "=" + datam1_i + "-tr;\r\n"); 											
					sbuffer.append(datam1_r + "=" + datam1_r + "-ti;\r\n"); 											
					sbuffer.append(datam1_i + "=" + datam1_i + "+tr;\r\n"); 											
					

					//tempr = n2w1r + m2ww1r;
					//tempi = n2w1i + m2ww1i;
					
					sbuffer.append("tr=n2w1r+m2ww1r;\r\n"); 											
					sbuffer.append("ti=n2w1i+m2ww1i;\r\n"); 						
					/*
					datan2_r = datan1_r - tempr; 
					datan2_i = datan1_i - tempi; 
					datan1_r = datan1_r + tempr; 
					datan1_i = datan1_i + tempi; 
					*/

					sbuffer.append(datan2_r + "=" + datan1_r + "-tr;\r\n"); 											
					sbuffer.append(datan2_i + "=" + datan1_i + "-ti;\r\n"); 											
					sbuffer.append(datan1_r + "=" + datan1_r + "+tr;\r\n"); 											
					sbuffer.append(datan1_i + "=" + datan1_i + "+ti;\r\n"); 	
										
					
					n -= nnstep;
					m -= nnstep;

				}
			}

			i += jmax << 1;
			ss++;
		}
		
		// Factor-2 Final Decomposition
		if (nstep << 1 == fftFrameSize2) {
			int jmax = nstep;
			nstep <<= 1;
			for (int n = 0; n < jmax; n += 2) {
				double wr = w[i++];
				double wi = w[i++];
				int m = n + jmax;
				
				String datam_r = "d" + (m);
				String datam_i = "d" + (m+1);
				String datan_r = "d" + (n); 
				String datan_i = "d" + (n+1);
				
				if((Math.abs(wr - 1) < 0.0000001) && (Math.abs(wi - 0) < 0.0000001))
				{
					sbuffer.append("tr=" + datam_r + ";\r\n");
					sbuffer.append("ti=" + datam_i + ";\r\n");						
				}
				else
				if((Math.abs(wr - 0) < 0.0000001) && (Math.abs(wi + 1) < 0.0000001))
				{
					sbuffer.append("tr=" + datam_i + ";\r\n");
					sbuffer.append("ti=-" + datam_r + ";\r\n");
				}
				else
				if((Math.abs(wr - wi) < 0.0000001))
				{
					sbuffer.append("tr=(" + datam_r + "-" + datam_i + ")*(" + wi + ");\r\n");
					sbuffer.append("ti=(" + datam_r + "+" + datam_i + ")*(" + wr + ");\r\n");						
				}
				else
				if((Math.abs(wr + wi) < 0.0000001))
				{
					sbuffer.append("tr=(" + datam_r + "+" + datam_i + ")*(" + wr + ");\r\n");
					sbuffer.append("ti=(" + datam_r + "-" + datam_i + ")*(" + wi + ");\r\n");						
				}
				else
				{						
					sbuffer.append("tr=" + datam_r + "*(" + wr + ")-" + datam_i + "*(" + wi + ");\r\n");
					sbuffer.append("ti=" + datam_r + "*(" + wi + ")+" + datam_i + "*(" + wr + ");\r\n");
				}
				
				
				sbuffer.append(datam_r + "="  + datan_r + "-tr;\r\n"); 											
				sbuffer.append(datam_i + "="  + datan_i + "-ti;\r\n"); 											
				sbuffer.append(datan_r + "="  + datan_r + "+tr;\r\n"); 											
				sbuffer.append(datan_i + "="  + datan_i + "+ti;\r\n"); 											
								
			}
		}
				
		
		for (int n = 0; n < fftFrameSize2; n++) {
			sbuffer.append("d[" + n + "+offset]=d" + n + ";\r\n");
			
		}
		
		sbuffer.append("}\r\n");
		sbuffer.append("}\r\n");
		
		
		return sbuffer.toString();
	}
	
	

}
