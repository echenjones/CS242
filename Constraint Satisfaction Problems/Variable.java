import java.util.ArrayList;

public class Variable {

	protected String name;
	protected int index;
	protected ArrayList<String> values = new ArrayList<String>();
	protected int length;
	
	public Variable(String n) {
		name = n;
	}
	
	public Variable(String n, int i) {
		name = n;
		index = i;
	}
	
	/**
	 * Gets the name of a variable
	 * @return the name of a variable
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the index of a variable in a corresponding CSP's list of variables
	 * @return the index of a variable in a corresponding CSP's list of variables
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Gets the list of possible values of a variable
	 * @return the list of possible values of a variable
	 */
	public ArrayList<String> getValues() {
		return values;
	}
	
	/**
	 * Gets the length of a task in a job shop scheduling CSP
	 * @return the length of a task in a job shop scheduling CSP
	 */
	public int getLength() {
		return length;
	}
	
	/**
	 * Sets the name of a variable
	 * @param n name
	 */
	public void setName(String n) {
		name = n;
	}
	
	/**
	 * Sets the index of a variable in a corresponding CSP's list of variables
	 * @param i index
	 */
	public void setIndex(int i) {
		index = i;
	}
	
	/**
	 * Sets the list of possible values of a variable
	 * @param v list of possible values
	 */
	public void setValues(ArrayList<String> v) {
		values.clear();
		for (String s : v) values.add(s);
	}
	
	/**
	 * Sets the length of a task in a job shop scheduling CSP
	 * @param l length of a task
	 */
	public void setLength(int l) {
		length = l;
	}
	
	/**
	 * Adds a value to the list of possible values of a variable
	 * @param v value to add
	 */
	public void addValue(String v) {
		values.add(v);
	}
	
	/**
	 * Removes a value from the list of possible values of a variable
	 * @param v value to remove
	 */
	public void removeValue(String v) {
		for (int i = 0; i < values.size(); i++) {
			if (values.get(i).equals(v)) values.remove(i);
		}
	}
	
	/**
	 * Clears the list of possible values of a variable
	 */
	public void clearValues() {
		values.clear();
	}
}
