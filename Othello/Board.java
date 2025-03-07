import java.util.LinkedList;

public class Board {
	
	protected int size;
	protected char[][] board;
	protected char currentPlayer;
	
	public Board(int s) {
		size = s;
	}

	public void newBoard() {
		board = new char[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				board[i][j] = ' ';
			}
		}
		board[size/2 - 1][size/2 - 1] = 'o';
		board[size/2 - 1][size/2] = 'x';
		board[size/2][size/2 - 1] = 'x';
		board[size/2][size/2] = 'o';
		
		currentPlayer = 'x';
	}
	
	public char getPlayer() {
		return currentPlayer;
	}
	
	public char getOpponent() {
		if (currentPlayer == 'x') return 'o';
		else return 'x';
	}
	
	public boolean testDirection(char[][] b, Move m, int directionX, int directionY) {
		int x = m.getX();
		int y = m.getY();
		char player = m.getPlayer();
		char opponent = m.getOpponent();
		
		if (b[directionX + x][directionY + y] == opponent) {
			do {
				x += directionX;
				y += directionY;
				if (x < 0 || x >= size || y < 0 || y >= size) return false;
				else if (b[x][y] == opponent) continue;
				else if (b[x][y] == player) return true;
				else return false;
			} while (x >= 0 && x < size && y >= 0 && y < size);
		}
		return false;
	}
	
	public boolean validMove(char[][] b, Move m) {
		int x = m.getX();
		int y = m.getY();
		
		for (int i = Math.max(x - 1, 0); i <= Math.min(x + 1, size - 1); i++) {
			for (int j = Math.max(y - 1, 0); j <= Math.min(y + 1, size - 1); j++) {
				if (!(i - x == 0 && j - y == 0) && emptySquare(b, x, y)) {
					if (testDirection(b, m, i - x, j - y)) return true;
				}
			}
		}
		return false;
	}
	
	public LinkedList<Move> actions(char[][] b, char p) {
		LinkedList<Move> actions = new LinkedList<Move>();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				Move m = new Move(i, j, p);
				if (validMove(b, m)) actions.add(m);
			}
		}
		if (actions.isEmpty()) {
			Move m = new Move(p);
			actions.add(m);
		}
		return actions;
	}
	
	public int numberOfMoves(Board b) {
		LinkedList<Move> actionsX = actions(b.board, 'x');
		LinkedList<Move> actionsO = actions(b.board, 'o');
		return actionsX.size() - actionsO.size();
	}
	
	public int numberOfCorners(Board b) {
		int numX = 0;
		int numO = 0;
		int[][] corners = {{0, 0}, {0, b.size - 1}, {b.size - 1, 0}, {b.size - 1, b.size - 1}};
		for (int i = 0; i < 4; i++) {
			if (b.board[corners[i][0]][corners[i][1]] == 'x') numX += 3;
			else if (b.board[corners[i][0]][corners[i][1]] == 'o') numO += 3;
		}
		return numX - numO;
	}
	
	public int numberOfSecondLine(Board b) {
		int numX = 0;
		int numO = 0;
		for (int i = 0; i < b.size; i++) {
			// left column
			if (b.board[1][i] == 'x') numX++;
			else if (b.board[1][i] == 'o') numO++;
			// top row
			if (b.board[i][1] == 'x') numX++;
			else if (b.board[i][1] == 'o') numO++;
			// right column
			if (b.board[b.size - 1][i] == 'x') numX++;
			else if (b.board[b.size - 1][i] == 'o') numO++;
			// bottom row
			if (b.board[i][b.size - 1] == 'x') numX++;
			else if (b.board[i][b.size - 1] == 'o') numO++;
		}
		return numO - numX;
	}
	
	public boolean emptySquare(char[][] b, int x, int y) {
		if (b[x][y] == ' ') return true;
		else return false;
	}
	
	public char[][] copyBoard(Board oldBoard) {
		char[][] newBoard = new char[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				newBoard[i][j] = oldBoard.board[i][j];
			}
		}
		return newBoard;
	}
	
	public Board makeMove(Board oldBoard, Move m) {
		int x = m.getX();
		int y = m.getY();
		char player = m.getPlayer();
		Board newBoard = new Board(size);
		newBoard.board = copyBoard(oldBoard);
		if (!m.skipMove()) {
			newBoard.board[x][y] = player;
			newBoard.board = flip(newBoard.board, m);
		}
		newBoard.currentPlayer = oldBoard.getOpponent();
		//System.out.println(m.getX() + ", " + m.getY());
		//newBoard.printBoard();
		return newBoard;
	}
	
	public char[][] flipInDirection(char[][] b, Move m, int directionX, int directionY) {
		int x = m.getX();
		int y = m.getY();
		char player = m.getPlayer();
		char opponent = m.getOpponent();
		//char[][] newBoard = oldBoard;
		
		do {
			x += directionX;
			y += directionY;
			if (x < 0 || x >= size || y < 0 || y >= size) break;
			else if (b[x][y] == opponent) b[x][y] = player;
			else if (b[x][y] == player) break;
		} while (x >= 0 && x < size && y >= 0 && y < size);
		
		return b;
	}
	
	public char[][] flip(char[][] b, Move m) {
		int x = m.getX();
		int y = m.getY();
		//char[][] newBoard = oldBoard;
		
		for (int i = Math.max(x - 1, 0); i <= Math.min(x + 1, size - 1); i++) {
			for (int j = Math.max(y - 1, 0); j <= Math.min(y + 1, size - 1); j++) {
				if (!(i - x == 0 && j - y == 0)) {
					if (testDirection(b, m, i - x, j - y)) {
						b = flipInDirection(b, m, i - x, j - y);
					}
				}
			}
		}
		return b;
	}
	
	public void printBoard(int count) { // int count
		for (int i = 0; i < count; i++) System.out.print(" "); //
		if (size == 4) System.out.println("  a b c d  ");
		else if (size == 6) System.out.println("  a b c d e f  ");
		else if (size == 8) System.out.println("  a b c d e f g h  ");
		
		for (int i = 1; i <= size; i++) {
			for (int e = 0; e < count; e++) System.out.print(" "); //
			System.out.print(i + " ");
			for (int j = 0; j < size; j++) {
				System.out.print(board[j][i - 1] + " ");
			}
			System.out.println(i);
		}

		for (int i = 0; i < count; i++) System.out.print(" "); //
		if (size == 4) System.out.println("  a b c d  ");
		else if (size == 6) System.out.println("  a b c d e f  ");
		else if (size == 8) System.out.println("  a b c d e f g h  ");
		if (currentPlayer == 'x' && !gameOver(board)) System.out.println("Next to play: DARK\n");
		else if (currentPlayer == 'o' && !gameOver(board)) System.out.println("Next to play: LIGHT\n");
	}
	
	public boolean anyMoves(char[][] b, char player) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				Move m = new Move(i, j, player);
				if (validMove(b, m)) return true;
			}
		}
		return false;
	}
	
	public boolean gameOver(char[][] b) {
		return (!anyMoves(b, 'x') && !anyMoves(b, 'o'));
	}
	
	public int terminalValue(char[][] b, char player) {
		int scorePlayer = 0;
		int scoreOpponent = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (b[i][j] == 'x') scorePlayer += 1;
				if (b[i][j] == 'o') scoreOpponent += 1;
			}
		}
		if (scorePlayer > scoreOpponent) return Integer.MAX_VALUE;
		else if (scoreOpponent > scorePlayer) return Integer.MIN_VALUE;
		else return 0;
	}
	
	public static void main(String[] args) {
		/*Board b1 = new Board(4);
		b1.newBoard();
		b1.printBoard();
		Move m;
		while (!b1.gameOver(b1.board)) {
			if (!b1.anyMoves(b1.board, b1.getPlayer())) {
				System.out.println("No valid moves for " + b1.getPlayer() + ".");
				b1.currentPlayer = b1.getOpponent();
			}
			do {
				m = new Move(b1.getPlayer());
				if (!b1.validMove(b1.board, m)) System.out.println("Invalid move. Try again.");
			} while (!b1.validMove(b1.board, m));
			b1.board = b1.makeMove(b1.board, m);
			b1.printBoard();
			b1.currentPlayer = b1.getOpponent();
		}
		System.out.println("\nGame over");
		int result = b1.terminalValue(b1.board, 'x');
		if (result == 1) System.out.println("Winner: DARK");
		else if (result == -1) System.out.println("Winner: LIGHT");
		else System.out.println("Winner: TIE");*/
		
		/*Board b2 = new Board(6);
		b2.newBoard();
		b2.printBoard();
		
		Board b3 = new Board(8);
		b3.newBoard();
		b3.printBoard();*/
	}
	
}
