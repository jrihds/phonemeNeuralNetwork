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

class ComplexToRealTransformer implements FFTTransformer
{

	FFTTransformer fft;
	int fftFrameSize;
	int fftFrameSize2;
	double[] realmag_sin = null;
	double[] realmag_cos = null;
	
	public ComplexToRealTransformer(int fftFrameSize, boolean inplace) {
		
		fft = FFT.getTransformer(fftFrameSize>>1, FFT.BACKWARD, inplace );
		fftFrameSize2 = fftFrameSize;
		this.fftFrameSize = fftFrameSize>>1;
		
    	realmag_sin = new double[fftFrameSize2];
    	realmag_cos = new double[fftFrameSize2];
    	for (int i = 2; i < fftFrameSize2; i+=2) {
            double arg = (Math.PI * i) / (fftFrameSize2);
            realmag_cos[i] = Math.cos(arg);
            realmag_sin[i]= Math.sin(arg);        		
    	}		
	}

	public void transform(double[] data) {
		
        final double[] realmag_sin = this.realmag_sin;
        final double[] realmag_cos = this.realmag_cos;
                
        for (int i = 2; i <= fftFrameSize; i+=2) {
            double c = -realmag_cos[i];
            double s = realmag_sin[i];
            double xxr = data[i]*0.5;
            double xxi = data[i+1]*0.5;
            double xxr_n = data[fftFrameSize2-i]*0.5;
            double xxi_n = data[fftFrameSize2-i+1]*0.5;
            double tr = xxr - xxr_n;
            double ti = xxi + xxi_n;                
            double cr = c * ti - s * tr;
            double ci = s * ti + c * tr;
            tr = xxr + xxr_n;
            ti = xxi - xxi_n;                                      
            data[i]   				= tr + cr; 
            data[i+1] 				= ti - ci;                
            data[fftFrameSize2-i]   = tr - cr; 
            data[fftFrameSize2-i+1] = -ti - ci;                 
                
        }		
        
        fft.transform(data);
        
	}

	public void transform(double[] data, double[] out) {
		
        final double[] realmag_sin = this.realmag_sin;
        final double[] realmag_cos = this.realmag_cos;
                        
        for (int i = 2; i <= fftFrameSize; i+=2) {
            double c = -realmag_cos[i];
            double s = realmag_sin[i];
            double xxr = data[i]*0.5;
            double xxi = data[i+1]*0.5;
            double xxr_n = data[fftFrameSize2-i]*0.5;
            double xxi_n = data[fftFrameSize2-i+1]*0.5;
            double tr = xxr - xxr_n;
            double ti = xxi + xxi_n;                
            double cr = c * ti - s * tr;
            double ci = s * ti + c * tr;
            tr = xxr + xxr_n;
            ti = xxi - xxi_n;                                      
            data[i]   				= tr + cr; 
            data[i+1] 				= ti - ci;                
            data[fftFrameSize2-i]   = tr - cr; 
            data[fftFrameSize2-i+1] = -ti - ci;                                 
        }		
        
        fft.transform(data, out);
        
	}
	
}

public class ComplexToReal extends FFTProvider {

	
	private Info info = new Info("ComplexToReal", "RasmusDSP", "Complex to Real FFT Transform layer", "", Info.PRIORITY_FFT-1);	

	public Info getInfo() { return info; }	
	
	public FFTTransformer getTransformer(int fftsize, int direction, boolean inplace)
	{
		if(fftsize % 2 == 1) return null; 
		if(direction == FFT.COMPLEX_TO_REAL ) return new ComplexToRealTransformer(fftsize, inplace);
		return null;
	}	

}