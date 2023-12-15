package pwr.ite.bedrylo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.Data;
import pwr.ite.bedrylo.model.enums.Status;

@Data
public class Painter implements Runnable {

  public static List<Painter> painterList = new ArrayList<>();

  private static Fence fence = Fence.getInstance();

  private final Random random = new Random();

  private Thread thread;

  private String name;

  private double speed;

  private double speedMultiplier;

  private PaintBucket bucket;

  private Integer indexOfPlankToPaint;

  private FencePart fencePartToPaint;

  public Painter(String name, double speedMultiplier) {
    this.name = name;
    this.speedMultiplier = speedMultiplier;
    this.speed = random.nextInt(100, 1000);
    this.bucket = new PaintBucket();
    painterList.add(this);
  }

  public void work() {
    try {
      paintFencePart();
      Thread.sleep((int) (speed * speedMultiplier));
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public void paintFencePart() {
    if (fencePartToPaint == null) {
      return;
    }
    if (fencePartToPaint.getUnpaintedPlanks().isEmpty()) {
      fencePartToPaint.setStatus(Status.Painted);
      fencePartToPaint.getPainters().remove(this);
      fencePartToPaint = null;
      indexOfPlankToPaint = null;
      return;
    } else if (fencePartToPaint.getPainters().isEmpty()) {
      fencePartToPaint.addPainter(this);
      indexOfPlankToPaint = 0;
    } else if (!fencePartToPaint.getPainters().contains(this)) {
      fencePartToPaint.addPainter(this);
      indexOfPlankToPaint = fencePartToPaint.getIndexOfMiddlePlankForPainting();
    }
    if (indexOfPlankToPaint >= fencePartToPaint.getLength()) {
      fencePartToPaint.getPainters().remove(this);
      fencePartToPaint = null;
      indexOfPlankToPaint = null;
      return;
    }
    Plank plankToTryPainting = fencePartToPaint.getPlankList().get(indexOfPlankToPaint);
    if (!plankToTryPainting.getStatus().equals(Status.Unpainted)) {
      fencePartToPaint.getPainters().remove(this);
      fencePartToPaint = null;
      indexOfPlankToPaint = null;
      return;
    }
    paint(plankToTryPainting);
  }

  public void paint(Plank plankBeingPainted) {
    if (!plankBeingPainted.getStatus().equals(Status.Unpainted)) {
      fencePartToPaint.getPainters().remove(this);
      fencePartToPaint = null;
      indexOfPlankToPaint = null; // wjebać te 3 w funkcje
      return;
    }
    if (bucket.isEmpty()) {
      if (fence.getPaintContainer().getPainterUsing() != null) {
        return;
      }
      fence.getPaintContainer().setPainterUsing(this);
      try {
        Thread.sleep((int) (speed * speedMultiplier));
        fence.getPaintContainer().setPainterUsing(null);
        if (!bucket.refill()) return;
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    System.out.println(fence.getPrettyString());
    try {
      Thread.sleep((int) (speed * speedMultiplier));
      if (fencePartToPaint.paintPlank(plankBeingPainted, this)) {
        bucket.takePaint(1);
        indexOfPlankToPaint++;
      } else {
        return;
      }
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    System.out.println(fence.getPrettyString());
  }

  @Override
  public void run() {
    try {
      Thread.sleep((int) (speed * speedMultiplier));
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    do {
      if (this.fencePartToPaint == null) {
        this.fencePartToPaint = fence.findFencePartToWork();
        if (this.fencePartToPaint != null) {
          this.fencePartToPaint.setStatus(Status.InPainting);
        }
      }
      work();
    } while (fence.getStatus() != Status.Painted);
    System.out.println(Thread.currentThread().getName() + " now dying");
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
