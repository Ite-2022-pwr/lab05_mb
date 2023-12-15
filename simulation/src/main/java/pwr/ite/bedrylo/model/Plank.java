package pwr.ite.bedrylo.model;

import lombok.Data;
import pwr.ite.bedrylo.model.enums.Status;

import java.util.UUID;

@Data
public class Plank {

  private UUID id;

  private volatile Status status;

  private volatile Painter painter;

  public Plank() {
    this.id = UUID.randomUUID();
    this.status = Status.Unpainted;
  }
}
