import java.util.LinkedList;
import java.util.Random;

public class Search {

	public Search() {
		
	}
	
	
	// RANDOM
	
	/**
	 * Chooses a random move on a board from a list of valid moves
	 * @param b the board
	 * @return the move
	 */
	public Move random(Board b) {
		LinkedList<Move> actions = b.actions(b.board, b.getPlayer());
		Random rand = new Random();
		Move m = actions.get(rand.nextInt(actions.size()));
		return m;
	}
	
	// MINIMAX
	
	/**
	 * The max-value function for minimax from the textbook's pseudocode
	 * @param b the board
	 * @return a utility value
	 */
	public int maxValueMinimax(Board b) {
		if (b.gameOver(b.board)) return b.terminalValue(b.board);
		int v = Integer.MIN_VALUE;
		LinkedList<Move> actions = b.actions(b.board, b.getPlayer());
		for (Move m : actions) {
			Board newBoard = b.makeMove(b, m);
			v = Math.max(v, minValueMinimax(newBoard));
		}
		return v;
	}
	
	/**
	 * The min-value function for minimax from the textbook's pseudocode
	 * @param b the board
	 * @return a utility value
	 */
	public int minValueMinimax(Board b) {
		//b.printBoard();
		if (b.gameOver(b.board)) return b.terminalValue(b.board);
		int v = Integer.MAX_VALUE;
		LinkedList<Move> actions = b.actions(b.board, b.getPlayer());
		for (Move m : actions) {
			Board newBoard = b.makeMove(b, m);
			v = Math.min(v, maxValueMinimax(newBoard));
		}
		return v;
	}
	
	/**
	 * The minimax-decision function from the textbook's pseudocode
	 * @param b the board
	 * @return an action
	 */
	public Move minimax(Board b) {
		LinkedList<Move> actions = b.actions(b.board, b.getPlayer());
		int maxVal = Integer.MIN_VALUE;
		int minVal = Integer.MAX_VALUE;
		Move bestMove = null;
		for (Move m : actions) {
			Board newBoard = b.makeMove(b, m);
			int value;
			if (b.getPlayer() == 'x') value = minValueMinimax(newBoard);
			else value = maxValueMinimax(newBoard);
			if (b.getPlayer() == 'x' && value > maxVal) {
				maxVal = value;
				bestMove = m;
			}
			else if (b.getPlayer() == 'o' && value < minVal) {
				minVal = value;
				bestMove = m;
			}
		}
		return bestMove;
	}
	
	// MINIMAX ALPHA-BETA PRUNING
	
	/**
	 * The max-value function for alpha-beta pruning from the textbook's pseudocode
	 * @param b the board
	 * @param alpha the alpha
	 * @param beta the beta
	 * @return a utility value
	 */
	public int maxValueAB(Board b, int alpha, int beta) {
		if (b.gameOver(b.board)) return b.terminalValue(b.board);
		int v = Integer.MIN_VALUE;
		LinkedList<Move> actions = b.actions(b.board, b.getPlayer());
		for (Move m : actions) {
			Board newBoard = b.makeMove(b, m); //HERE
			v = Math.max(v, minValueAB(newBoard, alpha, beta));
			if (v >= beta) return v;
			alpha = Math.max(alpha, v);
		}
		return v;
	}
	
	/**
	 * The min-value function for alpha-beta pruning from the textbook's pseudocode
	 * @param b the board
	 * @param alpha the alpha
	 * @param beta the beta
	 * @return a utility value
	 */
	public int minValueAB(Board b, int alpha, int beta) {
		if (b.gameOver(b.board)) return b.terminalValue(b.board);
		int v = Integer.MAX_VALUE;
		LinkedList<Move> actions = b.actions(b.board, b.getPlayer());
		for (Move m : actions) {
			Board newBoard = b.makeMove(b, m); //HERE
			v = Math.min(v, maxValueAB(newBoard, alpha, beta));
			if (v <= alpha) return v;
			beta = Math.min(beta, v);
		}
		return v;
	}
	
