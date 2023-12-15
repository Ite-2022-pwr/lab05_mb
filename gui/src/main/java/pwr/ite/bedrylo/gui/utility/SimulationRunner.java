package pwr.ite.bedrylo.gui.utility;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import lombok.Data;
import pwr.ite.bedrylo.model.*;

@Data
public class SimulationRunner {

  private PaintContainer paintContainer = PaintContainer.getInstance();

  private Fence fence = Fence.getInstance();

  public void runSimulation(
      int fenceSegments,
      int segmentLength,
      double speedMultiplier,
      int painterCount,
      int paintContainerCapacity) {
    for (int i = 0; i < painterCount; i++) {
      String name = "" + (char) (i + (int) 'a');
      new Painter(name, speedMultiplier);
    }
    fence.setFencePartList(new SimpleListProperty<>(FXCollections.observableArrayList()));
    for (int i = 0; i < fenceSegments; i++) {
      fence.getFencePartList().add(new FencePart(segmentLength));
    }
    paintContainer.setCapacity(paintContainerCapacity);
    PaintSupplier paintSupplier = new PaintSupplier(speedMultiplier);
    paintContainer.setPaintSupplier(paintSupplier);
    paintSupplier.start();
    for (Painter painter : Painter.painterList) {
      System.out.println(painter.getName() + " Starting");
      painter.start();
    }
  }
}
