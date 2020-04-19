package Controllers;

import GUI.Main;
import ImageProcessing.ImageLoader;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Popup;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ImagesLoadingController implements Initializable {

  private static Popup choosingDirectoriesDialog = new Popup();
  public CheckBox cropImagesCheckBock;
  public TextField pathToImagesTextField;
  public TextField pathToStoreProcessedImagesTextField;

  public static Popup getChoosingDirectoriesDialog() {
    return choosingDirectoriesDialog;
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
  }

  public void hidePopup(ActionEvent actionEvent) {
    choosingDirectoriesDialog.hide();
  }

  public void browsePathToImageSet(ActionEvent mouseEvent) {
    var directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Choose directory of images");
    choosingDirectoriesDialog.hide();
    var path = directoryChooser.showDialog(Main.mainStage);
    if (path != null) {
      pathToImagesTextField.setText(path.toString());
    }
    choosingDirectoriesDialog.show(Main.mainStage);
  }

  public void browsePathWhereToStoreImages(ActionEvent mouseEvent) {
    var directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Choose directory where to store processed images");
    choosingDirectoriesDialog.hide();
    var path = directoryChooser.showDialog(Main.mainStage);
    if (path != null) {
      pathToStoreProcessedImagesTextField.setText(path.toString());
    }
    choosingDirectoriesDialog.show(Main.mainStage);
  }

  public void processImages(ActionEvent actionEvent) throws IOException {
    choosingDirectoriesDialog.hide();
    ImageLoader imageLoader = configureImageVisitor();
    Popup progress = new Popup();
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("Layouts/Progress.fxml"));
    Parent progressLayout = loader.load();
    ProgressController controller = loader.getController();
    progress.getContent().add(progressLayout);
    controller.progressBar.progressProperty().bind(imageLoader.progressProperty());
    Task<Void> task = new Task<>() {
      @Override
      protected Void call() throws Exception {
        imageLoader.processDir();
        controller.hideWindow(null);
        progress.hide();
        return null;
      }
    };
    new Thread(task).start();
    progress.show(Main.mainStage);
  }

  private ImageLoader configureImageVisitor() {
    String pathToImages = pathToImagesTextField.getText();
    String pathToStoringImages = pathToStoreProcessedImagesTextField.getText();
    boolean cropImages = cropImagesCheckBock.isSelected();
    return new ImageLoader(pathToImages, pathToStoringImages, cropImages);
  }
}
