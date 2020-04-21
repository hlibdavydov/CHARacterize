package ImageProcessing;


import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class ImageSimplifier {

  private static final int WHITE_COLOR = -1;
  private static final int BLACK_COLOR = -16777216;
  public static final String BLACK = "0x000000ff";

  public static double[][] getSimplifiedPixelsArray(Image image) {
    double[][] buffer = new double[(int) image.getWidth()][(int) image.getHeight()];
    for (int i = 0; i < image.getWidth(); i++) {
      for (int j = 0; j < image.getHeight(); j++) {
        if (image.getPixelReader().getArgb(i,j) == BLACK_COLOR) {
          buffer[i][j] = -1;
        } else {
          buffer[i][j] = 1;
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
    System.out.println("==========================================");*/
    return buffer;
  }
}
