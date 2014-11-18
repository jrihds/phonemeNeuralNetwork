package speech.frontendGfx;

//addComponent function from http://www.java-forums.org/
//
//@author JER

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import speech.dataAcquisition.ReadImage;

public class MainApplicationWindow {

	DrawVocalTract drawCurrentVocalTract;
	DrawVocalTract drawTargetVocalTract;
	
	DrawLips drawCurrentLipShape;
	DrawLips drawTargetLipShape;
	
	DrawScrollingPhonemeGraph drawScrollingPhonemeGraph;
	
	public DrawScrollingSpectrum drawScrollingSpectrum;
	DrawScrollingSpectrumHistogram drawScrollingSpectrumHistogram;

	private JFrame masterFrame;
	
	public double vocalTractContour[][][];
	public double innerLipContour[][][];
	public double outerLipContour[][][];

	public MainApplicationWindow(int phonemes, int onscreenBins) {
		drawCurrentVocalTract = new DrawVocalTract(phonemes);
		drawTargetVocalTract = new DrawVocalTract(phonemes);
		drawCurrentLipShape = new DrawLips(phonemes);
		drawTargetLipShape = new DrawLips(phonemes);
		drawScrollingPhonemeGraph = new DrawScrollingPhonemeGraph(phonemes);
		drawScrollingSpectrum = new DrawScrollingSpectrum(480);
		drawScrollingSpectrumHistogram = new DrawScrollingSpectrumHistogram(onscreenBins);
		
		ReadImage readImage = new ReadImage();
		try {
			vocalTractContour = readImage.readTract(); 				// Read in data from images of lip
			innerLipContour = readImage.readInnerLipContour(); 			//		and vocal tract shapes
			outerLipContour = readImage.readOuterLipContour();			// 		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void makeMaster() {

		JFrame frame = new JFrame("JR Speech Analysis Toolbox");
		frame.setSize(1320, 800);
		frame.setLayout(null);

		class MyPanel extends JPanel {

			/**
			 * 
			 */
			private Image offScreenImage;
			private Dimension screenSize;
			private Graphics offScreenGraphics;

			private static final long serialVersionUID = 1L;

			MyPanel() {
				setDoubleBuffered(true);
			}

			@Override
			public void paint(Graphics g) {
				if (offScreenGraphics == null || !getSize().equals(screenSize)) {
					if (getSize().width == 0)
						return;
					screenSize = new Dimension(getSize());
					offScreenImage = createImage(getSize().width, getSize().height);
					offScreenGraphics = offScreenImage.getGraphics();
					offScreenGraphics = offScreenImage.getGraphics();
					
				}
				
				super.paint(offScreenGraphics);
				
				g.drawImage(offScreenImage, 0, 0, this);
			}

		}

		JPanel content = new MyPanel();

		frame.setContentPane(content);
		content.setLayout(null);
		frame.addKeyListener(new KeyHandler());

		addComponent(content, drawCurrentLipShape, 680, 0, 320, 400);
		addComponent(content, drawCurrentVocalTract, 680, 400, 320, 400);
		addComponent(content, drawTargetLipShape, 1000, 0, 320, 400);
		addComponent(content, drawTargetVocalTract, 1000, 400, 320, 400);
		addComponent(content, drawScrollingPhonemeGraph, 0, 0, 680, 400);
		addComponent(content, drawScrollingSpectrum, 0, 400, 480, 400);
		addComponent(content, drawScrollingSpectrumHistogram, 480, 400, 200, 400);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		masterFrame = frame;

	}

	public void updateGfx(String text, double[] neuralNetOutputs, double[] audioSpectrum) {
		
		drawCurrentVocalTract.vectorMean(vocalTractContour, neuralNetOutputs, text);
		drawCurrentLipShape.vectorMean(innerLipContour, outerLipContour, neuralNetOutputs);
		
		drawScrollingPhonemeGraph.updateGraph(neuralNetOutputs, KeyHandler.scrollSpeed,
				KeyHandler.scrollTransparent, KeyHandler.scrollPause, text);
		drawScrollingSpectrumHistogram.update(audioSpectrum);
		masterFrame.repaint();

		drawTargetVocalTract.vectorMean(vocalTractContour, KeyHandler.targetNeuralOutputs, KeyHandler.text);
		drawTargetLipShape.vectorMean(innerLipContour, outerLipContour, KeyHandler.targetNeuralOutputs);
		
		drawScrollingSpectrum.notifyMoreDataReady(audioSpectrum);
		
	}

	private void addComponent(Container container, Component c, int x, int y,
			int width, int height) {
		c.setBounds(x, y, width, height);
		container.add(c);
	}

}
