package pwr.ite.bedrylo.model;

import lombok.Data;
import pwr.ite.bedrylo.model.enums.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Painter implements Runnable{
    
    public static List<Painter> painterList = new ArrayList<>();
    
    private UUID id;
    
    private String name;
    
    private int speed;
    
    private PaintBucket bucket;
    
    public Painter(int speed, String name){
        this.id = UUID.randomUUID();
        this.name = name;
        this.speed = speed;
        this.bucket = new PaintBucket();
        painterList.add(this);
    }
    
    public void paintPlank(Plank plankBeingPainted){
        plankBeingPainted.setPainter(this);
        plankBeingPainted.setStatus(Status.InPainting);
        if (bucket.isEmpty()) {
            bucket.refill();
        }
        bucket.takePaint(1);
        try {
            Thread.sleep(1000-speed);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        plankBeingPainted.setStatus(Status.Painted);
    }
    
    @Override
    public void run(){
        
    }
    
}
