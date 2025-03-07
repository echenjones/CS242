import java.util.LinkedList;

public class Board {

	protected int rows;
	protected int columns;
	protected int k;
	protected char[][] board;
	protected char currentPlayer;
	protected boolean connectedX = false;
	protected boolean connectedO = false;
	
	public Board(int c, int r, int k) {
		columns = c;
		rows = r;
		this.k = k;
	}

	/**
	 * Creates a new board with a specific number of rows and columns
	 */
	public void newBoard() {
		board = new char[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				board[i][j] = ' ';
			}
		}
		currentPlayer = 'x';
	}
	
	/**
	 * Gets the current player
	 * @return the current player
	 */
	public char getPlayer() {
		return currentPlayer;
	}
	
	/**
	 * Gets the opponent of the current player
	 * @return the opponent of the current player
	 */
	public char getOpponent() {
		if (currentPlayer == 'x') return 'o';
		else return 'x';
	}
	
	/**
	 * Moves to the end of a row, column, or diagonal after a player moves, and goes through
	 * the row/column/diagonal, checking if it has a given number of tiles from a player that
	 * are connected
	 * @param b the board
	 * @param m the move
	 * @param directionX the x direction being checked
	 * @param directionY the y direction being checked
	 * @param k the given number of tiles to connect
	 * @return if the player has connected the correct number of tiles
	 */
	public boolean testDirection(char[][] b, Move m, int directionX, int directionY) {
		int x = m.getX();
		int y = m.getY();
		char player = m.getPlayer();
		int count = 0;
		
		while (x > -1 && y > -1 && y < rows) {
			x += directionX;
			y += directionY;
		}
		x -= directionX;
		y -= directionY;
		
		while (!(x >= columns || y >= rows || x < 0 || y < 0)) {
			if (b[y][x] == player) {
				count++;
				if (count == k) return true;
			}
			else count = 0;
			x -= directionX;
			y -= directionY;
		}
		
		return false;
	}
	
	/**
	 * Checks to see if a move is valid - an empty square at the bottom of the board
	 * @param b the board
	 * @param m the move
	 * @return if it is a valid move
	 */
	public boolean validMove(char[][] b, Move m) {
		int x = m.getX();
		int y = m.getY();
		if (!emptySquare(b, x, y)) return false;
		for (int i = rows - 1; i > y; i--) {
			if (emptySquare(b, x, i)) return false;
		}
		return true;
	}
	
	/**
	 * Creates a linked list of possible moves on a board for a given player
	 * @param b the board
	 * @param p the player
	 * @return the linked list of possible moves
	 */
	public LinkedList<Move> actions(char[][] b, char p) {
		LinkedList<Move> actions = new LinkedList<Move>();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				Move m = new Move(j, i, p);
				if (validMove(b, m)) actions.add(m);
			}
		}
		if (actions.isEmpty()) {
			Move m = new Move(p);
			actions.add(m);
		}
		return actions;
	}
	
	/**
	 * Checks a direction to see if there is a potential connect k on a board for a given move
	 * @param b the board
	 * @param m the move
	 * @param directionX the x direction being checked
	 * @param directionY the y direction being checked
	 * @return 1 if there is a potential connect k, 0 if not
	 */
	public int countHeuristic(char[][] b, Move m, int directionX, int directionY) {
		int x = m.getX();
		int y = m.getY();
		char player = m.getPlayer();
		int count = 0;
		
		while (x > -1 && y > -1 && y < rows) {
			x += directionX;
			y += directionY;
		}
		x -= directionX;
		y -= directionY;
		
		while (!(x >= columns || y >= rows || x < 0 || y < 0)) {
			if (b[y][x] == player || b[y][x] == ' ') {
				count++;
				if (count == k) return 1;
			}
			else count = 0;
			x -= directionX;
			y -= directionY;
		}
		
		return 0;
	}
	
	/**
	 * Checks every square to see how many potential connect k's there are on a board for a
	 * given player
	 * @param b the board
	 * @param player the player
	 * @return the number of potential connect k's
	 */
	public int numberOfConnectKs(Board b, char player) {
		int count = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				Move m = new Move(j, i, player);
				count += countHeuristic(b.board, m, 0, -1) + countHeuristic(b.board, m, -1, -1) +
						countHeuristic(b.board, m, -1, 0) + countHeuristic(b.board, m, -1, 1);
			}
		}
		return count;
	}
	
	/**
	 * Checks if a square on a board is empty
	 * @param b the board
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return if the square is empty
	 */
	public boolean emptySquare(char[][] b, int x, int y) {
		if (b[y][x] == ' ') return true;
		else return false;
	}
	
	/**
	 * Copies the contents of a board to a new board
	 * @param oldBoard the board being copied
	 * @return the new board
	 */
	public char[][] copyBoard(Board oldBoard) {
		char[][] newBoard = new char[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				newBoard[i][j] = oldBoard.board[i][j];
			}
		}
		return newBoard;
	}
	
	/**
	 * Adds a move to a new board, then switches players
	 * @param oldBoard the old board to be copied
	 * @param m the move
	 * @return the new board
	 */
	public Board makeMove(Board oldBoard, Move m) {
		int x = m.getX();
		int y = m.getY();
		char player = m.getPlayer();
		Board newBoard = new Board(columns, rows, k);
		newBoard.board = copyBoard(oldBoard);
		newBoard.board[y][x] = player;
		newBoard.connectK(newBoard.board, m);
		newBoard.currentPlayer = oldBoard.getOpponent();
		return newBoard;
	}
	
	/**
	 * Checks if a player has connected a given number of tiles on a board in any direction
	 * with a move
	 * @param b the board
	 * @param m the move
	 * @param k the given number of tiles to connect
	 */
	public void connectK(char[][] b, Move m) {
		if (testDirection(b, m, 0, -1) || testDirection(b, m, -1, -1) ||
				testDirection(b, m, -1, 0) || testDirection(b, m, -1, 1)) {
			if (m.getPlayer() == 'x') connectedX = true;
			else connectedO = true;
		}
	}
	
	/**
	 * Prints the contents of the board
	 */
	public void printBoard() {
		if (columns == 3) System.out.println("  a b c  ");
		else if (columns == 5) System.out.println("  a b c d e  ");
		else if (columns == 7) System.out.println("  a b c d e f g  ");
		
		for (int i = 1; i <= rows; i++) {
			System.out.print(i + "|");
			for (int j = 0; j < columns; j++) {
				System.out.print(board[i - 1][j] + "|");
			}
			System.out.println();
		}
		System.out.println();
		
		if (currentPlayer == 'x' && !gameOver(board)) System.out.println("Next to play: RED/X\n");
		else if (currentPlayer == 'o' && !gameOver(board)) System.out.println("Next to play: YELLOW/O\n");
	}
	
	/**
	 * Checks if a board is full
	 * @param b the board
	 * @return if the board is full
	 */
	public boolean boardFull(char[][] b) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (emptySquare(b, j, i)) return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks if the game is over - a player has connected the given number of tiles, or the
	 * board is full
	 * @param b the board
	 * @return if the game is over
	 */
	public boolean gameOver(char[][] b) {
		if (connectedX || connectedO || boardFull(b)) return true;
		else return false;
	}
	
	/**
	 * Returns which player won, or if there was a tie
	 * @param b the board
	 * @param player the player
	 * @return the player who won
	 */
	public int terminalValue(char[][] b) {
		if (connectedX) return 1;
		else if (connectedO) return -1;
		else return 0;
	}
	
	public static void main(String[] args) {
		Board b = new Board(3, 3, 3);
		char[][] board = {{'o', 'x', ' '}, {'x', 'x', 'x'}, {'x', 'o', 'o'}};
		b.board = board;
		Move m = new Move(2, 0, 'x');
		b = b.makeMove(b, m);
		b.connectK(b.board, m);
		int v = b.terminalValue(board);
		System.out.println(v);
	}
}
