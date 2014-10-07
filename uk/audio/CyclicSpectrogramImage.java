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

package uk.ac.bath.audio;



import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;

import javax.swing.JPanel;



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
public class CyclicSpectrogramImage extends JPanel implements
		CyclicSpectrogramDataListener {

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

	Mapper mapper;

	public CyclicSpectrogramImage(Mapper mapper, int nChunks) {                 //Constructs a instance with these arguements
		this.nChunks = nChunks;                                                 // Takes the arguement defined by the user and sets the object as
		this.mapper = mapper;                                                   // that so th rest of the program can use it
   //     setDoubleBuffered(true);
	}

	final Object imagSync = new Object();                                       //Creates and immutable object called imagSync

	void createGraphics() {

		synchronized (imagSync) {                                               //The synchronized modifier allows the user to lock objects that are shared
                                                                                //If you have more than one of your threads running at the same time that
                                                                                //access shared objects, it may cause your object to be in an inconsitent state
                                                                                // Making a method synchronized will lock the object. This means that no other thread can call
                                                                                //a synchronized method on that object.
       
            size = new Dimension(nChunks, nBins);                               //The dimension class 'encapsulates the width and height of a component in a single object
                                                                                //In this case i think the object imageSync
			int width = nChunks;
			int height = nBins;                                                 // nBins is the height maybe the no.of rows which get coloured in

			screenBuffer = new int[width * height];                             //i think this creates an integer array called screenBuffer

			screenConverter = new MemoryImageSource(width, height,              //The MemoryImageSource class--- This class is an implementation of the ImageProducer interface which uses an array to produce pixel values for an Image.
					screenBuffer, 0, width);                                    //The MemoryImageSource is also capable of managing a memory image which varies over time to allow animation or custom rendering.
			screenConverter.setAnimated(true);                                  //Changes this memory image into a multi-frame animation or a single-frame static image depending on the animated parameter.This is a multframe animation
			screenConverter.setFullBufferUpdates(false);                        // Specifies whether this animated memory image should always be updated by sending the complete buffer of pixels whenever there is a change.
			offscreen = Toolkit.getDefaultToolkit()
					.createImage(screenConverter);                              //Returns an image which gets pixel data from the specified file. there other possible create image
            setPreferredSize(size);
		}
	}

	boolean recursion = false;

	public void notifyMoreDataReady(float[] bins) {                             //an array of floats

		if (recursion)
			System.err.println(" RECURSION ");
		nBins = bins.length;
		if (nBins == 0)
			return;

		recursion = true;

		if (size == null || nBins != size.height || nChunks != size.width)      // || this is the or operator
			createGraphics();                                                   //  Returns a Graphics2D object for rendering into the specified BufferedImage.

		int width = size.width;

		for (int i = 0; i < nBins; i++) {                                       //this is recursing over all the rows (nbins is the number of rows)
			int bin = nBins - i - 1;                                            //I think this makes it start from the top
			float val = mapper.eval(bins[bin]);                                 //

			if (val < 0)
				val = 0.0f;
			if (val > 1.0)                                                      // I think this next section colours the pixels in the spectrum
				val = 1.0f;                                                     // display
			int c_r = (int) (255 * val);
			int c_g = c_r;
			int c_b = 255 - c_r;

			int color = (c_b) + (c_g << 8) + (c_r << 16) + (0xFF << 24);        // << operator means signed left shift-- it shifts the bits in the binary code to the left creating a bigger number the operand on the right specifies the amount of shift
			screenBuffer[i * width + ptr] = (255 << 24) + color;                // I think loads the screenBuffer array. the index[i*width+ptr] is filled with the right operand
			// rgbarray[i] = color;
		}

		if (ptr % 1 == 0) {                                                     //ptr remainder(%) 1
			screenConverter.newPixels(ptr, 0, 1, nBins);                        //newPixels is method inside the MemoryImageSource i think it does this----Sends a rectangular region of the buffer of pixels to any ImageConsumers that are currently interested in the data for this image and notify them that an animation frame is complete.
			screenRepaint = true;
			repaint();
		}

		ptr = (ptr + 1) % size.width;                                           // i think this may help in changing the co-ordinates ptr may be the x coordinate
		recursion = false;
	}

//	 public void paintX(Graphics g) {
//	 if (screenRepaint) {
//	 g.drawImage(offscreen, 0, 0, this);
//	 screenRepaint = false;
//	 }
//	 }

    @Override
	public void paint(Graphics g) {
     //   super.paintComponent(g);
		if (!screenRepaint)
			return;
		// System.out.println(" Spectro DRAWIMAGE");

		int w = size.width - ptr;
		int h = size.height;

		g.drawImage(offscreen, 0, 0, w, h,   ptr, 0, size.width, h, this);              //this draws the image but im not sure how it knows what t draw there

		if (ptr != 0) {
			g.drawImage(offscreen, w, 0, size.width,      h,  0, 0, ptr, h, this);
		}
		
		screenRepaint = false;
	}

	public int getHeightXX() {
		if (size == null)
			return 200;
		return size.height;
	}

}
