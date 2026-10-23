package piece;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

import main.Board;
import main.Gamepannel;
import main.Type;

public class Piece {
    
	public Type type;
    public BufferedImage image;
    public int x, y;
    public int col, row, perCol, perRow; // previous column and row  
    public int color; 
    public Piece hittingP;
    public boolean moved, twoStepeed;
    
    public Piece(int color, int col, int row) {
        this.color = color;
        this.col = col; 
        this.row = row;
        x = getX(col);
        y = getY(row);
        perCol = col;
        perRow = row;
    }
    
    public BufferedImage getImage(String imagePath) {
        BufferedImage image = null;
        try {
            InputStream is = Piece.class.getResourceAsStream(imagePath + ".png");
            if (is == null) {
                System.err.println("❌ Could not find image: " + imagePath + ".png");
                return null;
            }
            image = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    
    public int getX(int col) {
        return col * Board.SQUARE_SIZE;
    }
    public int getY(int row) {
        return row * Board.SQUARE_SIZE;
    }
    public int getCol(int x) {
    	return (x + Board.HALF_SQUARE_SIZE)/Board.SQUARE_SIZE;
    }
    public int getRow(int y) {
    	return (y + Board.HALF_SQUARE_SIZE)/Board.SQUARE_SIZE;
    }
    public int getIndex() {
    	for(int index = 0; index<Gamepannel.simPieces.size(); index++) {
    		if(Gamepannel.simPieces.get(index) == this) {
    			return index;
    		}
    	}
    	return 0;
    }
    public void updatePostion() {
    	
    	// To check En Passant
    	if(type == Type.PAWN) {
    		if(Math.abs(row - perRow) == 2) {
    			twoStepeed = true;
    		}
    	}
    	
    	
    	
    	x = getX(col);
    	y = getY(row);
    	perCol = getCol(x);
    	perRow = getRow(y);
    	moved = true;
    }
    public void resetPosition() {
    	col = perCol;
    	row = perRow;
    	x = getX(col);
    	y = getY(row);
    }
    public boolean canMove(int tragetCol, int tragetRow) {
    	return false;
    }
    public boolean isWithinBoard(int targetCol, int targetRow) {
    	if(targetCol >= 0 && targetCol <= 7 && targetRow >= 0 && targetRow <= 7) {
    		return true;
    	}
    	return false;
    }
    public boolean isSameSquare(int targetCol, int targetRow) {
    	if(targetCol == perCol && targetRow == perRow) {
    		return true;
    	}
    	return false;
    }
    public Piece getHittingP(int targetCol, int targetRow) {
    	for(Piece piece : Gamepannel.simPieces) {
    		if(piece.col == targetCol && piece.row == targetRow && piece != this) {
    			return piece;
    		}
    	}
    	return null;
    }
    public boolean isVaildSquare(int targetCol, int targetRow) {
    	hittingP = getHittingP(targetCol, targetRow);
    	if(hittingP ==null) { //this square is can't
    		return true;
    	}else { //this square is occupied
    		if(hittingP.color != this.color) { // if the color is different, it can be captured
    			return true;
    		}else {
    			hittingP = null;
    		}
    	}
    	
    	return false;
    }
    public boolean pieceIsOnStraightLine(int targetCol, int targetRow) {
    	
    	//when this piece is moving to the left
    	for(int c = perCol-1; c>targetCol; c--) {
    		for(Piece piece : Gamepannel.simPieces) {
    			if(piece.col == c && piece.row == targetRow) {
    				hittingP = piece;
    				return true;
    			}
    		}
    	}
    	//when this piece is moving to the right
    	for(int c = perCol+1; c<targetCol; c++) {
    		for(Piece piece : Gamepannel.simPieces) {
    			if(piece.col == c && piece.row == targetRow) {
    				hittingP = piece;
    				return true;
    			}
    		}
    	}
    	//when this piece is moving  up
    	for(int r = perRow-1; r>targetRow; r--) {
    		for(Piece piece : Gamepannel.simPieces) {
    			if(piece.col == targetRow && piece.row == r) {
    				hittingP = piece;
    				return true;
    			}
    		}
    	}
    	//when this piece is moving  down
    	for(int r = perRow+1; r<targetRow; r++) {
    		for(Piece piece : Gamepannel.simPieces) {
    			if(piece.col == targetRow && piece.row == r) {
    				hittingP = piece;
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    public boolean pieceIsOnDiagonalLine(int targetcol, int targetRow) {
    	
    	if(targetRow < perRow) {
    		//up left
    		for(int c = perCol-1; c > targetcol; c--) {
    			int diff = Math.abs(c - perCol);
    			for(Piece piece : Gamepannel.simPieces) {
    				if(piece.col == c && piece.row == perRow - diff) {
    					hittingP = piece;
    					return true;
    				}
    			}
    		}
    		
    		//up right
    		for(int c = perCol+1; c < targetcol; c++) {
    			int diff = Math.abs(c - perCol);
    			for(Piece piece : Gamepannel.simPieces) {
    				if(piece.col == c && piece.row == perRow - diff) {
    					hittingP = piece;
    					return true;
    				}
    			}
    		}
    	}
    	if(targetRow > perRow) {
    		//down left
    		for(int c = perCol-1; c > targetcol; c--) {
    			int diff = Math.abs(c - perCol);
    			for(Piece piece : Gamepannel.simPieces) {
    				if(piece.col == c && piece.row == perRow + diff) {
    					hittingP = piece;
    					return true;
    				}
    			}
    		}
    		//down right
    		for(int c = perCol+1; c < targetcol; c++) {
    			int diff = Math.abs(c - perCol);
    			for(Piece piece : Gamepannel.simPieces) {
    				if(piece.col == c && piece.row == perRow + diff) {
    					hittingP = piece;
    					return true;
    				}
    			}
    		}
    	}
    	return false;
    }
    public void draw(Graphics2D g2) {
        if (image != null) {
            g2.drawImage(image, x, y, Board.SQUARE_SIZE, Board.SQUARE_SIZE, null);
        }
    }
}
