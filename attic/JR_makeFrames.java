import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class JR_makeFrames {
	
   private static JFrame frame;
   public static int onscreen_bins = 128;
   private static MeterPanel meterArray[] = new MeterPanel[onscreen_bins]; 
   private static MeterPanel slah, slee, sleh, sloh, sloo;

   public static String text;
   public static JR_drawTract draw = new JR_drawTract();
   public static JR_drawLips draw_lips = new JR_drawLips();
   public static JR_drawGraph draw_graph = new JR_drawGraph();

   public JR_makeFrames() {
   }

	public static void updateMeters(double[] spectrum, int onscreen_bins, double[][] outputs) {
		   slah.updateMeter(outputs[0][2], Color.RED);
		   slee.updateMeter(outputs[1][2], Color.RED);
		   sleh.updateMeter(outputs[2][2], Color.RED);
		   sloh.updateMeter(outputs[3][2], Color.RED);
		   sloo.updateMeter(outputs[4][2], Color.RED);
		   for (int i=0; i<onscreen_bins; i++) {
			   if (spectrum[i]<1) {
		   			meterArray[i].updateMeter(spectrum[i], Color.CYAN);
		   		}
		   		else {
		   			meterArray[i].updateMeter(1, Color.CYAN);
		   		}
		   }
	   }
	   
   public static void makeTract() {
	   JFrame f = new JFrame();
	   f.add(draw);
	   f.setVisible(true);
	   f.setBackground(Color.WHITE);
	   f.setSize(450, 650);
	   f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   f.setBounds(0, 0, 2, 2);
   }
	   
   public static void makeLips() {
	   JFrame f2 = new JFrame();
	   f2.add(draw_lips);
	   f2.setVisible(true);
	   f2.setSize(448, 600);
	   f2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   f2.setBounds(450, 0, 2, 2);
   }
   
   public static void makeGraph() {
	   JFrame f3 = new JFrame();
	   f3.add(draw_graph);
       f3.addKeyListener(new KeyHandler());
	   f3.setVisible(true);
	   f3.setSize(1000, 600);
	   f3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   f3.setBounds(450, 0, 2, 2);
   }

   public static void makeAnalyser(int onscreen_bins) {
	   frame = new JFrame();
	   JPanel content = new JPanel();
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
       for (int i=0; i<onscreen_bins; i++) {
    	   content.add(meterArray[i]= new MeterPanel());
       }
       content.add(slah=new MeterPanel());
       content.add(slee=new MeterPanel());
       content.add(sleh=new MeterPanel());
       content.add(sloh=new MeterPanel());
       content.add(sloo=new MeterPanel());
       content.add(ah_button);
       content.add(ee_button);
       content.add(eh_button);
       content.add(oh_button);
       content.add(oo_button);
       content.add(nothing_button);
       
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
 	    draw_graph.updateGraph(outputs3, KeyHandler.speed, KeyHandler.trans, KeyHandler.play, text2);
	}

}

	class KeyHandler extends KeyAdapter {
		
		public static boolean play = true;
		public static boolean trans = false;
		public static int speed = 4;

		public void keyReleased(KeyEvent e) {
			int kcode = e.getKeyCode();
			if (kcode == KeyEvent.VK_SPACE) {
				if (play) play = false;
				else play = true;
			}
			if (kcode == KeyEvent.VK_EQUALS) {
				speed++;
			}
			if (kcode == KeyEvent.VK_MINUS) {
				speed--;
			}
			if (kcode == KeyEvent.VK_T) {
				if (trans) trans = false;
				else trans = true;
			}
		}
		
	}

	class ButtonListener implements ActionListener {
		  public void actionPerformed(ActionEvent ek) {
			  
			boolean append = false;
			  
		    if (ek.getActionCommand().equals("'ah'")) {
		    	System.out.println("ah has been clicked");
		    	try{
					  BufferedWriter out = new BufferedWriter(new FileWriter("textfiles/ah.txt", append));
					  for (int i=0; i<JR_MainApp.onscreen_bins; i++) {
						  out.write(JR_MainApp.smoothed[i]+"\n");
					  }
					  out.close();
				  } catch (IOException e) {}
		    	}
		    if (ek.getActionCommand().equals("'ee'")) {
		    	System.out.println("ee has been clicked");
		    	try{
					  BufferedWriter out = new BufferedWriter(new FileWriter("textfiles/ee.txt", append));
					  for (int i=0; i<JR_MainApp.onscreen_bins; i++) {
						  out.write(JR_MainApp.smoothed[i]+"\n");
					  }
					  out.close();
				  } catch (IOException e) {}
			    }
		    if (ek.getActionCommand().equals("'eh'")) {
			    System.out.println("eh has been clicked");
			    try{
			    	BufferedWriter out = new BufferedWriter(new FileWriter("textfiles/eh.txt", append));
					for (int i=0; i<JR_MainApp.onscreen_bins; i++) {
						out.write(JR_MainApp.smoothed[i]+"\n");
					}
					out.close();
				} catch (IOException e) {}
			}
		    if (ek.getActionCommand().equals("'oh'")) {
			    System.out.println("oh has been clicked");
			    try{
					BufferedWriter out = new BufferedWriter(new FileWriter("textfiles/oh.txt", append));
					for (int i=0; i<JR_MainApp.onscreen_bins; i++) {
						out.write(JR_MainApp.smoothed[i]+"\n");
					}
					out.close();
				} catch (IOException e) {}
			}
		    if (ek.getActionCommand().equals("'oo'")) {
			    System.out.println("oo has been clicked");
			    try{
					BufferedWriter out = new BufferedWriter(new FileWriter("textfiles/oo.txt", append));
					for (int i=0; i<JR_MainApp.onscreen_bins; i++) {
						out.write(JR_MainApp.smoothed[i]+"\n");
					}
					out.close();
				} catch (IOException e) {}
			  }
		    if (ek.getActionCommand().equals("'nothing'")) {
		    	System.out.println("nothing has been clicked");
		    	try{
					  BufferedWriter out = new BufferedWriter(new FileWriter("textfiles/silence.txt", append));
					  for (int i=0; i<JR_MainApp.onscreen_bins; i++) {
						  out.write(JR_MainApp.smoothed[i]+"\n");
					  }
					  out.close();
				  } catch (IOException e) {}
		    	}
		  }
		  
	}