package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ProgressController implements Initializable {

  @FXML
  public ProgressBar progressBar;
  public AnchorPane window;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    
  }

  public void hideWindow(ActionEvent actionEvent) {
    progressBar.setVisible(false);
    window.setVisible(false);
  }
}
