package DataProcessing;

import java.util.ArrayList;
import java.util.List;

public class Letter {

  private char name;
 private double[] pixels;

  public char getName() {
    return name;
  }

  public double[] getPixels() {
    return pixels;
  }

  public Letter(char name, double[][] pixels) {
    this.name = name;
    this.pixels = covertToOneDimensionalArray(pixels);
  }

  public Letter(char name, double[] pixels) {
    this.name = name;
    this.pixels = pixels;
  }

  private double[] covertToOneDimensionalArray(double[][] pixels) {
    double[] result = new double[pixels.length * pixels[0].length];
    int i = 0;
    for (var sublist :
            pixels) {
      for (var pixel :
              sublist) {
        result[i++] = pixel;
      }
    }
    return result;
  }
}
