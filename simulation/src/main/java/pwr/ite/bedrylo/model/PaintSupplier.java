package pwr.ite.bedrylo.model;

import lombok.Data;
import pwr.ite.bedrylo.model.enums.Status;

@Data
public class PaintSupplier implements Runnable {

  private static Thread thread;

  private String name = "PS";

  private double speed;

  private Fence fence = Fence.getInstance();

  private PaintContainer paintContainer = PaintContainer.getInstance();

  public PaintSupplier(double speed) {
    this.speed = speed;
  }

  public static Thread getThread() {
    return thread;
  }

  public void refillContainer() {
    try {
      paintContainer.setRefilling(true);
      System.out.println(fence.getPrettyString());
      Thread.sleep((int) (1000 * speed));
      paintContainer.refill();
      paintContainer.setRefilling(false);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void run() {
    while (fence.getStatus() != Status.Painted) {
      try {
        synchronized (this) {
          this.wait();
          refillContainer();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    System.out.println(name + " now dying");
    Thread.currentThread().interrupt();
  }

  public void start() {
    System.out.println("Starting " + name);
    if (thread == null) {
      thread = new Thread(this, name);
      thread.setDaemon(true);
      thread.start();
    }
  }
}
