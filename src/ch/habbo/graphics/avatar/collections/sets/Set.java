package ch.habbo.graphics.avatar.collections.sets;

import org.json.JSONObject;

public class Set {
    private final Integer Id;
    private String Type;
    private final Boolean Colorable;
    private final Integer Index;
    private final Integer ColorIndex;
    
    public Set(JSONObject set){
        this.Id = Integer.parseInt(set.getString("id"));
        this.Type = set.getString("type");
        this.Colorable = set.getString("colorable").equals("1");
        this.Index = Integer.parseInt(set.getString("index"));
        this.ColorIndex = Integer.parseInt(set.getString("colorindex"));
    }
    
    public Set(String type, Integer id, Boolean colorable, Integer index, Integer colorIndex){
        this.Type = type;
        this.Id = id;
        this.Colorable = colorable;
        this.Index = index;
        this.ColorIndex = colorIndex;
    }
    
    public Integer getId(){
        return this.Id;
    }
    
    public String getType(){
        return this.Type;
    }
    
    public Boolean isColorable(){
        return this.Colorable;
    }
    
    public Integer getIndex(){
        return this.Index;
    }
    
    public Integer getColorIndex(){
        return this.ColorIndex;
    }
    
    public void setType(String type){
        this.Type = type;
    }
}
