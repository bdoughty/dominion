package edu.brown.cs.dominion.AI.neuralNetwork;

public class FCLayer implements Layer{
  ActivationFunction af;
  public int inputSize;
  public int outputSize;
  public double[][] weights;
  double[] inputs;
  double[] preActOuputDerivatives;
  double[] outputs;
  double[] outputsNotActivated;

  public FCLayer(int inputSize, int outputSize, ActivationFunction af){
    this.af = af;
    this.inputSize = inputSize;
    this.outputSize = outputSize;

    this.outputs = new double[outputSize];
    this.outputsNotActivated = new double[outputSize];
    this.preActOuputDerivatives = new double[outputSize];
    this.inputs = new double[inputSize];

    this.weights = new double[inputSize + 1][outputSize];

    for (int output = 0; output < outputSize; output++){
      for (int input = 0; input < inputSize + 1; input++){
        weights[input][output] = Math.random() * 2 - 1;
      }
    }

  }

  @Override
  public NDArray calc(NDArray in) {
    double[] inputVals = in.data;
    assert inputVals.length == inputSize;

    this.inputs = clone(inputVals);

    for (int output = 0; output < outputSize; output++){
      outputsNotActivated[output] = 0;
      for (int input = 0; input < inputSize; input++){
        outputsNotActivated[output] += weights[input + 1][output] * inputVals[input];
      }
      outputsNotActivated[output] += weights[0][output];
      outputs[output] = af.eval.eval(outputsNotActivated[output]);
    }

    return new NDArray(outputs);
  }

  @Override
  public NDArray backprop(NDArray outputDerivs) {
    double[] outputDerivatives = outputDerivs.toDoubleArray();

    preActOuputDerivatives = new double[outputSize];
    for (int output = 0; output < outputSize; output++) {
      preActOuputDerivatives[output] =
          af.deriv.eval(outputsNotActivated[output], outputs[output]) * outputDerivatives[output];
    }

    double[] inputDerivatives = new double[inputSize];

    for (int input = 0; input < inputSize; input++){
      inputDerivatives[input] = 0;
      for (int output = 0; output < outputSize; output++){
        inputDerivatives[input] += weights[input + 1][output] * preActOuputDerivatives[output];
      }
    }
    return new NDArray(inputDerivatives);
  }

  private double[] clone(double[] a){
    double[] b = new double[a.length];
    for(int i = 0;i < a.length;i++){
      b[i] = a[i];
    }
    return b;
  }

  public void updateWeights(double dtot, double learningRate) {
    for (int output = 0; output < outputSize; output++){
      for (int input = 0; input < inputSize; input++){
        double d = inputs[input] * preActOuputDerivatives[output];
        weights[input + 1][output] += d / dtot * learningRate;
      }
      double d = preActOuputDerivatives[output];
      weights[0][output] += d / dtot * learningRate;
    }
  }

  @Override
  public double derivativeSquareMagnitude() {
    double dsq = 0;
    for (int output = 0; output < outputSize; output++){
      for (int input = 0; input < inputSize; input++){
        double d = inputs[input] * preActOuputDerivatives[output];
        dsq += d * d;
      }
      double d = preActOuputDerivatives[output];
      dsq += d * d;
    }
    return dsq;
  }


}
