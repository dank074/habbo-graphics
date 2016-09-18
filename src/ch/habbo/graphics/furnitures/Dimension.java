package ch.habbo.graphics.furnitures;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Dimension {
    
    private final Integer X;
    private final Integer Y;
    private final Double Z;
    
    public Dimension(Integer x, Integer y, Double z){
        this.X = x;
        this.Y = y;
        this.Z = z;
    }
    
    public Integer getX(){
        return this.X;
    }
    
    public Integer getY(){
        return this.Y;
    }
    
    public Double getZ(){
        return this.Z;
    }
    
    public static Dimension parse(String xmlFile) throws Exception{
        Document dom;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder dbu = dbf.newDocumentBuilder();
        dom = dbu.parse(xmlFile);
        Element dimension = (Element)dom.getElementsByTagName("dimensions").item(0);
        return new Dimension(
                Integer.parseInt(dimension.getAttribute("x")),
                Integer.parseInt(dimension.getAttribute("y")),
                Double.parseDouble(dimension.getAttribute("z")));
    }
}
