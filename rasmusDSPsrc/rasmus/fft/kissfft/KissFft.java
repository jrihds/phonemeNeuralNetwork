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

class KissFft {

	private int nfft;

	private boolean inverse;

	private int[] factors = new int[32];

	private double[] twiddles_r;

	private double[] twiddles_i;

	private double[] scratchbuf_i = null;

	private double[] scratchbuf_r = null;
	
	private double[] tmpbuf = null;

	public KissFft(int nfft, boolean inverse_fft) {

		twiddles_r = new double[(nfft)];
		twiddles_i = new double[(nfft)];

		this.nfft = nfft;
		this.inverse = inverse_fft;

		for (int i = 0; i < nfft; ++i) {
			double pi = Math.PI;
			double phase = -2 * pi * i / nfft;
			if (this.inverse)
				phase *= -1;
			twiddles_r[i] = Math.cos(phase);
			twiddles_i[i] = Math.sin(phase);
		}

		factor(nfft, this.factors);
		
		int p = 1;
		for (int j = 0; j < factors.length; j+=2) {
			if(factors[j] > p) p = factors[j];
		}
		if(p>5)
		{
			scratchbuf_r = new double[p];
			scratchbuf_i = new double[p];
		}		


	}

	private void bfly2(double[] Fout, int Fout_index, int fstride, int m) {
		int Fout2;
		int tw1 = 0;
		double t_r;
		double t_i;

		Fout2 = Fout_index + m;
		do {
			
			double Fout2_r = Fout[Fout2 << 1];
			double Fout2_i = Fout[(Fout2 << 1) + 1];
			double Fout_r = Fout[(Fout_index << 1)];
			double Fout_i = Fout[(Fout_index << 1) + 1];
			
			t_r = Fout2_r * twiddles_r[tw1] - Fout2_i * twiddles_i[tw1];
			t_i = Fout2_r * twiddles_i[tw1] + Fout2_i * twiddles_r[tw1];
			tw1 += fstride;
			Fout[(Fout2 << 1)] = Fout_r - t_r;
			Fout[(Fout2 << 1) + 1] = Fout_i - t_i;
			Fout[(Fout_index << 1)] = Fout_r + t_r;
			Fout[(Fout_index << 1) + 1] = Fout_i + t_i;
			++Fout2;
			++Fout_index;
		} while (--m != 0);
	}

	private void bfly4(double[] Fout, int Fout_index, int fstride, int m) {
		int tw1, tw2, tw3;

		double scratch_0_r;
		double scratch_0_i;
		double scratch_1_r;
		double scratch_1_i;
		double scratch_2_r;
		double scratch_2_i;
		double scratch_3_r;
		double scratch_3_i;
		double scratch_4_r;
		double scratch_4_i;
		double scratch_5_r;
		double scratch_5_i;

		int k = m;
		int m2 = 2 * m;
		int m3 = 3 * m;

		tw3 = tw2 = tw1 = 0; // this.twiddles;

		do {
			scratch_0_r = Fout[(Fout_index + m) << 1] * twiddles_r[tw1] - Fout[((Fout_index + m) << 1) + 1] * twiddles_i[tw1];
			scratch_0_i = Fout[(Fout_index + m) << 1] * twiddles_i[tw1] + Fout[((Fout_index + m) << 1) + 1] * twiddles_r[tw1];

			scratch_1_r = Fout[(Fout_index + m2) << 1] * twiddles_r[tw2] - Fout[((Fout_index + m2) << 1) + 1] * twiddles_i[tw2];
			scratch_1_i = Fout[(Fout_index + m2) << 1] * twiddles_i[tw2] + Fout[((Fout_index + m2) << 1) + 1] * twiddles_r[tw2];

			scratch_2_r = Fout[(Fout_index + m3) << 1] * twiddles_r[tw3] - Fout[((Fout_index + m3) << 1) + 1] * twiddles_i[tw3];
			scratch_2_i = Fout[(Fout_index + m3) << 1] * twiddles_i[tw3] + Fout[((Fout_index + m3) << 1) + 1] * twiddles_r[tw3];

			scratch_5_r = Fout[(Fout_index << 1)] - scratch_1_r;
			scratch_5_i = Fout[(Fout_index << 1) + 1] - scratch_1_i;

			Fout[(Fout_index << 1)] = Fout[(Fout_index << 1)] + scratch_1_r;
			Fout[(Fout_index << 1) + 1] = Fout[(Fout_index << 1) + 1] + scratch_1_i;

			scratch_3_r = scratch_0_r + scratch_2_r;
			scratch_3_i = scratch_0_i + scratch_2_i;

			scratch_4_r = scratch_0_r - scratch_2_r;
			scratch_4_i = scratch_0_i - scratch_2_i;

			Fout[((Fout_index + m2) << 1)] = Fout[(Fout_index << 1)] - scratch_3_r;
			Fout[((Fout_index + m2) << 1) + 1] = Fout[(Fout_index << 1) + 1] - scratch_3_i;

			tw1 += fstride;
			tw2 += fstride * 2;
			tw3 += fstride * 3;

			Fout[(Fout_index << 1)] += scratch_3_r;
			Fout[(Fout_index << 1) + 1] += scratch_3_i;

			if (this.inverse) {
				Fout[((Fout_index + m) << 1)] = scratch_5_r - scratch_4_i;
				Fout[((Fout_index + m) << 1) + 1] = scratch_5_i + scratch_4_r;
				Fout[((Fout_index + m3) << 1)] = scratch_5_r + scratch_4_i;
				Fout[((Fout_index + m3) << 1) + 1] = scratch_5_i - scratch_4_r;
			} else {
				Fout[((Fout_index + m) << 1)] = scratch_5_r + scratch_4_i;
				Fout[((Fout_index + m) << 1) + 1] = scratch_5_i - scratch_4_r;
				Fout[((Fout_index + m3) << 1)] = scratch_5_r - scratch_4_i;
				Fout[((Fout_index + m3) << 1) + 1] = scratch_5_i + scratch_4_r;
			}
			++Fout_index;
		} while (--k != 0);
	}

