import java.util.Scanner;

public class Move {

	protected int x, y;
	protected char player;
	
	public Move(char p, Scanner scan) {
		player = p;
		System.out.print("Your move (? for help): ");
		String m = scan.next();
		if (m == "?") System.out.println("Enter a letter corresponding to the column and a "
					  					 + "number cooresponding to the row you would like "
					  					 + "to play at (ex: a1).");
		else {
			x = (int) m.charAt(0) - (int) 'a';
			y = (int) m.charAt(1) - (int) '1';
			System.out.println(player + ": " + m);
		}
	}
	
	public Move(int a, int b, char p) {
		x = a;
		y = b;
		player = p;
	}
	
	public Move(char p) {
		x = - 1;
		y = -1;
		player = p;
	}
	
	/**
	 * Gets the x coordinate of a move
	 * @return the x coordinate of a move
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Gets the y coordinate of a move
	 * @return the y coordinate of a move
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Gets the player of a move
	 * @return the player of a move
	 */
	public char getPlayer() {
		return player;
	}
	
	/**
	 * Gets the opponent of the player
	 * @return the opponent of the player
	 */
	public char getOpponent() {
		if (player == 'x') return 'o';
		else return 'x';
	}
}
