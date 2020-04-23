package ImageProcessing;


import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class ImageSimplifier {

  static final int WHITE_COLOR = -1;
  static final int BLACK_COLOR = -16777216;

  public static double[][] getSimplifiedPixelsArray(Image image,int color) {
    double[][] buffer = new double[(int) image.getWidth()][(int) image.getHeight()];
    for (int i = 0; i < image.getWidth(); i++) {
      for (int j = 0; j < image.getHeight(); j++) {
        if (image.getPixelReader().getArgb(i,j) == color) {
          buffer[i][j] = -1;
        } else {
          buffer[i][j] = 1;
        }
      }
    }
    return buffer;
  }
  public static double[][] getSimplifiedPixelsArray(Image image) {
    double[][] buffer = new double[(int) image.getWidth()][(int) image.getHeight()];
    for (int i = 0; i < image.getWidth(); i++) {
      for (int j = 0; j <image.getHeight(); j++) {
        if (image.getPixelReader().getArgb(i,j) == WHITE_COLOR) {
          buffer[i][j] = -1;
        } else {
          buffer[i][j] = 1;
        }
      }
    }

    return buffer;
  }
}
