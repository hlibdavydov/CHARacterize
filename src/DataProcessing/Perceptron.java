package DataProcessing;

import java.io.Serializable;
import java.util.ArrayList;

public class Perceptron implements Serializable {

  private static final long serialVersionUID = 6456577629446212095L;
  private static double parameter = 0.1;
  private static double learningRate = 0.01;
  private ArrayList<Double> weights = new ArrayList<>();
  private char letterToIdentify;
  private int layerInNeuralNetwork;
  private int numberInLayerOfNeuralNetwork;
  private boolean isInTheLastLayer;
  private double threshold;

  private double delta;

  private double output;
  private double[] input;
  public Perceptron(char letterToIdentify, int layerInNeuralNetwork,int numberInLayerOfNeuralNetwork, boolean isInTheLastLayer) {
    this(layerInNeuralNetwork,numberInLayerOfNeuralNetwork, isInTheLastLayer);
    this.letterToIdentify = letterToIdentify;
    threshold = (int) (Math.random() * 5);
  }

  public Perceptron(int layerInNeuralNetwork,int numberInLayerOfNeuralNetwork, boolean isInTheLastLayer) {
    this.layerInNeuralNetwork = layerInNeuralNetwork;
    this.numberInLayerOfNeuralNetwork = numberInLayerOfNeuralNetwork;
    this.isInTheLastLayer = isInTheLastLayer;
  }

  public static void setParameter(double parameter) {
    Perceptron.parameter = parameter;
  }

  public static void setLearningRate(double learningRate) {
    Perceptron.learningRate = learningRate;
  }

  public void processInput(double[] input) {
    this.input = input;
    this.output = getPerceptronOutputBasedOn(input);
  }

  public static double getLearningRate() {
    return learningRate;
  }

  public void processInput() {
    this.input = NeuralNetwork.getOutputsOfPerceptronInLayer(layerInNeuralNetwork - 1);
    output = getPerceptronOutputBasedOn(input);
  }

  public void calculateDeltaOfPerceptron(Letter letter) {
    if (isInTheLastLayer) {
      delta = calculateDeltaForLastLayerOfNetworkWithExpectedValue(letter);
    } else {
      delta = output * (output - 1) *
              getSumOfAllDeltasForThisPerceptronFromTheHigherLayer(layerInNeuralNetwork, numberInLayerOfNeuralNetwork);
    }
  }

  public double calculateDeltaForLastLayerOfNetworkWithExpectedValue(Letter input) {
   return output * (output - 1) * (getExpectedValue(input) - output);
  }

  public double getOutput() {
    return output;
  }

  public void updateWeightsAndThreshold() {
    for (int i = 0; i < weights.size(); i++) {
      double updatedWeight = weights.get(i) - learningRate * input[i] * delta;
      weights.set(i, updatedWeight);
    }
    threshold = threshold - learningRate * delta * -1;
  }

  private double getSumOfAllDeltasForThisPerceptronFromTheHigherLayer(int layerOfPerceptron, int numberInLayerOfNeuralNetwork) {
    double result = 0;
    for (var perceptron :
            NeuralNetwork.getNetwork().get(layerOfPerceptron + 1)) {
      result += perceptron.getDelta() * perceptron.getWeights().get(numberInLayerOfNeuralNetwork);
    }
    return result;
  }

  public ArrayList<Double> getWeights() {
    return weights;
  }

  public double getDelta() {
    return delta;
  }

  private double getExpectedValue(Letter input) {
    if (letterToIdentify == input.getName()) {
      return 1;
    }
    return 0;
  }

  public double getPerceptronOutputBasedOn(double[] input) {
    if (!enoughWeightsToProcessInput(input)) {
      addWeightsToPerceptron(input.length - weights.size());
    }
    double net = calculateNetOf(weights, input);
    return activationFunctionOf(net);
  }

  private double activationFunctionOf(double net) {
    return 1.0 / (1 + Math.pow(Math.E, -1 * net * parameter));
  }


  private double calculateNetOf(ArrayList<Double> weights, double[] input) {
    double output = 0;
    for (int i = 0; i < input.length; i++) {
      output += weights.get(i) * input[i];
    }
    return output - threshold;
  }

  private boolean enoughWeightsToProcessInput(double[] input) {
    return weights.size() >= input.length;
  }

  private void addWeightsToPerceptron(int amountOfWeightsToAdd) {
    for (int i = 0; i < amountOfWeightsToAdd; i++) {
      weights.add((Math.random() * 10 - 5));
    }
  }

  public static double getParameter() {
    return parameter;
  }
}
