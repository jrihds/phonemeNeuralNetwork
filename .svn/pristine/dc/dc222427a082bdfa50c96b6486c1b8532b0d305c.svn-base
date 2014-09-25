package config;


import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.MidiDevice.Info;
import javax.swing.JOptionPane;

public class Config {

	private static final String CONFIG_FILE_NAME = "Config.xml";

	private static File userSpeechDir = new File(System.getProperty("user.home"), ".speech");
 
	private static File configFile = new File(userSpeechDir, CONFIG_FILE_NAME);
	
	private static Properties properties; // dynamic storage 
	
	public static float sampleRate=44100.0f;
	
	static { 
		properties = new Properties();
	
		if (!userSpeechDir.exists()) {
			System.out.println(" Creating speech user settings directory  " + userSpeechDir);
			if (!userSpeechDir.mkdir()) {
				System.err.println(" Failed to create " + userSpeechDir);
			}
		}
		
		try {
			reload();
		} catch (FileNotFoundException fnfe) {
			System.out.println("Can't find file " + configFile.getAbsolutePath() + ". It will be created when you quit the program or change configuration options.");
		} catch (Exception e) {
			System.err.println("error loading configuration. defaults will be used where possible.");
			e.printStackTrace();
		}		
	}
	

	
	public static Properties getProperties() {
		return properties;
	}
	
	public static String getProperty(String key) {
		return properties.getProperty(key);
	}
	
	public static void setProperty(String key, String value) {
		properties.setProperty(key, value);
	}

	
	public static void reload() throws IOException {
		InputStream r = new FileInputStream(configFile);
		load(r);
	}
	
	public static boolean store() {

        System.out.println(" Storing settings ");
		try {
			OutputStream w = new FileOutputStream(configFile);
			save(w);
			return true;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			JOptionPane.showConfirmDialog(null, "Error while saving configuration file.", "Error while saving.", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null);
			return false;
		}
	}
	
	public static void storeAndQuit() {
		if (!store()) {
			int answer = JOptionPane.showConfirmDialog(null, "Exit anyway?", "Error while saving.", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null);
			if (answer != JOptionPane.OK_OPTION) {
				return; // don't quit
			}
		}
		System.exit(0);
	}

	public static void load(InputStream r) throws IOException {
		properties = new Properties();
		properties.loadFromXML(r);
	
	}
	
	public static void save(OutputStream w) throws IOException {
		Properties p = new Properties();
		// copy all dynamic properties into
		for (Object key : properties.keySet()) {
			p.setProperty((String)key, properties.getProperty((String)key));
		}
	
		p.storeToXML(w, "Softsynth configuration");
	}
	
	public static Integer getIntProperty(String name){
		String n=getProperty(name);
		return Integer.parseInt(n);
		
	}


	public static Float getFloatProperty(String name){
		String n=getProperty(name);
		return Float.parseFloat(n);
		
	}


	
//	public static Object stringToValue(String prop, Class type) {
//		if (prop == null) return null;
//		
//		if (int.class.isAssignableFrom(type)) {
//			return Integer.parseInt(prop);
//		} else if (long.class.isAssignableFrom(type)) {
//			return Long.parseLong(prop);
//		} else if (double.class.isAssignableFrom(type)) {
//			return Double.parseDouble(prop);
//		} else if (float.class.isAssignableFrom(type)) {
//			return Float.parseFloat(prop);
//		} else if (boolean.class.isAssignableFrom(type)) {
//			return Boolean.parseBoolean(prop);
//		} else if (File.class.isAssignableFrom(type)) {
//			return new File(prop);
//		} else if (Font.class.isAssignableFrom(type)) {
//			return stringToFont(prop);
//		} else {
//			return prop;
//		}
//	}
	
	public static String valueToString(Object o, String name, Class type) {
		if (o == null) return null;
		if (File.class.isAssignableFrom(type)) {
			return ((File)o).getAbsolutePath() ;
		} else if (Font.class.isAssignableFrom(type)) {
				return  fontToString((Font)o);
		} else {
			return  o.toString();
		}
	}
	
	public static boolean isTrue(Object o) {
		if (o instanceof Boolean) {
			return ((Boolean)o).booleanValue();
		} else if (o instanceof Number) {
			return !(Math.abs(((Number)o).doubleValue()) < 0.000000001d);
		} else {
			String s = o.toString();
			s = s.trim().toLowerCase();
			return s.equals("true")||s.equals("yes");
		}
	}

	public static Font stringToFont(String s) {
		StringTokenizer st = new StringTokenizer(s, ",", false);
		String fontName = "Helvetica";
		String fontSizeStr = "12";
		String fontStyleStr = "plain";
		if (st.hasMoreTokens()) {
			fontName = st.nextToken();
			if (st.hasMoreTokens()) {
				fontSizeStr = st.nextToken();
				if (st.hasMoreTokens()) {
					fontStyleStr = st.nextToken();
				}
			}
		}
		int fontSize;
		try {
			fontSize = Integer.parseInt(fontSizeStr);
		} catch (NumberFormatException nfe) {
			fontSize = 12;
		}
		int fontStyle = 0;
		fontStyleStr = fontStyleStr.toLowerCase();
		if (fontStyleStr.indexOf("bold") != -1) {
			fontStyle |= Font.BOLD;
		}
		if (fontStyleStr.indexOf("italic") != -1) {
			fontStyle |= Font.ITALIC;
		}
		return new Font(fontName, fontStyle, fontSize);
	}
	
	public static String fontToString(Font font) {
		int fontStyle = font.getStyle();
		String fontStyleStr = "";
		if ((fontStyle&Font.BOLD) != 0) {
			fontStyleStr += "bold";
		}
		if ((fontStyle&Font.ITALIC) != 0) {
			fontStyleStr += "italic";
		}
		if (fontStyleStr.length() == 0) {
			fontStyleStr = "plain";
		}
		return font.getName()+","+font.getSize()+","+fontStyleStr;
	}
	
	public static String fileToString(File file) {
		String s;
		try {
			s = file.getCanonicalPath();
		} catch (IOException ioe) {
			s = file.getAbsolutePath();
		}
		return s;
	}


}
