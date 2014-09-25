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
class OpFFTI16 { 
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
tr=d18;
ti=d19;
d18=d16-tr;
d19=d17-ti;
d16+=tr;
d17+=ti;
tr=d22;
ti=d23;
d22=d20-tr;
d23=d21-ti;
d20+=tr;
d21+=ti;
tr=d26;
ti=d27;
d26=d24-tr;
d27=d25-ti;
d24+=tr;
d25+=ti;
tr=d30;
ti=d31;
d30=d28-tr;
d31=d29-ti;
d28+=tr;
d29+=ti;
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
tr=d20;
ti=d21;
d20=d16-tr;
d21=d17-ti;
d16+=tr;
d17+=ti;
tr=d28;
ti=d29;
d28=d24-tr;
d29=d25-ti;
d24+=tr;
d25+=ti;
/* i = 1, j = 2 ----------------------------------------- */ 
tr=-d7;
ti=d6;
d6=d2-tr;
d7=d3-ti;
d2+=tr;
d3+=ti;
tr=-d15;
ti=d14;
d14=d10-tr;
d15=d11-ti;
d10+=tr;
d11+=ti;
tr=-d23;
ti=d22;
d22=d18-tr;
d23=d19-ti;
d18+=tr;
d19+=ti;
tr=-d31;
ti=d30;
d30=d26-tr;
d31=d27-ti;
d26+=tr;
d27+=ti;
/* i = 2, j = 0 ----------------------------------------- */ 
tr=d8;
ti=d9;
d8=d0-tr;
d9=d1-ti;
d0+=tr;
d1+=ti;
tr=d24;
ti=d25;
d24=d16-tr;
d25=d17-ti;
d16+=tr;
d17+=ti;
/* i = 2, j = 2 ----------------------------------------- */ 
tr=(d10-d11)*(0.7071067811865476);
ti=(d10+d11)*(0.7071067811865476);
d10=d2-tr;
d11=d3-ti;
d2+=tr;
d3+=ti;
tr=(d26-d27)*(0.7071067811865476);
ti=(d26+d27)*(0.7071067811865476);
d26=d18-tr;
d27=d19-ti;
d18+=tr;
d19+=ti;
/* i = 2, j = 4 ----------------------------------------- */ 
tr=-d13;
ti=d12;
d12=d4-tr;
d13=d5-ti;
d4+=tr;
d5+=ti;
tr=-d29;
ti=d28;
d28=d20-tr;
d29=d21-ti;
d20+=tr;
d21+=ti;
/* i = 2, j = 6 ----------------------------------------- */ 
tr=(d14+d15)*(-0.7071067811865474);
ti=(d15-d14)*(-0.7071067811865474);
d14=d6-tr;
d15=d7-ti;
d6+=tr;
d7+=ti;
tr=(d30+d31)*(-0.7071067811865474);
ti=(d31-d30)*(-0.7071067811865474);
d30=d22-tr;
d31=d23-ti;
d22+=tr;
d23+=ti;
/* i = 3, j = 0 ----------------------------------------- */ 
tr=d16;
ti=d17;
d16=d0-tr;
d17=d1-ti;
d0+=tr;
d1+=ti;
/* i = 3, j = 2 ----------------------------------------- */ 
tr=d18*(0.9238795325112867)-d19*(0.3826834323650898);
ti=d18*(0.3826834323650898)+d19*(0.9238795325112867);
d18=d2-tr;
d19=d3-ti;
d2+=tr;
d3+=ti;
/* i = 3, j = 4 ----------------------------------------- */ 
tr=(d20-d21)*(0.7071067811865475);
ti=(d20+d21)*(0.7071067811865475);
d20=d4-tr;
d21=d5-ti;
d4+=tr;
d5+=ti;
/* i = 3, j = 6 ----------------------------------------- */ 
tr=d22*(0.38268343236508967)-d23*(0.9238795325112867);
ti=d22*(0.9238795325112867)+d23*(0.38268343236508967);
d22=d6-tr;
d23=d7-ti;
d6+=tr;
d7+=ti;
/* i = 3, j = 8 ----------------------------------------- */ 
tr=-d25;
ti=d24;
d24=d8-tr;
d25=d9-ti;
d8+=tr;
d9+=ti;
/* i = 3, j = 10 ----------------------------------------- */ 
tr=d26*(-0.3826834323650899)-d27*(0.9238795325112867);
ti=d26*(0.9238795325112867)+d27*(-0.3826834323650899);
d26=d10-tr;
d27=d11-ti;
d10+=tr;
d11+=ti;
/* i = 3, j = 12 ----------------------------------------- */ 
tr=(d28+d29)*(-0.7071067811865477);
ti=(d29-d28)*(-0.7071067811865477);
d28=d12-tr;
d29=d13-ti;
d12+=tr;
d13+=ti;
/* i = 3, j = 14 ----------------------------------------- */ 
tr=d30*(-0.9238795325112868)-d31*(0.3826834323650896);
ti=d30*(0.3826834323650896)+d31*(-0.9238795325112868);
d30=d14-tr;
d31=d15-ti;
d14+=tr;
d15+=ti;
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
