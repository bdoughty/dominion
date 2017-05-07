package edu.brown.cs.dominion.AI.neuralNetwork;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class NeuralNetworkIO {
  public static void save(NeuralNetwork nn, String filename) {
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
      bw.write(Integer.toString(nn.layers[0].inputSize));
      for (FCLayer l : nn.layers) {
        bw.write(" ");
        bw.write(Integer.toString(l.outputSize));
      }
      bw.write("\n");
      for (FCLayer l : nn.layers) {
        writeLayer(bw, l);
      }
      bw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void writeLayer(BufferedWriter bw, FCLayer l)
      throws IOException {
    for (int input = 0; input < l.inputSize + 1; input++) {
      for (int output = 0; output < l.outputSize; output++) {
        bw.write(Double.toString(l.weights[input][output]));
        bw.write(" ");
      }
      bw.write("\n");
    }
  }

  public static NeuralNetwork load(String filename) {
    try {
      Scanner s = new Scanner(new File(filename));
      String[] Ssizes = s.nextLine().split(" ");
      int[] sizes = new int[Ssizes.length];
      for (int i = 0; i < Ssizes.length; i++) {
        sizes[i] = Integer.parseInt(Ssizes[i]);
      }

      NeuralNetwork nn = new NeuralNetwork(sizes);
      for (int i = 0; i < Ssizes.length - 2; i++) {
        nn.layers[i] =
            loadLayer(s, sizes[i], sizes[i + 1], ActivationFunction.RELU);
      }
      nn.layers[sizes.length - 2] = loadLayer(s, sizes[sizes.length - 2],
          sizes[sizes.length - 1], ActivationFunction.LINEAR);

      System.out.println(Arrays.toString(sizes));

      return nn;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static FCLayer loadLayer(Scanner s, int is, int os,
      ActivationFunction af) {
    FCLayer l = new FCLayer(is, os, af);
    for (int input = 0; input < l.inputSize + 1; input++) {
      String line = s.nextLine();
      String[] weights = line.split(" ");
      for (int output = 0; output < l.outputSize; output++) {
        l.weights[input][output] = Double.parseDouble(weights[output]);
      }
    }
    return l;
  }
}
