package Controllers;

import DataProcessing.Letter;
import DataProcessing.NeuralNetwork;
import DataProcessing.Perceptron;
import GUI.Main;
import ImageProcessing.ImageCropper;
import ImageProcessing.ImageSimplifier;
import ImageProcessing.LearningImagesVisitor;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.DirectoryChooser;
import javafx.stage.Popup;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainLayoutController implements Initializable {

  @FXML
  public Canvas canvas;
  public Slider lineWidthSlider;
  public ColorPicker colorPicker;
  public ImageView imageView;
  public TextField guessedLetter;
  public TextArea infoTextField;
  public MenuItem teachNeuralNetworkButton;
  private GraphicsContext canvasContext;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    canvasContext = canvas.getGraphicsContext2D();
    canvasContext.setFill(Color.WHITE);
    canvasContext.setLineWidth(lineWidthSlider.getValue());
    canvasContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    lineWidthSlider.valueChangingProperty().addListener(observable -> {
      if (!lineWidthSlider.isValueChanging()) {
        canvasContext.setLineWidth(lineWidthSlider.getValue());
      }
    });
    colorPicker.valueProperty().addListener(observable ->
            canvasContext.setStroke(colorPicker.getValue()));
  }

  public void makeSnapshot() {
    WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
    SnapshotParameters parameters = new SnapshotParameters();
    canvas.snapshot(parameters, writableImage);
    File file = new File("D:/Projects/CHARacterize/src/Snapshots/Snapshot.png");
    BufferedImage buff = SwingFXUtils.fromFXImage(writableImage, null);
    var resized = ImageCropper.cropImage(buff);
    try {
      ImageIO.write(resized, "png", file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public void continueDrawing(MouseEvent mouseEvent) {
    canvasContext.lineTo(mouseEvent.getX(), mouseEvent.getY());
    canvasContext.stroke();
  }

  public void startDrawing(MouseEvent mouseEvent) {
    canvasContext.beginPath();
    canvasContext.moveTo(mouseEvent.getX(), mouseEvent.getY());
    canvasContext.stroke();
  }

  public void clearCanvas(ActionEvent actionEvent) {
    Paint fill = canvasContext.getFill();
    canvasContext.setFill(Color.WHITE);
    canvasContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    canvasContext.setFill(fill);
  }

  public void displaySnapshot(ActionEvent actionEvent) throws IOException {
    makeSnapshot();
    InputStream inputStream = new FileInputStream("D:\\Projects\\CHARacterize\\src\\Snapshots\\Snapshot.png");
    Image image = new Image(inputStream, LearningImagesVisitor.WIDTH_OF_IMAGE, LearningImagesVisitor.HIGH_OF_IMAGE, false, false);
    imageView.setImage(image);
  }

  public void displayDirectoryChoosingDialog(ActionEvent actionEvent) throws IOException {
    Parent choosingDialog = FXMLLoader.load(getClass().getResource("Layouts/ImagesLoadingDialog.fxml"));
    ImagesLoadingController.getChoosingDirectoriesDialog().getContent().setAll(choosingDialog);
    ImagesLoadingController.getChoosingDirectoriesDialog().show(Main.mainStage);
  }

  public void loadDataToNeuralNetwork(ActionEvent actionEvent) {
    var directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Choose directory of your data to load");
    var file = directoryChooser.showDialog(Main.mainStage);
    var learningImageVisitor = new LearningImagesVisitor(file.toPath());
    ProgressIndicator indicator = new ProgressIndicator();
    indicator.progressProperty().bind(learningImageVisitor.progressProperty());
    indicator.setMinSize(150, 150);
    Popup loadingPopup = new Popup();
    loadingPopup.getContent().add(indicator);
    loadingPopup.show(Main.mainStage);
    Task<Void> task = new Task<>() {
      @Override
      protected Void call() throws Exception {
        learningImageVisitor.processDir();
        indicator.setVisible(false);
        loadingPopup.hide();
        return null;
      }
    };
    new Thread(task).start();
  }

  public void teachNeuralNetwork(ActionEvent actionEvent) {
    ProgressIndicator indicator = new ProgressIndicator();
    indicator.setMinSize(150, 150);
    indicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
    Popup popup = new Popup();
    popup.getContent().add(indicator);
    popup.show(Main.mainStage);
    Task<Void> task = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        NeuralNetwork.teachNetwork();
        indicator.setVisible(false);
        popup.hide();
        return null;
      }
    };
    ExecutorService executorService =  Executors.newFixedThreadPool(1);
    executorService.execute(task);
    executorService.shutdown();
  }



  public void guessLetter(ActionEvent actionEvent) throws FileNotFoundException {
    makeSnapshot();
    infoTextField.clear();
    InputStream inputStream = new FileInputStream("D:\\Projects\\CHARacterize\\src\\Snapshots\\Snapshot.png");
    Image image = new Image(inputStream, LearningImagesVisitor.WIDTH_OF_IMAGE, LearningImagesVisitor.HIGH_OF_IMAGE, false, false);
    Letter letter = new Letter('?', ImageSimplifier.getSimplifiedPixelsArray(image));

    double[] outputs = NeuralNetwork.predictOutputOfLetter(letter);
    char predictedLetter = NeuralNetwork.getPredictedLetterFor(letter);
    for (int i = 0; i < outputs.length; i++) {
      infoTextField.appendText((char) ('A' + i) + ": " + outputs[i] + "\n");
    }
    guessedLetter.setText(String.valueOf(predictedLetter));
  }

  public void resetPerceptrons(ActionEvent actionEvent) {
    NeuralNetwork.resetPerceptrons();
  }

  public void clearNetwork(ActionEvent actionEvent) {
    NeuralNetwork.resetNetwork();
  }

  public void startTests(ActionEvent actionEvent) {
    double accuracy = NeuralNetwork.getAccuracy();
    infoTextField.appendText(String.format("Accuracy: %.2f", accuracy)+"%\n");
  }


  public void showPropertiesOfNeuralNetwork(ActionEvent actionEvent) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("Layouts/NeuralNetworkProperties.fxml"));
    Parent window = loader.load();
    NeuralNetworkPropertiesController controller = loader.getController();
    Popup popup = controller.popup;
    popup.getContent().setAll(window);
    popup.show(Main.mainStage);
  }

  public void clearLogs(ActionEvent actionEvent) {
    infoTextField.clear();
  }

  public void buildNeuralNetwork(ActionEvent actionEvent) {
    teachNeuralNetworkButton.setDisable(false);
    NeuralNetwork.buildNetwork();

  }
}
