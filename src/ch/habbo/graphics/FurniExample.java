package ch.habbo.graphics;

import ch.habbo.graphics.furnitures.FurniSize;
import ch.habbo.graphics.furnitures.Furniture;
import ch.habbo.graphics.furnitures.collections.ItemCollection;
import ch.habbo.graphics.furnitures.collections.Parser;
import ch.habbo.graphics.tools.swf.SWFExport;
import java.io.File;
import java.nio.file.Paths;
import javax.imageio.ImageIO;


public class FurniExample {
    
    public static void main(String[] args) throws Exception{
        Parser p = new Parser();
        ItemCollection collection = p.parseFurniData(Paths.get(System.getProperty("user.dir"), "xml", "furnidata.xml"));
        SWFExport export = new SWFExport();
        export.exportFurnis("swfs/furniture/{filename}.swf","furni/assets/{filename}", collection);
        Furniture furni = new Furniture("hblooza14_tent", 0, 0, 0, FurniSize.NORMAL, "furni/assets/{classname}/{classname}_{classname}_{file}.xml", "{classname}_{size}_{part}_{direction}_{frame}", "furni/assets/{classname}/{classname}_{classname}_icon.png", "furni/assets/{classname}/{classname}_{filename}.png");
        furni.render();
        File outputfile = new File("hblooza14_tent.png");
        ImageIO.write(furni.getImage(), "png", outputfile);
    }
    
}
