
public interface Layer {
  NDArray calc(NDArray input);
  NDArray backprop(NDArray ouputDerivatives);
  double derivativeSquareMagnitude();
  void updateWeights(double derivativeMag, double learningRate);
}
