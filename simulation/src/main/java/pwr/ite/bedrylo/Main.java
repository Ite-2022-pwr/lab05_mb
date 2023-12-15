package pwr.ite.bedrylo;

import pwr.ite.bedrylo.model.Fence;
import pwr.ite.bedrylo.model.PaintContainer;
import pwr.ite.bedrylo.model.PaintSupplier;
import pwr.ite.bedrylo.model.Painter;

public class Main {

  public static void main(String[] args) {
    Fence.getInstance(2, 10);
    PaintContainer paintContainer = PaintContainer.getInstance();

    for (int i = 0; i < 3; i++) {
      String name = "" + (char) (i + (int) 'a');
      new Painter(name, 1);
    }
    PaintSupplier paintSupplier = new PaintSupplier(10);
    paintContainer.setPaintSupplier(paintSupplier);
    paintSupplier.start();
    //        for (int i = 0; i < 25; i++) {
    //            System.out.println(Painter.painterList.get(i).getName());
    //            Painter.painterList.get(i).start();
    //        }
    for (Painter painter : Painter.painterList) {
      System.out.println(painter.getName() + " Starting");
      painter.start();
    }
  }
}
