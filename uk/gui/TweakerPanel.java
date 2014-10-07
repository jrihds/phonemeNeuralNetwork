package uk.ac.bath.tweaks.gui;

import java.awt.*;
import javax.swing.*;

import uk.ac.bath.tweaks.Tweakable;

public class TweakerPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	GridBagConstraints c;

	private int rows;

	private int cols;
	private int minWidth;
	
	public TweakerPanel(int rows, int cols) {
		setLayout(new GridBagLayout());
		this.rows = rows;
		this.cols = cols;
		c = new GridBagConstraints();
		c.ipadx = 2;
		c.ipady = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
	}

	
//	public TweakerPanel(int rows, int cols,int minWidth) {
//		setLayout(new GridBagLayout());
//		this.rows = rows;
//		this.cols = cols;
//		c = new GridBagConstraints();
//		c.ipadx = 2;
//		c.ipady = 1;
//		c.gridy = 0;
//		c.fill = GridBagConstraints.HORIZONTAL;
//		c.weightx = 1.0;
//		c.weighty = 1.0;
//		c.gridx = 0;
//		c.gridy = 0;
//		this.minWidth=minWidth;
//	}

	// public void add(JComponent label, JComponent cntrl) {
	// add(label, c);
	// c.gridx++;
	// add(cntrl, c);
	// c.gridx++;
	// }
	//	

	public void newRow() {
		c.gridy++;
		c.gridx = 0;
	}

	public void addSpinTweaker(Tweakable tweak) {
		add(new SpinTweakerPanel(tweak),c);
		c.gridx++;
		if (c.gridx >= cols) newRow();
	}
	
	public  void addComponent(JComponent comp) {
		add(comp,c);
		c.gridx++;
		if (c.gridx >= cols) newRow();
	}

}
