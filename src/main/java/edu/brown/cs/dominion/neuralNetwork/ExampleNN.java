
public class ExampleNN {
  public static double[][] test = {{1, 5, 7}, {2, 3, 9}};
  
  public static void main(String[] args) { 
    NeuralNetwork nn = new NeuralNetwork(1, 5, 5, 1);
    //System.out.println(nn.run(new double[]{1})[0]);
    
    
    double l = 1;
    for(int i = 0;i < 500000;i++){
      double a = Math.random() * 2 - 1;
      nn.run(new double[]{a});
      nn.backprop(new double[]{1/(1+Math.exp(-1 * 3 * a)) + Math.sin(5 * a)}, l);
    }
    
    for(int i = -100;i < 100;i+=2){
      System.out.println(String.format("%.4f", nn.run(new double[]{i / 100.0})[0]));
    }    
  }
}
