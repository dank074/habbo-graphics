package ch.habbo.graphics.furnitures.collections.items;

import java.util.List;

public class FloorItem {

    private final int Id;
    private final String ClassName;
    private final int Revision;
    private final int DefaultDirection;
    private final int X;
    private final int Y;
    private final List<String> Colors;
    public FloorItem(int id, String classname, int rev, int defaultDir, int x, int y, List<String> colors){
        this.Id = id;
        this.ClassName = classname;
        this.Revision = rev;
        this.DefaultDirection = defaultDir;
        this.X = x;
        this.Y = y;
        this.Colors = colors;
    }
    
    public int getId(){
        return this.Id;
    }
    
    public String getClassName(){
        return this.ClassName;
    }
    
    public int getRevision(){
        return this.Revision;
    }
    
    public int getDefaultDirection(){
        return this.DefaultDirection;
    }
    
    public int getX(){
        return this.X;
    }
    
    public int getY(){
        return this.Y;
    }
    
    public List<String> getColors(){
        return this.Colors;
    }
}
