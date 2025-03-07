import java.util.ArrayList;

public class UnaryConstraint {

	protected String name;
	protected Variable variable;
	protected ArrayList<String> values;
	
	public UnaryConstraint(String n, Variable var, ArrayList<String> val) {
		name = n;
		variable = var;
		values = val;
	}
	
	/**
	 * Gets the name of a unary constraint
	 * @return the name of a unary constraint
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the variable of a unary constraint
	 * @return the variable of a unary constraint
	 */
	public Variable getVariable() {
		return variable;
	}
	
	/**
	 * Gets the list of values of a unary constraint
	 * @return the list of values of a unary constraint
	 */
	public ArrayList<String> getValues() {
		return values;
	}
	
	/**
	 * Sets the name of a unary constraint
	 * @param n name
	 */
	public void setName(String n) {
		name = n;
	}
	
	/**
	 * Sets the variable of a unary constraint
	 * @param v variable
	 */
	public void setVariable(Variable v) {
		variable = v;
	}
	
	/**
	 * Sets the list of values of a unary constraint
	 * @param v list of values
	 */
	public void setValues(ArrayList<String> v) {
		values.clear();
		for (String s : v) values.add(s);
	}
	
	/**
	 * Adds a value to the list of values
	 * @param v value to add
	 */
	public void addValue(String v) {
		values.add(v);
	}
	
	/**
	 * Removes a specified value from the list of values
	 * @param v value to remove
	 */
	public void removeValue(String v) {
		for (int i = 0; i < values.size(); i++) {
			if (values.get(i).equals(v)) values.remove(i);
		}
	}
	
	/**
	 * Checks if an assignment of values is consistent with a unary constraint
	 * @param assignment assignment of values
	 * @return if the assignment is consistent
	 */
	public boolean consistent(ArrayList<String> assignment) {
		if (assignment.get(getVariable().getIndex()).equals("")) return true;
		for (int i = 0; i < getValues().size(); i++) {
			if (getValues().get(i) == assignment.get(getVariable().getIndex())) {
				return true;
			}
		}
		return false;
	}
}
