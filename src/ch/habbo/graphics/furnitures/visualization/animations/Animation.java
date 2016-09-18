package ch.habbo.graphics.furnitures.visualization.animations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Animation {
    
    private final Integer Id;
    private final HashMap<Integer, AnimationLayer> layers;
    public Animation(Element e){
         this.layers = new HashMap<>();
         this.Id = Integer.parseInt(e.getAttribute("id"));
         NodeList lyrs = e.getElementsByTagName("animationLayer");
         for(int i = 0; i < lyrs.getLength(); i++){
             Element layer = (Element)lyrs.item(i);
             List<Integer> sequences = new ArrayList();
             NodeList seqs = layer.getElementsByTagName("frame");
             for(int j = 0; j < seqs.getLength(); j++){
                 sequences.add(Integer.parseInt(((Element)seqs.item(j)).getAttribute("id")));
             }
             this.layers.put(Integer.parseInt(layer.getAttribute("id")),
                     new AnimationLayer(
                             Integer.parseInt(layer.getAttribute("id")),
                             sequences)
                     );
         }
    }
    
    public Integer getId(){
        return this.Id;
    }
    
    public AnimationLayer getLayer(Integer id){
        return this.layers.get(id);
    }
}
