package ImageProcessing;

import DataProcessing.Letter;
import DataProcessing.NeuralNetwork;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

public class LearningImagesVisitor implements FileVisitor<Path> {

  public static final double PROPORTION_OF_TRAINING_DATA_IN_SET = 0.80;
  public static final int WIDTH_OF_IMAGE = 28;
  public static final int HIGH_OF_IMAGE = 28;
  int numberOfFilesWithinTrainingSet;
  int numberOfCurrentFile;
  int numberOfFoldersProcessed;
  double averageBlackPixels = 0;
  private DoubleProperty progress = new SimpleDoubleProperty(0);
  Path startingDir;



  public DoubleProperty progressProperty() {
    return progress;
  }

  public LearningImagesVisitor(Path startingDir) {
    this.startingDir = startingDir;
  }

  public void processDir() {
    try {
      Files.walkFileTree(startingDir, this);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
    if (!dir.getFileName().toString().matches("[a-zA-Z]")) {
      return FileVisitResult.CONTINUE;
    }
    double numberOfFiles = Objects.requireNonNull(dir.toFile().listFiles()).length;
    numberOfFilesWithinTrainingSet = (int) Math.round(numberOfFiles * PROPORTION_OF_TRAINING_DATA_IN_SET);
    numberOfCurrentFile = 0;
    averageBlackPixels = 0;
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    InputStream inputStream = new FileInputStream(file.toFile());
    Image image = new Image(inputStream, WIDTH_OF_IMAGE, HIGH_OF_IMAGE, false, false);
    char nameOfLetter = file.getParent().getFileName().toString().toUpperCase().charAt(0);
    var pixels = ImageSimplifier.getSimplifiedPixelsArray(image);
    Letter letter = new Letter(nameOfLetter, pixels);
    if (numberOfCurrentFile <= numberOfFilesWithinTrainingSet) {
      NeuralNetwork.getTrainingData().add(letter);
    } else {
      NeuralNetwork.getTestData().add(letter);
    }
    numberOfCurrentFile++;
    return FileVisitResult.CONTINUE;
  }


  @Override
  public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
    progress.setValue(numberOfFoldersProcessed / 26.0);
    numberOfFoldersProcessed++;
    return FileVisitResult.CONTINUE;
  }
}
