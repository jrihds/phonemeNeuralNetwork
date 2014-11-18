package speech.frontendGfx;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


class KeyHandler extends KeyAdapter {

	static boolean scrollPause = true;
	static boolean scrollTransparent = false;
	static int scrollSpeed = 15;
	static double targetNeuralOutputs[] = new double[6];
	static String text = "";

	public void keyReleased(KeyEvent e) {

		int kCode = e.getKeyCode();

		if (kCode == KeyEvent.VK_SPACE) {
			if (scrollPause)
				scrollPause = false;
			else
				scrollPause = true;
		}

		if (kCode == KeyEvent.VK_EQUALS) {
			scrollSpeed += 5;
		}

		if (kCode == KeyEvent.VK_MINUS) {
			scrollSpeed -= 5;
		}

		if (kCode == KeyEvent.VK_T) {
			if (scrollTransparent)
				scrollTransparent = false;
			else
				scrollTransparent = true;
		}
		
		if (kCode == KeyEvent.VK_A) {
			targetNeuralOutputs = new double[6];
			targetNeuralOutputs[0] = 1.0;
			text = "EEE";
		}
		
		if (kCode == KeyEvent.VK_S) {
			targetNeuralOutputs = new double[6];
			targetNeuralOutputs[1] = 1.0;
			text = "EHH";
		}
		
		if (kCode == KeyEvent.VK_D) {
			targetNeuralOutputs = new double[6];
			targetNeuralOutputs[2] = 1.0;
			text = "ERR";
		}
		
		if (kCode == KeyEvent.VK_F) {
			targetNeuralOutputs = new double[6];
			targetNeuralOutputs[3] = 1.0;
			text = "AHH";
		}
		
		if (kCode == KeyEvent.VK_G) {
			targetNeuralOutputs = new double[6];
			targetNeuralOutputs[4] = 1.0;
			text = "OOH";
		}
		
		if (kCode == KeyEvent.VK_H) {
			targetNeuralOutputs = new double[6];
			targetNeuralOutputs[5] = 1.0;
			text = "UHH";
		}
		
	}
}
