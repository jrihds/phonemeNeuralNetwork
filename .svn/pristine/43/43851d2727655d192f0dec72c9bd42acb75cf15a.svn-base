package speech;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;

import com.sun.opengl.util.GLUT;

public class joglHelloWorld implements GLEventListener {

	public static void main(String[] args) {
		Frame frame = new Frame("JOGL HelloWorld");
		GLCanvas canvas = new GLCanvas();
		canvas.addGLEventListener(new joglHelloWorld());
		frame.add(canvas);
		frame.setSize(300, 300);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	public void init(GLAutoDrawable drawable) {
		
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
	}

	public void display(GLAutoDrawable drawable) {
	    GL gl = drawable.getGL();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glOrtho(-1.0, 1.0, -1.0, 1.0, -1.0, 1.0);
		gl.glBegin(GL.GL_POLYGON);
		gl.glVertex2f(-0.5f, -0.5f);
		gl.glVertex2f(-0.5f, 0.5f);
		gl.glVertex2f(0.5f, 0.5f);
		gl.glVertex2f(0.5f, -0.5f);
		gl.glEnd();
		gl.glFlush();
	}

	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
			boolean deviceChanged) {
	}

}