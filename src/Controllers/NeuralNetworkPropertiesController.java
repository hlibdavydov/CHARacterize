package Controllers;

import DataProcessing.NeuralNetwork;
import DataProcessing.Perceptron;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Popup;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class NeuralNetworkPropertiesController implements Initializable {

  public TextField learningRateForNeuralNetwork;
  public TextField valueOfParameterForNeuralNetwork;
  public TextField numberOfPerceptronsInLayer2;
  public TextField numberOfPerceptronsInLayer1;
  public TextField numberOfPerceptronsInLayer0;
  public TextField numberOfIterationForNeuralNetworkTeaching;
  public Popup popup = new Popup();

  public void saveNeuralNetworkProperties(ActionEvent actionEvent) {
    popup.hide();
    Perceptron.setParameter(Double.parseDouble(valueOfParameterForNeuralNetwork.getText()));
    Perceptron.setLearningRate(Double.parseDouble(learningRateForNeuralNetwork.getText()));
    NeuralNetwork.setNumberOfLearningIterations(Integer.parseInt(numberOfIterationForNeuralNetworkTeaching.getText()));
    HashMap<Integer, Integer> perceptronsForEachLayer = new HashMap<>();
    perceptronsForEachLayer.put(0, Integer.valueOf(numberOfPerceptronsInLayer0.getText()));
    perceptronsForEachLayer.put(1, Integer.valueOf(numberOfPerceptronsInLayer1.getText()));
    perceptronsForEachLayer.put(2, Integer.valueOf(numberOfPerceptronsInLayer2.getText()));
    NeuralNetwork.setNumbersOfPerceptronForEachLayer(perceptronsForEachLayer);
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
  }
}
