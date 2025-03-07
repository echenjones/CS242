import java.util.List;

public class LogisticClassifier extends LinearClassifier {
	
	public LogisticClassifier() {
		
	}
	
	public LogisticClassifier(double[] weights) {
		super(weights);
	}
	
	public LogisticClassifier(int ninputs) {
		super(ninputs);
	}
	
	/**
	 * A LogisticClassifier uses the logistic update rule
	 * (AIMA Eq. 18.8): w_i \leftarrow w_i+\alpha(y-h_w(x)) \times h_w(x)(1-h_w(x)) \times x_i 
	 */
	public void update(double[] x, double y, double alpha) {
		double est = eval(x);
		double delta = alpha * (y - est) * est * (1 - est); //1 instead of alpha
		weights[0] += delta;
		for (int i = 1; i < weights.length; i++) {
			weights[i] += delta * x[i - 1];
		}
	}
	
	/**
	 * A LogisticClassifier uses a 0/1 sigmoid threshold at z=0.
	 */
	public double threshold(double z) {
		return 1 / (1 + Math.exp(-z));
	}
	
	/**
	 * Runs the LogisticClassifier class on a file, with a given learning rate
	 * @param filename The filename
	 * @param learningRate The learning rate (decaying or constant)
	 */
	public void runLogistic(String filename, String learningRate) {
		LogisticClassifier c = new LogisticClassifier();
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