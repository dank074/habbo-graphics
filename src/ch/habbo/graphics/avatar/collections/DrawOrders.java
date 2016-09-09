package ch.habbo.graphics.avatar.collections;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

public class DrawOrders {
    private HashMap<String, HashMap<Integer, String[]>> drawOrders;
    public DrawOrders(String jsonFile) throws IOException{
        this.drawOrders = new HashMap<>();
        JSONObject main = new JSONObject(new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir"), jsonFile))));
        for(String key : main.keySet()){
            this.drawOrders.put(key, new HashMap<>());
            switch(main.get(key).getClass().getSimpleName()){
                case "JSONObject":
                {
                    JSONObject obj = main.getJSONObject(key);
                    for(String k : obj.keySet()){
                        String[] drawOrder = obj.getJSONArray(k).join(",").replace("\"", "").split(",");
                        this.drawOrders.get(key).put(Integer.parseInt(k), drawOrder);
                    }
                    continue;
                }
                case "JSONArray":
                {
                    JSONArray arr = main.getJSONArray(key);
                    for(int i = 0; i < arr.length(); i++){
                        String[] drawOrder = arr.getJSONArray(i).join(",").replace("\"", "").split(",");
                        this.drawOrders.get(key).put(i, drawOrder);
                    }
                }
            }
        }
    }
    
    public String[] getDrawOrder(String action, Integer direction){
        return this.drawOrders.get(action).get(direction);
    }
}
