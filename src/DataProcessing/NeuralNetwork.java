package DataProcessing;


import java.io.*;
import java.util.*;
import java.util.stream.Stream;

public class NeuralNetwork {

  public static HashMap<Character, Double> accuracyForEachLetter;
  static ArrayList<ArrayList<Perceptron>> network = new ArrayList<>();
  static int numberOfLayersInNeuralNetwork = 3;
  private static ArrayList<Letter> trainingData = new ArrayList<>();
  private static ArrayList<Letter> testData = new ArrayList<>();
  private static HashMap<Integer, Integer> numbersOfPerceptronForEachLayer = new HashMap<>();
  private static int numberOfLearningIterations = 1;
  private static double accuracy = 0;

  static {
    numbersOfPerceptronForEachLayer.put(0, 784);
    numbersOfPerceptronForEachLayer.put(1, 143);
    numbersOfPerceptronForEachLayer.put(2, 26);
  }

  public static void setNumberOfLearningIterations(int numberOfLearningIterations) {
    NeuralNetwork.numberOfLearningIterations = numberOfLearningIterations;
  }

  public static void setNumbersOfPerceptronForEachLayer(HashMap<Integer, Integer> numbersOfPerceptronForEachLayer) {
    NeuralNetwork.numbersOfPerceptronForEachLayer = numbersOfPerceptronForEachLayer;
  }

  public static void serializeNetwork(File file) {
    try (FileOutputStream fileOutputStream = new FileOutputStream(file);
         ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
    ) {
      outputStream.writeObject(network);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void deserializeNetwork(File file) {
    try (var fileInputStream = new FileInputStream(file);
         var objectInputStream = new ObjectInputStream(fileInputStream);
    ) {
      network = (ArrayList<ArrayList<Perceptron>>) objectInputStream.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static void teachNetwork() {
    for (int k = 0; k < numberOfLearningIterations; k++) {
      Collections.shuffle(trainingData, new Random(1 + k));
      int i = trainingData.size();
      for (var letter :
              trainingData) {
        predictOutputOfLetter(letter);
        calculateDeltaOfAllPerceptrons(letter);
        updateValuesOfAllPerceptrons();
        i--;
        //System.out.println("Left: " + i);
      }
      var acc = getAccuracy();
      if (acc > accuracy) {
        System.out.println(acc + "%" + "iteration " + k);
        accuracy = acc;
      }
      if (accuracy > 80.0) {
        break;
      }
      //System.out.println(k);
    }
  }

  private static void calculateDeltaOfAllPerceptrons(Letter letter) {
    for (int i = network.size() - 1; i >= 0; i--) {
      for (int j = network.get(i).size() - 1; j >= 0; j--) {
        network.get(i).get(j).calculateDeltaOfPerceptron(letter);
      }
    }
  }

  public static double[] predictOutputOfLetter(Letter letter) {
    processInput(letter.getPixels());
    return getOutputsFromLastLayer();
  }

  private static void processInput(double[] input) {
    for (var perceptron :
            network.get(0)) {
      perceptron.processInput(input);
    }
    for (int i = 1; i < network.size(); i++) {
      for (var perceptron : network.get(i)) {
        perceptron.processInput();
      }
    }
  }

  private static double[] getOutputsFromLastLayer() {
    int currentIndex = 0;
    double[] outputs = new double[26];
    for (var perceptron :
            network.get(numberOfLayersInNeuralNetwork - 1)) {
      outputs[currentIndex++] = perceptron.getOutput();
    }
    return outputs;
  }

  private static void updateValuesOfAllPerceptrons() {
    for (var layer :
            network) {
      for (var perceptron :
              layer) {
        perceptron.updateWeightsAndThreshold();
      }
    }
  }

  public static ArrayList<Letter> getTrainingData() {
    return trainingData;
  }

  public static ArrayList<Letter> getTestData() {
    return testData;
  }

  public static void buildNetwork() {
    network.clear();
    for (int layer = 0; layer < numberOfLayersInNeuralNetwork; layer++) {
      network.add(getLayerWithNumberOfPerceptrons(layer));
    }
  }

  private static ArrayList<Perceptron> getLayerWithNumberOfPerceptrons(int layer) {
    ArrayList<Perceptron> perceptrons = new ArrayList<>();
    for (int j = 0; j < numbersOfPerceptronForEachLayer.get(layer); j++) {
      if (layer == numberOfLayersInNeuralNetwork - 1) {
        perceptrons.add(new Perceptron((char) ('A' + j), layer, j, true));
      } else {
        perceptrons.add(new Perceptron(layer, j, false));
      }
    }
    return perceptrons;
  }

  public static ArrayList<ArrayList<Perceptron>> getNetwork() {
    return network;
  }

  public static void resetPerceptrons() {
    for (var layer :
            network) {
      for (var perceptron :
              layer) {
        perceptron.getWeights().clear();
      }
    }
  }

  public static void resetNetwork() {
    network.clear();
    buildNetwork();
  }

  public static double getAccuracy() {
    int correctlyGuessedLetters = 0;
    HashMap<Character, Double> accuracyForLetters = new HashMap<>();
    for (var letter :
            testData) {
      accuracyForLetters.putIfAbsent(letter.getName(), 0.0);
      if (getPredictedLetterFor(letter) == letter.getName()) {
        accuracyForLetters.put(letter.getName(), accuracyForLetters.get(letter.getName()) + 1);
        correctlyGuessedLetters++;
      }
    }
    for (var key :
            accuracyForLetters.keySet()) {
      accuracyForLetters.put(key, accuracyForLetters.get(key) / testData.stream().filter(letter -> letter.getName() == key).count());
    }
    accuracyForEachLetter = accuracyForLetters;
    return (double) correctlyGuessedLetters / testData.size() * 100;
  }

  public static char getPredictedLetterFor(Letter letter) {
    double[] outputs = predictOutputOfLetter(letter);
    int index = 0;
    double max = Double.MIN_VALUE;
    for (int i = 0; i < outputs.length; i++) {
      if (outputs[i] > max) {
        max = outputs[i];
        index = i;
      }
    }
    return (char) ('A' + index);
  }

  public static double[] getOutputsOfPerceptronInLayer(int layer) {
    double[] outputs = new double[network.get(layer).size()];
    int currentIndex = 0;
    for (var perceptron :
            network.get(layer)) {
      outputs[currentIndex++] = perceptron.getOutput();
    }
    return outputs;
  }
}

