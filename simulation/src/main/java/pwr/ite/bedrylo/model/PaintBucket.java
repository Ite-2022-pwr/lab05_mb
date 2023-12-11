package pwr.ite.bedrylo.model;

import lombok.Data;

import java.util.Random;
import java.util.UUID;

@Data
public class PaintBucket {

private UUID id;

private int capacity;

private int paintLeft;

private PaintContainer paintContainerAssigned = PaintContainer.getInstance();

private final Random random = new Random();

public PaintBucket(){
    this.id = UUID.randomUUID();
    this.capacity = random.nextInt(3,8);
    this.paintLeft = this.capacity;
}

public boolean refill(){
    if (!paintContainerAssigned.isEmpty()) {
    paintContainerAssigned.takePaint(this);
    return true;
    }
    return false;
}

public void takePaint(int amount){
    if (!isEmpty()) {
        setPaintLeft(getPaintLeft()-amount);
    }
}

public boolean isEmpty(){
    return getPaintLeft() == 0;
}

}
