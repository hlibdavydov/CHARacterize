package ImageProcessing;


import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageCropper {

  public static Color color;

  public static BufferedImage cropImage(BufferedImage imageToCrop) {
    int minX = getMinX(imageToCrop);
    int maxX = getMaxX(imageToCrop);
    int minY = getMinY(imageToCrop);
    int maxY = getMaxY(imageToCrop);
    BufferedImage subimage = imageToCrop.getSubimage(minX, minY, maxX - minX, maxY - minY);
    return subimage;
  }

  private static int getMinY(BufferedImage buff) {
    int min = 0;
    for (int y = 0; y < buff.getHeight(); y++) {
      for (int x = 0; x < buff.getWidth(); x++) {
        if (colorEqualsTo(buff, y, x)) {
          return min;
        }
      }
      min++;
    }
    return min;
  }

  private static boolean colorEqualsTo(BufferedImage buff, int y, int x) {
    return !(new Color(buff.getRGB(x, y)).equals(Color.WHITE));
  }

  private static int getMaxY(BufferedImage buff) {
    int max = buff.getHeight();
    for (int y = buff.getHeight() - 1; y >= 0; y--) {
      for (int x = buff.getWidth() - 1; x >= 0; x--) {
        if (colorEqualsTo(buff, y, x)) {
          return max;
        }
      }
      max--;
    }
    return max;
  }

  private static int getMaxX(BufferedImage buff) {
    int max = buff.getWidth();
    for (int x = buff.getWidth() - 1; x >= 0; x--) {
      for (int y = buff.getHeight() - 1; y >= 0; y--) {
        if (colorEqualsTo(buff, y, x)) {
          return max;
        }
      }
      max--;
    }
    return max;
  }

  private static int getMinX(BufferedImage buff) {
    int min = 0;
    for (int x = 0; x < buff.getWidth(); x++) {
      for (int y = 0; y < buff.getHeight(); y++) {
        if (colorEqualsTo(buff, y, x)) {
          return min;
        }
      }
      min++;
    }
    return min;
  }
}
