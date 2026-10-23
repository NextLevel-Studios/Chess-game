package piece;

import main.Gamepannel;
import main.Type;

public class Knight extends Piece {

	public Knight(int color, int col, int row) {
		super(color, col, row);
		
		type = Type.KNIGHT;
		
		if(color == Gamepannel.WHITE) {
			image = getImage("/piece/w-knight");
		}else {
			image = getImage("/piece/b-knight");
		}
	}
	
	public boolean canMove(int targetCol, int targetRow) {
		if(isWithinBoard(targetCol, targetRow)) {
			if(Math.abs(targetCol - perCol) * Math.abs(targetRow - perRow) == 2) {
				if(isVaildSquare(targetCol,targetRow)) {
					return true;
				}
			}
			
		}
		return false;
	}

}
