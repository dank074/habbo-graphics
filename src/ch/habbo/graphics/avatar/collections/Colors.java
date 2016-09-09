package ch.habbo.graphics.avatar.collections;

import ch.habbo.graphics.tools.ImageTools;
import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import org.json.JSONObject;

public class Colors {
    
    private final HashMap<Integer, HashMap<Integer, Color>> colors;
    
    public Colors(String jsonFile) throws IOException{
        this.colors = new HashMap<>();
        JSONObject main = new JSONObject(new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir"), jsonFile))));
        for (String s : main.keySet()) {
            Integer palette = Integer.parseInt(s);
            this.colors.put(palette, new HashMap<>());
            JSONObject obj2 = main.getJSONObject(s);
            for(String key : obj2.keySet()){
                this.colors.get(palette).put(Integer.parseInt(key), ImageTools.hexToRGB(obj2.getString(key)));
            }
        }
    }
    
    /**
     * 
     * @param Id Color Id (for example 1)
     * @return returns the Color as java.awt.Color
     */
    public Color getColor(Integer Id){
        for(HashMap<Integer, Color> color : colors.values()){
            if(color.containsKey(Id)){
                return color.get(Id);
            }
        }
        return null;
    }
}
