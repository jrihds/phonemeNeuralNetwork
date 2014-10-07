package uk.ac.bath.tweaks.gui;
import uk.ac.bath.tweaks.Tweakable;


import javax.swing.*;
import javax.swing.event.*;

class SlideTweaker implements ChangeListener {

    
    JSlider slider;
    Tweakable t;

    SlideTweaker(TweakerPanel p,Tweakable t) {
	this.t=t;
	slider = new JSlider(((Number)t.getMinimum()).intValue(),
			     ((Number)t.getMaximum()).intValue(),
			     t.getNumber().intValue());
	slider.addChangeListener(this);
	p.add(new JLabel(t.getLabel()),slider);
    }


    public void stateChanged(ChangeEvent e) {
	t.set(new Integer(slider.getValue()));
    }
}
