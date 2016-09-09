package ch.habbo.graphics.avatar.collections;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import org.json.JSONObject;

public class Libraries {
    private final HashMap<String, HashMap<String, String>> libraries;
    
    public Libraries(String jsonFile) throws IOException{
        this.libraries = new HashMap<>();
        JSONObject main = new JSONObject(new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir"), jsonFile))));
        for(String key : main.keySet()){
            this.libraries.put(key, new HashMap<>());
            JSONObject obj = main.getJSONObject(key);
            for(String key2 : obj.keySet()){
                this.libraries.get(key).put(key2, obj.getString(key2));
            }
        }
    }
    
    
    public String getLibrary(String part, String Id){
        return this.libraries.get(part).get(Id);
    }
}
