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

package rasmus.fft.kissfft;

import rasmus.fft.FFT;
import rasmus.fft.FFTTransformer;
import rasmus.fft.spi.FFTProvider;

class KissfftTransformer implements FFTTransformer
{

	
	KissFft kissfft;
	
	public KissfftTransformer(int fftsize, int direction, boolean inplace) {
	    
	    kissfft = new KissFft(fftsize, direction == FFT.BACKWARD);
	}

	public void transform(double[] inout) {
			
		kissfft.transform(inout, inout);
	}

	public void transform(double[] in, double[] out) {
		kissfft.transform(in, out);
	}
	
}

class KissfftNDTransformer implements FFTTransformer
{

	
	KissFftnd kissfft;
	
	public KissfftNDTransformer(int[] dims, int direction, boolean inplace) {
	    
	    kissfft = new KissFftnd(dims, direction == FFT.BACKWARD);
	}

	public void transform(double[] inout) {
			
		kissfft.transform(inout, inout);
	}

	public void transform(double[] in, double[] out) {
		kissfft.transform(in, out);
	}
	
}

public class KissFFTProvider extends FFTProvider {

	private Info info = new Info("KissFFT", "KissFFT", "KissFFT mixed radix FFT library", "", Info.PRIORITY_FFT);	

	public Info getInfo() { return info; }	

	public FFTTransformer getTransformer(int fftsize, int direction, boolean inplace) {
		
		if((direction == FFT.BACKWARD) || (direction == FFT.FORWARD))
			return new KissfftTransformer(fftsize, direction, inplace);
		
		return null;
	}
	
	public FFTTransformer getTransformer(int[] dims, int direction, boolean inplace) {
		
		if(dims.length == 1) return getTransformer(dims[0], direction, inplace);
		
		if((direction == FFT.BACKWARD) || (direction == FFT.FORWARD))
			return new KissfftNDTransformer(dims, direction, inplace);
		
		return null;
	}	

}
