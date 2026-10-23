package piece;

import main.Gamepannel;
import main.Type;

public class Pawan extends Piece {

	public Pawan(int color, int col, int row) {
		super(color, col, row);
		
		type = Type.PAWN;
		
		if(color == Gamepannel.WHITE) {
			image = getImage("/piece/w-pawn");
		}else {
			image = getImage("/piece/b-pawn");
		}
	}

	public boolean canMove(int targetCol, int targetRow) {
		
		if(isWithinBoard(targetCol, targetRow) && isSameSquare(targetCol, targetRow) == false) {
			//define the move value base on its color 
			int moveValue;
			if(color == Gamepannel.WHITE) {
				moveValue = -1;
			}else {
				moveValue = 1;
			}
			
			//check the hittingP piece
			hittingP = getHittingP(targetCol,targetRow);
			
			// 1 square movement 
			if(targetCol == perCol && targetRow == perRow + moveValue && hittingP == null) {
				return true;
			}
			
			// 2 square movement
			if(targetCol == perCol && targetRow == perRow + moveValue*2 && hittingP == null && moved == false && 
					pieceIsOnStraightLine(targetCol, targetRow) == false) {
				return true;
			}
			
			// diagonal movement & capture 
			if(Math.abs(targetCol - perCol) == 1 &&  targetRow == perRow + moveValue && hittingP != null &&
					hittingP.color != color) {
				return true;
			}
			
			// En Passant rule
			if(Math.abs(targetCol - perCol) == 1 &&  targetRow == perRow + moveValue) {
				for(Piece piece : Gamepannel.simPieces) {
					if(piece.col == targetCol && piece.row == perRow && piece.twoStepeed == true) {
						hittingP = piece;
						return true;
					}
				}
			}
			
		}
		return false;
	}
	
}
