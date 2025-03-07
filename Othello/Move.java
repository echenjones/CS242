import java.util.Scanner;

public class Move {

	protected int x, y;
	protected char player;
	
	public Move(char p, Scanner scan) {
		player = p;
		//scan = new Scanner(System.in);
		//if (player == 'x') System.out.println("Next to play: DARK\n");
		//else if (player == 'o') System.out.println("Next to play: LIGHT\n");
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
		//scan.close();
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
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public char getPlayer() {
		return player;
	}
	
	public char getOpponent() {
		if (player == 'x') return 'o';
		else return 'x';
	}
	
	public boolean skipMove() {
		if (x == -1) return true;
		else return false;
	}
}
