package ch.habbo.graphics.avatar.collections.animations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Animation {
    private final String Action;
    private final HashMap<String, List<Integer>> bodyParts; //String: Body Part, Integer[] Frames
    public Animation(String action, JSONObject object){
        this.Action = action;
        this.bodyParts = new HashMap<>();
        for(String s : object.keySet()){
            JSONArray arr = object.getJSONArray(s);
            this.bodyParts.put(s, new ArrayList<>());
            for(int i = 0; i < arr.length(); i++){
                this.bodyParts.get(s).add(Integer.parseInt(arr.getString(i)));
            }
        }
    }
    
    public String getAction(){
        return this.Action;
    }
    
    public Integer[] getFrames(String bodyPart){
        return this.bodyParts.get(bodyPart).toArray(new Integer[0]);
    }
    
    public HashMap<String, List<Integer>> getParts(){
        return this.bodyParts;
    }
    
    public Boolean hasPart(String bodyPart){
        return this.bodyParts.containsKey(bodyPart);
    }
}
