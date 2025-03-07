import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

abstract public class LinearClassifier {
	
	public double[] weights;
	
	public LinearClassifier() {
		
	}
	
	public LinearClassifier(double[] weights) {
		this.weights = weights;
	}
	
	public LinearClassifier(int ninputs) {
		this.weights = new double[ninputs];
	}
	
	public double[] getWeights() {
		return weights;
	}
	
	public void setWeights(double[]  w) {
		for (int i = 0; i < weights.length; i++) weights[i] = w[i];
	}
	
	public void initializeWeights(int ninputs) {
		weights = new double[ninputs];
	}
	
	/**
	 * Reads a file and creates a list of examples with inputs and an output
	 * @param filename The filename
	 * @return The list of examples
	 */
	public List<Example> readFile(String filename) {
		List<Example> examples = new ArrayList<Example>();
		File file = new File(filename);
		try {
			Scanner scan = new Scanner(file);
			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				String[] arr = line.split(",");
				double[] inputs = new double[arr.length - 1];
				for(int i = 0; i < arr.length - 1; i++) {
					inputs[i] = Double.parseDouble(arr[i]);
				}
				double output = Double.parseDouble(arr[arr.length - 1]);
				Example e = new Example(inputs, output);
				examples.add(e);
			}
			scan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return examples;
	}
	
	/**
	 * Update the weights of this LinearClassifer using the given
	 * inputs/output example and learning rate alpha.
	 */
	abstract public void update(double[] x, double y, double alpha);

	/**
	 * Threshold the given value using this LinearClassifier's
	 * threshold function.
	 */
	abstract public double threshold(double z);

	/**
	 * Evaluate the given input vector using this LinearClassifier
	 * and return the output value.
	 * This value is: Threshold(w \cdot x)
	 */
	public double eval(double[] x) {
		return threshold(VectorOps.dot1(this.weights, x));
	}
	
	/**
	 * Train this LinearClassifier on the given Examples for the
	 * given number of steps, using given learning rate schedule.
	 * "Typically the learning rule is applied one example at a time,
	 * choosing examples at random (as in stochastic gradient descent)."
	 * See AIMA p. 724.
	 */
	public void train(List<Example> examples, int nsteps, LearningRateSchedule schedule) {
		Random random = new Random();
		int n = examples.size();
		for (int i = 1; i <= nsteps; i++) {
			int j = random.nextInt(n);
			Example ex = examples.get(j);
			this.update(ex.inputs, ex.output, schedule.alpha(i));
			this.trainingReport(examples, i, nsteps);
		}
	}
	
	public void train(List<Example> examples, int nsteps, int alpha) {
		Random random = new Random();
		int n = examples.size();
		for (int i = 1; i <= nsteps; i++) {
			int j = random.nextInt(n);
			Example ex = examples.get(j);
			this.update(ex.inputs, ex.output, alpha);
			this.trainingReport(examples, i, nsteps);
		}
	}

	/**
	 * Train this LinearClassifier on the given Examples for the
	 * given number of steps, using given constant learning rate.
	 */
	public void train(List<Example> examples, int nsteps, double constant_alpha) {
		train(examples, nsteps, new LearningRateSchedule() {
			public double alpha(int t) { return constant_alpha; }
		});
	}
	
	/**
	 * This method is called after each weight update during training.
	 * Subclasses can override it to gather statistics or update displays.
	 */
	protected void trainingReport(List<Example> examples, int stepnum, int nsteps) {
		System.out.println(stepnum + "\t" + accuracy(examples)); //
	}
	
	/**
	 * Return the squared error per example (Mean Squared Error) for this
	 * LinearClassifier on the given Examples.
	 * The Mean Squared Error is the total L_2 loss divided by the number
	 * of samples.
	 */
	public double squaredErrorPerSample(List<Example> examples) {
		double sum = 0.0;
		for (Example ex : examples) {
			double result = eval(ex.inputs);
			double error = ex.output - result;
			sum += error*error;
		}
		return sum / examples.size();
	}

	/**
	 * Return the proportion of the given Examples that are classified
	 * correctly by this LinearClassifier.
	 * This is probably only meaningful for classifiers that use
	 * a hard threshold. Use with care.
	 */
	public double accuracy(List<Example> examples) {
		int ncorrect = 0;
		for (Example ex : examples) {
			double result = eval(ex.inputs);
			if (Math.round(result) == ex.output) {
				ncorrect += 1;
			}
		}
		return (double)ncorrect / examples.size();
	}
	
	public static void main(String[] args) {
		if (args[0].equals("p")) {
			PerceptronClassifier c = new PerceptronClassifier();
			c.runPerceptron(args[1], args[2]);
		}
		else if (args[0].equals("l")) {
			LogisticClassifier c = new LogisticClassifier();
			c.runLogistic(args[1], args[2]);
		}
		else System.out.println("Invalid input");
	}

}