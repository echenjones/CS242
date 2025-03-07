import java.util.ArrayList;

public class BNNode {

	Variable var;
	ArrayList<BNNode> parents;
	ArrayList<Double> table;
	
	public BNNode(Variable v, ArrayList<BNNode> p, ArrayList<Double> t) {
		var = v;
		parents = p;
		table = t;
	}
	
	/**
	 * Gets the variable of a node
	 * @return the variable of a node
	 */
	public Variable getVariable() {
		return var;
	}
	
	/**
	 * Gets the parents of a node
	 * @return the parents of a node
	 */
	public ArrayList<BNNode> getParents() {
		return parents;
	}
	
	/**
	 * Gets a parent of a node given an index
	 * @param i the index
	 * @return the parent of the node at the index
	 */
	public BNNode getParents(int i) {
		return parents.get(i);
	}
	
	/**
	 * Gets the conditional probability table of a node
	 * @return the conditional probability table of a node
	 */
	public ArrayList<Double> getTable() {
		return table;
	}
	
	/**
	 * Gets a probability in the conditional probability table of a node given an index
	 * @param i the index
	 * @return the probability at the index
	 */
	public Double getTable(int i) {
		return table.get(i);
	}
	
	/**
	 * Sets the variable of a node
	 * @param v the variable
	 */
	public void setVariable(Variable v) {
		var = v;
	}
	
	/**
	 * Sets the parents of a node
	 * @param p the parents
	 */
	public void setParents(ArrayList<BNNode> p) {
		parents.clear();
		for (BNNode n : p) parents.add(n);
	}
	
	/**
	 * Sets the conditional probability table of a node
	 * @param t the conditional probability table
	 */
	public void setTable(ArrayList<Double> t) {
		table.clear();
		for (Double d : t) table.add(d);
	}
	
	/**
	 * Gets an index of a conditional probability table, a one-dimensional array acting as a
	 * multi-dimensional array, given a set of coordinates
	 * @param coordinates the coordinates
	 * @return the index
	 */
	public int lookupIndex(int[] coordinates) {
		int[] bases = new int[1 + getParents().size()];
		bases[0] = getVariable().getCardinality();
		for (int i = 1; i < bases.length; i++) {
			bases[bases.length - i] = getParents(i - 1).getVariable().getCardinality();
		}
		int index = 0;
		int x = 1;
		for (int i = 0; i < coordinates.length; i++) {
			index += coordinates[i] * x;
			x *= bases[i];
		}
		return index;
	}
	
	/**
	 * Gets a probability in a conditional probability table given a node value and the parent
	 * values
	 * @param nodeValue the value of a node
	 * @param parentValues the values of the parents
	 * @return the probability
	 */
	public double probabilityLookup(String nodeValue, String[] parentValues) {
		if (parentValues == null || parentValues.length == 0) {
			int index = getVariable().getValues().indexOf(nodeValue);
			if (index == -1) return -1;
			else return getTable(index);
		}
		else {
			int[] coordinates = new int[getParents().size() + 1];
			for (int i = 0; i < coordinates.length; i++) coordinates[i] = -1;
			coordinates[0] = getVariable().getValues().indexOf(nodeValue);
			int index = 0;
			for (String s : parentValues) {
				coordinates[getParents().size() - index] = getParents(index).getVariable().getValues().indexOf(s);
				index++;
			}
			for (int i : coordinates) if (i == -1) return -1;
			return getTable(lookupIndex(coordinates));
		}
	}
	
	/**
	 * Gets a row in a conditional probability table given a node's parent values
	 * @param parentValues the parent values
	 * @return the row
	 */
	public double[] rowLookup(String[] parentValues) {
		int length = getVariable().getCardinality();
		double[] row = new double[length];
		for (int i = 0; i < row.length; i++) {
			row[i] = probabilityLookup(getVariable().getValues(i), parentValues);
		}
		return row;
	}
	
	/**
	 * Prints the name, parents, and conditional probability table of a node
	 */
	public void printNode() {
		System.out.println("node = " + getVariable().getName());
		System.out.print("parents = [");
		if (getParents().isEmpty()) System.out.println("]");
		else {
			for (int i = 0; i < getParents().size() - 1; i++) {
				System.out.print(getParents(i).getVariable().getName() + ", ");
			}
			System.out.println(getParents(getParents().size() - 1).getVariable().getName() + "]");
		}
		printTable();
	}
	
	/**
	 * Prints a row in a conditional probability table
	 * @param n index
	 * @param indices the parent values
	 */
	public void printRow(int n, String[] indices) {
		if (n == 0) {
			for (int i = 0; i < indices.length; i++) {
				String index = indices[i];
				System.out.print(index.substring(0, Math.min(7, index.length())) + "\t");
			}
			System.out.print("|");
			for (int i = 0; i < getVariable().getCardinality(); i++) {
				System.out.print("\t" + String.format("%.5f", probabilityLookup(getVariable().getValues(i), indices)));
			}
			System.out.println();
		}
		else {
			Variable parent = getParents(n - 1).getVariable();
			for (int i = 0; i < parent.getCardinality(); i++) {
				indices[n - 1] = parent.getValues(i);
				printRow(n - 1, indices);
			}
		}
	}
	
	/**
	 * Prints a conditional probability table
	 */
	public void printTable() {
		int length = 0;
		System.out.println(getVariable().getName());
		if (!getParents().isEmpty()) {
			for (int i = 0; i < getParents().size(); i++) {
				String name = getParents(i).getVariable().getName();
				System.out.print(name.substring(0, Math.min(7, name.length())) + "\t");
				length += 8;
			}
			System.out.print("|");
			length += 8;
			for (int i = 0; i < getVariable().getCardinality(); i++) {
				String value = getVariable().getValues(i);
				System.out.print("\t" + value.substring(0, Math.min(7, value.length())));
				length += 8;
			}
			System.out.println();
			for (int i = 0; i < length; i++) System.out.print("-");
			System.out.println();
			
			printRow(getParents().size(), new String[getParents().size()]);
			System.out.println();
		}
		else {
			for (int i = 0; i < getVariable().getCardinality(); i++) {
				String value = getVariable().getValues(i);
				System.out.print(value.substring(0, Math.min(7, value.length())) + "\t");
				length += 8;
			}
			System.out.println();
			for (int i = 0; i < length; i++) System.out.print("-");
			System.out.println();
			for (double d : getTable()) {
				System.out.print(String.format("%.5f", d) + "\t");
			}
			System.out.println("\n");
		}
	}
}
