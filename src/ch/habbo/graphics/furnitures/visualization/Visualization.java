package ch.habbo.graphics.furnitures.visualization;

import ch.habbo.graphics.furnitures.visualization.animations.Animation;
import ch.habbo.graphics.furnitures.visualization.animations.colors.ColorLayer;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;

public class Visualization {
    
    private final Integer Size;
    private final Integer Layers;
    private final Integer Angle;
    private final HashMap<Integer, Layer> Layer;
    private final List<Integer> Directions;
    private final List<Animation> Animations;
    private final List<ColorLayer> Colors;
    
    public Visualization(Integer size, Integer layers, Integer angle, HashMap<Integer, Layer> layer, List<Integer> directions, List<Animation> animations, List<ColorLayer> colors){
        this.Size = size;
        this.Layers = layers;
        this.Angle = angle;
        this.Layer = layer;
        this.Directions =  directions;
        this.Animations = animations;
        this.Colors = colors;
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
    
    public Color getColor(int LayerId, int ColorId){
        for(ColorLayer clayer : this.Colors){
            if(clayer.getLayerId() == LayerId && clayer.getColorId() == ColorId){
                return clayer.getColor();
            }
        }
        return null;
    }
}
