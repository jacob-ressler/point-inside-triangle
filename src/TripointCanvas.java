import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

public class TripointCanvas extends Canvas {
	public enum Mode {DEVICE, LOGICAL}; // enumerator for the 2 coordinate modes
	private Mode mode = Mode.DEVICE; // current coordinate mode (default is device for this application)
	
	private final float LOGICAL_WIDTH  = 200f; // logical width is always from 0 to 200
	private final float LOGICAL_HEIGHT = 200f; // logical height is always from 0 to 200
	private float pixelsize; // isotropic pixel size
	
	private float[] xs = new float[4]; // x-coords of points
	private float[] ys = new float[4]; // y-coords of points
	private int clickCount = 0; // number of canvas clicks registered
	private JLabel text; // reference to text in frame
	private JLabel log; // reference to eventLog in frame
	private String[] pos = {"A: (???, ???)   ",
							"B: (???, ???)   ",
							"C: (???, ???)   ",
							"P: (???, ???)   "
							};
	
	public TripointCanvas() {
		super();
		
		this.setBackground(Color.white);
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (clickCount < xs.length) {
					xs[clickCount] = fx(e.getX());
					ys[clickCount] = fy(e.getY());
					
					System.out.printf("Mouse clicked at (%f, %f) [logical]\n", xs[clickCount], ys[clickCount]);
					System.out.printf("Mouse clicked at (%d, %d) [device]\n", ix(xs[clickCount]), iy(ys[clickCount]));
					clickCount++;
					
					switch (clickCount) {
					case 1:
						text.setText("Select point B of the triangle.");
						break;
					case 2:
						text.setText("Select point C of the triangle.");
						break;
					case 3:
						text.setText("Select any point P on the canvas.");
						break;
					
					default:
						break;
					}
					
					repaint();
				}
			}
		});
	}

	// clear the canvas and user input data
	public void clear() {
		for (int i = 0; i < xs.length; i++) {
			xs[i] = ys[i] = 0; 
		}
		
		clickCount = 0;
		text.setText("Select point A of the triangle.");
		String[] pos2 = {"A: (???, ???)   ",
						 "B: (???, ???)   ",
						 "C: (???, ???)   ",
						 "P: (???, ???)   "
					};
		pos = pos2;
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		initGraphics();
		updateLog();
		Graphics2D g2 = (Graphics2D)g;
		renderTriangle(g2);
		renderCross(g2);
		checkPointIsInside();
		g = g2;
	}

	
	private void initGraphics() {
		Dimension d = this.getSize();
		int maxX = d.width - 1; int maxY = d.height - 1;
		pixelsize = Math.max(LOGICAL_WIDTH / maxX, LOGICAL_HEIGHT / maxY);
		System.out.println("Pixel size ratio: " + pixelsize);
	}

	/* Update the log with the coordinates of A, B, C, and P */
	private void updateLog() {
		for (int i = 0; i < clickCount; i++) {
			pos[i] = pos[i].substring(0, 4);
			if (mode == Mode.LOGICAL)
				pos[i] += xs[i] + ", " + ys[i] + ")   ";
			else // mode == Mode.DEVICE
				pos[i] += ix(xs[i]) + ", " + iy(ys[i]) + ")   ";
		}
		
		String logstr = pos[0] + pos[1] + pos[2] + pos[3];
		System.out.println(logstr);
		log.setText(logstr);
	}
	
	/* Render the triangle (or as much as can be rendered from the current points) */
	private void renderTriangle(Graphics2D g) {
		int diam = 8; // ellipse diameter for points
		for (int i = 0; i < clickCount && i < 3; i++) {
			// draw a small circle at each point
			g.fillOval(ix(xs[i]) - diam/2, iy(ys[i]) - diam/2, diam, diam);			
		}
		
		if (clickCount >= 2) {
			// draw a line from first point to second point
			g.drawLine(ix(xs[0]), iy(ys[0]), ix(xs[1]), iy(ys[1]));
		}
		if (clickCount >= 3) {
			// draw a line from second point to third point and third point to first point
			g.drawLine(ix(xs[1]), iy(ys[1]), ix(xs[2]), iy(ys[2]));
			g.drawLine(ix(xs[2]), iy(ys[2]), ix(xs[0]), iy(ys[0]));
		}
		
	}
	
	/* Checks the position of the point P relative to the triangle ABC */
	private void checkPointIsInside() {
		if (clickCount < 4)
			return; // we don't have all our points yet
		
		Vector2 a = new Vector2(xs[0], ys[0]);
		Vector2 b = new Vector2(xs[1], ys[1]);
		Vector2 c = new Vector2(xs[2], ys[2]);
		Vector2 p = new Vector2(xs[3], ys[3]);
		
		// Since it is practically impossible to place point P exactly on one of the triangle
		// edges, we add some leeway to the check (0.5% of ABC's area). 
		float delta = Math.abs(Tools.area2(a, b, c) * 0.005f);
		
		System.out.println("Delta: " + delta);
		System.out.println("ABP: " + Tools.area2(a, b, p));
		System.out.println("BCP: " + Tools.area2(b, c, p));
		System.out.println("CAP: " + Tools.area2(c, a, p));
		
		// first check for P lying on any side of ABC
		if (Tools.area2(a, b, c) == 0)
			text.setText("Point P is not in ABC, and ABC is not a triangle.");
		else if (Tools.area2(a, b, p) <= delta && Tools.area2(a, b, p) >= -delta)
			text.setText("Point P lies on side AB of Triangle ABC.");
		else if (Tools.area2(b, c, p) <= delta && Tools.area2(b, c, p) >= -delta) 
			text.setText("Point P lies on side BC of Triangle ABC.");
		else if (Tools.area2(c, a, p) <= delta && Tools.area2(c, a, p) >= -delta) 
			text.setText("Point P lies on side AC of Triangle ABC.");
		else {	
			// now we know P doesn't lie on ABC, so check if it is inside ABC
			if (Tools.isCW(a, b, c) && Tools.isCW(a, b, p) && Tools.isCW(b, c, p) && Tools.isCW(c, a, p))
				text.setText("Point P is inside Triangle ABC.");
			else if (!Tools.isCW(a, b, c) && !Tools.isCW(a, b, p) && !Tools.isCW(b, c, p) && !Tools.isCW(c, a, p))
				text.setText("Point P is inside Triangle ABC.");
			else
				text.setText("Point P is outside Triangle ABC.");
		}
	}
	
				
			
	/* Render a simple red 'X' at the location of point P */
	private void renderCross(Graphics2D g) {
		if (clickCount < 4)
			return; // we don't have a point P yet
		
		g.setColor(Color.red);
		g.setStroke(new BasicStroke(2));
		g.drawLine(ix(xs[3] - 2), iy(ys[3] - 2), ix(xs[3] + 2), iy(ys[3] + 2));
		g.drawLine(ix(xs[3] - 2), iy(ys[3] + 2), ix(xs[3] + 2), iy(ys[3] - 2));
		g.setStroke(new BasicStroke(1));
		g.setColor(Color.black);
	}
	
		
	/* Get a reference to the text Component so it can be updated from here */
	public void textReference(JLabel text) {
		this.text = text;
	}
	
	/* Get a reference to the log Component so it can be updated from here */
	public void logReference(JLabel log) {
		this.log = log;
	}
	
	/* Set the Displayed Coordinate Mode */
	public void setMode(Mode m) {
		this.mode = m;
		repaint();
	}
	
	/* HELPER METHODS - Converting Between Logical and Device Coordinates */
	int ix(float x) { return Math.round(x / pixelsize); }
	int iy(float y) { return Math.round(getHeight() - (y / pixelsize)); }
	float fx(int x) { return x * pixelsize; }
	float fy(int y) { return (getHeight() - y) * pixelsize; }


}
