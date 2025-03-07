import java.util.List;

public class PerceptronClassifier extends LinearClassifier {
	
	public PerceptronClassifier() {
		
	}
	
	public PerceptronClassifier(double[] weights) {
		super(weights);
	}
	
	public PerceptronClassifier(int ninputs) {
		super(ninputs);
	}
	
	/**
	 * A PerceptronClassifier uses the perceptron learning rule
	 * (AIMA Eq. 18.7): w_i \leftarrow w_i+\alpha(y-h_w(x)) \times x_i 
	 */
	public void update(double[] x, double y, double alpha) {
		double delta = y - eval(x);
		weights[0] += alpha * delta; // 1 instead of alpha
		for (int i = 1; i < weights.length; i++) {
			weights[i] += alpha * delta * x[i - 1]; // 1 instead of alpha
		}
	}
	
	/**
	 * A PerceptronClassifier uses a hard 0/1 threshold.
	 */
	public double threshold(double z) {
		if (z > 0) return 1;
		else return 0;
	}
	
	/**
	 * Runs the PerceptronClassifier class on a file, with a given learning rate
	 * @param filename The filename
	 * @param learningRate The learning rate (decaying or constant)
	 */
	public void runPerceptron(String filename, String learningRate) {
		PerceptronClassifier c = new PerceptronClassifier();
		List<Example> e = c.readFile(filename);
		c.initializeWeights(e.get(0).inputs.length + 1);
		if (learningRate.equals("0")) {
			LearningRateSchedule l = new DecayingLearningRateSchedule();
			c.train(e, 1000000, l);
		}
		else if (learningRate.equals("1")) c.train(e, 1000000, 1);
		else System.out.println("Invalid input");
	}
	
}