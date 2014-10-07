/*
 * Created on Mar 20, 2007
 *
 * Copyright (c) 2006-2007 P.J.Leonard
 * 
 * http://www.frinika.com
 * 
 * This file is part of Frinika.
 * 
 * Frinika is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * Frinika is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Frinika; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package uk.ac.bath.audio.gui;

//import com.frinika.global.FrinikaConfig;
import uk.ac.bath.audio.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.Timer;

import uk.ac.bath.tweaks.gui.TweakerPanel;
import uk.ac.bath.tweaks.Tweakable;
import uk.ac.bath.tweaks.TweakableDouble;

import uk.org.toot.audio.core.AudioProcess;
import uk.ac.bath.audio.Config;


/**
 * 
 * TOp level panel for the audioanalysis GUI
 * 
 * @author pjl
 * 
 */

public class CyclicBufferFFTAnalysisPanel extends JPanel {

	Vector<Tweakable> tweaks = new Vector<Tweakable>();

	TweakableDouble mindB = new TweakableDouble(tweaks, -400.0, 100.0, -40.0,   //Creates the mindB button properties
			5.0, "minDb");

	TweakableDouble maxdB = new TweakableDouble(tweaks, -400.0, 100.0, -50.0,   //Creates the maxdB button properties
			5.0, "maxDb");

	JToggleButton linearBut;                                                    //Defines toggle button linearBut

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// AudioAnalysisTimePanel timePanel;

	// SingleImagePanel spectroSlicePanel;

	AudioProcess reader;

	private ValMapper valMapper;                                                //ValMapper is later on

	// private Mapper freqMapper;

	CyclicSpectrogramImage image;                                               //CyclicSpectrumImage is another class within the project
                                                                                //image is an instance of this class
	CyclicBufferFFTSpectrogramDataBuilder spectroData;                          //CyclicBufferFFTSpectrogramDataBuilder within the project
                                                                                //spectroData is an instance of this class
	// SpectrumController spectroController;
	JPanel spectroPanel;                                                        //spectroPanel is an instance of jpanel

	// ' private KeyboardFocusManager kbd;

	FFTOption fftOpts[] = { new FFTOption(512, 256), new FFTOption(512, 128),   // FFTOption is a class later on inside this class
			new FFTOption(1024, 512), new FFTOption(1024, 256),                 // I think creates an array of options of fftsize and chunksize
			new FFTOption(1024, 128), new FFTOption(2048, 1024),                //FFTsize and chunksize are things you can change on the GUI
			new FFTOption(2048, 512), new FFTOption(2048, 256) };               //FFTsize is probably how big you want the FFT to be

	private float maxFreq;

