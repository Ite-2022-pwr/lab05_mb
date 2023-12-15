package pwr.ite.bedrylo.model;

import lombok.Data;

@Data
public class PaintContainer {

  public static PaintContainer INSTANCE = null;

  private int capacity;

  private volatile int paintLeft;

  private volatile boolean refilling;

  private volatile Painter painterUsing;

  private volatile PaintSupplier paintSupplier;

  public PaintContainer(int capacity) {
    this.capacity = capacity;
    this.paintLeft = capacity;
    this.refilling = false;
  }

  public static PaintContainer getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new PaintContainer(200);
    }
    return INSTANCE;
  }

  public static PaintContainer getInstance(int capacity) {
    if (INSTANCE == null) {
      INSTANCE = new PaintContainer(capacity);
    }
    return INSTANCE;
  }

  public boolean isEmpty() {
    return getPaintLeft() == 0;
  }

  public synchronized void takePaint(PaintBucket bucket) {
    if (!isEmpty()) {
      if (bucket.getCapacity() <= getPaintLeft()) {
        bucket.setPaintLeft(bucket.getCapacity());
        setPaintLeft(getPaintLeft() - bucket.getCapacity());
      } else {
        bucket.setPaintLeft(getPaintLeft());
        setPaintLeft(0);
      }
    }
  }

  public String getPrettyString() {
    String temp = "";
    if (refilling) {
      temp += "PS";
    } else {
      temp += ".";
    }
    temp += "[" + paintLeft + "]";
    if (painterUsing != null) {
      temp += painterUsing.getName();
    } else {
      temp += ".";
    }
    return temp;
  }

  public synchronized void callForPaint() {
    synchronized (paintSupplier) {
      paintSupplier.notify();
    }
  }

  public synchronized void refill() {
    this.paintLeft = this.capacity;
  }
}
