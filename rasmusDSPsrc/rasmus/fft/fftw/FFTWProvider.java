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

package rasmus.fft.fftw;

import java.nio.DoubleBuffer;

import rasmus.fft.FFT;
import rasmus.fft.FFTTransformer;
import rasmus.fft.spi.FFTProvider;

class FFTWTransformer implements FFTTransformer
{
	
	FFTW fftw;
	DoubleBuffer in;
	DoubleBuffer out;
	
	public FFTWTransformer(int fftsize, int direction, boolean inplace) {
	    
		if(direction == FFT.BACKWARD)
			fftw = new FFTW(fftsize, FFTW.FFTW_BACKWARD, FFTW.FFTW_ESTIMATE);
		else
			fftw = new FFTW(fftsize, FFTW.FFTW_FORWARD, FFTW.FFTW_ESTIMATE);
	    out = fftw.getOutput();
	    in = fftw.getInput();
	    
	}

	public void transform(double[] inout) {
					
		in.position(0);
		in.put(inout,0,in.capacity());
		fftw.execute();
		out.position(0);
		out.get(inout,0,out.capacity());
	}

	public void transform(double[] in, double[] out) {
		this.in.position(0);
		this.in.put(in,0,this.in.capacity());
		fftw.execute();
		this.out.position(0);
		this.out.get(out,0,this.out.capacity());
	}
	
}

class FFTWNDTransformer implements FFTTransformer
{

	
	FFTW fftw;
	DoubleBuffer in;
	DoubleBuffer out;
	
	public FFTWNDTransformer(int[] dims, int direction, boolean inplace) {
	    
		if(direction == FFT.BACKWARD)
			fftw = new FFTW(dims, FFTW.FFTW_BACKWARD, FFTW.FFTW_ESTIMATE);
		else
			fftw = new FFTW(dims, FFTW.FFTW_FORWARD, FFTW.FFTW_ESTIMATE);		
	    out = fftw.getOutput();
	    in = fftw.getInput();
	}

	public void transform(double[] inout) {
		
		in.position(0);
		in.put(inout,0,in.capacity());
		fftw.execute();
		out.position(0);
		out.get(inout,0,out.capacity());
	}

	public void transform(double[] in, double[] out) {
		this.in.position(0);
		this.in.put(in,0,this.in.capacity());
		fftw.execute();
		this.out.position(0);
		this.out.get(out,0,this.out.capacity());
	}
	
}

public class FFTWProvider extends FFTProvider {

	private Info info = new Info("FFTW3", "FFTW3", "FFTW3 Native library", "", Info.PRIORITY_NATIVE);	

	public Info getInfo() { 
		return info; 
	}	

	public FFTTransformer getTransformer(int fftsize, int direction, boolean inplace) {

		if(!FFTW.isAvailable()) return null;

		if((direction == FFT.BACKWARD) || (direction == FFT.FORWARD))
			return new FFTWTransformer(fftsize, direction, inplace);
		
		return null;
	}
	
	public FFTTransformer getTransformer(int[] dims, int direction, boolean inplace) {
				
		if(!FFTW.isAvailable()) return null;
		
		if(dims.length == 1) return getTransformer(dims[0], direction, inplace);
		/* NOT IMPLEMENTED YET
		if((direction == FFT.BACKWARD) || (direction == FFT.FORWARD))
			return new KissfftNDTransformer(dims, direction, inplace);
		*/
		return null;
	}	

}
