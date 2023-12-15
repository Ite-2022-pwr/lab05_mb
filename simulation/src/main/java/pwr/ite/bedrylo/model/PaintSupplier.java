package pwr.ite.bedrylo.model;

import lombok.Data;
import lombok.SneakyThrows;
import pwr.ite.bedrylo.model.enums.Status;

@Data
public class PaintSupplier implements Runnable {

  private Thread thread;

  private String name = "PS";

  private int speed;

  private Fence fence = Fence.getInstance();

  private PaintContainer paintContainer = PaintContainer.getInstance();

  public PaintSupplier(int speed) {
    this.speed = speed;
  }

  public void refillContainer() {
    paintContainer.setRefilling(true);
    System.out.println(fence.getPrettyString());
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    paintContainer.refill();
    paintContainer.setRefilling(false);
  }

  @SneakyThrows
  @Override
  public void run() {
    while (fence.getStatus() != Status.Painted) {
      synchronized (this) {
        this.wait();
      }
      refillContainer();
      if (fence.getStatus() == Status.Painted) {
        Thread.currentThread().interrupt();
      }
    }
  }

  public void start() {
    System.out.println("Starting " + name);
    if (thread == null) {
      thread = new Thread(this, name);
      thread.start();
    }
  }
}
