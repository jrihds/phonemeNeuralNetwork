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

import java.util.Arrays;

import rasmus.fft.FFT;
import rasmus.fft.FFTTransformer;
import rasmus.fft.spi.FFTProvider;

class ComplexToComplex2Transformer implements FFTTransformer
{
	
	private double[] ptable = null;
	private double[] aa = null;
	private int fftFrameSize;
	
	public ComplexToComplex2Transformer(int fftFrameSize, int direction)
	{
		this.fftFrameSize = fftFrameSize;
		int partials = fftFrameSize/2;
		int p = 0;
		ptable = new double[2 * fftFrameSize * partials];
		int hfs=fftFrameSize/2 ;
		double pd=Math.PI/hfs;
		for (int i=0;i<fftFrameSize;i++)
		{
			int im = i-hfs;
			for(int h=0;h<(partials);h++)
			{
				double th=(pd*(h+1))*im;
				ptable[p++] = Math.cos(th);
				ptable[p++] = Math.sin(th)*direction;
			}
		}						
	}
	
	/*
	 Based on:
	 http://www.musicdsp.org/archive.php?classid=2#21
	 Type : fourier transform
	 References : Posted by Andy Mucho
	 
	 */		
	
	public void transform(double[] data) {
		if(aa == null) aa = new double[fftFrameSize*2];
		transform(data,aa);
		for (int h=0;h<fftFrameSize;h++)
			data[h] = aa[h];		
	}
	
	public void transform(double[] in, double[] out) {
		
		Arrays.fill(out, 0);	   
		
		for (int i=0,p=0;i<fftFrameSize;i++)
		{
			double w=in[i*2];
			for(int h=0;h<fftFrameSize;h++)
				out[h]+=w*ptable[p++];
		}	   	   
		
	}	
	
}

public class ComplexDFT2 extends FFTProvider {
	
	private Info info = new Info("ComplexDFT2", "RasmusDSP", "Complex DFT Transformation, using precalculated factors", "", Info.PRIORITY_OPTIONAL);
	
	public Info getInfo() { return info; }	

	public FFTTransformer getTransformer(int fftsize, int direction, boolean inplace)
	{
		if(direction == FFT.FORWARD) return new ComplexToComplex2Transformer(fftsize, direction);
		if(direction == FFT.BACKWARD) return new ComplexToComplex2Transformer(fftsize, direction);
		return null;
	}	

}
