package pwr.ite.bedrylo.model;

import lombok.Data;
import pwr.ite.bedrylo.model.enums.Status;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Data
public class Fence {
    
    public static Fence INSTANCE = null;
    
    private PaintContainer paintContainer = PaintContainer.getInstance();
    
    private UUID id;
    
    private int length;
    
    private volatile List<FencePart> fencePartList;
    
    private volatile Status status;
    
    public Fence(int fenceLength, int fencePartLength){
        this.id = UUID.randomUUID();
        this.length = fenceLength;
        this.status = Status.Unpainted;
        this.fencePartList = new ArrayList<FencePart>(length);
        for (int i = 0; i < fenceLength; i++) {
            this.fencePartList.add(new FencePart(fencePartLength));
        }
    }
    
    public FencePart findFencePartByStatus(Status status){
        return fencePartList.stream()
                .filter(fencePart -> fencePart.getStatus() == status)
                .max(Comparator.comparing(o -> o.getUnpaintedPlanks().size()))
                .orElse(null);
    }
    
    public FencePart findFencePartToWork(){
        FencePart fencePartToWork = findFencePartByStatus(Status.Unpainted);
        if (fencePartToWork == null) {
            fencePartToWork = findFencePartByStatus(Status.InPainting);
        }
        if (fencePartToWork == null) {
            status = Status.Painted;
        }
        return fencePartToWork;
    }
    
    public static Fence getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new Fence(16,32);
        }
        return INSTANCE;
    }

    public static Fence getInstance(int length, int lengthOfPart){
        if (INSTANCE == null) {
            INSTANCE = new Fence(length,lengthOfPart);
        }
        return INSTANCE;
    }
    
    public String getPrettyString(int action, String name){
        String temp = paintContainer.getPrettyString(action, name) + "\n";
        for (Painter painter: Painter.painterList) {
            temp += painter.getName() + " ";
        }
        temp += "\n";
        for (Painter painter: Painter.painterList) {
            temp += painter.getBucket().getPaintLeft() + " ";
        }
        temp += "\n|";
        for (FencePart fencePart: fencePartList) {
            temp += fencePart.getPrettyString() + "|";
        }
        if (findFencePartByStatus(Status.Unpainted)==null && findFencePartByStatus(Status.InPainting)==null) {
            status = Status.Painted;
            temp += "finished";
        }
        return temp;
    }
    
}
