module pwr.ite.bedrylo.simulation {
  requires lombok;
  requires javafx.base;

  opens pwr.ite.bedrylo.model;
  opens pwr.ite.bedrylo.model.enums;
  opens pwr.ite.bedrylo;

  exports pwr.ite.bedrylo;
  exports pwr.ite.bedrylo.model;
  exports pwr.ite.bedrylo.model.enums;
}
