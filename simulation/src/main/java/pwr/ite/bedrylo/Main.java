package pwr.ite.bedrylo;

import pwr.ite.bedrylo.model.*;
import pwr.ite.bedrylo.model.enums.Status;

public class Main {
    public static void main(String[] args) {
        Fence.getInstance(10,20);
        for (int i = 0; i < 17; i++) {
            String name = "" + (char)(i + (int)'a');
            new Painter(name);
        }
        PaintSupplier paintSupplier = new PaintSupplier(10, "P");
        paintSupplier.start();
        for (int i = 0; i < 17; i++) {
            System.out.println(Painter.painterList.get(i).getName());
            Painter.painterList.get(i).start();
        }
        
        

        

        
    }
}