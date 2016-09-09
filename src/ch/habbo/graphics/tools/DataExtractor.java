package ch.habbo.graphics.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DataExtractor {
    
    private final String XMLPath;
    
    public DataExtractor(String xmlPath){
        this.XMLPath = xmlPath;
    }
    
    public JSONObject getDrawOrders(){
        if(!Files.exists(Paths.get(this.XMLPath, "draworder.xml"))){
            return null;
        }
        JSONObject result = new JSONObject();
        try
        {
            Document dom;
            
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbu = dbf.newDocumentBuilder();
            dom = dbu.parse(Paths.get(this.XMLPath, "draworder.xml").toAbsolutePath().toString());
            
            Element docEle = (Element)dom.getDocumentElement();
            NodeList nl = docEle.getElementsByTagName("action");
            //reads all actions
            for(int i = 0 ; i < nl.getLength();i++) {
                Element el = (Element)nl.item(i);
                result.put(el.getAttribute("id"), new JSONObject());
                NodeList directions = el.getElementsByTagName("direction");
                //reads all directions
                for(int i2 = 0; i2 < directions.getLength(); i2++){
                    Element el2 = (Element)directions.item(i2);
                    result.getJSONObject(el.getAttribute("id")).put(el2.getAttribute("id"), new JSONArray());
                    NodeList parts = el2.getElementsByTagName("part");
                    //reads every "part" set-type
                    for(int i3 = 0; i3 < parts.getLength(); i3++){
                        Element el3 = (Element)parts.item(i3);
                        String elementName = el3.getAttribute("set-type");
                        result.getJSONObject(el.getAttribute("id")).getJSONArray(el2.getAttribute("id")).put(elementName);
                        switch (elementName) {
                            case "ls":
                                result.getJSONObject(el.getAttribute("id")).getJSONArray(el2.getAttribute("id")).put("lc");
                                break;
                            case "ch":
                                result.getJSONObject(el.getAttribute("id")).getJSONArray(el2.getAttribute("id")).put("cc");
                                result.getJSONObject(el.getAttribute("id")).getJSONArray(el2.getAttribute("id")).put("cp");
                                break;
                            case "rs":
                                result.getJSONObject(el.getAttribute("id")).getJSONArray(el2.getAttribute("id")).put("rc");
                                break;
                            default:
                                break;
                        }
                        el3 = null;
                    }
                    parts = null;
                    el2 = null;
                }
                directions = null;
                el = null;
            }
            nl = null;
            docEle = null;
            dom = null;
            dbf = null;
            dbu = null;
            
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(DataExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.gc();
        return result;
    }
    
    public JSONObject getColors(){
        if(!Files.exists(Paths.get(this.XMLPath, "figuredata.xml"))){
            return null;
        }
        JSONObject result = new JSONObject();
        try
        {
            Document dom;
            
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbu = dbf.newDocumentBuilder();
            dom = dbu.parse(Paths.get(this.XMLPath, "figuredata.xml").toAbsolutePath().toString());
            
            Element docEle = (Element)dom.getDocumentElement().getElementsByTagName("colors").item(0);
            NodeList nl = docEle.getElementsByTagName("palette");
            //reads all palettes
            for(int i = 0; i < nl.getLength(); i++){
                Element el = (Element)nl.item(i);
                result.put(el.getAttribute("id"), new JSONObject());
                NodeList nl2 = el.getElementsByTagName("color");
                //reads every color
                for(int i2 = 0; i2 < nl2.getLength(); i2++){
                    Element el2 = (Element)nl2.item(i2);
                    result.getJSONObject(el.getAttribute("id")).put(el2.getAttribute("id"), el2.getTextContent());
                    el2 = null;
                }
                nl2 = null;
                el = null;
            }
            nl = null;
            docEle = null;
            dom = null;
            dbf = null;
            dbu = null;
            
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(DataExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.gc();
        return result;
    }
    
    public JSONObject getSets(){
        if(!Files.exists(Paths.get(this.XMLPath, "figuredata.xml"))){
            return null;
        }
        JSONObject result = new JSONObject();
        try
        {
            Document dom;
            
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbu = dbf.newDocumentBuilder();
            dom = dbu.parse(Paths.get(this.XMLPath, "figuredata.xml").toAbsolutePath().toString());
            
            Element docEle = (Element)dom.getDocumentElement().getElementsByTagName("sets").item(0);
            NodeList nl = docEle.getElementsByTagName("settype");
            //reads all settypes
            for(int i = 0; i < nl.getLength(); i++){
                Element el = (Element)nl.item(i);
                JSONObject obj = new JSONObject();
                result.put(el.getAttribute("type"), obj);
                obj.put("paletteid", el.getAttribute("paletteid"));
                obj.put("sets", new JSONObject());
                NodeList nl2 = el.getElementsByTagName("set");
                //reads all sets
                for(int i2 = 0; i2 < nl2.getLength(); i2++){
                    Element el2 = (Element)nl2.item(i2);
                    JSONArray arr = new JSONArray();
                    obj.getJSONObject("sets").put(el2.getAttribute("id"), arr);
                    NodeList nl3 = el2.getElementsByTagName("part");
                    //reads all parts of the set
                    for(int i3 = 0; i3 < nl3.getLength(); i3++){
                        Element el3 = (Element)nl3.item(i3);
                        JSONObject obj2 = new JSONObject();
                        obj2.put("id", el3.getAttribute("id"));
                        obj2.put("type", el3.getAttribute("type"));
                        obj2.put("colorable", el3.getAttribute("colorable"));
                        obj2.put("index", el3.getAttribute("index"));
                        obj2.put("colorindex", el3.getAttribute("colorindex"));
                        arr.put(i3, obj2);
                        el3 = null;
                    }
                    el2 = null;
                    nl3 = null;
                }
                el = null;
                nl2 = null;
            }
            nl = null;
            docEle = null;
            dom = null;
            dbf = null;
            dbu = null;
            
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(DataExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.gc();
        return result;
    }
    
    public JSONObject getLibraries(){
        if(!Files.exists(Paths.get(this.XMLPath, "figuremap.xml"))){
            return null;
        }
        JSONObject result = new JSONObject();
        try
        {
            Document dom;
            
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbu = dbf.newDocumentBuilder();
            dom = dbu.parse(Paths.get(this.XMLPath, "figuremap.xml").toAbsolutePath().toString());
            
            Element docEle = (Element)dom.getDocumentElement();
            NodeList nl = docEle.getElementsByTagName("lib");
            //reads every library
            for(int i = 0; i < nl.getLength(); i++){
                Element el = (Element)nl.item(i);
                NodeList nl2 = el.getElementsByTagName("part");
                //reads every part
                for(int i2 = 0; i2 < nl2.getLength(); i2++){
                    Element el2 = (Element)nl2.item(i2);
                    String id = el2.getAttribute("id");
                    String type = el2.getAttribute("type");
                    if(!result.has(type)){
                        result.put(type, new JSONObject());
                    }
                    result.getJSONObject(type).put(id, el.getAttribute("id"));
                    el2 = null;
                }
                el = null;
                nl2 = null;
            }
            
            nl = null;
            docEle = null;
            dom = null;
            dbf = null;
            dbu = null;
            
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(DataExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.gc();
        return result;
    }
}
