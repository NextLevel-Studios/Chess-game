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
				
				//right castling
				if(tragetCol == perCol + 2 && tragetRow == perRow && pieceIsOnStraightLine(tragetCol, tragetRow) == false) {
					for(Piece piece : Gamepannel.simPieces) {
						if(piece.col == perCol + 3 && piece.row == perRow && piece.moved == false) {
							Gamepannel.castlingP = piece;
							return true;
						}
					}
				}
				
				//left castling
				if(tragetCol == perCol - 2 && tragetRow == perRow && pieceIsOnStraightLine(tragetCol, tragetRow) == false) {
					Piece p[] = new Piece[2];
					for(Piece piece : Gamepannel.simPieces) {
						if(piece.col == perCol - 3 && piece.row == tragetRow) {
							p[0] = piece;
						}
						if(piece.col == perCol - 4 && piece.row == tragetRow) {
							p[1] = piece;
						}
						if(p[0] == null && p[1] != null && p[1].moved == false) {
							Gamepannel.castlingP = p[1];
							return true;
						}
					}
				}
				
			}
		}
		return false;
	}
}
