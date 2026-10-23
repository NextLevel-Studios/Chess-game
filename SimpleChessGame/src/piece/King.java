package piece;

import main.Gamepannel;
import main.Type;

public class King extends Piece{

	public King(int color, int col, int row) {
		super(color, col, row);
		
		type = Type.KING;
		
		if(color == Gamepannel.WHITE) {
			image = getImage("/piece/w-king");
		}else {
			image = getImage("/piece/b-king");
		}
	}

	
	public boolean canMove(int tragetCol, int tragetRow) {
		
		if(isWithinBoard(tragetCol, tragetRow)) {
			//movement 
			if(Math.abs(tragetCol - perCol) + Math.abs(tragetRow - perRow) == 1 || 
					Math.abs(tragetCol - perCol) * Math.abs(tragetRow - perRow) == 1) {
				if(isVaildSquare(tragetCol, tragetRow)) {
					return true;
				}	
			}
			
			//castling

			if(moved == false) {

				//right castling: the two squares next to the king must be empty
				if(tragetCol == perCol + 2 && tragetRow == perRow && isEmpty(perCol + 1, perRow) && isEmpty(perCol + 2, perRow)) {
					Piece rook = getPiece(perCol + 3, perRow);
					if(rook != null && rook.type == Type.ROOK && rook.color == color && rook.moved == false) {
						Gamepannel.castlingP = rook;
						return true;
					}
				}

				//left castling: the three squares between the king and the rook must be empty
				if(tragetCol == perCol - 2 && tragetRow == perRow && isEmpty(perCol - 1, perRow) && isEmpty(perCol - 2, perRow)
						&& isEmpty(perCol - 3, perRow)) {
					Piece rook = getPiece(perCol - 4, perRow);
					if(rook != null && rook.type == Type.ROOK && rook.color == color && rook.moved == false) {
						Gamepannel.castlingP = rook;
						return true;
					}
				}

			}
		}
		return false;
	}
}
