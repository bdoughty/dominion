import java.awt.event.WindowEvent;

public class ConvolutionLayer implements Layer{
  NDArray weights;
  int cx;
  int cy;

  @Override
  public NDArray calc(NDArray input) {
    assert input.getDimension() == weights.getDimension();
    
    NDArray output = new NDArray(input.getDimensions());
    input.forEach(loc -> {
       output.set(input.get(loc), loc);
    });
    
    return output;
  }

  @Override
  public NDArray backprop(NDArray ouputDerivatives) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public double derivativeSquareMagnitude() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void updateWeights(double derivativeMag, double learningRate) {
    // TODO Auto-generated method stub
    
  }

}
