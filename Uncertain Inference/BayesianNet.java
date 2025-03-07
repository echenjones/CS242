import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class BayesianNet {
	
	public ArrayList<BNNode> nodes = new ArrayList<BNNode>();
	
	public BayesianNet() {
		
	}
	
	public BayesianNet(ArrayList<BNNode> n) {
		nodes = n;
		topologicalSort();
	}
	
	/**
	 * Gets the variable of a node in a Bayesian network given a name
	 * @param name the name of the variable
	 * @return the variable with the corresponding name
	 */
	public Variable getVariable(String name) {
		for (BNNode n : nodes) {
			if (n.getVariable().getName().equals(name)) return n.getVariable();
		}
		return null;
	}
	
	/**
	 * Gets a node in a Bayesian network given a name
	 * @param name the name of a variable
	 * @return the node with the corresponding name
	 */
	public BNNode getNode(String name) {
		for (BNNode n : nodes) {
			if (n.getVariable().getName().equals(name)) return n;
		}
		return null;
	}
	
	/**
	 * Gets the nodes of a Bayesian network
	 * @return the nodes of a Bayesian network
	 */
	public ArrayList<BNNode> getNodes() {
		return nodes;
	}
	
	/**
	 * Gets a node in a Bayesian network given an index
	 * @param i the index
	 * @return the node in a Bayesian network at the index
	 */
	public BNNode getNodes(int i) {
		return nodes.get(i);
	}
	
	/**
	 * Adds a node to a Bayesian network
	 * @param n the node
	 */
	public void addNode(BNNode n) {
		nodes.add(n);
	}
	
	/**
	 * Gets the index of a given node in a Bayesian network
	 * @param n the node
	 * @return the index of the node
	 */
	public int nodeIndex(BNNode n) {
		return nodes.indexOf(n);
	}
	
	/**
	 * Topologically sorts a Bayesian network
	 */
	public void topologicalSort() {
		ArrayList<BNNode> temp = new ArrayList<BNNode>();
		for (BNNode n : nodes) temp.add(n);
		nodes.clear();
		int size = temp.size();
		while (nodes.size() < size) {
			BNNode added = null;
			for (BNNode n : temp) {
				boolean allParents = true;
				if (n.parents != null) {
					for (BNNode p : n.parents) {
						if (!nodes.contains(p)) {
							allParents = false;
							break;
						}
					}
				}
				if (allParents) {
					nodes.add(n);
					added = n;
					break;
				}
			}
			if (added != null) temp.remove(added);
		}
	}
	
	/**
	 * Normalizes the elements of a double array to add up to one
	 * @param q the array
	 * @return the normalized array
	 */
	public double[] normalize(double[] q) {
		double sum = 0;
		for (int i = 0; i < q.length; i++) sum += q[i];
		for (int i = 0; i < q.length; i++) {
			if (sum == 0) q[i] = 0;
			else q[i] /= sum;
		}
		return q;
	}
	
	/**
	 * Gets the element in a string array of values that corresponds with a node in a Bayesian
	 * network
	 * @param n the node
	 * @param e the array
	 * @return the element
	 */
	public String bindingLookup(BNNode n, String[] e) {
		int index = getNodes().indexOf(n);
		return e[index];
	}
	
	/**
	 * The enumeration-ask function from the textbook's pseudocode
	 * @param x the query variable
	 * @param e observed values
	 * @return a distribution over x
	 */
	public double[] enumerationAsk(BNNode x, String[] e) {
		double[] q = new double[x.getVariable().getCardinality()];
		for (int i = 0; i < x.getVariable().getCardinality(); i++) {
			int index = getNodes().indexOf(x);
			e[index] = x.getVariable().getValues(i);
			q[i] = enumerateAll(getNodes(), e);
		}
		return normalize(q);
	}
	
	/**
	 * The enumerate-all function from the textbook's pseudocode
	 * @param vars the nodes in a Bayesian network
	 * @param e observed values
	 * @return a real number
	 */
	public double enumerateAll(ArrayList<BNNode> vars, String[] e) {
		if (vars.isEmpty()) return 1;
		ArrayList<BNNode> newVars = new ArrayList<BNNode>();
		for (int i = 0; i < vars.size(); i++) newVars.add(vars.get(i));
		BNNode y = newVars.get(0);
		newVars.remove(0);
		int index = getNodes().indexOf(y);
		String[] newE = new String[e.length];
		for (int i = 0; i < e.length; i++) newE[i] = e[i];
		if (y.getParents() != null) {
			String[] parentValues = new String[y.getParents().size()];
			for (int i = 0; i < y.getParents().size(); i++) {
				parentValues[i] = bindingLookup(y.getParents(i), newE);
			}
			if (newE[index] != null) {
				return y.probabilityLookup(bindingLookup(y, newE), parentValues) * enumerateAll(newVars, newE);
			}
			else {
				double sum = 0;
				for (int i = 0; i < y.getVariable().getCardinality(); i++) {
					newE[index] = y.getVariable().getValues(i);
					sum += y.probabilityLookup(newE[index], parentValues) * enumerateAll(newVars, newE);
				}
				return sum;
			}
		}
		else {
			if (newE[index] != null) {
				return y.probabilityLookup(bindingLookup(y, newE), null) * enumerateAll(newVars, newE);
			}
			else {
				double sum = 0;
				for (int i = 0; i < y.getVariable().getCardinality(); i++) {
					newE[index] = y.getVariable().getValues(i);
					sum += y.probabilityLookup(newE[index], null) * enumerateAll(newVars, newE);
				}
				return sum;
			}
		}
	}
	
	/**
	 * Picks a random index in a double array
	 * @param d the array
	 * @return the index
	 */
	public int randomIndex(double[] d) {
		double rand = Math.random();
		double min = 0;
		double max = d[0];
		for (int i = 0; i < d.length; i++) {
			if ((rand < max) && (rand >= min)) return i;
			min += d[i];
			max += d[i + 1];
		}
		return -1;
	}
	
	/**
	 * The prior-sample function from the textbook's pseudocode
	 * @return an event sampled from the prior specified by the Bayesian network
	 */
	public ArrayList<String> priorSample() {
		ArrayList<String> x = new ArrayList<String>();
		for (int i = 0; i < getNodes().size(); i++) {
			BNNode n = getNodes(i);
			String[] parentValues = new String[n.getParents().size()];
			for (int j = 0; j < parentValues.length; j++) {
				parentValues[j] = x.get(nodeIndex(n.getParents(j)));
			}
			int index = randomIndex(n.rowLookup(parentValues));
			x.add(i, n.getVariable().getValues(index));
		}
		return x;
	}
	
	/**
	 * The rejection-sampling function from the textbook's pseudocode
	 * @param x the query variable
	 * @param e observed values
	 * @param n the total number of samples to be generated
	 * @return an estimate of the probability of x given e
	 */
	public double[] rejectionSampling(BNNode x, String[] e, int n) {
		double[] counts = new double[x.getVariable().getCardinality()];
		
		for (int i = 0; i < n; i++) {
			ArrayList<String> sample = priorSample();
			boolean consistent = true;
			for (int j = 0; j < e.length; j++) {
				if (e[j] != null && !sample.get(j).equals(e[j])) consistent = false;
			}
			if (consistent) {
				counts[x.getVariable().getIndex(sample.get(nodeIndex(x)))]++;
			}
		}
		return normalize(counts);
	}
	
	/**
	 * The likelihood-weighting function from the textbook's pseudocode
	 * @param x the query variable
	 * @param e observed values
	 * @param n the total number of samples to be generated
	 * @return an estimate of the probability of x given e
	 */
	public double[] likelihoodWeighting(BNNode x, String[] e, int n) {
		double[] weights = new double[x.getVariable().getCardinality()];
		
		for (int i = 0; i < n; i++) {
			ArrayList<String> sample = randomSample(e);
			double w = getWeight(sample, e);
			weights[x.getVariable().getIndex(sample.get(nodeIndex(x)))] += w;
		}
		return normalize(weights);
	}
	
	/**
	 * The weighted-sample function from the textbook's pseudocode, minus the random sample
	 * part
	 * @param sample a random sample
	 * @param e observed values
	 * @return a weight
	 */
	public double getWeight(ArrayList<String> sample, String[] e) {
		double w = 1;
		for (int i = 0; i < getNodes().size(); i++) {
			if (e[i] != null) {
				BNNode n = getNodes(i);
				String[] parentValues = new String[n.getParents().size()];
				for (int j = 0; j < parentValues.length; j++) {
					parentValues[j] = sample.get(nodeIndex(n.getParents(j)));
				}
				w *= n.probabilityLookup(sample.get(i), parentValues);
			}
		}
		return w;
	}
	
	/**
	 * The random sample part of the weighted-sample function from the textbook's pseudocode
	 * @param e observed values
	 * @return an event
	 */
	public ArrayList<String> randomSample(String[] e) {
		ArrayList<String> x = new ArrayList<String>();
		for (String s : e) x.add(s);
		for (int i = 0; i < getNodes().size(); i++) {
			if (e[i] == null){
				BNNode n = getNodes(i);
				String[] parentValues = new String[n.getParents().size()];
				for (int j = 0; j < parentValues.length; j++) {
					parentValues[j] = x.get(nodeIndex(n.getParents(j)));
				}
				int index = randomIndex(n.rowLookup(parentValues));
				x.set(i, n.getVariable().getValues(index));
			}
		}
		return x;
	}
	
	/**
	 * The gibbs-ask function from the textbook's pseudocode
	 * @param x the query variable
	 * @param e observed variables
	 * @param n the total number of samples to be generated
	 * @return an estimate of the probability of x given e
	 */
	public double[] gibbsAsk(BNNode x, String[] e, int n) {
		//local variables:
			//N, a vector of counts for each value of X, initially zero
			//Z, the nonevidence variables in bn
			//x, the current state of the network, initially copied from e
		
		//initialize x with random values for the variables in Z
		for (int i = 0; i < n; i++) {
			//for each Zi in Z do
				//set the value of Zi in x by sampling from P(Zi|mb(Zi))
				//N[x] = N[x] + 1 where x is the value of X in x
		}
		return normalize(null); //N
	}
 	
	/**
	 * Prints a Bayesian network
	 */
	public void printNetwork() {
		for (BNNode n : nodes) {
			n.printNode();
			System.out.println();
		}
	}
	
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
		XMLBIFParser parser = new XMLBIFParser();
		
		if (args[0].equals("exact") || args[0].equals("rejection") || args[0].equals("likelihood")) {
			int addend;
			int n = 0;
			if (args[0].equals("exact")) addend = 0;
			else {
				addend = 1;
				n = Integer.valueOf(args[1]);
			}
			BayesianNet bn = parser.readNetworkFromFile(args[1 + addend]);
			BNNode node = bn.getNode(args[2 + addend]);
			String[] e = new String[bn.getNodes().size()];
			for (int i = 3 + addend; i < args.length; i += 2) {
				BNNode evidence = bn.getNode(args[i]);
				String value = args[i + 1];
				e[bn.nodeIndex(evidence)] = value;
			}
			double[] d;
			if (args[0].equals("exact")) d = bn.enumerationAsk(node, e); // Exact Inference
			else if (args[0].equals("rejection")) d = bn.rejectionSampling(node, e, n); // Rejection Sampling
			else d = bn.likelihoodWeighting(node, e, n); // Likelihood Weighting
			for (int i = 0; i < d.length; i++) {
				System.out.println(node.getVariable().getValues(i) + " " + d[i]);
			}
		}
		else System.out.println("Invalid input");
	}
}
