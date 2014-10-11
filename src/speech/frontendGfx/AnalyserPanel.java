package speech.frontendGfx;

import java.awt.Color;
import javax.swing.JPanel;


public class AnalyserPanel extends JPanel{

	static final long serialVersionUID = 0L;
	public  int onscreen_bins = 128;
	public int outputs = 6;
	private MeterPanel meterArray[] = new MeterPanel[onscreen_bins]; 
	private MeterPanel OutputArray[] = new MeterPanel[outputs]; 
	   
	AnalyserPanel() {
		for (int i = 0; i < onscreen_bins; i++) {
			add(meterArray[i] = new MeterPanel());
		}
		for (int i = 0; i < outputs; i++) {
			add(OutputArray[i] = new MeterPanel());
		}
		
	}
	
	 public void updateMeters(double[] spectrum, int onscreen_bins, double[] outputs_in) {
		 for (int i=0; i<outputs; i++) {
			   if (outputs_in[i]<1) {
				   	OutputArray[i].updateMeter(outputs_in[i], Color.YELLOW);
		   		}
		   		else {
		   				OutputArray[i].updateMeter(1, Color.RED);
		   		}
		   }
		   for (int i=0; i<onscreen_bins; i++) {
			   if (spectrum[i]<1) {
		   			meterArray[i].updateMeter(spectrum[i], Color.CYAN);
		   		}
		   		else {
		   			meterArray[i].updateMeter(1, Color.RED);
		   		}
		   }
	   }
}
