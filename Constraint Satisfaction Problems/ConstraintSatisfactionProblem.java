import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ConstraintSatisfactionProblem {

	private File file;
	private ArrayList<Variable> variables = new ArrayList<Variable>();
	private ArrayList<String> values = new ArrayList<String>();
	private ArrayList<UnaryConstraint> unaryConstraints = new ArrayList<UnaryConstraint>();
	private ArrayList<BinaryConstraint> binaryConstraints = new ArrayList<BinaryConstraint>();
	private int maxTime;
	
	public ConstraintSatisfactionProblem(String filename) {
		file = new File(filename);
	}
	
	/**
	 * Gets the list of variables in a constraint satisfaction problem
	 * @return the list of variables in a constraint satisfaction problem
	 */
	public ArrayList<Variable> getVariables() {
		return variables;
	}
	
	/**
	 * Gets a variable at a specified index in the list of variables in a constraint satisfaction
	 * problem
	 * @param i index
	 * @return the variable at the index
	 */
	public Variable getVariable(int i) {
		return getVariables().get(i);
	}
	
	/**
	 * Gets the list of possible values in a constraint satisfaction problem
	 * @return the list of possible values in a constraint satisfaction problem
	 */
	public ArrayList<String> getValues() {
		return values;
	}
	
	/**
	 * Gets a value at a specified index in the list of possible values in a constraint satisfaction
	 * problem
	 * @param i index
	 * @return the value at the index
	 */
	public String getValue(int i) {
		return getValues().get(i);
	}
	
	/**
	 * Gets the list of unary constraints in a constraint satisfaction problem
	 * @return the list of unary constraints in a constraint satisfaction problem
	 */
	public ArrayList<UnaryConstraint> getUnaryConstraints() {
		return unaryConstraints;
	}
	
	/**
	 * Gets the list of binary constraints in a constraint satisfaction problem
	 * @return the list of binary constraints in a constraint satisfaction problem
	 */
	public ArrayList<BinaryConstraint> getBinaryConstraints() {
		return binaryConstraints;
	}
	
	/**
	 * Sets the list of variables in a constraint satisfaction problem
	 * @param vars list of variables
	 */
	public void setVariables(ArrayList<Variable> vars) {
		variables.clear();
		for (Variable v : vars) variables.add(v);
	}
	
	/**
	 * Sets a variable at a specified index in the list of variables in a constraint satisfaction
	 * problem
	 * @param i index
	 * @param v variable
	 */
	public void setVariable(int i, Variable v) {
		variables.set(i, v);
	}
	
	/**
	 * Sets the list of possible values in a constraint satisfaction problem
	 * @param vals list of possible values
	 */
	public void setValues(ArrayList<String> vals) {
		values.clear();
		for (String s : vals) values.add(s);
	}
	
	/**
	 * Sets a value at a specified index in the list of possible values in a constraint satisfaction
	 * problem
	 * @param i index
	 * @param s value
	 */
	public void setValue(int i, String s) {
		values.set(i, s);
	}
	
	/**
	 * Sets the list of unary constraints in a constraint satisfaction problem
	 * @param cons list of unary constraints
	 */
	public void setUnaryConstraints(ArrayList<UnaryConstraint> cons) {
		unaryConstraints.clear();
		for (UnaryConstraint c : cons) unaryConstraints.add(c);
	}
	
	/**
	 * Sets the list of binary constraints in a constraint satisfaction problem
	 * @param cons list of binary constraints
	 */
	public void setBinaryConstraints(ArrayList<BinaryConstraint> cons) {
		binaryConstraints.clear();
		for (BinaryConstraint c : cons) binaryConstraints.add(c);
	}
	
	/**
	 * Reads a map coloring constraint satisfaction problem from a file and assigns variables, values,
	 * and constraints from the information given
	 */
	public void readMapFile() {
		try {
			Scanner scan1 = new Scanner(file);
			scan1.nextLine();
			String varLine = scan1.nextLine();
			String[] temp1 = varLine.split(" ");
			for (int i = 0; i < temp1.length; i++) {
				Variable v = new Variable(temp1[i]);
				v.setIndex(i);
				variables.add(v);
			}
			for (int i = 0; i < getVariables().size(); i++) scan1.nextLine();
			String valLine = scan1.nextLine();
			String[] temp2 = valLine.split(" ");
			for (String s : temp2) values.add(s);
			for (Variable v : getVariables()) v.setValues(getValues());
			while (scan1.hasNextLine()) {
				String line = scan1.nextLine();
				String[] lineArray = line.split(" ");
				Variable v = new Variable(lineArray[0], indexOf(lineArray[0]));
				v.setValues(getValues());
				if (lineArray[1].equals("=")) {
					UnaryConstraint c = newUnaryEqualityConstraint(v, lineArray[2]);
					unaryConstraints.add(c);
				}
				else if (lineArray[1].equals("!=")) {
					UnaryConstraint c = newUnaryInequalityConstraint(v, lineArray[2]);
					unaryConstraints.add(c);
				}
			}
			scan1.close();
			
			Scanner scan2 = new Scanner(file);
			for (int i = 0; i < 2; i++) scan2.nextLine();
			for (int i = 0; i < getVariables().size(); i++) {
				String line = scan2.nextLine();
				String[] lineArray = line.split(" ");
				Variable v1 = getVariables().get(indexOf(lineArray[0]));
				for (int j = 1; j < lineArray.length; j++) {
					Variable v2 = getVariables().get(indexOf(lineArray[j]));
					BinaryConstraint c = newBinaryInequalityConstraint(v1, v2);
					binaryConstraints.add(c);
				}
			}
			scan2.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads a job-shop scheduling constraint satisfaction problem from a file and assigns variables,
	 * values, and constraints from the information given
	 */
	public void readJobFile() {
		try {
			Scanner scan = new Scanner(file);
			scan.nextLine();
			
			String varLine = scan.nextLine();
			String[] temp1 = varLine.split(" ");
			for (int i = 0; i < temp1.length; i++) {
				Variable v = new Variable(temp1[i]);
				v.setIndex(i);
				variables.add(v);
			}
			
			for (int i = 0; i < getVariables().size(); i++) {
				String line = scan.nextLine();
				String[] temp2 = line.split(" ");
				Variable v = getVariables().get(i);
				v.setLength(Integer.valueOf(temp2[1]));
			}
			
			maxTime = scan.nextInt();
			scan.nextLine();
			
			for (int i = 1; i <= maxTime; i++) values.add(Integer.toString(i));
			for (Variable v : getVariables()) v.setValues(getValues());
			
			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				String[] lineArray = line.split(" ");
				Variable v1 = getVariables().get(indexOf(lineArray[0]));
				Variable v2 = getVariables().get(indexOf(lineArray[2]));
				if (lineArray[1].equals("before")) {
					BinaryConstraint c = newPrecedenceConstraint(v1, v2);
					binaryConstraints.add(c);
				}
				else if (lineArray[1].equals("disjoint")) {
					BinaryConstraint c = newDisjointConstraint(v1, v2);
					binaryConstraints.add(c);
				}
			}
			
			scan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Runs a map coloring constraint satisfaction problem
	 */
	public void newMapCSP() {
		readMapFile();
		Search s = new Search();
		ArrayList<String> a = s.backtrackingSearch(this);
		if (a != null) printCSP(a);
		else System.out.println("No solution");
	}
	
	/**
	 * Runs a job-shop scheduling constraint satisfaction problem
	 */
	public void newJobCSP() {
		readJobFile();
		Search s = new Search();
		ArrayList<String> a = s.backtrackingSearch(this);
		if (a != null) printCSP(a);
		else System.out.println("No solution");
	}
	
	/**
	 * Finds the index of a variable, given its name, in the list of variables in a constraint
	 * satisfaction problem
	 * @param var variable name
	 * @return index of variable or -1 if it is not included
	 */
	public int indexOf(String var) {
		for (int i = 0; i < getVariables().size(); i++) {
			if (getVariables().get(i).getName().equals(var)) return i;
		}
		return -1;
	}
	
	/**
	 * Creates a unary constraint, given a variable and a value from an equality
	 * @param var variable
	 * @param val value
	 * @return unary constraint
	 */
	public UnaryConstraint newUnaryEqualityConstraint(Variable var, String val) {
		String name = var.getName() + " = " + val;
		ArrayList<String> vals = new ArrayList<String>();
		for (String s : var.getValues()) vals.add(s);
		UnaryConstraint c = new UnaryConstraint(name, var, vals);
		for (String s : c.getValues()) {
			if (!s.equals(val)) c.removeValue(s);
		}
		return c;
	}
	
	/**
	 * Creates a unary constraint, given a variable and a value from an inequality
	 * @param var variable
	 * @param val value
	 * @return unary constraint
	 */
	public UnaryConstraint newUnaryInequalityConstraint(Variable var, String val) {
		String name = var.getName() + " != " + val;
		ArrayList<String> vals = new ArrayList<String>();
		for (String s : var.getValues()) vals.add(s);
		UnaryConstraint c = new UnaryConstraint(name, var, vals);
		for (String s : c.getValues()) {
			if (s.equals(val)) c.removeValue(s);
		}
		return c;
	}
	
	/**
	 * Creates a binary constraint, given two variables from an inequality
	 * @param var1 first variable
	 * @param var2 second variable
	 * @return binary constraint
	 */
	public BinaryConstraint newBinaryInequalityConstraint(Variable var1, Variable var2) {
		String name = var1.getName() + " != " + var2.getName();
		BinaryConstraint c = new BinaryConstraint(name, var1, var2);
		for (int i = 0; i < var1.getValues().size(); i++) {
			for (int j = 0; j < var2.getValues().size(); j++) {
				if (var1.getValues().get(i) != var2.getValues().get(j)) {
					c.addValue1(var1.getValues().get(i));
					c.addValue2(var2.getValues().get(j));
				}
			}
		}
		return c;
	}
	
	/**
	 * Adds a list of tuples where the first variable comes before the second variable to a binary
	 * constraint
	 * @param c binary constraint
	 * @param var1 first variable
	 * @param var2 second variable
	 */
	public void createBeforeTuples(BinaryConstraint c, Variable var1, Variable var2) {
		for (int i = 1; i <= maxTime - var1.getLength() - var2.getLength() + 1; i++) {
			for (int j = i + var1.getLength(); j <= maxTime - var2.getLength() + 1; j++) {
				c.addValue1(Integer.toString(i));
				c.addValue2(Integer.toString(j));
			}
		}
	}
	
	/**
	 * Adds a list of tuples where the first variable comes after the second variable to a binary
	 * constraint
	 * @param c binary constraint
	 * @param var1 first variable
	 * @param var2 second variable
	 */
	public void createAfterTuples(BinaryConstraint c, Variable var1, Variable var2) {
		for (int i = 1; i <= maxTime - var1.getLength() - var2.getLength() + 1; i++) {
			for (int j = i + var2.getLength(); j <= maxTime - var1.getLength() + 1; j++) {
				c.addValue1(Integer.toString(j));
				c.addValue2(Integer.toString(i));
			}
		}
	}
	
	/**
	 * Creates a precedence constraint, given two variables
	 * @param var1 first variable
	 * @param var2 second variable
	 * @return precedence constraint
	 */
	public BinaryConstraint newPrecedenceConstraint(Variable var1, Variable var2) {
		String name = var1.getName() + " before " + var2.getName();
		BinaryConstraint c = new BinaryConstraint(name, var1, var2);
		createBeforeTuples(c, var1, var2);
		return c;
	}
	
	/**
	 * Creates a disjunctive constraint, given two variables
	 * @param var1
	 * @param var2
	 * @return disjunctive constraint
	 */
	public BinaryConstraint newDisjointConstraint(Variable var1, Variable var2) {
		String name = var1.getName() + " disjoint " + var2.getName();
		BinaryConstraint c = new BinaryConstraint(name, var1, var2);
		createBeforeTuples(c, var1, var2);
		createAfterTuples(c, var2, var1);
		return c;
	}
	
	/**
	 * Prints the variables and corresponding values to a constraint satisfaction problem solution
	 * @param a list of values
	 */
	public void printCSP(ArrayList<String> a) {
		for (int i = 0; i < getVariables().size(); i++) {
			System.out.println(getVariable(i).getName() + " " + a.get(i));
		}
	}
	
	public static void main(String[] args) {
		String type = args[0];
		if (type.equals("map")) {
			ConstraintSatisfactionProblem mapColoring = new ConstraintSatisfactionProblem(args[1]);
			mapColoring.newMapCSP();
		}
		else if (type.equals("job")) {
			ConstraintSatisfactionProblem jobShopScheduling = new ConstraintSatisfactionProblem(args[1]);
			jobShopScheduling.newJobCSP();
		}
		else System.out.println("Type not found");
	}
}
