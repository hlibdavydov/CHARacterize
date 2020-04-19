package ImageProcessing;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class ImageLoader implements FileVisitor<Path> {

  private String pathToImageSet;
  private String pathToDirectoryWhereToStoreImages;
  private boolean cropImage;
  private String currentlyWorkingDirectory;
  private DoubleProperty progress = new SimpleDoubleProperty(0);
  private int numberOfDirectoriesProcessed = 0;


  public DoubleProperty progressProperty() {
    return progress;
  }

  public ImageLoader(String pathToImageSet, String pathToDirectoryWhereToStoreImages, boolean cropImage) {
    this.pathToImageSet = pathToImageSet;
    this.pathToDirectoryWhereToStoreImages = pathToDirectoryWhereToStoreImages;
    this.cropImage = cropImage;
  }


  public void processDir() {
    try {
      Files.walkFileTree(Paths.get(pathToImageSet), this);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
    if (!dir.getFileName().toString().matches("[a-zA-Z]")) {
      return FileVisitResult.CONTINUE;
    }
    createNewDirectoryToStoreDataFor(dir);
    return FileVisitResult.CONTINUE;
  }

  private void createNewDirectoryToStoreDataFor(Path dir) throws IOException {
    File directory = new File(pathToDirectoryWhereToStoreImages + "/" + dir.getFileName());
    Files.deleteIfExists(directory.toPath());
    if (!directory.mkdir()) {
      System.out.println("Failed to crete directory" + directory.getAbsolutePath());
    }
    currentlyWorkingDirectory = directory.toString();
  }

  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    if (new File(file.toString()).isDirectory()) {
      return FileVisitResult.CONTINUE;
    }
    BufferedImage image = ImageIO.read(new FileInputStream(file.toAbsolutePath().toString()));
    if (image == null) {
      return FileVisitResult.CONTINUE;
    }
    if (cropImage) {
      image = ImageCropper.cropImage(image);
    }
    ImageIO.write(image, "png", new File(currentlyWorkingDirectory + "/" + file.getFileName()));
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
    numberOfDirectoriesProcessed++;
    progress.setValue(numberOfDirectoriesProcessed/26.0);
    return FileVisitResult.CONTINUE;
  }
}
