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
class OpFFT16f4 { 
public static void calc(double[] d, int offset) { 
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
double d16=d[16+offset];
double d17=d[17+offset];
double d18=d[18+offset];
double d19=d[19+offset];
double d20=d[20+offset];
double d21=d[21+offset];
double d22=d[22+offset];
double d23=d[23+offset];
double d24=d[24+offset];
double d25=d[25+offset];
double d26=d[26+offset];
double d27=d[27+offset];
double d28=d[28+offset];
double d29=d[29+offset];
double d30=d[30+offset];
double d31=d[31+offset];
double tr;
double ti;
double n2w1r;
double n2w1i;
double m2ww1r;
double m2ww1i;
/* i = 0, j = 0 ----------------------------------------- */ 
tr=d2;
ti=d3;
d2=d0-tr;
d3=d1-ti;
d0=d0+tr;
d1=d1+ti;
n2w1r=d4;
n2w1i=d5;
m2ww1r=d6;
m2ww1i=d7;
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
tr=d10;
ti=d11;
d10=d8-tr;
d11=d9-ti;
d8=d8+tr;
d9=d9+ti;
n2w1r=d12;
n2w1i=d13;
m2ww1r=d14;
m2ww1i=d15;
tr=m2ww1r-n2w1r;
ti=m2ww1i-n2w1i;
d14=d10+ti;
d15=d11-tr;
d10=d10-ti;
d11=d11+tr;
tr=n2w1r+m2ww1r;
ti=n2w1i+m2ww1i;
d12=d8-tr;
d13=d9-ti;
d8=d8+tr;
d9=d9+ti;
tr=d18;
ti=d19;
d18=d16-tr;
d19=d17-ti;
d16=d16+tr;
d17=d17+ti;
n2w1r=d20;
n2w1i=d21;
m2ww1r=d22;
m2ww1i=d23;
tr=m2ww1r-n2w1r;
ti=m2ww1i-n2w1i;
d22=d18+ti;
d23=d19-tr;
d18=d18-ti;
d19=d19+tr;
tr=n2w1r+m2ww1r;
ti=n2w1i+m2ww1i;
d20=d16-tr;
d21=d17-ti;
d16=d16+tr;
d17=d17+ti;
tr=d26;
ti=d27;
d26=d24-tr;
d27=d25-ti;
d24=d24+tr;
d25=d25+ti;
n2w1r=d28;
n2w1i=d29;
m2ww1r=d30;
m2ww1i=d31;
tr=m2ww1r-n2w1r;
ti=m2ww1i-n2w1i;
d30=d26+ti;
d31=d27-tr;
d26=d26-ti;
d27=d27+tr;
tr=n2w1r+m2ww1r;
ti=n2w1i+m2ww1i;
d28=d24-tr;
d29=d25-ti;
d24=d24+tr;
d25=d25+ti;
/* i = 1, j = 0 ----------------------------------------- */ 
tr=d8;
ti=d9;
d8=d0-tr;
d9=d1-ti;
d0=d0+tr;
d1=d1+ti;
n2w1r=d16;
n2w1i=d17;
m2ww1r=d24;
m2ww1i=d25;
tr=m2ww1r-n2w1r;
ti=m2ww1i-n2w1i;
d24=d8+ti;
d25=d9-tr;
d8=d8-ti;
d9=d9+tr;
tr=n2w1r+m2ww1r;
ti=n2w1i+m2ww1i;
d16=d0-tr;
d17=d1-ti;
d0=d0+tr;
d1=d1+ti;
/* i = 1, j = 2 ----------------------------------------- */ 
tr=(d10+d11)*(0.7071067811865476);
ti=(d10-d11)*(-0.7071067811865475);
d10=d2-tr;
d11=d3-ti;
d2=d2+tr;
d3=d3+ti;
n2w1r=d18*(0.9238795325112867)-d19*(-0.3826834323650898);
n2w1i=d18*(-0.3826834323650898)+d19*(0.9238795325112867);
m2ww1r=d26*(0.38268343236508984)-d27*(-0.9238795325112867);
m2ww1i=d26*(-0.9238795325112867)+d27*(0.38268343236508984);
tr=m2ww1r-n2w1r;
ti=m2ww1i-n2w1i;
d26=d10+ti;
d27=d11-tr;
d10=d10-ti;
d11=d11+tr;
tr=n2w1r+m2ww1r;
ti=n2w1i+m2ww1i;
d18=d2-tr;
d19=d3-ti;
d2=d2+tr;
d3=d3+ti;
/* i = 1, j = 4 ----------------------------------------- */ 
tr=d13;
ti=-d12;
d12=d4-tr;
d13=d5-ti;
d4=d4+tr;
d5=d5+ti;
n2w1r=(d20+d21)*(0.7071067811865475);
n2w1i=(d20-d21)*(-0.7071067811865476);
m2ww1r=(d28-d29)*(-0.7071067811865476);
m2ww1i=(d28+d29)*(-0.7071067811865475);
tr=m2ww1r-n2w1r;
ti=m2ww1i-n2w1i;
d28=d12+ti;
d29=d13-tr;
d12=d12-ti;
d13=d13+tr;
tr=n2w1r+m2ww1r;
ti=n2w1i+m2ww1i;
d20=d4-tr;
d21=d5-ti;
d4=d4+tr;
d5=d5+ti;
/* i = 1, j = 6 ----------------------------------------- */ 
tr=(d14-d15)*(-0.7071067811865477);
ti=(d14+d15)*(-0.7071067811865474);
d14=d6-tr;
d15=d7-ti;
d6=d6+tr;
d7=d7+ti;
n2w1r=d22*(0.38268343236508967)-d23*(-0.9238795325112867);
n2w1i=d22*(-0.9238795325112867)+d23*(0.38268343236508967);
m2ww1r=d30*(-0.9238795325112867)-d31*(0.38268343236508956);
m2ww1i=d30*(0.38268343236508956)+d31*(-0.9238795325112867);
tr=m2ww1r-n2w1r;
ti=m2ww1i-n2w1i;
d30=d14+ti;
d31=d15-tr;
d14=d14-ti;
d15=d15+tr;
tr=n2w1r+m2ww1r;
ti=n2w1i+m2ww1i;
d22=d6-tr;
d23=d7-ti;
d6=d6+tr;
d7=d7+ti;
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
d[16+offset]=d16;
d[17+offset]=d17;
d[18+offset]=d18;
d[19+offset]=d19;
d[20+offset]=d20;
d[21+offset]=d21;
d[22+offset]=d22;
d[23+offset]=d23;
d[24+offset]=d24;
d[25+offset]=d25;
d[26+offset]=d26;
d[27+offset]=d27;
d[28+offset]=d28;
d[29+offset]=d29;
d[30+offset]=d30;
d[31+offset]=d31;
}
}
