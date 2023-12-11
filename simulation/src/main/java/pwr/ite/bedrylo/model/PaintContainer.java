package pwr.ite.bedrylo.model;

import lombok.Data;

@Data
public class PaintContainer {

    public static PaintContainer INSTANCE = null;

    private int capacity;

    private volatile int paintLeft;

    public PaintContainer(int capacity) {
        this.capacity = capacity;
        this.paintLeft = capacity;
    }

    public boolean isEmpty() {
        return getPaintLeft() == 0;
    }

    public synchronized void takePaint(PaintBucket bucket) {
        if (!isEmpty()) {
            if (bucket.getCapacity() <= getPaintLeft()) {
                bucket.setPaintLeft(bucket.getCapacity());
                setPaintLeft(getPaintLeft() - bucket.getCapacity());
            } else {
                bucket.setPaintLeft(getPaintLeft());
                setPaintLeft(0);
            }
        }
    }

    public String getPrettyString(int action, String nameOfCaller) {
        return switch (action) {
            case 0 -> nameOfCaller + "[" + paintLeft + "].";
            case 1 -> ".[" + paintLeft + "].";
            case 2 -> ".[" + paintLeft + "]" + nameOfCaller;
            default -> ".[" + paintLeft + "].";
        };
    }

    public static PaintContainer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PaintContainer(100);
        }
        return INSTANCE;
    }

    public static PaintContainer getInstance(int capacity) {
        if (INSTANCE == null) {
            INSTANCE = new PaintContainer(capacity);
        }
        return INSTANCE;
    }

    public synchronized void refill() {
        this.paintLeft = this.capacity;
    }
}
