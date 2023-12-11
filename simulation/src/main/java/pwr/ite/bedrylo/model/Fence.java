package pwr.ite.bedrylo.model;

import lombok.Data;
import pwr.ite.bedrylo.model.enums.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Fence {
    
    public static Fence INSTANCE = null;
    
    private UUID id;
    
    private int length;
    
    private List<FencePart> fencePartList;
    
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
    
    public static Fence getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new Fence(8,16);
        }
        return INSTANCE;
    }
    
}
