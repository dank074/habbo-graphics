package ch.habbo.graphics;

import ch.habbo.graphics.furnitures.collections.ItemCollection;
import ch.habbo.graphics.furnitures.collections.Parser;
import ch.habbo.graphics.tools.swf.SWFExport;
import java.nio.file.Paths;

public class FurniExample {
    
    public static void main(String[] args) throws Exception{
        System.out.println("Furni example :D");
        Parser p = new Parser();
        ItemCollection collection = p.parseFurniData(Paths.get(System.getProperty("user.dir"), "xml", "furnidata.xml"));
        SWFExport export = new SWFExport();
        export.exportFurnis("swfs/furniture/{filename}.swf","furni/assets/{filename}", collection);
        
    }
    
}
