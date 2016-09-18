package ch.habbo.graphics.furnitures.collections;

import ch.habbo.graphics.furnitures.collections.items.FloorItem;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Parser {
    
    public Parser(){
        
    }
    
    public ItemCollection parseFurniData(Path furniData) throws Exception{
        ItemCollection result = new ItemCollection();
        Document dom;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder dbu = dbf.newDocumentBuilder();
        dom = dbu.parse(furniData.toString());
        Element floorItemsElement = (Element)dom.getDocumentElement().getElementsByTagName("roomitemtypes").item(0);
        NodeList fItem = floorItemsElement.getElementsByTagName("furnitype");
        for(int i = 0 ; i < fItem.getLength();i++) {
            Element roomItem = (Element)fItem.item(i);
            List<String> clrs = new ArrayList();
            if(roomItem.getElementsByTagName("partcolors").getLength() != 0){
                NodeList colors = ((Element)roomItem.getElementsByTagName("partcolors").item(0)).getElementsByTagName("color");
                for(int i2 = 0; i2 < colors.getLength(); i2++){
                    Element c = (Element)colors.item(i2);
                    clrs.add(c.getTextContent());
                }
            }
            result.add(new FloorItem(
                    this.getIntAttribute(roomItem, "id"),
                    roomItem.getAttribute("classname"),
                    this.getIntContent(roomItem, "revision"),
                    this.getIntContent(roomItem, "defaultdir"),
                    this.getIntContent(roomItem, "xdim"),
                    this.getIntContent(roomItem, "ydim"),
                    clrs
            ));
            
        }
        Element wallItemsElement = (Element)dom.getDocumentElement().getElementsByTagName("wallitemtypes").item(0);
        NodeList wItems = floorItemsElement.getElementsByTagName("furnitype");
        for(int i = 0 ; i < fItem.getLength();i++) {
            Element roomItem = (Element)fItem.item(i);
            List<String> clrs = new ArrayList();
            NodeList colors = ((Element)roomItem.getElementsByTagName("partcolors").item(0)).getElementsByTagName("color");
            for(int i2 = 0; i2 < colors.getLength(); i2++){
                Element c = (Element)colors.item(i2);
                clrs.add(c.getTextContent());
            }
            result.add(new FloorItem(
                    this.getIntAttribute(roomItem, "id"),
                    roomItem.getAttribute("classname"),
                    this.getIntContent(roomItem, "revision"),
                    this.getIntContent(roomItem, "defaultdir"),
                    this.getIntContent(roomItem, "xdim"),
                    this.getIntContent(roomItem, "ydim"),
                    clrs
            ));
        }
        return result;
    }
    
    public String getStringContent(Element element, String tag){
        return ((Element)element.getElementsByTagName(tag).item(0)).getTextContent();
    }
    
    public Integer getIntContent(Element element, String tag){
        return Integer.parseInt(((Element)element.getElementsByTagName(tag).item(0)).getTextContent());
    }
    
    public Integer getIntAttribute(Element element, String attr){
        return Integer.parseInt(element.getAttribute(attr));
    }
}
