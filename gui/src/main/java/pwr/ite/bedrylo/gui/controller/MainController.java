package pwr.ite.bedrylo.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import pwr.ite.bedrylo.gui.utility.SimulationRunner;

public class MainController implements Initializable {

  @FXML private TextArea simulationField;
  @FXML private Spinner<Integer> painterCountSpinner;
  @FXML private Spinner<Double> speedMultiplierSpinner;
  @FXML private Spinner<Integer> fenceSegmentCountSpinner;
  @FXML private Spinner<Integer> fenceFragmentPlankCountSpinner;
  @FXML private Spinner<Integer> paintContainerCapacitySpinner;
  @FXML private Button startButton;

  @Override
  @FXML
  public void initialize(URL url, ResourceBundle resourceBundle) {
    painterCountSpinner.setValueFactory(
        new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 25, 5, 1));
    speedMultiplierSpinner.setValueFactory(
        new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, 10, 1, 0.1));
    fenceFragmentPlankCountSpinner.setValueFactory(
        new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 10, 1));
    fenceSegmentCountSpinner.setValueFactory(
        new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 4, 1));
    paintContainerCapacitySpinner.setValueFactory(
        new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 500, 100, 1));
  }

  @FXML
  protected void onStartButtonClick() {

    StringProperty stringProperty = new SimpleStringProperty();
    simulationField.textProperty().bind(stringProperty);
    SimulationRunner simulationRunner = new SimulationRunner();
    startButton.setDisable(true);

    simulationRunner
        .getFence()
        .prettyString()
        .addListener(
            (observable, oldValue, newValue) -> {
              // Update the label text when the data property changes
              Platform.runLater(() -> stringProperty.set(newValue));
            });

    Platform.runLater(
        () ->
            simulationRunner.runSimulation(
                fenceSegmentCountSpinner.getValue(),
                fenceFragmentPlankCountSpinner.getValue(),
                speedMultiplierSpinner.getValue(),
                painterCountSpinner.getValue(),
                paintContainerCapacitySpinner.getValue()));
  }
}
