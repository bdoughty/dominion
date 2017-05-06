import java.util.function.Consumer;
import java.util.function.Function;

public class NDArray {
  int[] dimSizes;
  double[] data;
  
  public NDArray(double[][] input){
    dimSizes = new int[] { input.length, input[0].length };
    data = new double[length()];
    for(int x = 0; x < dimSizes[0]; x++){
      for(int y = 0; y < dimSizes[1]; y++){
        data[x + y * dimSizes[0]] = input[x][y];
      }
    }
  }
  
  public NDArray(int... dimensions){
    dimSizes = dimensions;
    data = new double[length()];
  }
  
  public int[] getDimensions(){
    return dimSizes;
  }
  
  private int length(){
    int l = 1;
    for(int i = 0;i < dimSizes.length;i++){
      l *= dimSizes[i];
    }
    return l;
  }
  
  public int size(){
    return data.length;
  }
  
  public NDArray(double[] input){
    dimSizes = new int[] { input.length };
    data = new double[length()];
    for(int i = 0;i < data.length;i++){
      this.data[i] = input[i];
    }
  }
  
  public int getDimension() {
    return dimSizes.length;
  }
  
  public double[] toDoubleArray(){
    assert getDimension() == 1;
    return data;
  }
  
  public double get(int... loc){
    assert withinBounds(loc);
    return data[getIndex(loc)];
  }
  
  public void set(double val, int... loc){
    assert withinBounds(loc);
    data[getIndex(loc)] = val;
  }
  
  public double getSafe(int... loc){
    if(withinBounds(loc)){
      return data[getIndex(loc)];
    } else {
      return 0;
    }
  }
  
  public int getIndex(int[] loc){
    int ind = 0;
    int mag = 1;
    for(int i = 0;i < loc.length;i++){
      ind += loc[i] * mag;
      mag *= dimSizes[i];
    }
    return ind;
  }
  
  public void forEach(Consumer<int[]> fun) {
    for (int i = 0;i < data.length;i++) {
      int[] loc = new int[dimSizes.length];
      int j = i;
      
      for (int k = 0; k < dimSizes.length; k++) {
        loc[k] = j % dimSizes[k];
        j /= dimSizes[k];
      }
      
      fun.accept(loc);
    }
  }
  
  private boolean withinBounds(int[] loc){
    if(loc.length != dimSizes.length){
      return false;
    }
    for(int i = 0;i < loc.length;i++){
      if (loc[i] >= dimSizes[i] || loc[i] < 0){
        return false;
      }
    }
    return true;
  }
}