	/**
	 * The alpha-beta-search function from the textbook's pseudocode
	 * @param b the board
	 * @return an action
	 */
	public Move abMinimax(Board b) {
		LinkedList<Move> actions = b.actions(b.board, b.getPlayer());
		int maxVal = Integer.MIN_VALUE;
		int minVal = Integer.MAX_VALUE;
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		Move bestMove = null;
		for (Move m : actions) {
			Board newBoard = b.makeMove(b, m);
			int value;
			if (b.getPlayer() == 'x') value = minValueAB(newBoard, alpha, beta);
			else value = maxValueAB(newBoard, alpha, beta);
			if (b.getPlayer() == 'x' && value > maxVal) {
				maxVal = value;
				bestMove = m;
			}
			else if (b.getPlayer() == 'o' && value < minVal) {
				minVal = value;
				bestMove = m;
			}
		}
		return bestMove;
	}
	
	// HEURISTIC MINIMAX ALPHA-BETA PRUNING
	
	/**
	 * The max-value function for heuristic minimax with alpha-beta pruning to a given depth
	 * from the textbook's pseudocode
	 * @param b the board
	 * @param alpha the alpha
	 * @param beta the beta
	 * @param count the count of how deep the search is
	 * @param depth the depth
	 * @return a utility value
	 */
	public int maxValueH(Board b, int alpha, int beta, int count, int depth) {
		if (b.gameOver(b.board)) return b.terminalValue(b.board);
		int v = Integer.MIN_VALUE;
		LinkedList<Move> actions = b.actions(b.board, b.getPlayer());
		if (count >= depth) return b.numberOfConnectKs(b, b.getPlayer()) - b.numberOfConnectKs(b, b.getOpponent());
		for (Move m : actions) {
			Board newBoard = b.makeMove(b, m);
			if (count < depth) v = Math.max(v, minValueH(newBoard, alpha, beta, count + 1, depth));
			if (v >= beta) return b.numberOfConnectKs(b, b.getPlayer()) - b.numberOfConnectKs(b, b.getOpponent());;
			alpha = Math.max(alpha, v);
		}
		return v;
	}
	
	/**
	 * The min-value function for heuristic minimax with alpha-beta pruning to a given depth
	 * from the textbook's pseudocode
	 * @param b the board
	 * @param alpha the alpha
	 * @param beta the beta
	 * @param count the count of how deep the search is
	 * @param depth the depth
	 * @return a utility value
	 */
	public int minValueH(Board b, int alpha, int beta, int count, int depth) {
		if (b.gameOver(b.board)) return b.terminalValue(b.board);
		int v = Integer.MAX_VALUE;
		LinkedList<Move> actions = b.actions(b.board, b.getPlayer());
		if (count >= depth) return b.numberOfConnectKs(b, b.getPlayer()) - b.numberOfConnectKs(b, b.getOpponent());
		for (Move m : actions) {
			Board newBoard = b.makeMove(b, m);
			if (count < depth) v = Math.min(v, maxValueH(newBoard, alpha, beta, count + 1, depth));
			if (v <= alpha) return b.numberOfConnectKs(b, b.getPlayer()) - b.numberOfConnectKs(b, b.getOpponent());;
			beta = Math.min(beta, v);
		}
		return v;
	}
	
	/**
	 * The h-minimax function from the textbook's pseudocode
	 * @param b the board
	 * @param depth the depth
	 * @return an action
	 */
	public Move hMinimax(Board b, int depth) {
		LinkedList<Move> actions = b.actions(b.board, b.getPlayer());
		int maxVal = Integer.MIN_VALUE;
		int minVal = Integer.MAX_VALUE;
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		Move bestMove = null;
		for (Move m : actions) {
			Board newBoard = b.makeMove(b, m);
			int value;
			int count = 0;
			if (b.getPlayer() == 'x') value = minValueH(newBoard, alpha, beta, count, depth);
			else value = maxValueH(newBoard, alpha, beta, count, depth);
			if (b.getPlayer() == 'x' && value > maxVal) {
				maxVal = value;
				bestMove = m;
			}
			else if (b.getPlayer() == 'o' && value < minVal) {
				minVal = value;
				bestMove = m;
			}
		}
		return bestMove;
	}
}
