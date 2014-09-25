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

package rasmus.interpreter.sampled.util;

public final class FFT {
	
	
	final int fftFrameSize;
	
	// Generate window function (Hanning) see http://www.musicdsp.org/files/wsfir.h
	public double[] wHanning()
	{
		return rasmus.fft.FFT.wHanning(fftFrameSize);
	}
		
	public FFT(int fftFrameSize)
	{		
		this.fftFrameSize = fftFrameSize;
	}
	
	rasmus.fft.FFTTransformer fft_forward = null;
	rasmus.fft.FFTTransformer fft_backward = null;
	
	public final void calc(double[] data, int sign)
	{
		if(sign == -1)
		{
			if(fft_forward == null) fft_forward = rasmus.fft.FFT.getTransformer(fftFrameSize,  rasmus.fft.FFT.FORWARD, true);
			fft_forward.transform(data);
		}
		else
		{
			if(fft_backward == null) fft_backward = rasmus.fft.FFT.getTransformer(fftFrameSize,  rasmus.fft.FFT.BACKWARD, true);
			fft_backward.transform(data);
		}
	}
	
	rasmus.fft.FFTTransformer fft_realtocomplex = null;
	rasmus.fft.FFTTransformer fft_complextoreal = null;
	
	public final void calcReal(double[] data, int sign)
	{
		if(sign == -1)
		{
			if(fft_realtocomplex == null) fft_realtocomplex = rasmus.fft.FFT.getTransformer(fftFrameSize,  rasmus.fft.FFT.REAL_TO_COMPLEX, true);
			fft_realtocomplex.transform(data);
		}
		else
		{
			if(fft_complextoreal == null) fft_complextoreal = rasmus.fft.FFT.getTransformer(fftFrameSize,  rasmus.fft.FFT.COMPLEX_TO_REAL, true);
			fft_complextoreal.transform(data);
		}
	}
}
