package ch.habbo.graphics.avatar.collections;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Manifest {

    private final HashMap<String, HashMap<String, Integer[]>> cache;
    private final String manifestBase;
    
    public Manifest(String base){
        this.cache = new HashMap<>();
        this.manifestBase = base;
    }
    
    /**
     * reads the Manifest XML File an caches it
     * @param manifest Filename
     * @return returns a Map with Key ImageName and Value ImageOffsets
     */
    public HashMap<String, Integer[]> parse(String manifest){
        if(this.cache.containsKey(manifest)){
            return this.cache.get(manifest);
        }
        try
        {
            Document dom;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbu = dbf.newDocumentBuilder();
            dom = dbu.parse(this.manifestBase.replace("{libraryName}", manifest));
        
            Element docEle = (Element)dom.getDocumentElement().getElementsByTagName("assets").item(0);
            NodeList nl = docEle.getElementsByTagName("asset");
            HashMap<String, Integer[]> result = new HashMap<>();
            for(int i = 0 ; i < nl.getLength();i++) {
                Element el = (Element)nl.item(i);
                Element elem = (Element)el.getElementsByTagName("param").item(0);
                String[] splitted = elem.getAttribute("value").split(",");
                result.put(el.getAttribute("name"), new Integer[] {Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1])});
                el = null;
                elem = null;
            }
            nl = null;
            docEle = null;
            dom = null;
            dbf = null;
            dbu = null;
            this.cache.put(manifest, result);
            return result;
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(Manifest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
