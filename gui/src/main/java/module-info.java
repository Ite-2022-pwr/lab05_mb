module pwr.ite.bedrylo.gui {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.graphics;
  requires pwr.ite.bedrylo.simulation;
  requires lombok;

  opens pwr.ite.bedrylo.gui to
      javafx.fxml;

  exports pwr.ite.bedrylo.gui;
  exports pwr.ite.bedrylo.gui.controller;

  opens pwr.ite.bedrylo.gui.controller to
      javafx.fxml;
}
