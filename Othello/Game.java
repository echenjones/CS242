import java.util.Scanner;

public class Game {
	
	protected int boardSize, opponent;
	protected char color;
	public static Scanner scan;
	
	public Game() {
		boardSize = 0;
		opponent = 0;
		color = ' ';
	}
	
	public void getBoardSize() {
		while (boardSize < 1 || boardSize > 3) {
			System.out.println("Choose your game:");
			System.out.println("1. Small 4x4 Othello");
			System.out.println("2. Medium 6x6 Othello");
			System.out.println("3. Standard 8x8 Othello");
			System.out.print("Your choice? ");
			boardSize = scan.nextInt();
			if (boardSize < 1 || boardSize > 3) System.out.println("Invalid option. Try again.");
			else System.out.println("You chose " + boardSize + "\n");
		}
		if (boardSize == 1) boardSize = 4;
		else if (boardSize == 2) boardSize = 6;
		else if (boardSize == 3) boardSize = 8;
	}
	
	public void getOpponent() {
		while (opponent < 1 || opponent > 4) {
			System.out.println("Choose your opponent:");
			System.out.println("1. An agent that plays randomly");
			System.out.println("2. An agent that uses MINIMAX");
			System.out.println("3. An agent that uses MINIMAX with alpha-beta pruning");
			System.out.println("4. An agent that uses H-MINIMAX with a fixed depth cutoff and"
					+ " alpha-beta pruning");
			System.out.print("Your choice? ");
			opponent = scan.nextInt();
			if (opponent < 1 || opponent > 4) {
				System.out.println("Invalid option. Try again.");
			}
			else {
				System.out.println("You chose " + opponent + "\n");
			}
		}
	}
	
	public void getColor() {
		while (color != 'x' && color != 'o') {
			System.out.print("Do you want to play DARK (x) or LIGHT (o)? ");
			color = scan.next().charAt(0);
			if (color != 'x' && color != 'o') {
				System.out.println("Invalid option. Try again.");
			}
			else {
				System.out.println("You chose " + color + "\n");
			}
		}
	}
	
	public void setup() {
		System.out.println("Othello by Elana Chen-Jones");
		getBoardSize();
		getOpponent();
		getColor();
	}
	
	public Board playRandom(Board b) {
		return b;
	}
	
	/*public Board playMinimax(Board b) {
		Move m;
		while (!b.gameOver(b.board)) {
			if (!b.anyMoves(b.board, b.getPlayer())) {
				System.out.println("No valid moves for " + b.getPlayer() + ".");
				b.currentPlayer = b.getOpponent();
			}
			if (b.getPlayer() == color) {
				do {
					m = new Move(b.getPlayer(), scan);
					if (!b.validMove(b.board, m)) System.out.println("Invalid move. Try again.");
				} while (!b.validMove(b.board, m));
			}
			else {
				Search s = new Search();
				m = s.minimax(b);
			}
			b = b.makeMove(b, m);
			b.printBoard();
			//b.currentPlayer = b.getOpponent();
		}
		return b;
	}
	
	public Board playAbMinimax(Board b) {
		Move m;
		while (!b.gameOver(b.board)) {
			if (!b.anyMoves(b.board, b.getPlayer())) {
				System.out.println("No valid moves for " + b.getPlayer() + ".");
				b.currentPlayer = b.getOpponent();
			}
			if (b.getPlayer() == color) {
				do {
					m = new Move(b.getPlayer(), scan);
					if (!b.validMove(b.board, m)) System.out.println("Invalid move. Try again.");
				} while (!b.validMove(b.board, m));
			}
			else {
				Search s = new Search();
				m = s.abMinimax(b);
			}
			b = b.makeMove(b, m);
			b.printBoard();
			//b.currentPlayer = b.getOpponent();
		}
		return b;
	}
	
	public Board playHMinimax(Board b) {
		return b;
	}*/
	
	public Board twoPlayer(Board b) {
		Move m;
		while (!b.gameOver(b.board)) {
			if (!b.anyMoves(b.board, b.getPlayer())) {
				System.out.println("No valid moves for " + b.getPlayer() + ".");
				b.currentPlayer = b.getOpponent();
			}
			do {
				m = new Move(b.getPlayer(), scan);
				if (!b.validMove(b.board, m)) System.out.println("Invalid move. Try again.");
			} while (!b.validMove(b.board, m));
			b = b.makeMove(b, m);
			b.printBoard(0);
			//b.currentPlayer = b.getOpponent();
		}
		return b;
	}
	
	public static void main(String[] args) {
		Game g = new Game();
		scan = new Scanner(System.in);
		g.setup();
		Board b = new Board(g.boardSize);
		b.newBoard();
		b.printBoard(0);
		
		Move m;
		while (!b.gameOver(b.board)) {
			if (!b.anyMoves(b.board, b.getPlayer())) {
				System.out.println("No valid moves for " + b.getPlayer() + ".");
				b.currentPlayer = b.getOpponent();
			}
			if (b.getPlayer() == g.color) {
				do {
					m = new Move(b.getPlayer(), scan);
					if (!b.validMove(b.board, m)) System.out.println("Invalid move. Try again.");
				} while (!b.validMove(b.board, m));
			}
			else {
				Search s = new Search();
				if (g.opponent == 1) m = s.random(b);
				else if (g.opponent == 2) m = s.minimax(b);
				else if (g.opponent == 3) m = s.abMinimax(b);
				else m = s.hMinimax(b, 7);
			}
			b = b.makeMove(b, m);
			b.printBoard(0);
			//b.currentPlayer = b.getOpponent();
		}
		
		System.out.println("\nGame over");
		int result = b.terminalValue(b.board, 'x');
		if (result == Integer.MAX_VALUE) System.out.println("Winner: DARK");
		else if (result == Integer.MIN_VALUE) System.out.println("Winner: LIGHT");
		else System.out.println("Winner: TIE");
		scan.close();
	}

}
