/*
 * Copyright (C) 2011 Gilles Gigan (gilles.gigan@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public  License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package video;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import speech.NeuralNet;

import au.edu.jcu.v4l4j.FrameGrabber;
import au.edu.jcu.v4l4j.CaptureCallback;
import au.edu.jcu.v4l4j.V4L4JConstants;
import au.edu.jcu.v4l4j.VideoDevice;
import au.edu.jcu.v4l4j.VideoFrame;
import au.edu.jcu.v4l4j.exceptions.StateException;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;

/**
 * This class demonstrates how to perform a simple push-mode capture. It starts
 * the capture and display the video stream in a JLabel
 * 
 * @author gilles
 * 
 *         Edited by JR
 * 
 */
public class WebcamViewer extends WindowAdapter implements CaptureCallback {
	private static int width, height, std, channel;
	private static String device;

	private VideoDevice videoDevice;
	private FrameGrabber frameGrabber;

	private JLabel label;
	private JFrame frame;
	public int countPlay = 0;

	public static NeuralNet neuralNet;

	public static int xLeft = 275;
	public static int xRight = 365;
	public static int yTop = 220;
	public static int yBottom = 300;

	public int red[][] = new int[640][480];
	public int green[][] = new int[640][480];
	public int blue[][] = new int[640][480];
	public double outputs[] = new double[2];
	double networkInputs[] = new double[(xRight - xLeft) * (yBottom - yTop) * 3];

	public static void main(String args[]) {
		device = (System.getProperty("test.device") != null) ? System
				.getProperty("test.device") : "/dev/video0";
		width = (System.getProperty("test.width") != null) ? Integer
				.parseInt(System.getProperty("test.width")) : 320;
		height = (System.getProperty("test.height") != null) ? Integer
				.parseInt(System.getProperty("test.height")) : 240;
		std = (System.getProperty("test.standard") != null) ? Integer
				.parseInt(System.getProperty("test.standard"))
				: V4L4JConstants.STANDARD_WEBCAM;
		channel = (System.getProperty("test.channel") != null) ? Integer
				.parseInt(System.getProperty("test.channel")) : 0;

		FileInputStream ostr;
		try {
			ostr = new FileInputStream("src/video/textfiles/videoNetwork.txt");
			ObjectInputStream in = new ObjectInputStream(ostr);
			neuralNet = (NeuralNet) in.readObject();
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		// drawWC = new DrawWC();
		// frames = new MakeView(drawWC);
		// frames.makeWC();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new WebcamViewer();
			}
		});
	}

	/**
	 * Builds a WebcamViewer object
	 * 
	 * @throws V4L4JException
	 *             if any parameter if invalid
	 */
	public WebcamViewer() {
		// Initialise video device and frame grabber
		try {
			initFrameGrabber();
		} catch (V4L4JException e1) {
			System.err.println("Error setting up capture");
			e1.printStackTrace();

			// cleanup and exit
			cleanupCapture();
			return;
		}

		// create and initialise UI
		initGUI();

		// start capture
		try {
			frameGrabber.startCapture();
		} catch (V4L4JException e) {
			System.err.println("Error starting the capture");
			e.printStackTrace();
		}
	}

	/**
	 * Initialises the FrameGrabber object
	 * 
	 * @throws V4L4JException
	 *             if any parameter if invalid
	 */
	private void initFrameGrabber() throws V4L4JException {
		videoDevice = new VideoDevice(device);
		frameGrabber = videoDevice.getJPEGFrameGrabber(width, height, channel,
				std, 80);
		frameGrabber.setCaptureCallback(this);
		width = frameGrabber.getWidth();
		height = frameGrabber.getHeight();
		System.out.println("Starting capture at " + width + "x" + height);
	}

	/**
	 * Creates the UI components and initialises them
	 */
	private void initGUI() {
		frame = new JFrame();
		label = new JLabel();
		frame.getContentPane().add(label);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(this);
		frame.setVisible(true);
		frame.setSize(width, height);
	}

	/**
	 * this method stops the capture and releases the frame grabber and video
	 * device
	 */
	private void cleanupCapture() {
		try {
			frameGrabber.stopCapture();
		} catch (StateException ex) {
			// the frame grabber may be already stopped, so we just ignore
			// any exception and simply continue.
		}

		// release the frame grabber and video device
		videoDevice.releaseFrameGrabber();
		videoDevice.release();
	}

	/**
	 * Catch window closing event so we can free up resources before exiting
	 * 
	 * @param e
	 */
	public void windowClosing(WindowEvent e) {
		cleanupCapture();

		// close window
		frame.dispose();
	}

	@Override
	public void exceptionReceived(V4L4JException e) {
		// This method is called by v4l4j if an exception
		// occurs while waiting for a new frame to be ready.
		// The exception is available through e.getCause()
		e.printStackTrace();
	}

	@Override
	public synchronized void nextFrame(VideoFrame frame) {
		// This method is called when a new frame is ready.
		// Don't forget to recycle it when done dealing with the frame.

		// draw the new frame onto the JLabel
		label.getGraphics().drawImage(frame.getBufferedImage(), 0, 0, width,
				height, null);

		// Draw Eye and Nose guide
		label.getGraphics().drawString("O", 285, 165);
		label.getGraphics().drawString("O", 355, 165);
		label.getGraphics().drawString("U", 320, 200);

		// Draw Mouth Guide
		label.getGraphics().drawRect(280, 216, 365-275, 300-220);

		BufferedImage data;
		data = frame.getBufferedImage();
		for (int i = xLeft; i < xRight; i++) {
			for (int j = yTop; j < yBottom; j++) {
				red[i][j] = (data.getRGB(i, j) >> 16) & 0xff;
				green[i][j] = (data.getRGB(i, j) >> 8) & 0xff;
				blue[i][j] = (data.getRGB(i, j)) & 0xff;
			}
		}

		// Load Pixel values into Neural Net input array
		int count = 0;
		for (int i = xLeft; i < xRight; i++) {
			for (int j = yTop; j < yBottom; j++) {
				networkInputs[count] = red[i][j];
				count++;
				networkInputs[count] = green[i][j];
				count++;
				networkInputs[count] = blue[i][j];
				count++;
			}
		}

		outputs = neuralNet.forwardPass(networkInputs);

		if (outputs[0] > 0.7) {
			//System.out.print("EEE ");
			label.getGraphics().drawString("EEE", 500, 40);
			countPlay++;
		}
		if (outputs[1] > 0.7) {
			//System.out.print("AHH ");
			label.getGraphics().drawString("AHH", 500, 80);
			countPlay++;
		}
		if (outputs[2] > 0.7) {
			//System.out.print("OOH ");
			label.getGraphics().drawString("OOH", 500, 120);
			countPlay++;
		}
		if (outputs[3] > 0.7) {
			//System.out.print("EER ");
			label.getGraphics().drawString("EER", 500, 160);
			countPlay++;
		}
		if (countPlay == 26) {
			//System.out.println("");
			countPlay = 0;
		}

		// recycle the frame
		frame.recycle();
	}
}