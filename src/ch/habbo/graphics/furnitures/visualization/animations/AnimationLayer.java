package ch.habbo.graphics.furnitures.visualization.animations;

import java.util.List;

public class AnimationLayer {
    private final Integer Id;
    private final List<Integer> Sequences;
    public AnimationLayer(int id, List<Integer> sequences){
        this.Id = id;
        this.Sequences = sequences;
    }
    
    public Integer getId(){
        return this.Id;
    }
    
    public Integer getFrame(Integer frm){
        if(!this.Sequences.isEmpty() && this.Sequences.size() >= frm && frm != 0){
            return this.Sequences.get(frm - 1);
        }
        if(!this.Sequences.isEmpty()){
            return this.Sequences.get(0);
        }
        return 0;
    }
}
