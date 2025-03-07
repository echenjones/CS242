import java.util.ArrayList;

public class BinaryConstraint {

	protected String name;
	protected Variable variable1;
	protected Variable variable2;
	protected ArrayList<String> values1 = new ArrayList<String>();
	protected ArrayList<String> values2 = new ArrayList<String>();
	
	public BinaryConstraint(String n, Variable var1, Variable var2) {
		name = n;
		variable1 = var1;
		variable2 = var2;
	}
	
	/**
	 * Gets the name of a binary constraint
	 * @return the name of a binary constraint
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the first variable of a binary constraint
	 * @return the first variable of a binary constraint
	 */
	public Variable getVariable1() {
		return variable1;
	}
	
	/**
	 * Gets the second variable of a binary constraint
	 * @return the second variable of a binary constraint
	 */
	public Variable getVariable2() {
		return variable2;
	}
	
	/**
	 * Gets the list of values corresponding to the first variable in a binary constraint
	 * @return the list of values corresponding to the first variable in a binary constraint
	 */
	public ArrayList<String> getValues1() {
		return values1;
	}
	
	/**
	 * Gets the list of values corresponding to the second variable in a binary constraint
	 * @return the list of values corresponding to the second variable in a binary constraint
	 */
	public ArrayList<String> getValues2() {
		return values2;
	}
	
	/**
	 * Sets the name of a binary constraint
	 * @param n name
	 */
	public void setName(String n) {
		name = n;
	}
	
	/**
	 * Sets the first variable of a binary constraint
	 * @param v variable
	 */
	public void setVariable1(Variable v) {
		variable1 = v;
	}
	
	/**
	 * Sets the second variable of a binary constraint
	 * @param v variable
	 */
	public void setVariable2(Variable v) {
		variable2 = v;
	}
	
	/**
	 * Sets the list of values corresponding to the first variable in a binary constraint
	 * @param v list of values corresponding to the first variable
	 */
	public void setValues1(ArrayList<String> v) {
		values1.clear();
		for (String s : v) values1.add(s);
	}
	
	/**
	 * Sets the list of values corresponding to the second variable in a binary constraint
	 * @param v list of values corresponding to the second variable
	 */
	public void setValues2(ArrayList<String> v) {
		values2.clear();
		for (String s : v) values2.add(s);
	}
	
	/**
	 * Adds a value to the list of values corresponding to the first variable in a binary
	 * constraint
	 * @param v value to add
	 */
	public void addValue1(String v) {
		values1.add(v);
	}
	
	/**
	 * Adds a value to the list of values corresponding to the second variable in a binary
	 * constraint
	 * @param v value to add
	 */
	public void addValue2(String v) {
		values2.add(v);
	}
	
	/**
	 * Removes a specified value from the list of values corresponding to the first variable
	 * in a binary constraint
	 * @param v value to remove
	 */
	public void removeValue1(String v) {
		for (int i = 0; i < values1.size(); i++) {
			if (values1.get(i).equals(v)) values1.remove(i);
		}
	}
	
	/**
	 * Removes a specified value from the list of values corresponding to the second variable
	 * in a binary constraint
	 * @param v value to remove
	 */
	public void removeValue2(String v) {
		for (int i = 0; i < values2.size(); i++) {
			if (values2.get(i).equals(v)) values2.remove(i);
		}
	}
	
	/**
	 * Checks if an assignment of values is consistent with a binary constraint
	 * @param assignment assignment of values
	 * @return if the assignment is consistent
	 */
	public boolean consistent(ArrayList<String> assignment) {
		if (assignment.get(getVariable1().getIndex()).equals("") || 
				assignment.get(getVariable2().getIndex()).equals("")) {
			return true;
		}
		for (int i = 0; i < getValues1().size(); i++) {
			if (getValues1().get(i).equals(assignment.get(getVariable1().getIndex())) && 
					getValues2().get(i).equals(assignment.get(getVariable2().getIndex()))) {
				return true;
			}
		}
		return false;
	}
}
