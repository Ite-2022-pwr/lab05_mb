module pwr.ite.bedrylo.gui {
    requires javafx.controls;
    requires javafx.fxml;


    opens pwr.ite.bedrylo.gui to javafx.fxml;
    exports pwr.ite.bedrylo.gui;
    exports pwr.ite.bedrylo.gui.controller;
    opens pwr.ite.bedrylo.gui.controller to javafx.fxml;
}