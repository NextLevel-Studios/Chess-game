package main;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class Board {

	final int MAX_COL = 8;
	final int MAX_ROW = 8;
	public static final int SQUARE_SIZE = 100;
	public static final int HALF_SQUARE_SIZE = SQUARE_SIZE/2;
	
	// board size in flat (game logic) coordinates
	public static final int FLAT_SIZE = SQUARE_SIZE * 8;
	
	// 3D view settings: the board is tilted back in perspective
	public static final int TOP = 50;        // screen y of the far edge
	public static final int HEIGHT_3D = 690; // screen height of the board
	public static final double TILT = 0.5;   // how strongly the far edge shrinks
	public static final int THICKNESS = 24;  // front face height of the board
	
	// scale of the board at flat y (1.0 at the near edge)
	public static double scaleAt(double flatY) {
		double u = flatY / FLAT_SIZE;
		return 1.0 / (1 + TILT - TILT * u);
	}
	public static int projX(double flatX, double flatY) {
		return (int)Math.round(FLAT_SIZE/2 + (flatX - FLAT_SIZE/2) * scaleAt(flatY));
	}
	public static int projY(double flatY) {
		double u = flatY / FLAT_SIZE;
		return (int)Math.round(TOP + HEIGHT_3D * u * scaleAt(flatY));
	}
	// screen -> flat coordinates (used for the mouse)
	public static double unprojY(int screenY) {
		double v = Math.min(1, Math.max(0, (screenY - TOP) / (double)HEIGHT_3D));
		double u = v * (1 + TILT) / (1 + TILT * v);
		return u * FLAT_SIZE;
	}
	public static double unprojX(int screenX, double flatY) {
		return FLAT_SIZE/2 + (screenX - FLAT_SIZE/2) / scaleAt(flatY);
	}
	
	private static Polygon squarePolygon(int col, int row) {
		int x0 = col * SQUARE_SIZE, x1 = x0 + SQUARE_SIZE;
		int y0 = row * SQUARE_SIZE, y1 = y0 + SQUARE_SIZE;
		Polygon p = new Polygon();
		p.addPoint(projX(x0, y0), projY(y0));
		p.addPoint(projX(x1, y0), projY(y0));
		p.addPoint(projX(x1, y1), projY(y1));
		p.addPoint(projX(x0, y1), projY(y1));
		return p;
	}
	
	// fill one board square with the current color
	public static void fillSquare(Graphics2D g2, int col, int row) {
		g2.fillPolygon(squarePolygon(col, row));
	}
	
	public void draw(Graphics2D g2) {
		
		// table behind the board
		g2.setPaint(new GradientPaint(0, 0, new Color(40,40,48), 0, 800, new Color(15,15,20)));
		g2.fillRect(0, 0, FLAT_SIZE, 800);
		
		// front and side faces of the board
		int bottom = projY(FLAT_SIZE);
		int topLeft = projX(0, 0), topRight = projX(FLAT_SIZE, 0);
		g2.setColor(new Color(70,40,20));
		g2.fillRect(0, bottom, FLAT_SIZE, THICKNESS);
		g2.setColor(new Color(100,60,30));
		g2.fillRect(0, bottom, FLAT_SIZE, 5);
		g2.setColor(new Color(55,30,15));
		g2.fillPolygon(new int[]{0, topLeft, topLeft, 0}, new int[]{bottom, projY(0), projY(0) - 10, bottom - 10}, 4);
		g2.fillPolygon(new int[]{FLAT_SIZE, topRight, topRight, FLAT_SIZE}, new int[]{bottom, projY(0), projY(0) - 10, bottom - 10}, 4);
		
		int c = 0 ;
		for(int row=0; row<MAX_ROW; row++) {
			for(int col=0; col<MAX_COL; col++) {
				if(c==0) {
					g2.setColor(new Color(210,165,125));
					c = 1;
				}
				else {
					g2.setColor(new Color(175,115,70));
					c = 0;
				}
				fillSquare(g2, col, row);
			}
			
			if(c == 0) {
				c = 1;
			}else {
				c=0;
			}
		}
	}
}
