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
public final class OpFFT8 { 
public final static void calc(double[] d, int offset) { 
double d0=d[0+offset];
double d1=d[1+offset];
double d2=d[2+offset];
double d3=d[3+offset];
double d4=d[4+offset];
double d5=d[5+offset];
double d6=d[6+offset];
double d7=d[7+offset];
double d8=d[8+offset];
double d9=d[9+offset];
double d10=d[10+offset];
double d11=d[11+offset];
double d12=d[12+offset];
double d13=d[13+offset];
double d14=d[14+offset];
double d15=d[15+offset];
double tr;
double ti;
/* i = 0, j = 0 ----------------------------------------- */ 
tr=d2;
ti=d3;
d2=d0-tr;
d3=d1-ti;
d0+=tr;
d1+=ti;
tr=d6;
ti=d7;
d6=d4-tr;
d7=d5-ti;
d4+=tr;
d5+=ti;
tr=d10;
ti=d11;
d10=d8-tr;
d11=d9-ti;
d8+=tr;
d9+=ti;
tr=d14;
ti=d15;
d14=d12-tr;
d15=d13-ti;
d12+=tr;
d13+=ti;
/* i = 1, j = 0 ----------------------------------------- */ 
tr=d4;
ti=d5;
d4=d0-tr;
d5=d1-ti;
d0+=tr;
d1+=ti;
tr=d12;
ti=d13;
d12=d8-tr;
d13=d9-ti;
d8+=tr;
d9+=ti;
/* i = 1, j = 2 ----------------------------------------- */ 
tr=d6*(6.123233995736766E-17)+d7;
ti=d7*(6.123233995736766E-17)-d6;
d6=d2-tr;
d7=d3-ti;
d2+=tr;
d3+=ti;
tr=d14*(6.123233995736766E-17)+d15;
ti=d15*(6.123233995736766E-17)-d14;
d14=d10-tr;
d15=d11-ti;
d10+=tr;
d11+=ti;
/* i = 2, j = 0 ----------------------------------------- */ 
tr=d8;
ti=d9;
d8=d0-tr;
d9=d1-ti;
d0+=tr;
d1+=ti;
/* i = 2, j = 2 ----------------------------------------- */ 
tr=(d10+d11)*(0.7071067811865476);
ti=(d11-d10)*(0.7071067811865476);
d10=d2-tr;
d11=d3-ti;
d2+=tr;
d3+=ti;
/* i = 2, j = 4 ----------------------------------------- */ 
tr=d12*(2.220446049250313E-16)+d13;
ti=d13*(2.220446049250313E-16)-d12;
d12=d4-tr;
d13=d5-ti;
d4+=tr;
d5+=ti;
/* i = 2, j = 6 ----------------------------------------- */ 
tr=(d14-d15)*(-0.7071067811865474);
ti=(d14+d15)*(-0.7071067811865474);
d14=d6-tr;
d15=d7-ti;
d6+=tr;
d7+=ti;
d[0+offset]=d0;
d[1+offset]=d1;
d[2+offset]=d2;
d[3+offset]=d3;
d[4+offset]=d4;
d[5+offset]=d5;
d[6+offset]=d6;
d[7+offset]=d7;
d[8+offset]=d8;
d[9+offset]=d9;
d[10+offset]=d10;
d[11+offset]=d11;
d[12+offset]=d12;
d[13+offset]=d13;
d[14+offset]=d14;
d[15+offset]=d15;
}
}
