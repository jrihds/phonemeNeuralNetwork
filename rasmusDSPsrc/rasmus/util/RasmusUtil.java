/* 
 * Copyright (c) 2006, Karl Helgason
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 *    1. Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    2. Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *    3. The name of the author may not be used to endorse or promote
 *       products derived from this software without specific prior
 *       written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package rasmus.util;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

final public class RasmusUtil {

	static Hashtable providercahce = new Hashtable();
	public static List getProviders(Class providerclass) {
		ArrayList providers = (ArrayList)providercahce.get(providerclass.getName());
		if(providers != null) return providers;
		providers = new ArrayList();
		try {
			Enumeration extenum = RasmusUtil.class.getClassLoader()
					.getResources(
							"META-INF/services/" + providerclass.getName());
			while (extenum.hasMoreElements()) {
				InputStream extstream = ((URL) extenum.nextElement())
						.openStream();
				BufferedReader r = new BufferedReader(new InputStreamReader(
						extstream));
				String line = r.readLine();
				while (line != null) {
					if (!line.startsWith("#")) {
						try {
							Class c = Class.forName(line.trim());
							Object o = c.newInstance();
							providers.add(o);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					line = r.readLine();
				}
				extstream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		providercahce.put(providerclass.getName(), providers);
		return providers;
	}

	public static double parseNumber(String number) {
		int li = number.indexOf("/");
		if (li == -1)
			return Double.parseDouble(number);
		return Double.parseDouble(number.substring(0, li))
				/ Double.parseDouble(number.substring(li + 1));
	}

	public static Color webColorCodeToJava(String value) {
		if (value.startsWith("#")) {
			value = value.substring(1);
			int red = Integer.parseInt(value.substring(0, 2), 16);
			int green = Integer.parseInt(value.substring(2, 4), 16);
			int blue = Integer.parseInt(value.substring(4, 6), 16);
			return new Color(red, green, blue);
		}

		return Color.WHITE;
	}

	public static void reimplantNewNodeText(Node node, String value) {

		if (node.getNodeType() == Node.ATTRIBUTE_NODE)
			node.setNodeValue(value);
		else if (node.getNodeType() == Node.CDATA_SECTION_NODE)
			node.setNodeValue(value);
		else if (node.getNodeType() == Node.TEXT_NODE)
			node.setNodeValue(value);
		else {
			NodeList nodelist = node.getChildNodes();
			Node[] childs = new Node[nodelist.getLength()];
			for (int i = 0; i < childs.length; i++) {
				childs[i] = nodelist.item(i);
			}
			for (int i = 0; i < childs.length; i++) {
				node.removeChild(childs[i]);
			}
			node.appendChild(node.getOwnerDocument().createTextNode(value));

		}

	}

	public static String getRelativeLink(String from, String to) {
		if (to.startsWith("/")) {
			return to;
		}

		int slashindex = from.lastIndexOf('/');
		if (slashindex != -1) {
			from = from.substring(0, slashindex);
		}

		while (to.startsWith("../")) {
			if (from.length() != 0) {
				slashindex = from.lastIndexOf('/');
				from = from.substring(0, slashindex);
			}
			to = to.substring(3);
		}

		return from + "/" + to;
	}

	public static String shortestReleativeLink(String from, String to) {
		if (from == null)
			return to;
		to = getRelativeLink(from, to);

		int laa = from.indexOf('/', 2);
		int lbb = to.indexOf('/', 2);
		if (laa != -1)
			if (lbb != -1) {
				if (laa != lbb)
					return to;
				if (!to.substring(0, laa + 1)
						.equals(from.substring(0, laa + 1))) {
					return to;
				}
			}

		if (to.startsWith("//"))
			to = to.substring(2);
		if (from.startsWith("//"))
			from = from.substring(2);

		if (from == null)
			return to;
		if (from.indexOf('/') == -1)
			return to;

		while (true) {
			int la = from.indexOf('/');
			int lb = to.indexOf('/');
			if (la == -1)
				break;
			if (lb == -1)
				break;
			if (la != lb) {
				break;
			}
			if (to.substring(0, la + 1).equals(from.substring(0, la + 1))) {
				to = to.substring(la + 1);
				from = from.substring(la + 1);
			} else {
				break;
			}
		}

		String prefix = "";
		while (true) {
			int lb = from.indexOf('/');
			if (lb == -1)
				break;
			prefix = "../";
			from = from.substring(lb + 1);
		}

		return prefix + to;
	}

	public static void deleteDirectory(File file) {
		try {
			File[] files = file.listFiles();
			if (files != null)
				for (int i = 0; i < files.length; i++) {
					deleteDirectory(files[i]);
				}
		} catch (Exception e) {
		}
		try {
			file.delete();

		} catch (Exception e) {
		}
	}

	public static String extractNodeText(Node node) {
		if (node.getNodeType() == Node.CDATA_SECTION_NODE)
			return node.getNodeValue();
		if (node.getNodeType() == Node.TEXT_NODE)
			return node.getNodeValue();
		NodeList nodelist = node.getChildNodes();
		String ctext = "";
		for (int i = 0; i < nodelist.getLength(); i++) {
			Node subnode = nodelist.item(i);
			ctext += extractNodeText(subnode);
		}
		return ctext;
	}

	public static String extractFileWithoutExtension(String filename) {
		try {
			int cutlen = extractFileExtension(filename).length();
			if (cutlen == 0)
				return filename;
			return filename.substring(0, filename.length() - cutlen - 1);
		} catch (Exception e) {
			return filename;
		}
	}

	public static String extractFileExtension(String filename) {
		int lastslash = filename.lastIndexOf("/");
		if (lastslash != -1)
			filename = filename.substring(lastslash + 1);
		lastslash = filename.lastIndexOf("\\");
		if (lastslash != -1)
			filename = filename.substring(lastslash + 1);
		int lastdot = filename.lastIndexOf(".");
		if (lastdot != -1)
			return filename.substring(lastdot + 1);
		else
			return "";
	}

	public static String encodeMusicNote(int note) {
		int base = note % 12;
		String s = "";
		switch (base) {
		case (0):
			s = "c";
			break;
		case (1):
			s = "c#";
			break;
		case (2):
			s = "d";
			break;
		case (3):
			s = "d#";
			break;
		case (4):
			s = "e";
			break;
		case (5):
			s = "f";
			break;
		case (6):
			s = "f#";
			break;
		case (7):
			s = "g";
			break;
		case (8):
			s = "g#";
			break;
		case (9):
			s = "a";
			break;
		case (10):
			s = "a#";
			break;
		case (11):
			s = "b";
			break;
		}
		s += Integer.toString(note / 12);
		return s;
	}

	public static int decodeMusicNote(String note) {
		int basevalue = 0;
		int octave = 0;
		for (int i = 0; i < note.length(); i++) {
			char ch = note.charAt(i);
			if (ch == 'C' || ch == 'c')
				basevalue += 0;
			if (ch == 'D' || ch == 'd')
				basevalue += 2;
			if (ch == 'E' || ch == 'e')
				basevalue += 4;
			if (ch == 'F' || ch == 'f')
				basevalue += 5;
			if (ch == 'G' || ch == 'g')
				basevalue += 7;
			if (ch == 'A' || ch == 'a')
				basevalue += 9;
			if (ch == 'B' || ch == 'b')
				basevalue += 11;
			if (ch == '#')
				basevalue += 1;
			if (ch >= '0' && ch <= '9') {
				octave *= 10;
				if (ch == '0')
					octave += 0;
				if (ch == '1')
					octave += 1;
				if (ch == '2')
					octave += 2;
				if (ch == '3')
					octave += 3;
				if (ch == '4')
					octave += 4;
				if (ch == '5')
					octave += 5;
				if (ch == '6')
					octave += 6;
				if (ch == '7')
					octave += 7;
				if (ch == '8')
					octave += 8;
				if (ch == '9')
					octave += 9;
			}
		}
		return basevalue + octave * 12;
	}

}
