package uk.ac.bath.tweaks.gui;

import uk.ac.bath.tweaks.Tweakable;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

public class SpinTweaker extends JSpinner implements ChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Tweakable t;

	//SpinnerNumberModel model;

	public SpinTweaker(Tweakable t) {
		super(new SpinnerNumberModel(t.getNumber(), t
				.getMinimum(), t.getMaximum(), t.getStepSize()));
		this.t = t;

	
		addChangeListener(this);
		
		
		getEditor().addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void mouseEntered(MouseEvent e) {
				System.out.println("N");

				getEditor().requestFocusInWindow();

				// TODO Auto-generated method stub
				
			}

			public void mouseExited(MouseEvent e) {
				System.out.println("X");
				getEditor().getRootPane().requestFocusInWindow();
				
			}

			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
	}
	
//	public SpinTweaker(TweakerPanel p, Tweakable t) {
//		this.t = t;
//
//		model = new SpinnerNumberModel(t.getNumber(), (Comparable) t
//				.getMinimum(), (Comparable) t.getMaximum(), t.getStepSize());
//		JSpinner spin = new JSpinner(model);
//		// model = (SpinnerNumberModel)spin.getModel();
//		// model.setValue(t.getNumber());
//		// model.setMinimum((Comparable)t.getMinimum());
//		// model.setMaximum((Comparable)t.getMaximum());
//
//		spin.addChangeListener(this);
//
//		p.add(new JLabel(t.getLabel()), spin);
//	}

	public void stateChanged(ChangeEvent e) {
		
		t.set(((SpinnerNumberModel)getModel()).getNumber());
		getModel().setValue(t.getNumber());
	}
}
