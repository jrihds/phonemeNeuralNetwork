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

package rasmus.fft.providers;

import rasmus.fft.FFT;
import rasmus.fft.FFTTransformer;
import rasmus.fft.radix2.BitReversal;
import rasmus.fft.radix2.OpFFT;
import rasmus.fft.spi.FFTProvider;

class ComplexRadix2Transformer implements FFTTransformer
{

	double[] w;
	BitReversal bitrev;
	int direction;
	int fftFrameSize;
	
	public ComplexRadix2Transformer(int fftFrameSize, int direction) {
		w = OpFFT.computeTwiddleFactors(fftFrameSize, direction);
		bitrev = new BitReversal(fftFrameSize);
		this.direction = direction;
		this.fftFrameSize = fftFrameSize;
	}

	public void transform(double[] data) {
		bitrev.transform(data);
		OpFFT.calc(fftFrameSize, data, direction, w);		
	}

	public void transform(double[] in, double[] out) {
		System.arraycopy(in, 0, out, 0, in.length);
		transform(out);
	}
	
}

public class ComplexRadix2 extends FFTProvider {
	
	private Info info = new Info("ComplexRadix2", "RasmusDSP", "Optimized Radix 2 Cooley-Tukey FFT", "", Info.PRIORITY_OPTIMIZED);	

	public Info getInfo() { return info; }	
	

	public FFTTransformer getTransformer(int fftsize, int direction, boolean inplace)
	{
		if(!FFT.isRadix2(fftsize)) return null;
		if(direction == FFT.FORWARD) return new ComplexRadix2Transformer(fftsize, direction);
		if(direction == FFT.BACKWARD) return new ComplexRadix2Transformer(fftsize, direction);
		return null;
	}	

}