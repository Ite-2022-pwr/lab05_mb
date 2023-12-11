package pwr.ite.bedrylo.model;

import lombok.Data;
import pwr.ite.bedrylo.model.enums.Status;

@Data
public class PaintSupplier implements Runnable{
    
    private Thread thread;
    
    private String name;
    
    private int speed;
    
    private Fence fence = Fence.getInstance();
    
    private PaintContainer paintContainer = PaintContainer.getInstance();
    
    public PaintSupplier(int speed, String name){
        this.speed = speed;
        this.name = name;
    }
    
    public void refillContainer(){
        paintContainer.setPaintLeft(paintContainer.getCapacity());
        System.out.println(fence.getPrettyString(0, name));
    }
    
    @Override
    public void run() {
        while (fence.getStatus() != Status.Painted) {
            try {
                Thread.sleep(100 - getSpeed());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (paintContainer.isEmpty()) {
                refillContainer();
            }
        }
    }

    public void start () {
        System.out.println("Starting " +  name );
        if (thread == null) {
            thread = new Thread (this, name);
            thread.start ();
        }
    }
}
