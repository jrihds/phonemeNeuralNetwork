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

package rasmus.fft;

import rasmus.fft.spi.FFTProvider;

public class Benchmark {

	public static void main(String[] args) {
		//FFTProvider provider1 = new rasmus.fft.providers.ComplexRadix2();
		//FFTProvider provider2 = new rasmus.fft.providers.ComplexSimple2Radix2();		
		FFTProvider provider1 = new rasmus.fft.fftw.FFTWProvider();
		FFTProvider provider2 = new rasmus.fft.providers.ComplexRadix2();		
		benchmark(provider1, provider2);
	}
	

	
	
	public static void benchmark(FFTProvider provider1, FFTProvider provider2, int fftsize)
	{
		int prelen = 20;
		int testlen = 50;
		if(fftsize < 512) prelen = 200;
		if(fftsize <= 128) prelen = 10000;
		if(fftsize < 4) prelen = 20000;
		
		if(fftsize < 8192) prelen = 100;
		if(fftsize < 4096) prelen = 200;
		if(fftsize < 2048) prelen = 500;
		if(fftsize < 64) prelen = 2000;
		
		if(fftsize >= 65536)
		{
			prelen = 15;
			testlen = 20;
		}
		
		FFTTransformer fft = provider1.getTransformer(fftsize, FFT.FORWARD,true);
		FFTTransformer ffti = provider1.getTransformer(fftsize, FFT.FORWARD,true);
		
		FFTTransformer fft2 = provider2.getTransformer(fftsize, FFT.FORWARD,true);
		FFTTransformer ffti2 =provider2.getTransformer(fftsize, FFT.FORWARD,true);

		double[] fftBuffer = new double[fftsize*2];
		
		for (int i = 0; i < fftBuffer.length; i++) {
			fftBuffer[i] = Math.random() * 2;
		}

		for (int k = 0; k < prelen; k++) {
			fft2.transform(fftBuffer); 
			ffti2.transform(fftBuffer); 
		}	

		long nanotime = System.nanoTime();

		for (int k = 0; k < testlen; k++) {
			fft2.transform(fftBuffer); 
			ffti2.transform(fftBuffer); 
		}	
					
		long aa = (System.nanoTime() - nanotime);
			
		for (int i = 0; i < fftBuffer.length; i++) {
			fftBuffer[i] = Math.random() * 2;
		}				
		
		for (int k = 0; k < prelen; k++) {
			fft.transform(fftBuffer); 
			ffti.transform(fftBuffer);
		}
		
		nanotime = System.nanoTime();
					
		for (int k = 0; k < testlen; k++) {
			fft.transform(fftBuffer); 
			ffti.transform(fftBuffer); 
		}
		
		long  bb = (System.nanoTime() - nanotime);
		
		
		                   // times 2 because I perform FFT and FFT Inverse. 
		int mflops1 = (int) ( 2 * (5 * fftsize * Math.log(fftsize) / Math.log(2)) / (aa / (testlen*1000.0)) );
		int mflops2 = (int) ( 2 * (5 * fftsize * Math.log(fftsize) / Math.log(2)) / (bb / (testlen*1000.0)) );
		
		System.out.print(("FFT:" + fftsize + "                   ").substring(0, 10));
		System.out.print(" | ");
		System.out.print((((double)aa) / ((double)bb) + "     ").substring(0, 5));
		System.out.println(" | " + mflops1 + " mflops v.s. " + mflops2 + " mflops.");
		Thread.yield();
				
		
	}
	
	
	public static void benchmark(FFTTransformer fft, FFTTransformer fft2, int fftsize)
	{
		int prelen = 20;
		int testlen = 50;
		if(fftsize < 512) prelen = 200;
		if(fftsize <= 128) prelen = 10000;
		if(fftsize < 4) prelen = 20000;
		
		if(fftsize < 8192) prelen = 100;
		if(fftsize < 4096) prelen = 200;
		if(fftsize < 2048) prelen = 500;
		if(fftsize < 64) prelen = 2000;
		
		if(fftsize >= 65536)
		{
			prelen = 15;
			testlen = 20;
		}
		
		double[] fftBuffer = new double[fftsize*2];
		
		for (int i = 0; i < fftBuffer.length; i++) {
			fftBuffer[i] = Math.random() * 2;
		}

		for (int k = 0; k < prelen; k++) {
			fft2.transform(fftBuffer); 
		}	

		long nanotime = System.nanoTime();

		for (int k = 0; k < testlen; k++) {
			fft2.transform(fftBuffer); 
		}	
					
		long aa = (System.nanoTime() - nanotime);
			
		for (int i = 0; i < fftBuffer.length; i++) {
			fftBuffer[i] = Math.random() * 2;
		}				
		
		for (int k = 0; k < prelen; k++) {
			fft.transform(fftBuffer); 
		}
		
		nanotime = System.nanoTime();
					
		for (int k = 0; k < testlen; k++) {
			fft.transform(fftBuffer); 
		}
		
		long  bb = (System.nanoTime() - nanotime);
		
		
		                   // times 2 because I perform FFT and FFT Inverse. 
		int mflops1 = (int) ( (5 * fftsize * Math.log(fftsize) / Math.log(2)) / (aa / (testlen*1000.0)) );
		int mflops2 = (int) ( (5 * fftsize * Math.log(fftsize) / Math.log(2)) / (bb / (testlen*1000.0)) );
		
		System.out.print(("FFT:" + fftsize + "                   ").substring(0, 10));
		System.out.print(" | ");
		System.out.print((((double)aa) / ((double)bb) + "     ").substring(0, 5));
		System.out.println(" | " + mflops1 + " mflops v.s. " + mflops2 + " mflops.");
		Thread.yield();
				
		
	}
	
	
	public static void benchmark(FFTProvider provider1, FFTProvider provider2)
	{	
		int fftsize = 1;
		
		for (int j = 2; j < 20; j++) {

			
			fftsize <<= 1;
			
			benchmark(provider1, provider2, fftsize);
			
		}

	}

}
