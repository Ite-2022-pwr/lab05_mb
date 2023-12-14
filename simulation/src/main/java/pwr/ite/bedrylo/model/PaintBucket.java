package pwr.ite.bedrylo.model;

import lombok.Data;

import java.util.Random;
import java.util.UUID;

@Data
public class PaintBucket {

    private final Random random = new Random();
    private UUID id;
    private int capacity;
    private int paintLeft;
    private PaintContainer paintContainerAssigned = PaintContainer.getInstance();

    public PaintBucket() {
        this.id = UUID.randomUUID();
        this.capacity = random.nextInt(8, 16);
        this.paintLeft = this.capacity;
    }

    public synchronized boolean refill() {
        if (!paintContainerAssigned.isEmpty()) {
            paintContainerAssigned.takePaint(this);
            return true;
        }
        paintContainerAssigned.callForPaint();
        return false;
    }

    public void takePaint(int amount) {
        if (!isEmpty()) {
            setPaintLeft(getPaintLeft() - amount);
        }
    }

    public boolean isEmpty() {
        return getPaintLeft() == 0;
    }
}
