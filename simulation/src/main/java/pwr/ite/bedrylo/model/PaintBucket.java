package pwr.ite.bedrylo.model;

import java.util.Random;
import lombok.Data;

@Data
public class PaintBucket {

  private final Random random = new Random();
  private int capacity;
  private int paintLeft;
  private PaintContainer paintContainerAssigned = PaintContainer.getInstance();

  public PaintBucket() {
    this.capacity = random.nextInt(8, 16);
    this.paintLeft = this.capacity;
  }

  public synchronized boolean refill() {
    if (!paintContainerAssigned.isEmpty()) {
      paintContainerAssigned.takePaint(this);
      return true;
    }
    paintContainerAssigned.callForPaintSupplier();
    return false;
  }

  public void takePaint(int amount) {
    if (!isEmpty()) {
      paintLeft = paintLeft - amount;
    }
  }

  public boolean isEmpty() {
    return paintLeft == 0;
  }
}
