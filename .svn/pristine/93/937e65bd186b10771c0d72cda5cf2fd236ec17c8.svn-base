import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class PJL_makeFrames {

	// private c JFrame frame;

	public String text;
	public JR_drawTract draw = new JR_drawTract();
	public JR_drawLips draw_lips = new JR_drawLips();
	public PJL_AnalyserPanel meterPanel;

	public void makeTract() {
		JFrame f = new JFrame();
		f.add(draw);
		f.setVisible(true);
		f.setBackground(Color.WHITE);
		f.setSize(450, 650);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setBounds(0, 0, 2, 2);
	}

	public void makeLips() {
		JFrame f2 = new JFrame();
		f2.add(draw_lips);
		f2.setVisible(true);
		f2.setSize(448, 600);
		f2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f2.setBounds(450, 0, 2, 2);
	}

	public void makeAnalyser(int onscreen_bins) {
		JFrame frame = new JFrame();
		meterPanel = new PJL_AnalyserPanel();
		JPanel content = meterPanel;

		frame.setContentPane(content);
		frame.pack();
		frame.setVisible(true);
		frame.setBounds(0, 0, 2, 2);
	}

	public void updateFace(double[][][] vocalTract, double[][] outputs2,
			String text2, double[][][] lips_12, double[][][] lips_22,
			double[][] outputs3) {
		draw.vectorMean(vocalTract, outputs2, text2);
		draw_lips.vectorMean(lips_12, lips_22, outputs3);

	}

}
