package uk.ac.bath.tweaks.gui;

import uk.ac.bath.tweaks.Tweakable;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

public class SpinTweakerPanel extends JPanel implements ChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Tweakable t;

	SpinnerNumberModel model;

	public SpinTweakerPanel(Tweakable t) {
		this.t = t;

		model = new SpinnerNumberModel(t.getNumber(), t
				.getMinimum(), t.getMaximum(), t.getStepSize());
		final JSpinner spin = new JSpinner(model);
		// model = (SpinnerNumberModel)spin.getModel();
		// model.setValue(t.getNumber());
		// model.setMinimum((Comparable)t.getMinimum());
		// model.setMaximum((Comparable)t.getMaximum());

		spin.addChangeListener(this);
		add(new JLabel(t.getLabel()));
		add(spin);
		spin.getEditor().addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void mouseEntered(MouseEvent e) {
				System.out.println("N");

				spin.getEditor().requestFocusInWindow();

				// TODO Auto-generated method stub
				
			}

			public void mouseExited(MouseEvent e) {
				System.out.println("X");
				spin.getEditor().getRootPane().requestFocusInWindow();
				
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
		t.set(model.getNumber());
		model.setValue(t.getNumber());
	}
}
