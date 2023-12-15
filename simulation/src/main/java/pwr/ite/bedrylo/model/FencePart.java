package pwr.ite.bedrylo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import pwr.ite.bedrylo.model.enums.Status;

@Data
public class FencePart {

  private UUID id;

  private volatile List<Plank> plankList;

  private Status status;

  private int length;

  private List<Plank> longestUnpaintedSegment;

  private List<Painter> painters;

  public FencePart(int length) {
    this.id = UUID.randomUUID();
    this.length = length;
    this.status = Status.Unpainted;
    this.painters = Collections.synchronizedList(new ArrayList<>());
    this.plankList = Collections.synchronizedList(new ArrayList<>());
    for (int i = 0; i < getLength(); i++) {
      plankList.add(new Plank());
    }
    this.longestUnpaintedSegment = new ArrayList<Plank>();
  }

  public synchronized List<Plank> getUnpaintedPlanks() {
    return plankList.stream().filter(plank -> plank.getStatus() == Status.Unpainted).toList();
  }

  public synchronized List<Plank> getLongestUnpaintedPlanksList() {
    ArrayList<Plank> temporaryPlankList = new ArrayList<>();
    this.getLongestUnpaintedSegment().clear();
    for (Plank plank : plankList) {
      if (plank.getStatus() == Status.Unpainted) {
        temporaryPlankList.add(plank);
      } else {
        if (temporaryPlankList.size() > getLongestUnpaintedSegment().size()) {
          this.longestUnpaintedSegment = new ArrayList<>(temporaryPlankList);
        }
        temporaryPlankList.clear();
      }
    }
    if (this.getLongestUnpaintedSegment().isEmpty()
        || temporaryPlankList.size() > getLongestUnpaintedSegment().size()) {
      this.longestUnpaintedSegment = new ArrayList<>(temporaryPlankList);
    }
    return longestUnpaintedSegment;
  }

  public int getIndexOfStartOfLongestUnpaintedSegment() {
    return plankList.indexOf(getLongestUnpaintedPlanksList().get(0));
  }

  public Plank getMiddlePlankForPainting() {
    int indexOfPlankToPaintInLongestUnpainted = getLongestUnpaintedPlanksList().size() / 2;
    return longestUnpaintedSegment.get(indexOfPlankToPaintInLongestUnpainted);
  }

  public int getIndexOfMiddlePlankForPainting() {
    return plankList.indexOf(getMiddlePlankForPainting());
  }

  public synchronized String getPrettyString() {
    String temp = "";
    for (Plank plank : plankList) {
      if (plank.getPainter() == null) {
        temp += ".";
      } else {
        temp += plank.getPainter().getName();
      }
    }
    return temp;
  }
}
