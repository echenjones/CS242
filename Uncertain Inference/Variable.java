import java.util.ArrayList;

public class Variable {
	
	public String name;
	public ArrayList<String> values;
	public int cardinality = 0;
	
	public Variable(String n, ArrayList<String> v) {
		name = n;
		values = v;
		setCardinality(values);
	}
	
	/**
	 * Gets the name of a variable
	 * @return the name of a variable
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the values of a variable
	 * @return the values of a variable
	 */
	public ArrayList<String> getValues() {
		return values;
	}
	
	/**
	 * Gets the value of a variable at a given index
	 * @param i the index
	 * @return the value of the variable at the index
	 */
	public String getValues(int i) {
		return values.get(i);
	}
	
	/**
	 * Gets the cardinality of a variable's values
	 * @return the cardinality of a variable's values
	 */
	public int getCardinality() {
		return cardinality;
	}
	
	/**
	 * Gets the index of a value given the value
	 * @param v the value
	 * @return the index
	 */
	public int getIndex(String v) {
		for (int i = 0; i < getCardinality(); i++) {
			if (getValues(i).equals(v)) return i;
		}
		return -1;
	}
	
	/**
	 * Sets the name of a variable
	 * @param n the name
	 */
	public void setName(String n) {
		name = n;
	}
	
	/**
	 * Sets the values of a variable
	 * @param v
	 */
	public void setValues(ArrayList<String> v) {
		values.clear();
		for (String s : v) values.add(s);
	}
	
	/**
	 * Sets the cardinality of a variable's values
	 * @param v the values
	 */
	public void setCardinality(ArrayList<String> v) {
		cardinality = v.size();
	}
	
	/**
	 * Checks if a variable is equal to a given variable
	 * @param v the given variable
	 * @return if the variables are equal
	 */
	public boolean equals(Variable v) {
		boolean same = true;
		if (getName().equals(v.getName())) {
			for (int i = 0; i < getCardinality(); i++) {
				if (!getValues(i).equals(v.getValues(i))) same = false;
			}
		}
		return same;
	}
}
