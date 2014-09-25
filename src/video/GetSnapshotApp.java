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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import au.edu.jcu.v4l4j.FrameGrabber;
import au.edu.jcu.v4l4j.CaptureCallback;
import au.edu.jcu.v4l4j.V4L4JConstants;
import au.edu.jcu.v4l4j.VideoDevice;
import au.edu.jcu.v4l4j.VideoFrame;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;

/**
 * This class creates a user interface to capture and display the current image
 * from a video device.
 * 
 * @author gilles
 * 
 * edit by JR
 * 
 */
public class GetSnapshotApp extends WindowAdapter implements CaptureCallback {
	private static int width, height, std, channel;
	private static String device;

	private JFrame frame;
	private JLabel label;
	private JButton buttonEEE;
	private JButton buttonAHH;
	private JButton buttonOOH;
	private JButton buttonERR;
	private JButton buttonCLEAR;
	
	private int count = 0;

	private VideoDevice videoDevice;
	private FrameGrabber frameGrabber;
	private VideoFrame lastVideoFrame;

	public static void main(String args[]) {
		device = (System.getProperty("test.device") != null) ? System
				.getProperty("test.device") : "/dev/video0";
		width = (System.getProperty("test.width") != null) ? Integer
				.parseInt(System.getProperty("test.width")) : 640;
		height = (System.getProperty("test.height") != null) ? Integer
				.parseInt(System.getProperty("test.height")) : 480;
		std = (System.getProperty("test.standard") != null) ? Integer
				.parseInt(System.getProperty("test.standard"))
				: V4L4JConstants.STANDARD_WEBCAM;
		channel = (System.getProperty("test.channel") != null) ? Integer
				.parseInt(System.getProperty("test.channel")) : 0;

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new GetSnapshotApp();
			}
		});

	}

	/**
	 * Start a new GetSnapshot UI
	 * 
	 * @throws V4L4JException
	 *             if there is a problem capturing from the given device
	 */
	public GetSnapshotApp() {
		lastVideoFrame = null;

		// initialise the video device and frame grabber
		try {
			initFrameGrabber();
		} catch (V4L4JException e) {
			System.err.println("Error setting up capture");
			e.printStackTrace();

			// cleanup and exit
			cleanupCapture();
			return;
		}

		// Create and initialise UI
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
	 * This method creates the VideoDevice and frame grabber. It enables push
	 * mode and starts the capture
	 * 
	 * @throws V4L4JException
	 */
	private void initFrameGrabber() throws V4L4JException {
		videoDevice = new VideoDevice(device);
		frameGrabber = videoDevice.getJPEGFrameGrabber(width, height, channel,
				std, 80);
		width = frameGrabber.getWidth();
		height = frameGrabber.getHeight();
		frameGrabber.setCaptureCallback(this);
	}

	/**
	 * This method builds the UI
	 */
	private void initGUI() {
		frame = new JFrame();
		frame.setLayout(new BoxLayout(frame.getContentPane(),
				BoxLayout.LINE_AXIS));

		label = new JLabel(width + " x " + height);
		label.setPreferredSize(new Dimension(width, height));
		label.setMaximumSize(new Dimension(width, height));
		label.setSize(new Dimension(width, height));
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		label.setAlignmentY(Component.CENTER_ALIGNMENT);

		buttonEEE = new JButton("EEE");
		buttonAHH = new JButton("AHH");
		buttonOOH = new JButton("OOH");
		buttonERR = new JButton("ERR");
		buttonCLEAR = new JButton("CLR");
		buttonEEE.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				getSnapshot("EEE");
			}
		});
		buttonAHH.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				getSnapshot("AHH");
			}
		});
		buttonOOH.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				getSnapshot("OOH");
			}
		});
		buttonERR.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				getSnapshot("ERR");
			}
		});
		buttonCLEAR.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				getSnapshot("CLEAR");
			}
		});

		frame.getContentPane().add(label);
		frame.getContentPane().add(Box.createGlue());
		frame.getContentPane().add(buttonEEE);
		frame.getContentPane().add(buttonAHH);
		frame.getContentPane().add(buttonOOH);
		frame.getContentPane().add(buttonERR);
		frame.getContentPane().add(buttonCLEAR);

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(this);

		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * This method is called when the Get snapshot button is hit and it displays
	 * the current video frame.
	 */
	private void getSnapshot(String filename) {
		VideoFrame lastFrameCopy = null;

		synchronized (this) {
			// If there is a current frame ...
			if (lastVideoFrame != null) {
				// Make a local copy
				lastFrameCopy = lastVideoFrame;

				// and set the class member to null so it does not get recycled
				// in nextFrame(), since we are going to recycle it
				// ourselves once drawn in the label.
				lastVideoFrame = null;
			}
		}

		// Draw the frame and recycle it
		if (lastFrameCopy != null) {
			label.getGraphics().drawImage(lastFrameCopy.getBufferedImage(), 0,
					0, width, height, null);
			label.getGraphics().setColor(new Color(255, 255, 255));
			// Draw Eye and Nose guide
			label.getGraphics().drawString("O", 285, 165);
			label.getGraphics().drawString("O", 355, 165);
			label.getGraphics().drawString("U", 320, 200);

			// Draw Mouth Guide
			label.getGraphics().drawRect(280, 216, 365-275, 300-220);
			
			try {
				BufferedImage data;
				data = lastFrameCopy.getBufferedImage();
			    File outputfile = new File("src/video/imagefiles/"+filename+".bmp");
			    count++;
			    ImageIO.write(data, "bmp", outputfile);
			} catch (IOException e) {
			}
			
			lastFrameCopy.recycle();
			
		}
	}

	/**
	 * This method stop the capture and releases the frame grabber and video
	 * device
	 */
	private void cleanupCapture() {
		// stop capture
		try {
			frameGrabber.stopCapture();
		} catch (Exception ex) {
			// frame grabber may be already stopped, so ignore this
		}

		// release frame grabber and video device
		try {
			videoDevice.releaseFrameGrabber();
			videoDevice.release();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void windowClosing(WindowEvent e) {
		cleanupCapture();
		frame.dispose();
	}

	@Override
	public void exceptionReceived(V4L4JException e) {
		label.setText(e.toString());
	}

	@Override
	public synchronized void nextFrame(VideoFrame frame) {
		// Recycle the previous frame if there is one
		if (lastVideoFrame != null)
			lastVideoFrame.recycle();

		// Store a pointer to this new frame
		lastVideoFrame = frame;
	}
}
