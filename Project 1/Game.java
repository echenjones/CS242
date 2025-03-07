import java.util.Scanner;

public class Game {
	
	protected int rows, columns, opponent, depth, k;
	protected char color;
	public static Scanner scan;
	
	public Game() {
		opponent = 0;
		depth = 0;
		color = ' ';
	}
	
	/**
	 * Sets up the type of game
	 */
	public void setup() {
		System.out.println("Connect-Four by Elana Chen-Jones");
		getBoardSize();
		getOpponent();
		if (opponent == 4) getDepth();
		getColor();
	}
	
	/**
	 * Gets a board size from the user
	 */
	public void getBoardSize() {
		int boardSize = 0;
		while (boardSize < 1 || boardSize > 3) {
			System.out.println("Choose your game:");
			System.out.println("1. Tiny 3x3x3 Connect-Three");
			System.out.println("2. Wider 5x3x3 Connect-Three");
			System.out.println("3. Standard 7x6x4 Connect-Four");
			System.out.print("Your choice? ");
			boardSize = scan.nextInt();
			if (boardSize < 1 || boardSize > 3) System.out.println("Invalid option. Try again.");
		}
		if (boardSize == 1) {
			rows = 3;
			columns = 3;
			k = 3;
		}
		else if (boardSize == 2) {
			rows = 3;
			columns = 5;
			k = 3;
		}
		else if (boardSize == 3) {
			rows = 6;
			columns = 7;
			k = 4;
		}
	}
	
	/**
	 * Gets an opponent type from the user
	 */
	public void getOpponent() {
		while (opponent < 1 || opponent > 4) {
			System.out.println("Choose your opponent:");
			System.out.println("1. An agent that plays randomly");
			System.out.println("2. An agent that uses MINIMAX");
			System.out.println("3. An agent that uses MINIMAX with alpha-beta pruning");
			System.out.println("4. An agent that uses H-MINIMAX with a fixed depth cutoff");
			System.out.print("Your choice? ");
			opponent = scan.nextInt();
			if (opponent < 1 || opponent > 4) System.out.println("Invalid option. Try again.");
		}
	}
	
	/**
	 * Gets a depth limit from the user
	 */
	public void getDepth() {
		while (depth < 1) {
			System.out.print("Depth limit? ");
			depth = scan.nextInt();
			if (depth < 1) System.out.println("Invalid option. Try again.");
		}
	}
	
	/**
	 * Gets a color choice from the user
	 */
	public void getColor() {
		int c = 0;
		while (c != 1 && c != 2) {
			System.out.print("Do you want to play RED (1) or YELLOW (2)? ");
			c = scan.nextInt();
			if (c != 1 && c != 2) System.out.println("Invalid option. Try again.");
			else {
				if (c == 1) color = 'x';
				else color = 'o';
			}
		}
	}
	
	/**
	 * Plays a game of Connect-Four
	 * @param b the initial board
	 */
	public void playGame(Board b) {
		Move m = new Move('x');
		while (!b.gameOver(b.board)) {
			if (b.getPlayer() == color) {
				do {
					m = new Move(b.getPlayer(), scan);
					if (!b.validMove(b.board, m)) System.out.println("Invalid move. Try again.");
				} while (!b.validMove(b.board, m));
			}
			else {
				Search s = new Search();
				if (opponent == 1) m = s.random(b);
				else if (opponent == 2) {
					System.out.println("go to minimax");
					m = s.minimax(b);
				}
				else if (opponent == 3) m = s.abMinimax(b);
				else m = s.hMinimax(b, depth);
			}
			b = b.makeMove(b, m);
			b.printBoard();
		}
		System.out.println("\nGame over");
		int result = b.terminalValue(b.board);
		if (result == 1) System.out.println("Winner: RED");
		else if (result == -1) System.out.println("Winner: YELLOW");
		else System.out.println("Winner: TIE");
	}
	
	public static void main(String[] args) {
		Game g = new Game();
		scan = new Scanner(System.in);
		g.setup();
		Board b = new Board(g.columns, g.rows, g.k);
		b.newBoard();
		b.printBoard();
		g.playGame(b);
		scan.close();
	}
}
