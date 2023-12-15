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

  private volatile List<Plank> longestUnpaintedSegment;

  private List<Painter> painters;

  public FencePart(int length) {
    this.id = UUID.randomUUID();
    this.length = length;
    this.status = Status.Unpainted;
    this.painters = Collections.synchronizedList(new ArrayList<>());
    this.plankList = Collections.synchronizedList(new ArrayList<>());
    this.longestUnpaintedSegment = Collections.synchronizedList(new ArrayList<>());
    for (int i = 0; i < getLength(); i++) {
      plankList.add(new Plank());
    }
    this.longestUnpaintedSegment = new ArrayList<Plank>();
  }

  public synchronized void addPainter(Painter painter) {
    painters.add(painter);
  }

  public synchronized List<Painter> getPainters() {
    return painters;
  }

  public synchronized List<Plank> getUnpaintedPlanks() {
    return plankList.stream().filter(plank -> Status.Unpainted.equals(plank.getStatus())).toList();
  }

  public synchronized List<Plank> getLongestUnpaintedPlanksList() {
    ArrayList<Plank> temporaryPlankList = new ArrayList<>();
    for (Plank plank : plankList) {
      if (plank.getStatus().equals(Status.Unpainted)) {
        temporaryPlankList.add(plank);
      } else {
        if (temporaryPlankList.size() > longestUnpaintedSegment.size()) {
          longestUnpaintedSegment.clear();
          longestUnpaintedSegment.addAll(temporaryPlankList);
        }
        temporaryPlankList.clear();
      }
    }
    if (longestUnpaintedSegment.isEmpty()
        || temporaryPlankList.size() > longestUnpaintedSegment.size()) {
      longestUnpaintedSegment = new ArrayList<>(temporaryPlankList);
    }
    return longestUnpaintedSegment;
  }

  public synchronized Plank getMiddlePlankForPainting() {
    int indexOfPlankToPaintInLongestUnpainted = getLongestUnpaintedPlanksList().size() / 2;
    return longestUnpaintedSegment.get(indexOfPlankToPaintInLongestUnpainted);
  }

  public synchronized boolean paintPlank(Plank plank, Painter painter) {
    if (!plank.getStatus().equals(Status.Unpainted)) {
      return false;
    }
    try {
      plank.setPainter(painter);
      plank.setStatus(Status.InPainting);
      painter.getThread().sleep(200);
      plank.setStatus(Status.Painted);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public int getIndexOfMiddlePlankForPainting() {
    return plankList.indexOf(getMiddlePlankForPainting());
  }

  public synchronized String getPrettyString() {
    StringBuilder temp = new StringBuilder();
    for (Plank plank : plankList) {
      if (plank.getPainter() == null) {
        temp.append(".");
      } else {
        temp.append(plank.getPainter().getName());
      }
    }
    return temp.toString();
  }
}
