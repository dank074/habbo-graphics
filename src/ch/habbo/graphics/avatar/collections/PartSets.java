package ch.habbo.graphics.avatar.collections;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class PartSets {
    
    private final HashMap<String, String> partflips;
    private final HashMap<String, List<String>> activeparts;
    
    public PartSets(String jsonFile) throws IOException{
        JSONObject main = new JSONObject(new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir"), jsonFile))));
        this.partflips = new HashMap<>();
        this.activeparts = new HashMap<>();
        for(String key : main.keySet()){
            JSONObject object = main.getJSONObject(key);
            if(key.equals("partflips")){
                for(String key2 : object.keySet()){
                    this.partflips.put(key2, object.getString(key2));
                }
            }else if(key.equals("activepart")){
                for(String key3 : object.keySet()){
                    this.activeparts.put(key3, new ArrayList<>());
                    JSONArray array = object.getJSONArray(key3);
                    for(int i = 0; i < array.length(); i++){
                        this.activeparts.get(key3).add(array.getString(i));
                    }
                }
            }
        }
    }
    
    public List<String> getActiveParts(String renderType){
        return this.activeparts.get(renderType);
    }
    
    public String getPartFlip(String part){
        return this.partflips.get(part);
    }

    public boolean hasActivePart(String renderType) {
        return this.activeparts.containsKey(renderType);
    }
}
