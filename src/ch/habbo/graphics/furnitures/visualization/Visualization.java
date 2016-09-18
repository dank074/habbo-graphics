package ch.habbo.graphics.furnitures.visualization;

import ch.habbo.graphics.furnitures.visualization.animations.Animation;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Visualization {
    
    private final Integer Size;
    private final Integer Layers;
    private final Integer Angle;
    private final HashMap<Integer, Layer> Layer;
    private final List<Integer> Directions;
    private final List<Animation> Animations;
    public Visualization(Integer size, Integer layers, Integer angle, HashMap<Integer, Layer> layer, List<Integer> directions, List<Animation> animations){
        this.Size = size;
        this.Layers = layers;
        this.Angle = angle;
        this.Layer = layer;
        this.Directions =  directions;
        this.Animations = animations;
    }
    
    public Integer getSize(){
        return this.Size;
    }
    
    public Integer getLayers(){
        return this.Layers;
    }
    
    public Integer getAngle(){
        return this.Angle;
    }
    
    public Layer getLayer(Integer layerId){
        if(this.Layer.containsKey(layerId)){
            return this.Layer.get(layerId);
        }
        return null;
    }
    
    public Boolean hasDirection(Integer Direction){
        return this.Directions.contains(Direction);
    }
    
    public Integer getDefaultDirection(){
        return this.Directions.get(0);
    }
    
    public Animation getAnimation(int AnimationId){
        for(Animation ani : this.Animations){
            if(ani.getId() == AnimationId){
                return ani;
            }
        }
        return null;
    }
}
