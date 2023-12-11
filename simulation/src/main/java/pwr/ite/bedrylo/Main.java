package pwr.ite.bedrylo;

import pwr.ite.bedrylo.model.*;
import pwr.ite.bedrylo.model.enums.Status;

public class Main {
    public static void main(String[] args) {
        Painter painter1 = new Painter(10, "a");
        Painter painter2 = new Painter(10, "b");
        System.out.println(Painter.painterList);
        
        FencePart fencePart = new FencePart(16);
        System.out.println(fencePart.getIndexOfStartOfLongestUnpaintedSegment());
        painter1.paintPlank(fencePart.getUnpaintedPlanks().get(3));
        System.out.println(fencePart.getIndexOfStartOfLongestUnpaintedSegment());
        
    }
}