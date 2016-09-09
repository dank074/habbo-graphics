package ch.habbo.graphics;

import ch.habbo.graphics.avatar.AvatarImage;
import ch.habbo.graphics.avatar.AvatarRenderType;
import ch.habbo.graphics.avatar.collections.AvatarCollections;
import ch.habbo.graphics.furnitures.collections.Parser;
import ch.habbo.graphics.tools.DataExtractor;
import ch.habbo.graphics.tools.swf.SWFExport;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.imageio.ImageIO;

public class Example {
    public static void main(String[] args) throws Exception{
        
        //Parser p = new Parser();
       // p.parseFurniData(Paths.get("xml", "furnidata.xml"));
        //DataExtractor Example
        
        DataExtractor extractor = new DataExtractor("xml");
        String jsonFolder = "avatar/json";
        if(!Files.exists(Paths.get(jsonFolder))){
            Files.createDirectory(Paths.get(jsonFolder));
        }
        Files.write(Paths.get(jsonFolder, "avatar_draworder.json"), extractor.getDrawOrders().toString().getBytes());
        Files.write(Paths.get(jsonFolder, "avatar_colors.json"), extractor.getColors().toString().getBytes());
        Files.write(Paths.get(jsonFolder, "avatar_sets.json"), extractor.getSets().toString().getBytes());
        Files.write(Paths.get(jsonFolder, "avatar_parts.json"), extractor.getLibraries().toString().getBytes());
        
        //SWFExport Example
       SWFExport export = new SWFExport();
       export.exportClothes("swfs/{filename}.swf","avatar/assets/{filename}","xml");
        
        //Avatar Render Example
        
        String base = "avatar/json/avatar_{c}.json";
        AvatarCollections collection = new AvatarCollections(
                base.replace("{c}", "animations"), 
                base.replace("{c}", "colors"), 
                base.replace("{c}", "draworder"), 
                base.replace("{c}", "parts"),
                base.replace("{c}", "partsets"),
                base.replace("{c}", "sets"),
                "avatar/assets/{libraryName}/{libraryName}_",
                "avatar/assets/{libraryName}/{libraryName}_manifest.xml"
        );
        AvatarImage image = new AvatarImage("hr-155-31.hd-209-19.ch-3030-82.lg-275-64.ha-1006", 3, 3, "wav", 0, 0, 1 , AvatarRenderType.FULL, collection);
        image.render();
        File outputfile = new File("hr-155-31.hd-209-19.ch-3030-82.lg-275-64.ha-1006.png");
        ImageIO.write(image.getImage(), "png", outputfile);
        
    }
}
