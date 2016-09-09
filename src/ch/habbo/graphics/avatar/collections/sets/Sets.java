package ch.habbo.graphics.avatar.collections.sets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import org.json.JSONObject;

public class Sets {
    
    private final HashMap<String, Palette> sets;
    
    public Sets(String jsonFile) throws IOException{
        this.sets = new HashMap<>();
        JSONObject main = new JSONObject(new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir"), jsonFile))));
        for(String key : main.keySet()){
            JSONObject type = main.getJSONObject(key);
            this.sets.put(key, new Palette(key,Integer.parseInt(type.getString("paletteid")), type.getJSONObject("sets")));
        }
    }
    
    public Palette getPalette(String type){
        return this.sets.get(type);
    }
}
