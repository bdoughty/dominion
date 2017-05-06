import java.util.Arrays;

public class NeuralNetwork {
  FCLayer[] layers;
  double[] lastOutput;
  
  public NeuralNetwork(int... layerSizes){
    layers = new FCLayer[layerSizes.length - 1];
    for(int i = 0; i < layers.length - 1; i++){
      layers[i] = new FCLayer(
          layerSizes[i], 
          layerSizes[i + 1], 
          ActivationFunction.TANH);
    }
    layers[layers.length - 1] = new FCLayer(
        layerSizes[layers.length - 1], 
        layerSizes[layers.length], 
        ActivationFunction.LINEAR);
  }
  
  public double[] run(double[] input) {
    NDArray data = new NDArray(input);
    for (FCLayer l : layers) {
      data = l.calc(data);
    }
    lastOutput = data.toDoubleArray();
    return data.toDoubleArray();
  }
  
  public double[] runPrint(double[] input) {
    double[] data = run(input);
    System.out.println(Arrays.toString(data));
    return data;
  }
  
  public void backprop(double[] result, double learningRate) {
    double[] temp = diff(result, lastOutput);
    double diffMag = mag(temp);
    NDArray deriv = new NDArray(temp);
    for (int i = layers.length - 1; i >= 0; i--) {
      FCLayer l = layers[i];
      //System.out.println(Arrays.toString(deriv));
      deriv = l.backprop(deriv);
      //System.out.println(Arrays.toString(deriv));
    }
    double dsq = 0;
    for (FCLayer l : layers){
      dsq += l.derivativeSquareMagnitude();
    }
    double d = Math.sqrt(dsq);
    for (FCLayer l : layers){
      if(d > 0.0001){
        l.updateWeights(d, learningRate * diffMag);
      }
    }
  }
  
  private double[] clone(double[] a){
    double[] b = new double[a.length];
    for(int i = 0;i < a.length;i++){
      b[i] = a[i];
    }
    return b;
  }
  
  private double[] diff(double[] a, double[] b){
    double[] c = new double[a.length];
    for(int i = 0;i < a.length;i++){
      c[i] = a[i] - b[i];
    }
    return c;
  }
  
  private double mag(double[] a){
    double b = 0;
    for(int i = 0; i < a.length;i++){
      b += a[i] * a[i];
    }
    return Math.sqrt(b);
  }
}