	public CyclicBufferFFTAnalysisPanel(final CycliclyBufferedAudio bp) {

		setLayout(new BorderLayout());

        valMapper = new ValMapper();
		maxdB.addObserver(valMapper);                                           //A class implements the observer class when it wants to be informed
		mindB.addObserver(valMapper);                                           //of changes in observable objects.I assume that these lines make these
                                                                                //objects observable by the class valMapper

		spectroData = new CyclicBufferFFTSpectrogramDataBuilder(bp.out, 1000,Config.sampleRate); // Creates an instance of this class
		// spectroController=new
		// FFTSpectrumController((FFTSpectrogramControlable) spectroData);
		spectroData.setParameters(fftOpts[0].chunkSize, fftOpts[0].fftSize);    // sets the chunksize and fftsize of spectroData to the 1st value in the fftoptions array
		
		final JComboBox combo = new JComboBox(fftOpts);                         //creates a combo box that contains the fftOptions array

		combo.addActionListener(new ActionListener() {                          //add listener to the combo box combo

			public void actionPerformed(ActionEvent arg0) {
				final FFTOption opt = (FFTOption) combo.getSelectedItem();      //gets the item that was selected from the combo box
				System.err.println(" xxxxxxxxxxxxxx ");
				new Thread(new Runnable() {
					public void run() {
						spectroData.setParameters(opt.chunkSize, opt.fftSize);  //sets the parameters of spectroData to the values selected by the user
                        revalidate();
					}
				}).start();

			}

		});

    	image = new CyclicSpectrogramImage(valMapper,800);                      //has the arguements (Mapper Mapper, int nchunks)

       // JLabel p=new JLabel(" HELKP ME WORK ");
        JScrollPane sp=new JScrollPane(image);                                  //sets up the scroll panel with image contained i think this contains the
                                                                                // the moving spectrograph
		spectroData.addSizeObserver(image);                                     //

		add(sp, BorderLayout.CENTER);                                           //adds the scrollpane sp to the center of...notsure what its added to

		JPanel buts = new JPanel();                                             //Creates a new panel called buts
		buts.setLayout(new BoxLayout(buts, BoxLayout.Y_AXIS));                  //gives the buts panel a box layout sets the buts panel to the y.axis

		// Log linear switch
		linearBut = new JToggleButton("linear");                                //Creates a new toggle button
		linearBut.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {                     //when button is pressed the text toggles from linear and Log10
				if (!linearBut.isSelected()) {
					linearBut.setText("Linear");
				} else {
					linearBut.setText("Log10");
				}
				valMapper.update(null, null);
			}
		});
		buts.add(linearBut);                                                    //adds toggle button linearBut to buts panel

		// Switch to static synth
		final JToggleButton synthBut = new JToggleButton("Synth Mode");         //Creates a toggle button synthBut
		synthBut.addActionListener(new ActionListener() {                       //adds listener

			public void actionPerformed(ActionEvent arg0) {
				// timePanel.setSynthMode(synthBut.isSelected());               //not sure what this action event does maybe nothing
			}
		});
		buts.add(synthBut);                                                     //adds synthBut to buts panel

		TweakerPanel tpanel = new TweakerPanel(2, 4);                           //Creates an instance of tweakerPanel tpanel

		for (Tweakable t : tweaks) {
			tpanel.addSpinTweaker(t);
		}

		JPanel control = new JPanel();                                          //Creates a panel called control

		control.add(buts);                                                      //adds buts   panel to control panel
		control.add(tpanel);                                                    //adds tpanel panel to control panel
		control.add(combo); // spectroController.getTweakPanel());              //adds combobox combo to control panel

		add(control, BorderLayout.SOUTH);                                       //adds control to the south

		valMapper.update(null, null);

		// spectroController.update();
		revalidate();                                                           //revalidate - relayout the component (which can cause a repaint as well)
		repaint();                                                              //You need to call revalidate if you change the layout dynamically.

		final JLabel label=new JLabel("            ");;                         //Creates a label called label
		control.add(label);                                                     //adds the label to the control panel
		new Timer(200, new ActionListener() {                                   //200ms delay between events
			public void actionPerformed(ActionEvent arg0) {
				label.setText(String.valueOf(bp.getLag()));                     //sets the text of label--not sure what bp
			}

		}).start();
	}

	// public void dispose() {
	// timePanel.dispose();
	// }

	final class ValMapper implements Observer, Mapper {

		double maxdb;

		double mindb;

		double max;

		double min;

		boolean linear;

		private Thread thread;

		public final float eval(float val) {                                    //this a value returning method. It is passed val from somewhere
			if (linear) {                                                       //there is a linear button
				float vv = (float) ((val - min) / (max - min));                 //not sure what vv is
				return vv;
			} else {
				double dB = 20 * Math.log10(val + 1e-15);
				float vv = (float) ((dB - mindb) / (maxdb - mindb));
				return vv;
			}
		}

		public void update(Observable o, Object arg) {                          //This method is called whenever the observed object is changed.
                                                                                //An application calls an Observable object's notifyObservers method
			linear = linearBut.isSelected();                                    //to have all the object's observers notified of the change.

			maxdb = maxdB.doubleValue();
			max = Math.pow(10, maxdb / 20.0);

			mindb = mindB.doubleValue();
			min = Math.pow(10, mindb / 20.0);

			repaint();
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

}
class FFTOption {
	int fftSize;

	int chunkSize;

	public FFTOption(int i, int j) {
		fftSize = i;
		chunkSize = j;
	}

	public String toString() {
		return fftSize + "/" + chunkSize;
	}
}