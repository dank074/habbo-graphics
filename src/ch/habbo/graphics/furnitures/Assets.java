package ch.habbo.graphics.furnitures;

import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Assets {
    
    private final HashMap<String, Asset> assets;
    
    public Assets(String xmlFile) throws Exception{
        this.assets = new HashMap<>();
        Document dom;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder dbu = dbf.newDocumentBuilder();
        dom = dbu.parse(xmlFile);
        NodeList asts = dom.getDocumentElement().getElementsByTagName("asset");
        for(int i = 0 ; i < asts.getLength();i++) {
            Element asset = (Element)asts.item(i);
            this.assets.put(asset.getAttribute("name"), 
                    new Asset(
                    asset.getAttribute("name"),
                    Integer.parseInt(asset.getAttribute("x")),
                    Integer.parseInt(asset.getAttribute("y")),
                    asset.hasAttribute("flipH") ? asset.getAttribute("flipH").equals("1") : false,
                    asset.hasAttribute("source") ? asset.getAttribute("source") : ""
                    ));
        }
    }
    
    public Asset getAsset(String name){
        return this.assets.get(name);
    }
}