	private void bfly3(double[] Fout, int Fout_index, int fstride, int m) {
		int k = m;
		int m2 = 2 * m;
		int tw1, tw2;

		double scratch_0_r;
		double scratch_0_i;
		double scratch_1_r;
		double scratch_1_i;
		double scratch_2_r;
		double scratch_2_i;
		double scratch_3_r;
		double scratch_3_i;

		double epi3_i;
		epi3_i = this.twiddles_i[fstride * m];

		tw1 = tw2 = 0;

		do {

			scratch_1_r = Fout[(Fout_index + m) << 1] * twiddles_r[tw1] - Fout[((Fout_index + m) << 1) + 1] * twiddles_i[tw1];
			scratch_1_i = Fout[(Fout_index + m) << 1] * twiddles_i[tw1] + Fout[((Fout_index + m) << 1) + 1] * twiddles_r[tw1];

			scratch_2_r = Fout[(Fout_index + m2) << 1] * twiddles_r[tw2] - Fout[((Fout_index + m2) << 1) + 1] * twiddles_i[tw2];
			scratch_2_i = Fout[(Fout_index + m2) << 1] * twiddles_i[tw2] + Fout[((Fout_index + m2) << 1) + 1] * twiddles_r[tw2];

			scratch_3_r = scratch_1_r + scratch_2_r;
			scratch_3_i = scratch_1_i + scratch_2_i;

			scratch_0_r = scratch_1_r - scratch_2_r;
			scratch_0_i = scratch_1_i - scratch_2_i;

			tw1 += fstride;
			tw2 += fstride * 2;

			Fout[(Fout_index + m) << 1] = Fout[(Fout_index) << 1]
					- (scratch_3_r * 0.5);
			Fout[((Fout_index + m) << 1) + 1] = Fout[((Fout_index) << 1) + 1]
					- (scratch_3_i * 0.5);

			scratch_0_i *= epi3_i;
			scratch_0_r *= epi3_i;

			Fout[(Fout_index) << 1] += scratch_3_r;
			Fout[((Fout_index) << 1) + 1] += scratch_3_i;

			Fout[(Fout_index + m2) << 1] = Fout[(Fout_index + m) << 1] + scratch_0_i;
			Fout[((Fout_index + m2) << 1) + 1] = Fout[((Fout_index + m) << 1) + 1] - scratch_0_r;

			Fout[(Fout_index + m) << 1] -= scratch_0_i;
			Fout[((Fout_index + m) << 1) + 1] += scratch_0_r;

			++Fout_index;
		} while (--k != 0);
	}

