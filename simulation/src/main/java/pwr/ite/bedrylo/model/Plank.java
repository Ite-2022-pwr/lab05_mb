package pwr.ite.bedrylo.model;

import java.util.UUID;
import lombok.Data;
import pwr.ite.bedrylo.model.enums.Status;

@Data
public class Plank {

    private UUID id;

    private volatile Status status;

    private Painter painter;

    public Plank() {
        this.id = UUID.randomUUID();
        this.status = Status.Unpainted;
    }
}
