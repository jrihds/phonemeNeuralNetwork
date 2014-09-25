package uk.ac.bath.tweaks.gui;
import uk.ac.bath.tweaks.Tweakable;
import java.awt.event.*;

import javax.swing.*;

class TextTweaker  implements ActionListener {

    Tweakable t;
    JTextField textField;


    TextTweaker(TweakerPanel p,Tweakable t) {
	this.t=t;
	int len = t.getMaximum().toString().length();
	textField = new JTextField(String.valueOf(t.getNumber()),len);
	textField.addMouseListener(new MouseListener(){

		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseEntered(MouseEvent e) {
			textField.requestFocusInWindow();

			// TODO Auto-generated method stub
			
		}

		public void mouseExited(MouseEvent e) {
			textField.getRootPane().requestFocusInWindow();
			
		}

		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	});
	
	textField.addActionListener(this);
	p.add(new JLabel(t.getLabel()),textField);
    }


    public void actionPerformed(ActionEvent e) {

	//	Object o=e.getSource();
	//	Object v = ((JFormattedTextField)o).getValue();
	t.set(textField.getText()); //.toString());
	textField.setText(t.toString());

    }
	

}
