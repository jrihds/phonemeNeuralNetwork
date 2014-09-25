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

	// Based on code (fftn.c) by
	// can be found: http://www.epiphyte.ca/code/vocoder/download.html
	/* (C) Copyright 1993 by Steven Trainoff.  Permission is granted to make
	* any use of this code subject to the condition that all copies contain
	* this notice and an indication of what has been changed.
	* 
	* Has been completely rewritten, but uses the same algorithm.
	* Inverse method has been added
	* */

package rasmus.fft.providers;

import rasmus.fft.FFT;
import rasmus.fft.FFTTransformer;
import rasmus.fft.spi.FFTProvider;

class ComplexToReal2Transformer implements FFTTransformer
{

	FFTTransformer fft;
	int fftFrameSize;
	int fftFrameSize2;
	double[] buffer; 
	double[] buffer2;
	
	public ComplexToReal2Transformer(int fftFrameSize, boolean inplace) {
		
		fft = FFT.getTransformer(fftFrameSize, FFT.BACKWARD, inplace );
		this.fftFrameSize = fftFrameSize;
		fftFrameSize2 = fftFrameSize << 1;
		buffer = new double[fftFrameSize2];
		buffer2 = new double[fftFrameSize2];
	}

	public void transform(double[] data) {
		
		for (int i = 0; i < fftFrameSize; i+=2) {
			buffer[i] = data[i];	
			buffer[i+1] = data[i+1];		
			buffer[(fftFrameSize2-2)-i] = 0;
			buffer[((fftFrameSize2-2)-i)+1] = 0;
		}

		
		fft.transform(buffer);

		for (int i = 0; i < fftFrameSize; i++) {
			data[i] = buffer[i<<1];
		}		
	}

	public void transform(double[] data, double[] out) {
		
		for (int i = 0; i < fftFrameSize; i+=2) {
			buffer[i] = data[i];	
			buffer[i+1] = data[i+1];		
			buffer[(fftFrameSize2-2)-i] = 0;
			buffer[((fftFrameSize2-2)-i)+1] = 0;
		}

		
		fft.transform(buffer, buffer2);

		for (int i = 0; i < data.length; i++) {
			data[i] = buffer2[i<<1];
		}		
		
	}
	
}

public class ComplexToReal2 extends FFTProvider {

	
	private Info info = new Info("ComplexToReal2", "RasmusDSP", "Complex to Real casting FFT Transform layer", "", Info.PRIORITY_FFT-2);	

	public Info getInfo() { return info; }	
	
	public FFTTransformer getTransformer(int fftsize, int direction, boolean inplace)
	{
		if(direction == FFT.COMPLEX_TO_REAL ) return new ComplexToReal2Transformer(fftsize, inplace);
		return null;
	}	

}