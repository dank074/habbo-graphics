package ch.habbo.graphics.avatar;

import java.awt.image.BufferedImage;

public class Part {

    private final BufferedImage Image;
    private Integer X;
    private Integer Y;
    private final Integer Width;
    private final Integer Height;
    private String Type;
    private final Integer Id;
    
    public Part(BufferedImage img,Integer x,Integer y,Integer width, Integer height, String type,Integer id){
        this.Image = img;
        this.X = x;
        this.Y = y;
        this.Width = width;
        this.Height = height;
        this.Type = type;
        this.Id = id;
    }
    
    public BufferedImage getImage(){
        return this.Image;
    }
    
    public Integer getX(){
        return this.X;
    }
    
    public Integer getY(){
        return this.Y;
    }
    
    public Integer getWidth(){
        return this.Width;
    }
    
    public Integer getHeight(){
        return this.Height;
    }
    
    public String getType(){
        return this.Type;
    }
    
    public Integer getId(){
        return this.Id;
    }

    public void setType(String type){
        this.Type = type;
    }
    
    public void setX(Integer x){
        this.X = x;
    }
    
    public void setY(Integer y){
        this.Y = y;
    }
    
}
