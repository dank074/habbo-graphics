package ch.habbo.graphics.furnitures.visualization;

import org.w3c.dom.Element;

public class Layer {
    private final Integer Id;
    private final Integer Z;
    private final Integer Alpha;
    private final String Ink;
    public Layer(Element e){
        this.Id = Integer.parseInt(e.getAttribute("id"));
        this.Z = e.hasAttribute("z") ? Integer.parseInt(e.getAttribute("z")) : 0;
        this.Alpha = e.hasAttribute("alpha") ? Integer.parseInt(e.getAttribute("alpha")) : 0;
        this.Ink = e.hasAttribute("ink") ? e.getAttribute("ink") : "";
    }
    
    public Integer getId(){
        return this.Id;
    }
    
    public Integer getZ(){
        return this.Z;
    }
    
    public Integer getAlpha(){
        return this.Alpha;
    }
    public String getInk(){
        return this.Ink;
    }
}
