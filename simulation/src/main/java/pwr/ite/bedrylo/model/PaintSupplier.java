package pwr.ite.bedrylo.model;

import lombok.Data;
import pwr.ite.bedrylo.model.enums.Status;

@Data
public class PaintSupplier implements Runnable{
    
    private int speed;
    
    private Fence fence = Fence.getInstance();
    
    private PaintContainer paintContainer = PaintContainer.getInstance();
    
    public PaintSupplier(int speed){
        this.speed = speed;
    }
    
    public void refillContainer(){
        if (paintContainer.isEmpty()) {
            paintContainer.setPaintLeft(paintContainer.getCapacity());
        }
    }
    
    @Override
    public void run() {
        while (fence.getStatus() != Status.Painted) {
            try {
                Thread.sleep(1000 - getSpeed());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (paintContainer.isEmpty()) {
                refillContainer();
            }
        }
    }
}
