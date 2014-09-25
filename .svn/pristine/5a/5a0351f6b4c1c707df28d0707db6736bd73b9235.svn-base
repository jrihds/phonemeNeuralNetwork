package speech;

/* 
 * Copyright (c) 2007 P.J.Leonard
 * 
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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
import speech.Mapper;

/**
 * 
 * notifies observers if it need to be redrawn (typically the panel(s))
 * 
 * update() redoes all the drawing.
 * 
 * SpectrogramListener - does image resize - incrementally redraws as the data
 * become ready.
 * 
 * @author pjl
 * 
 */
public class DrawScrollingSpect extends JPanel {

	private static final long serialVersionUID = 1L;
	Dimension size;
	private int[] screenBuffer;
	private boolean dirty = true;
	private double thresh;
	private MemoryImageSource screenConverter;
	private boolean screenRepaint = false;
	int nChunks;
	int nBins;
	int ptr = 0;
	Image offscreen;
	ValMapper mapper;
	double peaks[] = new double[128];

	public DrawScrollingSpect(int nChunks) {
		this.nChunks = nChunks;
		this.mapper = new ValMapper();
		mapper.update(null, null);
		// setDoubleBuffered(true);
	}

	final Object imagSync = new Object();

	void createGraphics() {

		synchronized (imagSync) {
			// nChunks=getWidth();
			size = new Dimension(nChunks, nBins);

			int width = nChunks;
			int height = nBins;

			screenBuffer = new int[width * height];

			screenConverter = new MemoryImageSource(width, height,
					screenBuffer, 0, width);
			screenConverter.setAnimated(true);
			screenConverter.setFullBufferUpdates(false);
			offscreen = Toolkit.getDefaultToolkit()
					.createImage(screenConverter);
			// setPreferredSize(size);
			// setSize(size);
		}
	}

	boolean recursion = false;

	public void notifyMoreDataReady(double[] bins) {
		
		for (int i = 1; i < bins.length - 1; i++) {
			if (bins[i] > bins[i - 1] && bins[i] > bins[i + 1]) {
				peaks[i] = bins[i];
			} else {
				peaks[i] = 0;
			}
		}
		
		if (recursion) {
			System.err.println(" RECURSION ");
		}
		nBins = bins.length;
		if (nBins == 0) {
			return;
		}

		recursion = true;

		if (size == null || nBins != size.height || nChunks != size.width) {
			createGraphics();
		}

		int width = size.width;

		for (int i = 0; i < nBins; i++) {
			int bin = nBins - i - 1;
			float val = mapper.eval(bins[bin]);

			if (val < 0) {
				val = 0.0f;
			}
			if (val > 1.0) {
				val = 1.0f;
			}
			int c_r = (int) (255 * val);
			int c_g = c_r;
			int c_b = 255 - c_r;

			int color = (c_b) + (c_g << 8) + (c_r << 16) + (0xFF << 24);
			screenBuffer[i * width + ptr] = (255 << 24) + color;
			// rgbarray[i] = color;
		}

		if (ptr % 1 == 0) {
			screenConverter.newPixels(ptr, 0, 1, nBins);
			screenRepaint = true;
		//	repaint();
		}

		ptr = (ptr + 1) % size.width;
		recursion = false;
	}

	// public void paintX(Graphics g) {
	// if (screenRepaint) {
	// g.drawImage(offscreen, 0, 0, this);
	// screenRepaint = false;
	// }
	// }
	@Override
	public void paint(Graphics g) {
		// super.paintComponent(g);
//		if (!screenRepaint) {
//			return;
//		}
		// System.out.println(" Spectro DRAWIMAGE");
		if (size == null) return;
		int w = size.width - ptr;
		int h = size.height;

		int magX = Math.max(1, getWidth() / size.width);
		int magY = Math.max(1, getHeight() / size.height);
		// System.out.println(magX+" "+magY);

		g.drawImage(offscreen, 0, 0, w * magX, h * magY, ptr, 0, size.width, h,
				this);
		if (ptr != 0) {
			g.drawImage(offscreen, w * magX, 0, size.width * magX, h * magY, 0,
					0, ptr, h, this);
		}

		// g.drawImage(offscreen, 0, 0, w, h, ptr, 0, size.width, h, this);
		//
		// if (ptr != 0) {
		// g.drawImage(offscreen, w, 0, size.width, h, 0, 0, ptr, h, this);
		// }
		double peaksRev[] = new double[128];
		int j=127;
		for (int i = 0; i <128; i++) {
			peaksRev[j] = peaks[i];
			j--;
		}
		
		double top=0.0;;
		for (int i = 0; i <128; i++) {
			if (peaksRev[i] > 0.5) {
				g.drawString("<------", 440, 5+(int)i*3);
			}
		}

		screenRepaint = false;
	}

	public int getHeightXX() {
		if (size == null) {
			return 200;
		}
		return size.height;
	}

}

class ValMapper implements Observer, Mapper {

	double maxdb = 50;
	double mindb = -80;
	double max;
	double min;
	boolean linear;
	private Thread thread;

	public final float eval(double val) {
		if (linear) {
			float vv = (float) ((val - min) / (max - min));
			return vv;
		} else {
			double dB = 20 * Math.log10(val + 1e-15);
			float vv = (float) ((dB - mindb) / (maxdb - mindb));
			return vv;
		}
	}

	public void update(Observable o, Object arg) {

		// linear = linearBut.isSelected();

		// maxdb = maxdB.doubleValue();
		max = Math.pow(10, maxdb / 20.0);

		// mindb = mindB.doubleValue();
		min = Math.pow(10, mindb / 20.0);

		// repaint();
		// if (thread != null)
		// thread.interrupt();

		// thread = new Thread(new Runnable() {
		// public void run() {
		// spectroSlicePanel.repaint();
		// timePanel.spectroImage.update(null, null);
		// thread = null;
		// }
		// });
		// thread.start();
	}
}