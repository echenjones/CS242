import java.util.Arrays;

/**
 * An example is a vector of double values for the inputs and single
 * double value for the output.
 * Note that the first value of the vector should always be 1.0
 * (which is multiplied by the first weight, w_0, to give the
 * constant term in the function's expression. See AIMA p. 721.
 */
public class Example {
	
	public double[] inputs;
	public double[] outputs;
	public double output;
	
	// LC
	public Example(double[] inputs, double output) {
		this.inputs = inputs;
		this.output = output;
	}
	
	// LC
	public Example(int ninputs) {
		this(new double[ninputs], 0.0);
	}
	
	// NN
	public Example(double[] inputs, double[] outputs) {
		this.inputs = inputs;
		this.outputs = outputs;
	}
	
	// NN
	public Example(int ninputs, int noutputs) {
		this(new double[ninputs], new double[noutputs]);
	}

	public String toStringLC() {
		return "<" + Arrays.toString(inputs) + ", " + output + ">";
	}
	
	public String toStringNN() {
		return Arrays.toString(inputs) + " -> " + Arrays.toString(outputs);
	}

}
