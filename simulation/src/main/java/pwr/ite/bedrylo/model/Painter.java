package pwr.ite.bedrylo.model;

import lombok.Data;
import lombok.SneakyThrows;
import pwr.ite.bedrylo.model.enums.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Data
public class Painter implements Runnable {

    public static List<Painter> painterList = new ArrayList<>();
    private final Random random = new Random();
    private Thread thread;
    private Fence fence = Fence.getInstance();
    private UUID id;

    private String name;

    private int speed;

    private PaintBucket bucket;

    private Integer indexOfPlankToPaint;

    private FencePart fencePartToPaint;

    public Painter(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.speed = random.nextInt(-100, 500);
        this.bucket = new PaintBucket();
        painterList.add(this);
    }

    public void work() {
        paintFencePart();
        try {
            Thread.sleep(600 - speed);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void paintFencePart() {
        if (fencePartToPaint == null) {
            return;
        }
        if (fencePartToPaint.getUnpaintedPlanks().isEmpty()) {
            this.fencePartToPaint.setStatus(Status.Painted);
            this.fencePartToPaint.getPainters().remove(this);
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
        if (indexOfPlankToPaint == null || indexOfPlankToPaint >= fencePartToPaint.getLength()) {
            this.fencePartToPaint.getPainters().remove(this);
            this.fencePartToPaint = null;
            indexOfPlankToPaint = null;
            return;
        }
        if (fencePartToPaint.getPlankList().get(indexOfPlankToPaint) == null ||
                fencePartToPaint.getPlankList().get(indexOfPlankToPaint).getStatus() == Status.Painted ||
                fencePartToPaint.getPlankList().get(indexOfPlankToPaint).getStatus() == Status.InPainting) {
            this.fencePartToPaint.getPainters().remove(this);
            this.fencePartToPaint = null;
            indexOfPlankToPaint = null;
            return;
        }
        paintPlank(fencePartToPaint.getPlankList().get(indexOfPlankToPaint));
        indexOfPlankToPaint++;
    }

    public void paintPlank(Plank plankBeingPainted) {
        if (plankBeingPainted.getStatus() != Status.Unpainted) {
            fencePartToPaint = null;
            return;
        }
        plankBeingPainted.setPainter(this);
        plankBeingPainted.setStatus(Status.InPainting);
        if (bucket.isEmpty()) {
            if (fence.getPaintContainer().getPainterUsing() != null) {
                plankBeingPainted.setStatus(Status.Unpainted);
                plankBeingPainted.setPainter(null);
                indexOfPlankToPaint--;
                return;
            }
            fence.getPaintContainer().setPainterUsing(this);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (!bucket.refill()) {
                plankBeingPainted.setStatus(Status.Unpainted);
                plankBeingPainted.setPainter(null);
                indexOfPlankToPaint--;
                fence.getPaintContainer().setPainterUsing(null);
                return;
            }
            fence.getPaintContainer().setPainterUsing(null);
            System.out.println(fence.getPrettyString());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        bucket.takePaint(1);
        plankBeingPainted.setStatus(Status.Painted);
        System.out.println(fence.getPrettyString());
    }

    @SneakyThrows
    @Override
    public void run() {
        try {
            Thread.sleep(random.nextInt(0, 300));
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
        } while (fence.getStatus() != Status.Painted);
        System.out.println(Thread.currentThread().getName() + " now dying");
        Thread.currentThread().interrupt();
    }

    public void start() {
        System.out.println("Starting " + name);
        if (thread == null) {
            thread = new Thread(this, name);
            thread.start();
        }
    }
}
