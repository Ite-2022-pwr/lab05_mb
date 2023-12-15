package pwr.ite.bedrylo.gui;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pwr.ite.bedrylo.model.PaintSupplier;
import pwr.ite.bedrylo.model.Painter;

public class FencePaintingApp extends Application {
  public static void main(String[] args) {
    launch();
  }

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(FencePaintingApp.class.getResource("hello-view.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 1000, 400);
    stage.setTitle("Płotek!");
    stage.setScene(scene);
    stage.show();
  }

  @Override
  public void stop() throws Exception {
    super.stop();
    Painter.painterList.forEach(p -> p.getThread().interrupt());
    PaintSupplier.getThread().interrupt();
  }
}
