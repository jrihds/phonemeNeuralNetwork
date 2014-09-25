/*
 * Copyright (c) 2003-2004, Mark Borgerding
 * Copyright (c) 2007, Conversion to Java by Karl helgason
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
 *    3. Neither the author nor the names of any contributors 
 *       may be used to endorse or promote 
 *       products derived from this software without specific prior 
 *       written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS 
 * AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR 
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT 
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE 
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package rasmus.fft.kissfft;

public class KissFftnd {

	private int ndims;
	private int dimprod;
	private int[] dims;
	private KissFft[] states;
	private double[] tmpbuf;
	
	public KissFftnd(int[] dims,boolean inverse_fft)
	{
		ndims = dims.length;		
	    int dimprod=1;	    

	    for (int i=0;i<ndims;++i) {
	        dimprod *= dims[i];
	    }

	    this.dimprod = dimprod;
	    this.dims = new int[ndims];
	    this.states = new KissFft[ndims];

	    this.tmpbuf = new double[dimprod<<1]; 

	    for (int i=0;i<ndims;++i) {
	        this.dims[i] = dims[i];	        
	        this.states[i] = new KissFft(this.dims[i], inverse_fft); 
	    }
	
	}

	/*
	 This works by tackling one dimension at a time.

	 In effect,
	 Each stage starts out by reshaping the matrix into a DixSi 2d matrix.
	 A Di-sized fft is taken of each column, transposing the matrix as it goes.

	Here's a 3-d example:
	Take a 2x3x4 matrix, laid out in memory as a contiguous buffer
	 [ [ [ a b c d ] [ e f g h ] [ i j k l ] ]
	   [ [ m n o p ] [ q r s t ] [ u v w x ] ] ]

	Stage 0 ( D=2): treat the buffer as a 2x12 matrix
	   [ [a b ... k l]
	     [m n ... w x] ]

	   FFT each column with size 2.
	   Transpose the matrix at the same time using kiss_fft_stride.

	   [ [ a+m a-m ]
	     [ b+n b-n]
	     ...
	     [ k+w k-w ]
	     [ l+x l-x ] ]

	   Note fft([x y]) == [x+y x-y]

	Stage 1 ( D=3) treats the buffer (the output of stage D=2) as an 3x8 matrix,
	   [ [ a+m a-m b+n b-n c+o c-o d+p d-p ] 
	     [ e+q e-q f+r f-r g+s g-s h+t h-t ]
	     [ i+u i-u j+v j-v k+w k-w l+x l-x ] ]

	   And perform FFTs (size=3) on each of the columns as above, transposing 
	   the matrix as it goes.  The output of stage 1 is 
	       (Legend: ap = [ a+m e+q i+u ]
	                am = [ a-m e-q i-u ] )
	   
	   [ [ sum(ap) fft(ap)[0] fft(ap)[1] ]
	     [ sum(am) fft(am)[0] fft(am)[1] ]
	     [ sum(bp) fft(bp)[0] fft(bp)[1] ]
	     [ sum(bm) fft(bm)[0] fft(bm)[1] ]
	     [ sum(cp) fft(cp)[0] fft(cp)[1] ]
	     [ sum(cm) fft(cm)[0] fft(cm)[1] ]
	     [ sum(dp) fft(dp)[0] fft(dp)[1] ]
	     [ sum(dm) fft(dm)[0] fft(dm)[1] ]  ]

	Stage 2 ( D=4) treats this buffer as a 4*6 matrix,
	   [ [ sum(ap) fft(ap)[0] fft(ap)[1] sum(am) fft(am)[0] fft(am)[1] ]
	     [ sum(bp) fft(bp)[0] fft(bp)[1] sum(bm) fft(bm)[0] fft(bm)[1] ]
	     [ sum(cp) fft(cp)[0] fft(cp)[1] sum(cm) fft(cm)[0] fft(cm)[1] ]
	     [ sum(dp) fft(dp)[0] fft(dp)[1] sum(dm) fft(dm)[0] fft(dm)[1] ]  ]

	   Then FFTs each column, transposing as it goes.

	   The resulting matrix is the 3d FFT of the 2x3x4 input matrix.

	   Note as a sanity check that the first element of the final 
	   stage's output (DC term) is 
	   sum( [ sum(ap) sum(bp) sum(cp) sum(dp) ] )
	   , i.e. the summation of all 24 input elements. 

	*/
	public void transform(double[] fin,double[] fout)
	{
	    int i,k;	    
	    double[] bufin=fin;
	    double[] bufout; 

	    /*arrange it so the last bufout == fout*/
	    if ( ndims % 2 == 1 ) {
	        bufout = fout;
	        if (fin==fout) {
	        	System.arraycopy(fin,0,tmpbuf,0,dimprod);
	            bufin = tmpbuf;
	        }
	    }else
	        bufout = tmpbuf;

	    for ( k=0; k < ndims; ++k) {
	        int curdim = dims[k];
	        int stride = dimprod / curdim;

	        for ( i=0 ; i<stride ; ++i ) 
	        	states[k].work(bufout,i*curdim, bufin, i, 1, stride, 0 );

	        /*toggle back and forth between the two buffers*/
	        if (bufout == tmpbuf){
	            bufout = fout;
	            bufin = tmpbuf;
	        }else{
	            bufout = tmpbuf;
	            bufin = fout;
	        }
	    }
	}
	
	
}
