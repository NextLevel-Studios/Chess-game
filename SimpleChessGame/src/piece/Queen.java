package piece;

import main.Gamepannel;
import main.Type;

public class Queen extends Piece{

	public Queen(int color, int col, int row) {
		super(color, col, row);
		
		type  = Type.QUEEN;
		
		if(color == Gamepannel.WHITE) {
			image = getImage("/piece/w-queen");
		}else {
			image = getImage("/piece/b-queen");
		}
	}

	
	public boolean canMove(int targetCol, int targetRow) {
		
		if(isWithinBoard(targetCol, targetRow) && isSameSquare(targetCol, targetRow) == false) {
			
			//vertical & horizontal
			if(targetCol == perCol || targetRow == perRow) {
				if(isVaildSquare(targetCol,targetRow) && pieceIsOnStraightLine(targetCol,targetRow) == false) {
					return true;
				}
			}
			//diagonal
			if(Math.abs(targetCol - perCol) == Math.abs(targetRow - perRow)) {
				if(isVaildSquare(targetCol,targetRow) && pieceIsOnDiagonalLine(targetCol,targetRow) == false) {
					return true;
				}
			}
		}
		return false;
	}
}
