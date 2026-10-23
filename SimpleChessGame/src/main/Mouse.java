package main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Mouse extends MouseAdapter {
	
	public int x,y;
	public int rawX, rawY; // window coordinates, used by the start menu
	public boolean pressed;
	
	@Override
	public void mousePressed(MouseEvent e) {
		pressed = true;
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		pressed = false;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		setPosition(e);
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		setPosition(e);
	}
	
	// convert the 3D board view back to flat game coordinates, the side panel stays as it is
	private void setPosition(MouseEvent e) {
		rawX = e.getX();
		rawY = e.getY();
		if(e.getX() < Board.FLAT_SIZE + 50) {
			double flatY = Board.unprojY(e.getY());
			x = (int)Board.unprojX(e.getX(), flatY);
			y = (int)flatY;
		}else {
			x = e.getX();
			y = e.getY();
		}
	}
}
