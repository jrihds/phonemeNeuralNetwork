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

package rasmus.fft.radix2; 
class OpFFT4f4 { 
public static void calc(double[] d, int offset) { 
double d0=d[0+offset];
double d1=d[1+offset];
double d2=d[2+offset];
double d3=d[3+offset];
double d4=d[4+offset];
double d5=d[5+offset];
double d6=d[6+offset];
double d7=d[7+offset];
double tr;
double ti;
double m1wr;
double m1wi;
double n2w1r;
double n2w1i;
double m2ww1r;
double m2ww1i;
m1wr=d2;
m1wi=d3;
n2w1r=d4;
n2w1i=d5;
m2ww1r=d6;
m2ww1i=d7;
d2=d0-m1wr;
d3=d1-m1wi;
d0=d0+m1wr;
d1=d1+m1wi;
tr=m2ww1r-n2w1r;
ti=m2ww1i-n2w1i;
d6=d2+ti;
d7=d3-tr;
d2=d2-ti;
d3=d3+tr;
tr=n2w1r+m2ww1r;
ti=n2w1i+m2ww1i;
d4=d0-tr;
d5=d1-ti;
d0=d0+tr;
d1=d1+ti;
d[0+offset]=d0;
d[1+offset]=d1;
d[2+offset]=d2;
d[3+offset]=d3;
d[4+offset]=d4;
d[5+offset]=d5;
d[6+offset]=d6;
d[7+offset]=d7;
}
}
