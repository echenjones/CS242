import java.util.ArrayList;

public class Search {

	/**
	 * The backtracking-search function from the textbook's pseudocode
	 * @param csp constraint satisfaction problem
	 * @return a solution or failure via the backtrack function
	 */
	public ArrayList<String> backtrackingSearch(ConstraintSatisfactionProblem csp) {
		ArrayList<String> assignment = new ArrayList<String>();
		for (int i = 0; i < csp.getVariables().size(); i++) assignment.add("");
		return backtrack(assignment, csp);
	}
	
	/**
	 * The backtrack function from the textbook's pseudocode
	 * @param assignment assignment of values
	 * @param csp constraint satisfaction problem
	 * @return a solution or failure
	 */
	public ArrayList<String> backtrack(ArrayList<String> assignment, ConstraintSatisfactionProblem csp) {
		if (!assignment.contains("")) return assignment;
		else {
			int index = assignment.indexOf("");
			Variable var = csp.getVariable(index);
			for (String s : var.getValues()) {
				assignment.set(index, s);
				boolean unaryTrue = true;
				boolean binaryTrue = true;
				for (UnaryConstraint c : csp.getUnaryConstraints()) {
					if (!c.consistent(assignment)) unaryTrue = false;
				}
				for (BinaryConstraint c : csp.getBinaryConstraints()) {
					if (!c.consistent(assignment)) binaryTrue = false;
				}
				ArrayList<String> a = null;
				if (unaryTrue && binaryTrue) {
					a = new ArrayList<String>();
					for (int i = 0; i < assignment.size(); i++) a.add(assignment.get(i));
					a = backtrack(a, csp);
				}
				if (a != null) return a;
				assignment.set(index, "");
			}
		}
		return null;
	}
}
