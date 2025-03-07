import java.util.LinkedList;
import java.util.Random;

public class Search {
	
	public Search() {
		
	}
	
	
	// RANDOM
	public Move random(Board b) {
		LinkedList<Move> actions = b.actions(b.board, b.currentPlayer);
		Random rand = new Random();
		Move m = actions.get(rand.nextInt(actions.size()));
		return m;
	}
	
	// MINIMAX
	public int maxValueMinimax(Board b) {
		if (b.gameOver(b.board)) return b.terminalValue(b.board, b.getPlayer());
		int v = Integer.MIN_VALUE;
		LinkedList<Move> actions = b.actions(b.board, b.currentPlayer);
		for (Move m : actions) {
			Board newBoard = b.makeMove(b, m);
			v = Math.max(v, minValueMinimax(newBoard));
		}
		return v;
	}
	
	public int minValueMinimax(Board b) {
		if (b.gameOver(b.board)) return b.terminalValue(b.board, b.getPlayer());
		int v = Integer.MAX_VALUE;
		LinkedList<Move> actions = b.actions(b.board, b.currentPlayer);
		for (Move m : actions) {
			Board newBoard = b.makeMove(b, m);
			v = Math.min(v, maxValueMinimax(newBoard));
		}
		return v;
	}
	
	/*public void printStuff1(int count, int terminalVal, Board b) { // delete
		b.printBoard(count);
		for (int i = 0; i < count; i++) System.out.print(" ");
		System.out.println("terminalValue = " + terminalVal);
	}
	
	public void printStuff2(int count, Move m, Board newBoard) { // delete
		for (int i = 0; i < count; i++) System.out.print(" ");
		int x = m.getX() + (int) 'a';
		char x1 = (char) x;
		int y = m.getY() + 1;
		if (m.getX() != -1) System.out.println(newBoard.getOpponent() + ", " + x1 + y);
		else System.out.println("Skip move");
		newBoard.printBoard(count);
	}*/
	
	public Move minimax(Board b) {
		LinkedList<Move> actions = b.actions(b.board, b.currentPlayer);
		int maxVal = Integer.MIN_VALUE;
		int minVal = Integer.MAX_VALUE;
		Move bestMove = null;
		for (Move m : actions) {
			Board newBoard = b.makeMove(b, m);
			int value;
			if (b.currentPlayer == 'x') value = minValueMinimax(newBoard);
			else value = maxValueMinimax(newBoard);
			if (b.currentPlayer == 'x' && value > maxVal) {
				maxVal = value;
				bestMove = m;
			}
			else if (b.currentPlayer == 'o' && value < minVal) {
				minVal = value;
				bestMove = m;
			}
		}
		return bestMove;
	}
	
	// MINIMAX ALPHA-BETA PRUNING
	public int maxValueAB(Board b, int alpha, int beta) {
		if (b.gameOver(b.board)) return b.terminalValue(b.board, b.getPlayer());
		int v = Integer.MIN_VALUE;
		LinkedList<Move> actions = b.actions(b.board, b.currentPlayer);
		for (Move m : actions) {
			Board newBoard = b.makeMove(b, m); //HERE
			v = Math.max(v, minValueAB(newBoard, alpha, beta));
			if (v >= beta) return v;
			alpha = Math.max(alpha, v);
		}
		return v;
	}
	
	public int minValueAB(Board b, int alpha, int beta) {
		if (b.gameOver(b.board)) return b.terminalValue(b.board, b.getPlayer());
		int v = Integer.MAX_VALUE;
		LinkedList<Move> actions = b.actions(b.board, b.currentPlayer);
		for (Move m : actions) {
			Board newBoard = b.makeMove(b, m); //HERE
			v = Math.min(v, maxValueAB(newBoard, alpha, beta));
			if (v <= alpha) return v;
			beta = Math.min(beta, v);
		}
		return v;
	}
	
	public Move abMinimax(Board b) {
		LinkedList<Move> actions = b.actions(b.board, b.currentPlayer);
		int maxVal = Integer.MIN_VALUE;
		int minVal = Integer.MAX_VALUE;
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		Move bestMove = null;
		for (Move m : actions) {
			Board newBoard = b.makeMove(b, m);
			int value;
			if (b.currentPlayer == 'x') value = minValueAB(newBoard, alpha, beta);
			else value = maxValueAB(newBoard, alpha, beta);
			if (b.currentPlayer == 'x' && value > maxVal) {
				maxVal = value;
				bestMove = m;
			}
			else if (b.currentPlayer == 'o' && value < minVal) {
				minVal = value;
				bestMove = m;
			}
		}
		return bestMove;
	}
	
	// HEURISTIC MINIMAX ALPHA-BETA PRUNING
	public int maxValueH(Board b, int alpha, int beta, int count, int length) {
		if (b.gameOver(b.board)) return b.terminalValue(b.board, b.getPlayer());
		int v = Integer.MIN_VALUE;
		LinkedList<Move> actions = b.actions(b.board, b.currentPlayer);
		int heuristic = b.numberOfMoves(b) + b.numberOfCorners(b) + b.numberOfSecondLine(b);
		if (count >= length) return heuristic;
		for (Move m : actions) {
			Board newBoard = b.makeMove(b, m);
			if (count < length) v = Math.max(v, minValueH(newBoard, alpha, beta, count + 1, length));
			if (v >= beta) return heuristic;
			alpha = Math.max(alpha, v);
		}
		return v;
	}
	
	public int minValueH(Board b, int alpha, int beta, int count, int length) {
		if (b.gameOver(b.board)) return b.terminalValue(b.board, b.getPlayer());
		int v = Integer.MAX_VALUE;
		LinkedList<Move> actions = b.actions(b.board, b.currentPlayer);
		int heuristic = b.numberOfMoves(b) + b.numberOfCorners(b) + b.numberOfSecondLine(b);
		if (count >= length) return heuristic;
		for (Move m : actions) {
			Board newBoard = b.makeMove(b, m);
			if (count < length) v = Math.min(v, maxValueH(newBoard, alpha, beta, count + 1, length));
			if (v <= alpha) return heuristic;
			beta = Math.min(beta, v);
		}
		return v;
	}
	
	public Move hMinimax(Board b, int length) {
		LinkedList<Move> actions = b.actions(b.board, b.currentPlayer);
		int maxVal = Integer.MIN_VALUE;
		int minVal = Integer.MAX_VALUE;
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		Move bestMove = null;
		for (Move m : actions) {
			Board newBoard = b.makeMove(b, m);
			int value;
			int count = 0;
			if (b.currentPlayer == 'x') value = minValueH(newBoard, alpha, beta, count, length);
			else value = maxValueH(newBoard, alpha, beta, count, length);
			if (b.currentPlayer == 'x' && value > maxVal) {
				maxVal = value;
				bestMove = m;
			}
			else if (b.currentPlayer == 'o' && value < minVal) {
				minVal = value;
				bestMove = m;
			}
		}
		return bestMove;
	}
}
