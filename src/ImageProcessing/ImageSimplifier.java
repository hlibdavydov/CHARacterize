package ImageProcessing;


import javafx.scene.image.Image;

import java.util.Arrays;

public class ImageSimplifier {

  private static final int WHITE_COLOR = -1;
  public static int i = 0;

  public static double[][] getSimplifiedPixelsArray(Image image) {
    double[][] buffer = new double[(int) image.getWidth()][(int) image.getHeight()];
    for (int i = 0; i < image.getWidth(); i++) {
      for (int j = 0; j < image.getHeight(); j++) {
        if (image.getPixelReader().getArgb(i, j) != WHITE_COLOR) {
          buffer[i][j] = 1;
        } else {
          buffer[i][j] = -1;
        }
      }
    }
/*    for (var n :
            buffer) {
      for (var p :
              n) {
        System.out.print((char) p);
      }
      System.out.println();
    }
    System.out.println("==========================================" + i++);*/
    return buffer;
  }
}
