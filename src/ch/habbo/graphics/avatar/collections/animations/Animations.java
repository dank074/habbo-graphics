package ch.habbo.graphics.avatar.collections.animations;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import org.json.JSONObject;

public class Animations {
    private final HashMap<String, Animation> animations;
    
    public Animations(String jsonFile) throws Exception{
        this.animations = new HashMap<>();
        JSONObject main = new JSONObject(new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir"), jsonFile))));
        for(String s : main.keySet()){
            this.animations.put(s, new Animation(s, main.getJSONObject(s)));
        }
    }
    
    public Animation getAnimation(String action){
        return this.animations.get(action);
    }
    
    public Collection<Animation> getAnimations(){
        return this.animations.values();
    }
    
    public Integer size(){
        return this.animations.size();
    }
    
    public Boolean hasAnimation(String action){
        return this.animations.containsKey(action);
    }
}
