package ch.habbo.graphics.furnitures.visualization.animations.colors;

import ch.habbo.graphics.tools.ImageTools;
import java.awt.Color;

public class ColorLayer {
    private final Integer LayerId;
    private final Integer ColorId;
    private final String Color;
    
    public ColorLayer(Integer layerId, Integer colorId, String color){
        this.LayerId = layerId;
        this.ColorId = colorId;
        this.Color = color;
    }
    
    public Integer getLayerId(){
        return this.LayerId;
    }
    
    public Integer getColorId(){
        return this.ColorId;
    }
    
    public Color getColor(){
        return ImageTools.hexToRGB(this.Color);
    }
}
