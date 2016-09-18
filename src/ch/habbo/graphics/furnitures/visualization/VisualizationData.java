package ch.habbo.graphics.furnitures.visualization;

import ch.habbo.graphics.furnitures.FurniSize;
import ch.habbo.graphics.furnitures.visualization.animations.Animation;
import ch.habbo.graphics.furnitures.visualization.animations.colors.ColorLayer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class VisualizationData {
    private final HashMap<Integer, Visualization> visualizations;
    
    public VisualizationData(String XMLFile) throws Exception{
        visualizations = new HashMap<>();
        Document dom;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder dbu = dbf.newDocumentBuilder();
        dom = dbu.parse(XMLFile);
        NodeList visualss = dom.getDocumentElement().getElementsByTagName("visualization");
        for(int i = 0 ; i < visualss.getLength();i++) {
            Element visual = (Element)visualss.item(i);
            HashMap<Integer, Layer> layers = new HashMap<>();
            NodeList lyrs = visual.getElementsByTagName("layer");
            for(int j = 0; j < lyrs.getLength(); j++){
                Element layer = (Element)lyrs.item(j);
                layers.put(
                        Integer.parseInt(layer.getAttribute("id")),
                        new Layer(layer));
            }
            List<Integer> directions = new ArrayList();
            if(visual.getElementsByTagName("directions").getLength() > 0){
                NodeList dirs = ((Element)visual.getElementsByTagName("directions").item(0)).getElementsByTagName("direction");
                for(int j = 0; j < dirs.getLength(); j++){
                    Element direction = (Element)dirs.item(j);
                    directions.add(Integer.parseInt(direction.getAttribute("id")));
                }
            }
            List<Animation> animations = new ArrayList();
            if(visual.getElementsByTagName("animations").getLength() > 0){
                NodeList anis = ((Element)visual.getElementsByTagName("animations").item(0)).getElementsByTagName("animation");
                for(int j = 0; j < anis.getLength(); j++){
                    Element anim = (Element)anis.item(j);
                    animations.add(new Animation(anim));
                }
            }
            List<ColorLayer> colors = new ArrayList();
            if(visual.getElementsByTagName("colors").getLength() > 0){
                NodeList colorList = ((Element)visual.getElementsByTagName("colors").item(0)).getElementsByTagName("color");
                for(int j = 0; j < colorList.getLength(); j++){
                    Element color = (Element)colorList.item(j);
                    NodeList cLayers = color.getElementsByTagName("colorLayer");
                    for(int k = 0; k < cLayers.getLength(); k++){
                        Element colorLayer = (Element)cLayers.item(k);
                        colors.add(new ColorLayer(
                                Integer.parseInt(colorLayer.getAttribute("id")),
                                Integer.parseInt(color.getAttribute("id")),
                                colorLayer.getAttribute("color")
                        ));
                    }
                }
            }
            this.visualizations.put(
                    Integer.parseInt(visual.getAttribute("size")), 
                    new Visualization(
                            Integer.parseInt(visual.getAttribute("size")),
                            Integer.parseInt(visual.getAttribute("layerCount")),
                            Integer.parseInt(visual.getAttribute("angle")),
                            layers,
                            directions,
                            animations,
                            colors
            ));
        }
    }
    
    public Visualization getVisualization(FurniSize size){
        switch(size){
            case NORMAL:
                return this.visualizations.get(64);
            case ZOOMOUT:
                return this.visualizations.get(64); // 32 ?
            case ICON:
                return this.visualizations.get(1);
        }
        return null;
    }
}
