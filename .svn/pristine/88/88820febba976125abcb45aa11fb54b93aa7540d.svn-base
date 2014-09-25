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

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import rasmus.fft.spi.FFTProvider;
import rasmus.util.RasmusUtil;

public class FFT {

	public static final int FORWARD = -1;
	public static final int BACKWARD = 1;
	
	public static final int REAL_TO_COMPLEX = -2;
	public static final int COMPLEX_TO_REAL = 2;
	
	private static FFTProvider[] providers;
	
	public static FFTProvider[] getProviders() { return providers; }
	
	public static void init()
	{
		List providers_list = RasmusUtil.getProviders(FFTProvider.class);
		Iterator iter = providers_list.iterator();
		while (iter.hasNext()) {
			Object obj = iter.next();
			if(!(obj instanceof FFTProvider))
				iter.remove();
			else
			if(((FFTProvider)obj).getInfo() == null)
				iter.remove();
		}
		providers = new FFTProvider[providers_list.size()];
		providers_list.toArray(providers);
		Arrays.sort(providers, new Comparator<FFTProvider>()
				{
					public int compare(FFTProvider arg0, FFTProvider arg1) {
						return arg1.getInfo().getPriority() - arg0.getInfo().getPriority();
					}
				});
		
	}

	public static FFTTransformer getTransformer(int fftsize)
	{
		return getTransformer(fftsize, FORWARD, true);
	}
	
	public static FFTTransformer getTransformer(int fftsize, int direction)
	{
		return getTransformer(fftsize, direction, true);
	}
		
	public static FFTTransformer getTransformer(int fftsize, int direction, boolean inplace)
	{
		
		for (int i = 0; i < providers.length; i++) {
			FFTProvider provider = providers[i];
			FFTTransformer transformer = provider.getTransformer(fftsize, direction, inplace);
			if(transformer != null) return transformer;			
		}
		
		// No transformer found
		return null;
	}
	
	public static FFTTransformer getTransformer(int[] dims)
	{
		return getTransformer(dims, FORWARD, true);
	}
	
	public static FFTTransformer getTransformer(int[] dims, int direction)
	{
		return getTransformer(dims, direction, true);
	}
		
	public static FFTTransformer getTransformer(int[] dims, int direction, boolean inplace)
	{
		
		for (int i = 0; i < providers.length; i++) {
			FFTProvider provider = providers[i];
			FFTTransformer transformer = provider.getTransformer(dims, direction, inplace);
			if(transformer != null) return transformer;			
		}
		
		// No transformer found
		return null;
	}		
	
	public static boolean isRadix2(int fftsize)
	{
		int x = (int)(Math.log(fftsize)/Math.log(2.));		
		return (1 << x) == fftsize;
	}	
	
	// Generate window function (Hanning) see http://www.musicdsp.org/files/wsfir.h
	public static double[] wHanning(int fftFrameSize)
	{
		double[] window_table = new double[fftFrameSize];
		for (int k = 0; k < fftFrameSize;k++) {			
			window_table[k] = -.5*Math.cos(2.*Math.PI*(double)k/(double)fftFrameSize)+.5;			
		}			
		return window_table; 
	}
	
	// Generate window function (Hamming)
	public static double[] wHamming(int fftFrameSize)
	{
		double[] window_table = new double[fftFrameSize];
		for (int k = 0; k < fftFrameSize;k++) {
			window_table[k] = -.46*Math.cos(2.*Math.PI*(double)k/(double)fftFrameSize)+.54;			
		}			
		return window_table;     	    	
	}
	
	// Generate window function (Blackman)
	public static double[] wBlackman(int fftFrameSize)
	{
		double[] window_table = new double[fftFrameSize];
		for (int k = 0; k < fftFrameSize;k++) {
			window_table[k] = -.5*Math.cos(2.*Math.PI*(double)k/(double)fftFrameSize)
			+.08*Math.cos(4.*Math.PI*(double)k/(double)fftFrameSize)+.42;			
		}			
		return window_table;     	    	
	}    	


	static {
		init();
	}

}
