package ch.habbo.graphics.furnitures;

public class Asset {
    
    private final String Name;
    private Integer X;
    private Integer Y;
    private final Integer originalX;
    private final Integer originalY;
    private final Boolean FlipH;
    private final String Source;
    
    public Asset(String name, int x, int y, boolean flipHorizontal, String source){
        this.Name = name;
        this.X = x;
        this.Y = y;
        this.originalX = x;
        this.originalY = y;
        this.FlipH = flipHorizontal;
        this.Source = source;
    }
    
    public String getName(){
        return this.Name;
    }
    
    public Integer getX(){
        return this.X;
    }
    
    public Integer getY(){
        return this.Y;
    }
    
    public Boolean flipHorizontal(){
        return this.FlipH;
    }
    
    public String getSource(){
        return this.Source.equals("") ? this.getName() : this.Source;
    }
    
    public void setX(Integer x){
        this.X = x;
    }
    
    public void setY(Integer y){
        this.Y = y;
    }
    
    public Integer originalX(){
        return this.originalX;
    }
    public Integer originalY(){
        return this.originalY;
    }
}
