package ch.habbo.graphics.furnitures.collections.items;

public class WallItem {
    
    private final int Id;
    private final String ClassName;
    private final int Revision;
    
    public WallItem(int id, String classname, int rev){
        this.Id = id;
        this.ClassName = classname;
        this.Revision = rev;
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
}
