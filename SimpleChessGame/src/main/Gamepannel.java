package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JPanel;

import piece.Bishop;
import piece.King;
import piece.Knight;
import piece.Pawan;
import piece.Piece;
import piece.Queen;
import piece.Rook;

public class Gamepannel extends JPanel implements Runnable{

	public static final int WIDTH = 1111;
	public static final int HEIGHT = 800;
	final int FPS = 60;
	Thread gameThread;
	Board board = new Board();
	Mouse mouse = new Mouse();
	
	//piece
	public static ArrayList<Piece> pieces = new ArrayList<>();
	public static ArrayList<Piece> simPieces = new  ArrayList<>();
	ArrayList<Piece> promoPieces = new ArrayList<>();
	Piece activeP, checkingP ;
	public static Piece castlingP; 
	
	//color
	public static final int WHITE = 0;
	public static final int BLACK = 1;
	int currentColor =  WHITE;

	//boolean
	boolean canMove;
	boolean vaildSquare;
	boolean promotion;
	boolean gameover;
	boolean stalemate;
	boolean started;     // false while the start menu is showing
	boolean startArmed;  // the start button was pressed and waits for the release
	static final java.awt.Rectangle START_BUTTON = new java.awt.Rectangle(250, 400, 300, 90);
	
	public Gamepannel() {
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		setBackground (Color.black);
		addMouseMotionListener(mouse);
		addMouseListener(mouse);
		
		//testPromotion();
		setPieces();
		//testIllegal();
		copyPieces(pieces, simPieces);
	}
	
	
	public void launchGame () {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	//set pieces
	public void setPieces () {
		//white pieces
		
		pieces.add(new Pawan(WHITE,0,6));
		pieces.add(new Pawan(WHITE,1,6));
		pieces.add(new Pawan(WHITE,2,6));
		pieces.add(new Pawan(WHITE,3,6));
		pieces.add(new Pawan(WHITE,4,6));
		pieces.add(new Pawan(WHITE,5,6));
		pieces.add(new Pawan(WHITE,6,6));
		pieces.add(new Pawan(WHITE,7,6));
		pieces.add(new Rook(WHITE,0,7));
		pieces.add(new Rook(WHITE,7,7));
		pieces.add(new Knight(WHITE,1,7));
		pieces.add(new Knight(WHITE,6,7));
		pieces.add(new Bishop(WHITE,2,7));
		pieces.add(new Bishop(WHITE,5,7));
		pieces.add(new Queen(WHITE,3,7));
		pieces.add(new King(WHITE,4,7));
		
		
		
		
		//black pieces
		
		pieces.add(new Pawan(BLACK,0,1));
		pieces.add(new Pawan(BLACK,1,1));
		pieces.add(new Pawan(BLACK,2,1));
		pieces.add(new Pawan(BLACK,3,1));
		pieces.add(new Pawan(BLACK,4,1));
		pieces.add(new Pawan(BLACK,5,1));
		pieces.add(new Pawan(BLACK,6,1));
		pieces.add(new Pawan(BLACK,7,1));
		pieces.add(new Rook(BLACK,0,0));
		pieces.add(new Rook(BLACK,7,0));
		pieces.add(new Knight(BLACK,1,0));
		pieces.add(new Knight(BLACK,6,0));
		pieces.add(new Bishop(BLACK,2,0));
		pieces.add(new Bishop(BLACK,5,0));
		pieces.add(new Queen(BLACK,3,0));
		pieces.add(new King(BLACK,4,0));
		
	}
	
	public void testPromotion() {
		pieces.add(new Pawan(WHITE,0,3));
		pieces.add(new Pawan(BLACK,5,4));
	}
	
	public void testIllegal() {
		pieces.add(new Pawan(WHITE,7,6));
		pieces.add(new King(WHITE,3,7));
		pieces.add(new King(BLACK,0,3));
		pieces.add(new Bishop(BLACK,1,4));
		pieces.add(new Queen (BLACK,4,5));
	}
	private void copyPieces(ArrayList<Piece> source, ArrayList<Piece> target) {
		
		target.clear();
		for(int i=0; i<source.size();i++) {
			target.add(source.get(i));
		}
	}
	
	
	@Override
	public void run() {
		//game loop
		double drawInterval = 1000000000/FPS;
		double delta = 0;
		long lestTime = System.nanoTime();
		long currentTime;
		
		while(gameThread != null) {
			currentTime = System.nanoTime();
			
			delta += (currentTime - lestTime)/drawInterval;
			lestTime = currentTime;
			
			if(delta >= 1) {
				update();
				repaint();
				delta--; 
			}
		}
	}
	
	private void update() {
		
		if(started == false) {
			boolean onButton = START_BUTTON.contains(mouse.rawX, mouse.rawY);
			if(mouse.pressed && onButton) {
				startArmed = true;
			}else if(mouse.pressed == false) {
				// start on release so the click doesn't pick up a piece
				if(startArmed && onButton) {
					started = true;
				}
				startArmed = false;
			}
			return;
		}
		
		if(promotion) {
			promoting();
		}
		else if(gameover == false && stalemate == false){
			//mouse button pressed
			if(mouse.pressed)
			{
				if(activeP == null) 
					//if the activeP is null, check if you can pick up a piece 
				{
					for(Piece piece : simPieces) 
					{
						// if the mouse is on an ally piece, pick it up as the activeP
						if(piece.color == currentColor 
								&& piece.col == mouse.x/Board.SQUARE_SIZE
								&& piece.row == mouse.y/Board.SQUARE_SIZE)
						{
							activeP = piece;
						}
					}
				}else {
					//player is holding a pieces simulate the move 
					simulate();
				}
			}
			
			if(mouse.pressed == false) {
				if(activeP != null) {
					if(vaildSquare) {
						//move confirmed
						
						//update the piece list in case a piece has been captured and removed during the simulation
						copyPieces(simPieces, pieces);
						activeP.updatePostion();
						if(castlingP != null) {
							castlingP.updatePostion();
						}
						
						castlingP = null;

						if(canPromote()) {
							promotion = true;
						}else {
							afterMove();
						}
					}else {
						//the move is not valid so rest everything
						copyPieces(pieces, simPieces);
						activeP.resetPosition();
						// put the castling rook back too
						if(castlingP != null) {
							castlingP.resetPosition();
							castlingP = null;
						}
						activeP = null;
					}
				}
			}
		}
	}
	
	private void simulate() {
		
		canMove = false;
		vaildSquare = false;
		
		copyPieces(pieces, simPieces);
		
		// rest the castling piece's position
		if(castlingP != null) {
			castlingP.col = castlingP.perCol;
			castlingP.x = castlingP.getX(castlingP.col);
			castlingP = null; 
		}
		
		
		activeP.x = mouse.x -Board.HALF_SQUARE_SIZE;
		activeP.y = mouse.y - Board.HALF_SQUARE_SIZE;
		activeP.col = activeP.getCol(activeP.x);
		activeP.row = activeP.getRow(activeP.y);
		
		if(activeP.canMove(activeP.col, activeP.row)) {
			canMove =true;
			
			//if hitting piece, remove it from the list 
			if(activeP.hittingP != null) {
				simPieces.remove(activeP.hittingP.getIndex());
			}
			
			checkCastling();
			
			if(opponentCanCaptureKing() == false && castlingIsSafe()) {
				vaildSquare = true;
			}
		}
	}
	// the king may not castle out of check or across an attacked square
	private boolean castlingIsSafe() {
		if(activeP.type == Type.KING && castlingP != null) {
			int opponent = (currentColor == WHITE) ? BLACK : WHITE;
			int middleCol = (activeP.perCol + activeP.col) / 2;
			if(isSquareAttacked(activeP.perCol, activeP.perRow, opponent)
					|| isSquareAttacked(middleCol, activeP.perRow, opponent)) {
				return false;
			}
		}
		return true;
	}

	private Piece getPieceAt(int col, int row) {
		for(Piece piece : simPieces) {
			if(piece.col == col && piece.row == row) {
				return piece;
			}
		}
		return null;
	}

	// true if there is no piece between the attacker and the target on the line they share
	private boolean isLineClear(Piece attacker, int targetCol, int targetRow) {
		int stepCol = Integer.compare(targetCol, attacker.col);
		int stepRow = Integer.compare(targetRow, attacker.row);
		int col = attacker.col + stepCol;
		int row = attacker.row + stepRow;
		while(col != targetCol || row != targetRow) {
			if(getPieceAt(col, row) != null) {
				return false;
			}
			col += stepCol;
			row += stepRow;
		}
		return true;
	}

	// Article 3.1: a piece of this color that attacks the square (as the pieces stand in simPieces)
	private Piece getAttacker(int col, int row, int byColor) {
		for(Piece piece : simPieces) {
			if(piece.color != byColor || (piece.col == col && piece.row == row)) {
				continue;
			}
			int colDiff = col - piece.col;
			int rowDiff = row - piece.row;
			boolean straight = (colDiff == 0) != (rowDiff == 0);
			boolean diagonal = colDiff != 0 && Math.abs(colDiff) == Math.abs(rowDiff);
			boolean attacks = false;

			switch(piece.type) {
			case PAWN:
				int direction = (piece.color == WHITE) ? -1 : 1;
				attacks = Math.abs(colDiff) == 1 && rowDiff == direction;
				break;
			case KNIGHT:
				attacks = Math.abs(colDiff) * Math.abs(rowDiff) == 2;
				break;
			case KING:
				attacks = Math.max(Math.abs(colDiff), Math.abs(rowDiff)) == 1;
				break;
			case BISHOP:
				attacks = diagonal && isLineClear(piece, col, row);
				break;
			case ROOK:
				attacks = straight && isLineClear(piece, col, row);
				break;
			case QUEEN:
				attacks = (straight || diagonal) && isLineClear(piece, col, row);
				break;
			}
			if(attacks) {
				return piece;
			}
		}
		return null;
	}

	private boolean isSquareAttacked(int col, int row, int byColor) {
		return getAttacker(col, row, byColor) != null;
	}

	// Article 3.9: would the king of this piece's color be attacked if the piece moved to the square
	private boolean isLegalMove(Piece piece, int targetCol, int targetRow, Piece captured) {
		int oldCol = piece.col;
		int oldRow = piece.row;
		ArrayList<Piece> backup = new ArrayList<>(simPieces);

		if(captured != null) {
			simPieces.remove(captured);
		}
		piece.col = targetCol;
		piece.row = targetRow;

		boolean legal = true;
		for(Piece king : simPieces) {
			if(king.type == Type.KING && king.color == piece.color) {
				int opponent = (piece.color == WHITE) ? BLACK : WHITE;
				legal = isSquareAttacked(king.col, king.row, opponent) == false;
			}
		}

		piece.col = oldCol;
		piece.row = oldRow;
		copyPieces(backup, simPieces);
		return legal;
	}

	// Article 5.1 and 5.2: a player with no legal move is checkmated or stalemated
	private boolean hasLegalMove(int color) {
		ArrayList<Piece> myPieces = new ArrayList<>();
		for(Piece piece : simPieces) {
			if(piece.color == color) {
				myPieces.add(piece);
			}
		}
		for(Piece piece : myPieces) {
			for(int col = 0; col < 8; col++) {
				for(int row = 0; row < 8; row++) {
					// castling is skipped: it is only possible if the king can also step one square
					if(piece.type == Type.KING && Math.abs(col - piece.col) == 2) {
						continue;
					}
					piece.hittingP = null;
					if(piece.canMove(col, row) && isLegalMove(piece, col, row, piece.hittingP)) {
						castlingP = null;
						return true;
					}
				}
			}
		}
		castlingP = null;
		return false;
	}

	// check, checkmate and stalemate once a move is finished
	private void afterMove() {
		int opponent = (currentColor == WHITE) ? BLACK : WHITE;
		Piece king = getKing(true);

		checkingP = getAttacker(king.col, king.row, currentColor);
		boolean inCheck = checkingP != null;

		if(hasLegalMove(opponent) == false) {
			if(inCheck) {
				gameover = true;
			}else {
				stalemate = true;
			}
			activeP = null;
		}else {
			changePlayer();
		}
	}

	private void checkCastling() {
		if(castlingP != null) {
			if(castlingP.col == 0) {
				castlingP.col += 3;
			}
			else if(castlingP.col == 7) {
				castlingP.col -= 2;
			}
			castlingP.x = castlingP.getX(castlingP.col);
		}
	}
	
	private boolean opponentCanCaptureKing() {

		Piece king  = getKing(false);
		int opponent = (currentColor == WHITE) ? BLACK : WHITE;

		return isSquareAttacked(king.col, king.row, opponent);
	}

	private Piece getKing(boolean opponent) {
		
		Piece king = null;
		
		for(Piece piece : simPieces) {
			if(opponent) {
				if(piece.type == Type.KING && piece.color != currentColor) {
					king = piece;
				}
			}
			else {
				if(piece.type == Type.KING && piece.color == currentColor) {
					king = piece;
				}
			}
		}
		return king;
	}
	public void changePlayer() {
		if(currentColor == WHITE) {
			currentColor = BLACK;
			//reset black's two stepped status
			for(Piece piece : pieces) {
				if(piece.color == BLACK) {
					piece.twoStepeed = false;
				}
			}
		}
		else {
			currentColor = WHITE;
			//reset white's two stepped status
			for(Piece piece : pieces) {
				if(piece.color == WHITE) {
					piece.twoStepeed = false;
				}
			}
		}
		activeP = null;
	}
	
	public boolean canPromote() {
		
		if(activeP.type == Type.PAWN) {
			if(currentColor == WHITE && activeP.row == 0 ||  currentColor == BLACK && activeP.row == 7) {
				promoPieces.clear();
				promoPieces.add(new Rook(currentColor,9,2));
				promoPieces.add(new Knight(currentColor,9,3));
				promoPieces.add(new Bishop(currentColor,9,4));
				promoPieces.add(new Queen(currentColor,9,5));
				return true;
			}
		}
		return false;
	}
	
	public void promoting() {
		if(mouse.pressed) {
			for(Piece piece: promoPieces) {
				if(piece.col == mouse.x/Board.SQUARE_SIZE && piece.row == mouse.y/Board.SQUARE_SIZE ) {
					switch(piece.type) {
					case ROOK: simPieces.add(new Rook(currentColor, activeP.col, activeP.row)); break;
					case KNIGHT: simPieces.add(new Knight(currentColor, activeP.col, activeP.row)); break;
					case BISHOP: simPieces.add(new Bishop(currentColor, activeP.col, activeP.row)); break;
					case QUEEN: simPieces.add(new Queen(currentColor, activeP.col, activeP.row)); break;
					default : break;
					}
					simPieces.remove(activeP.getIndex());
					copyPieces(simPieces, pieces);
					promotion = false;
					afterMove();
				}
			}
		}
	}
	private void drawStartMenu(Graphics2D g2) {
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		// dim the board behind the menu
		g2.setColor(new Color(0, 0, 0, 170));
		g2.fillRect(0, 0, WIDTH, HEIGHT);
		
		g2.setColor(Color.WHITE);
		g2.setFont(new Font("Book Antiqua", Font.BOLD, 110));
		g2.drawString("CHESS", 195, 270);
		g2.setFont(new Font("Book Antiqua", Font.PLAIN, 30));
		g2.drawString("Two players, one board", 255, 330);
		
		boolean hover = START_BUTTON.contains(mouse.rawX, mouse.rawY);
		g2.setColor(hover ? new Color(230, 190, 140) : new Color(210, 165, 125));
		g2.fillRoundRect(START_BUTTON.x, START_BUTTON.y, START_BUTTON.width, START_BUTTON.height, 30, 30);
		g2.setColor(new Color(70, 40, 20));
		g2.setFont(new Font("Book Antiqua", Font.BOLD, 44));
		g2.drawString("Start Game", START_BUTTON.x + 40, START_BUTTON.y + 60);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		//board
		board.draw(g2);
		
		//piece
		// draw back to front so the pieces in front overlap the ones behind
		ArrayList<Piece> drawOrder = new ArrayList<>(simPieces);
		drawOrder.sort((a, b) -> Integer.compare(a.y, b.y));
		for(Piece p : drawOrder) {
			p.draw(g2);
		}
		
		if(started == false) {
			drawStartMenu(g2);
			return;
		}
		
		if(activeP != null) {
			if(canMove) {
				if(vaildSquare) {
					g2.setColor(Color.gray);
					g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
					Board.fillSquare(g2, activeP.col, activeP.row);
					g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
				}
				else
				{
					g2.setColor(Color.white);
					g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
					Board.fillSquare(g2, activeP.col, activeP.row);
					g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
				}
			}
			
			// draw the active piece in the end so it won't be hidden by the board or the colored square
			activeP.draw(g2);
		}
		
		// status messages
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setFont(new Font("Book Antiqua", Font.PLAIN,40));
		g2.setColor(Color.WHITE);
		
		if(promotion) {
			g2.drawString("Promote to:", 840, 150);
			for(Piece piece :  promoPieces) {
				g2.drawImage(piece.image, piece.getX(piece.col), piece.getY(piece.row),
						Board.SQUARE_SIZE, Board.SQUARE_SIZE, null);
			}
		}
		else {
			if(currentColor == WHITE) {
				g2.drawString("White's turn", 840, 550);
				if(checkingP != null && checkingP.color == BLACK) {
					g2.setColor(Color.red);
					g2.drawString("The King", 840, 650);
					g2.drawString("is in check!", 840, 700);
				}
			}else {
				g2.drawString("Black's turn", 840, 250);
				if(checkingP != null && checkingP.color == WHITE) {
					g2.setColor(Color.red);
					g2.drawString("The King", 840, 100);
					g2.drawString("is in check!", 840, 150);
				}
			}
		}
		
		if(gameover) {
			String s;
			if(currentColor == WHITE) {
				s = "White Wins";
			}else {
				s = "Black Wins";
			}
			g2.setFont(new Font("Arial", Font.PLAIN, 90));
			g2.setColor(Color.green);
			g2.drawString(s, 200, 420);
		}
		
		if(stalemate) {
			g2.setFont(new Font("Arial", Font.PLAIN, 90));
			g2.setColor(Color.lightGray);
			g2.drawString("Stalemate", 200, 420);
		}
	}
	
	
	
}
