package ch.habbo.graphics.avatar.collections.sets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Palette {
    
    private final String Type;
    private final Integer Id;
    private final HashMap<Integer, List<Set>> sets;
    
    public Palette(String type, Integer id, JSONObject _sets){
        this.Type = type;
        this.Id = id;
        this.sets = new HashMap<>();
        for(String setsId : _sets.keySet()){
            this.sets.put(Integer.parseInt(setsId), new ArrayList<>());
            JSONArray arr = _sets.getJSONArray(setsId);
            for(int i = 0; i < arr.length(); i++){
                this.sets.get(Integer.parseInt(setsId)).add(new Set(arr.getJSONObject(i)));
            }
        }
    }
    
    public String getType(){
        return this.Type;
    }
    
    public Integer getId(){
        return this.Id;
    }
    
    public List<Set> getSets(Integer setId){
        return this.sets.get(setId);
    }
    
    public List<Set> cloneSets(Integer setId){
        if(this.sets.get(setId) == null){
            return null;
        }
        List<Set> result = new ArrayList<>();
        this.sets.get(setId).stream().forEach((s) -> {
            result.add(new Set(s.getType(), s.getId(), s.isColorable(), s.getIndex(), s.getColorIndex()));
        });
        return result;
    }
    
    public HashMap<Integer, List<Set>> getSets(){
        return this.sets;
    }
}