	private void bfly5(double[] Fout, int Fout_index, int fstride, int m) {
		int Fout0, Fout1, Fout2, Fout3, Fout4;
		int u;

		double scratch_0_r;
		double scratch_0_i;
		double scratch_1_r;
		double scratch_1_i;
		double scratch_2_r;
		double scratch_2_i;
		double scratch_3_r;
		double scratch_3_i;
		double scratch_4_r;
		double scratch_4_i;
		double scratch_5_r;
		double scratch_5_i;
		double scratch_6_r;
		double scratch_6_i;
		double scratch_7_r;
		double scratch_7_i;
		double scratch_8_r;
		double scratch_8_i;
		double scratch_9_r;
		double scratch_9_i;
		double scratch_10_r;
		double scratch_10_i;
		double scratch_11_r;
		double scratch_11_i;
		double scratch_12_r;
		double scratch_12_i;

		double ya_r = twiddles_r[fstride * m];
		double ya_i = twiddles_i[fstride * m];
		double yb_r = twiddles_r[fstride * 2 * m];
		double yb_i = twiddles_i[fstride * 2 * m];

		Fout0 = Fout_index;
		Fout1 = Fout0 + m;
		Fout2 = Fout0 + 2 * m;
		Fout3 = Fout0 + 3 * m;
		Fout4 = Fout0 + 4 * m;

		for (u = 0; u < m; ++u) {

			scratch_0_r = Fout[(Fout0) << 1];
			scratch_0_i = Fout[((Fout0) << 1) + 1];

			scratch_1_r = Fout[(Fout1) << 1] * twiddles_r[u * fstride] - Fout[((Fout1) << 1) + 1] * twiddles_i[u * fstride];
			scratch_1_i = Fout[(Fout1) << 1] * twiddles_i[u * fstride] + Fout[((Fout1) << 1) + 1] * twiddles_r[u * fstride];
			scratch_2_r = Fout[(Fout2) << 1] * twiddles_r[2 * u * fstride] - Fout[((Fout2) << 1) + 1] * twiddles_i[2 * u * fstride];
			scratch_2_i = Fout[(Fout2) << 1] * twiddles_i[2 * u * fstride] + Fout[((Fout2) << 1) + 1] * twiddles_r[2 * u * fstride];
			scratch_3_r = Fout[(Fout3) << 1] * twiddles_r[3 * u * fstride] - Fout[((Fout3) << 1) + 1] * twiddles_i[3 * u * fstride];
			scratch_3_i = Fout[(Fout3) << 1] * twiddles_i[3 * u * fstride] + Fout[((Fout3) << 1) + 1] * twiddles_r[3 * u * fstride];
			scratch_4_r = Fout[(Fout4) << 1] * twiddles_r[4 * u * fstride] - Fout[((Fout4) << 1) + 1] * twiddles_i[4 * u * fstride];
			scratch_4_i = Fout[(Fout4) << 1] * twiddles_i[4 * u * fstride] + Fout[((Fout4) << 1) + 1] * twiddles_r[4 * u * fstride];

			scratch_7_r = scratch_1_r + scratch_4_r;
			scratch_7_i = scratch_1_i + scratch_4_i;
			scratch_10_r = scratch_1_r - scratch_4_r;
			scratch_10_i = scratch_1_i - scratch_4_i;
			scratch_8_r = scratch_2_r + scratch_3_r;
			scratch_8_i = scratch_2_i + scratch_3_i;
			scratch_9_r = scratch_2_r - scratch_3_r;
			scratch_9_i = scratch_2_i - scratch_3_i;

			Fout[(Fout0) << 1] += scratch_7_r + scratch_8_r;
			Fout[((Fout0) << 1) + 1] += scratch_7_i + scratch_8_i;

			scratch_5_r = scratch_0_r + (scratch_7_r * ya_r) + (scratch_8_r * yb_r);
			scratch_5_i = scratch_0_i + (scratch_7_i * ya_r) + (scratch_8_i * yb_r);

			scratch_6_r = (scratch_10_i * ya_i) + (scratch_9_i * yb_i);
			scratch_6_i = -(scratch_10_r * ya_i) - (scratch_9_r * yb_i);

			Fout[(Fout1) << 1] = scratch_5_r - scratch_6_r;
			Fout[((Fout1) << 1) + 1] = scratch_5_i - scratch_6_i;
			Fout[(Fout4) << 1] = scratch_5_r + scratch_6_r;
			Fout[((Fout4) << 1) + 1] = scratch_5_i + scratch_6_i;

			scratch_11_r = scratch_0_r + (scratch_7_r * yb_r) + (scratch_8_r * ya_r);
			scratch_11_i = scratch_0_i + (scratch_7_i * yb_r) + (scratch_8_i * ya_r);
			scratch_12_r = -(scratch_10_i * yb_i) + (scratch_9_i * ya_i);
			scratch_12_i = (scratch_10_r * yb_i) - (scratch_9_r * ya_i);

			Fout[(Fout2) << 1] = scratch_11_r + scratch_12_r;
			Fout[((Fout2) << 1) + 1] = scratch_11_i + scratch_12_i;
			Fout[(Fout3) << 1] = scratch_11_r - scratch_12_r;
			Fout[((Fout3) << 1) + 1] = scratch_11_i - scratch_12_i;

			++Fout0;
			++Fout1;
			++Fout2;
			++Fout3;
			++Fout4;
		}
	}

