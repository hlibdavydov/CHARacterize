package GUI;

import DataProcessing.NeuralNetwork;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

  public static Stage mainStage;
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("Layouts/MainLayout.fxml"));
    mainStage = primaryStage;
    primaryStage.setTitle("CHARacterize");
    primaryStage.getIcons().add(new Image("Resources/icon.jpg"));
    primaryStage.setScene(new Scene(root, 1000, 650));
    primaryStage.setMaximized(true);
    primaryStage.show();
  }
}
