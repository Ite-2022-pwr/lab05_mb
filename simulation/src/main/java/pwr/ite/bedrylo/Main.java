package pwr.ite.bedrylo;

import pwr.ite.bedrylo.model.*;
import pwr.ite.bedrylo.model.enums.Status;

public class Main {
    public static void main(String[] args) {
        Fence fence = Fence.getInstance(4,8);
        for (int i = 0; i < 25; i++) {
            String name = "" + (char)(i + (int)'a');
            new Painter(name);
        }
        PaintSupplier paintSupplier = new PaintSupplier(10, "P");
        paintSupplier.start();
        for (int i = 0; i < 25; i++) {
            System.out.println(Painter.painterList.get(i).getName());
            Painter.painterList.get(i).start();
        }
        
        

        

        
    }
}