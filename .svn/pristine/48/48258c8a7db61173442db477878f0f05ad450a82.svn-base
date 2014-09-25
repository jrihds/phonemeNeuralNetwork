import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JPanel;

public class PJL_AnalyserPanel extends JPanel{

	public  int onscreen_bins = 128;
	private MeterPanel meterArray[] = new MeterPanel[onscreen_bins]; 
	private MeterPanel signallevel, slah, slee, sleh, sloh, sloo;
	   
	PJL_AnalyserPanel() {
		JButton ah_button = new JButton("'ah'");
		JButton ee_button = new JButton("'ee'");
		JButton eh_button = new JButton("'eh'");
		JButton oh_button = new JButton("'oh'");
		JButton oo_button = new JButton("'oo'");
		JButton nothing_button = new JButton("'nothing'");
		ah_button.addActionListener(new ButtonListener());
		ee_button.addActionListener(new ButtonListener());
		eh_button.addActionListener(new ButtonListener());
		oh_button.addActionListener(new ButtonListener());
		oo_button.addActionListener(new ButtonListener());
		nothing_button.addActionListener(new ButtonListener());
		for (int i = 0; i < onscreen_bins; i++) {
			add(meterArray[i] = new MeterPanel());
		}
		add(signallevel = new MeterPanel());
		add(slah = new MeterPanel());
		add(slee = new MeterPanel());
		add(sleh = new MeterPanel());
		add(sloh = new MeterPanel());
		add(sloo = new MeterPanel());
		add(ah_button);
		add(ee_button);
		add(eh_button);
		add(oh_button);
		add(oo_button);
		add(nothing_button);
	}
	
	 public void updateMeters(double[] spectrum, int onscreen_bins, double[][] outputs) {
		   //signallevel.updateMeter(values[0][1], Color.YELLOW);
		   slah.updateMeter(outputs[0][1], Color.RED);
		   slee.updateMeter(outputs[1][1], Color.RED);
		   sleh.updateMeter(outputs[2][1], Color.RED);
		   sloh.updateMeter(outputs[3][1], Color.RED);
		   sloo.updateMeter(outputs[4][1], Color.RED);
		   for (int i=0; i<onscreen_bins; i++) {
			   if (spectrum[i]<1) {
		   			meterArray[i].updateMeter(spectrum[i], Color.CYAN);
		   		}
		   		else {
		   			meterArray[i].updateMeter(1, Color.CYAN);
		   		}
		   }
	   }
}