	/* perform the butterfly for one stage of a mixed radix FFT */
	private void bfly_generic(double[] Fout, int Fout_index, int fstride,
			int m, int p) {
		int u, k, q1, q;
		double t_r;
		double t_i;
		int Norig = this.nfft;

		double[] scratchbuf_r = this.scratchbuf_r;
		double[] scratchbuf_i = this.scratchbuf_i;

		for (u = 0; u < m; ++u) {
			k = u;
			for (q1 = 0; q1 < p; ++q1) {
				scratchbuf_r[q1] = Fout[(Fout_index + k) << 1];
				scratchbuf_i[q1] = Fout[((Fout_index + k) << 1) + 1];
				k += m;
			}

			k = u;
			for (q1 = 0; q1 < p; ++q1) {
				int twidx = 0;
				Fout[(Fout_index + k) << 1] = scratchbuf_r[0];
				Fout[((Fout_index + k) << 1) + 1] = scratchbuf_i[0];

				for (q = 1; q < p; ++q) {
					twidx += fstride * k;
					if (twidx >= Norig)
						twidx -= Norig;
					t_r = scratchbuf_r[q] * twiddles_r[twidx] - scratchbuf_i[q]
							* twiddles_i[twidx];
					t_i = scratchbuf_r[q] * twiddles_i[twidx] + scratchbuf_i[q]
							* twiddles_r[twidx];

					Fout[(Fout_index + k) << 1] += t_r;
					Fout[((Fout_index + k) << 1) + 1] += t_i;
				}
				k += m;
			}
		}
	}

	public void work(double[] Fout, int Fout_index, double[] f, int f_index,
			int fstride, int in_stride, int factors) {
		int Fout_beg_index = Fout_index;
		int p = this.factors[factors++]; /* the radix */
		int m = this.factors[factors++]; /* stage's fft length/p */
		int Fout_end = Fout_index + p * m;

		if (m == 1) {
			do {
				Fout[Fout_index << 1] = f[f_index << 1];
				Fout[(Fout_index << 1) + 1] = f[(f_index << 1) + 1];
				f_index += fstride * in_stride;
			} while (++Fout_index != Fout_end);
		} else {
			do {
				work(Fout, Fout_index, f, f_index, fstride * p, in_stride,
						factors);
				f_index += fstride * in_stride;
			} while ((Fout_index += m) != Fout_end);
		}

		Fout_index = Fout_beg_index;

		switch (p) {
		case 2:
			bfly2(Fout, Fout_index, fstride, m);
			break;
		case 3:
			bfly3(Fout, Fout_index, fstride, m);
			break;
		case 4:
			bfly4(Fout, Fout_index, fstride, m);
			break;
		case 5:
			bfly5(Fout, Fout_index, fstride, m);
			break;
		default:
			bfly_generic(Fout, Fout_index, fstride, m, p);
			break;
		}
	}

	/*
	 * facbuf is populated by p1,m1,p2,m2, ... where p[i] * m[i] = m[i-1] m0 = n
	 */
	private void factor(int n, int[] facbuf) {
		int p = 4;
		double floor_sqrt;
		floor_sqrt = Math.floor(Math.sqrt((double) n));

		int i = 0;
		/* factor out powers of 4, powers of 2, then any remaining primes */
		do {
			while (n % p != 0) {
				switch (p) {
				case 4:
					p = 2;
					break;
				case 2:
					p = 3;
					break;
				default:
					p += 2;
					break;
				}
				if (p > floor_sqrt)
					p = n; /* no more factors, skip to end */
			}
			n /= p;
			facbuf[i++] = p;
			facbuf[i++] = n;
		} while (n > 1);
	}
	
	public void transform_stride(double[] fin, double[] fout, int in_stride) {
		if (fin == fout) {
			if (tmpbuf == null)
				tmpbuf = new double[nfft * 2];
			work(tmpbuf, 0, fin, 0, 1, in_stride, 0);
			System.arraycopy(tmpbuf, 0, fout, 0, tmpbuf.length);
		} else {
			work(fout, 0, fin, 0, 1, in_stride, 0);
		}
	}

	public void transform(double[] fin, double[] fout) {
		transform_stride(fin, fout, 1);
	}

	public static int nextFastSize(int n) {
		while (true) {
			int m = n;
			while ((m % 2) == 0)
				m /= 2;
			while ((m % 3) == 0)
				m /= 3;
			while ((m % 5) == 0)
				m /= 5;
			if (m <= 1)
				break; /* n is completely factorable by twos, threes, and fives */
			n++;
		}
		return n;
	}

}