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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;

public class FFTW
{
	
	private long plan = 0;
    private ByteBuffer invar = null;
    private ByteBuffer outvar = null;

	/* signs */ 
	public static final int FFTW_FORWARD = -1;
	public static final int FFTW_BACKWARD = -1;
	
	/* documented flags */ 
	public static final int FFTW_MEASURE = 0;
	public static final int FFTW_DESTROY_INPUT = 1 << 0;
	public static final int FFTW_UNALIGNED = 1 << 1;	
	public static final int FFTW_CONSERVE_MEMORY = 1 << 2;	
	public static final int FFTW_EXHAUSTIVE = 1 << 3;	
	public static final int FFTW_PRESERVE_INPUT = 1 << 4;	
	public static final int FFTW_PATIENT = 1 << 5;	
	public static final int FFTW_ESTIMATE = 1 << 6;	

    public FFTW(int n, int sign, int flags)
    {
        plan = createPlanDft1D(n, sign, flags);
    }
    public FFTW(int[] n, int sign, int flags)
    {
    	plan = createPlanDft(n, sign, flags);
    }
    
	private native long createPlanDft1D(int n, int sign, int flags );
    private native long createPlanDft(int[] n, int sign, int flags );
    private native void destroyPlan(long plan);

    public DoubleBuffer getInput()
    {
       return invar.order(ByteOrder.nativeOrder()).asDoubleBuffer();
    }

    public DoubleBuffer getOutput()
    {    	
       return outvar.order(ByteOrder.nativeOrder()).asDoubleBuffer();
    }

    public void execute()
    {
    	execute(plan);
    }
    private native void execute(long plan);

    protected void finalize()
    {
        if(plan != 0) destroyPlan(plan);
        plan = 0;
    }

    private static boolean fftw3_available = true;
    
    public static boolean isAvailable()
    {
    	return fftw3_available;
    }
    
    static 
    {    	
    	try
    	{
    		System.loadLibrary( "jfftw3" );
    	}
    	catch(Throwable t)
    	{
    		t.printStackTrace();
    		fftw3_available = false;
    	}
    }

}