package pwr.ite.bedrylo.model;

import java.util.*;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import lombok.Data;
import pwr.ite.bedrylo.model.enums.Status;

@Data
public class Fence {

  public static Fence INSTANCE = null;

  private PaintContainer paintContainer = PaintContainer.getInstance();

  private int length;

  private volatile SimpleListProperty<FencePart> fencePartList;

  private volatile Status status;

  private SimpleStringProperty prettyString = new SimpleStringProperty();

  public Fence(int fenceLength, int fencePartLength) {
    this.length = fenceLength;
    this.status = Status.Unpainted;
    this.fencePartList = new SimpleListProperty<>(FXCollections.observableArrayList());
    for (int i = 0; i < fenceLength; i++) {
      this.fencePartList.add(new FencePart(fencePartLength));
    }
  }

  public static Fence getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new Fence(16, 32);
    }
    return INSTANCE;
  }

  public static Fence getInstance(int length, int lengthOfPart) {
    if (INSTANCE == null) {
      INSTANCE = new Fence(length, lengthOfPart);
    }
    return INSTANCE;
  }

  public SimpleStringProperty prettyString() {
    return prettyString;
  }

  public synchronized FencePart findFencePartByStatus(Status status) {
    return fencePartList.stream()
        .filter(fencePart -> fencePart.getStatus().equals(status))
        .max(Comparator.comparing(o -> o.getUnpaintedPlanks().size()))
        .orElse(null);
  }

  public synchronized FencePart findFencePartToWork() {
    FencePart fencePartToWork =
        findFencePartByStatus(Status.Unpainted) != null
            ? findFencePartByStatus(Status.Unpainted)
            : findFencePartByStatus(Status.InPainting);
    if (fencePartToWork == null) {
      status = Status.Painted;
      paintContainer.callForPaintSupplier();
    }
    return fencePartToWork;
  }

  public synchronized String getPrettyString() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
    StringBuilder temp = new StringBuilder(paintContainer.getPrettyString() + "\n");
    for (Painter painter : Painter.painterList) {
      temp.append(painter.getName()).append(" ");
    }
    temp.append("\n");
    for (Painter painter : Painter.painterList) {
      temp.append(painter.getBucket().getPaintLeft()).append(" ");
    }
    temp.append("\n|");
    for (FencePart fencePart : fencePartList) {
      temp.append(fencePart.getPrettyString()).append("|");
    }
    prettyString.set(temp.toString());
    return prettyString.toString();
  }
}
