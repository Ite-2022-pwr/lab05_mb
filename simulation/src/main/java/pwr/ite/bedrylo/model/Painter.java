package pwr.ite.bedrylo.model;

import lombok.Data;
import lombok.SneakyThrows;
import pwr.ite.bedrylo.model.enums.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Data
public class Painter implements Runnable{
    
    private Thread thread;
    
    private Fence fence = Fence.getInstance();
    
    public static List<Painter> painterList = new ArrayList<>();
    
    private final Random random = new Random();
    
    private UUID id;
    
    private String name;
    
    private int speed;
    
    private PaintBucket bucket;
    
    private Integer indexOfPlankToPaint;
    
    private FencePart fencePartToPaint;
    
    public Painter(String name){
        this.id = UUID.randomUUID();
        this.name = name;
        this.speed = random.nextInt(-200,200);
        this.bucket = new PaintBucket();
        painterList.add(this);
    }
    
    public void work(){
        paintFencePart();
        try {
            Thread.sleep(1300-speed);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void paintFencePart(){
        if (fencePartToPaint == null) {
            return;
        }
        if (fencePartToPaint.getUnpaintedPlanks().isEmpty()) {
            this.fencePartToPaint.setStatus(Status.Painted);
            this.fencePartToPaint = null;
            indexOfPlankToPaint = null;
            return;
        }
        if (fencePartToPaint.getPainters().isEmpty()) {
            fencePartToPaint.getPainters().add(this);
            indexOfPlankToPaint = 0;
        } else if (!fencePartToPaint.getPainters().contains(this)) {
            fencePartToPaint.getPainters().add(this);
            indexOfPlankToPaint = fencePartToPaint.getIndexOfMiddlePlankForPainting();
        }
        if (indexOfPlankToPaint >= fencePartToPaint.getLength()) {
            return;
        }
        paintPlank(fencePartToPaint.getPlankList().get(indexOfPlankToPaint));
        indexOfPlankToPaint++;
    }
    
    public void paintPlank(Plank plankBeingPainted){
        if (plankBeingPainted.getStatus() != Status.Unpainted) {
            fencePartToPaint = null;
            return;
        }
        plankBeingPainted.setPainter(this);
        plankBeingPainted.setStatus(Status.InPainting);
        if (bucket.isEmpty()) {
            if (!bucket.refill()) {
                return;
            };
            System.out.println(fence.getPrettyString(2, name));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        bucket.takePaint(1);
        plankBeingPainted.setStatus(Status.Painted);
        System.out.println(fence.getPrettyString(1, name));
    }
    
    @SneakyThrows
    @Override
    public void run(){
        try {
            Thread.sleep(random.nextInt(0,300));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        do {
            if (this.fencePartToPaint == null) {
                this.fencePartToPaint = fence.findFencePartToWork();
                //Thread.sleep(1);
//                if (this.fencePartToPaint == null) {
//                    System.out.println(name +" dupa");
//                } else {
//                    this.fencePartToPaint.setStatus(Status.InPainting);
//                    System.out.println(name+" znalazłem: "+this.fencePartToPaint.getId());
//                }
                if (this.fencePartToPaint != null) {
                    this.fencePartToPaint.setStatus(Status.InPainting);
                }
            }
            work();
        } while (fence.getStatus()!=Status.Painted);
        Thread.currentThread().interrupt();
    }

    public void start () {
        System.out.println("Starting " +  name );
        if (thread == null) {
            thread = new Thread (this, name);
            thread.start ();
        }
    }
    
}
