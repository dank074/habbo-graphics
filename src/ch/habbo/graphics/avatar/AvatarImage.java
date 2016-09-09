package ch.habbo.graphics.avatar;

import ch.habbo.graphics.avatar.collections.AvatarCollections;
import ch.habbo.graphics.avatar.collections.sets.Set;
import ch.habbo.graphics.tools.ImageTools;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class AvatarImage {
    
    private final String Figure;
    private final HashMap<String, Integer[]> FigureParts;
    private final Integer Direction;
    private final Integer HeadDirection;
    private final Integer CarryItem;
    private final Integer Sign;
    private final List<String> Actions;
    private final Integer Frame;
    private final HashMap<String, String> PartData;
    private final HashMap<String, Integer> PartDataIds;
    private final HashMap<String, List<Part>> Parts;
    private final List<String> UseHrbFor;
    private BufferedImage Avatar;
    private Integer AvatarWidth = 64;
    private Integer AvatarHeight = 110;
    private Integer[] AvatarOffset = new Integer[]{-32,45};
    private AvatarRenderType RenderType = AvatarRenderType.FULL;
    private String[] DrawOrder = null;
    private final AvatarCollections Collections;
    private final List<String> Rendered;
    
    /**
     * Initializes the AvatarImage
     * @param figure The avatar figure code
     * @param direction In which way the body "looks"
     * @param headDirection In which way the head looks
     * @param actions Actions like wlk (walk) std (stand) sit, spk (speak) sig (sign) drk (drink) more Actions can be separated with ","
     * @param carryItem If the User should drink set this to the item Id (not drinking : 0)
     * @param sign If the user should show a sing set this to the Sign Id (no sign : 0)
     * @param frame Walking / Waving / whatever Frame
     * @param renderType Render Type (FULL, HEAD or Part)
     * @param collections The AvatarCollections
     */
    public AvatarImage(String figure, Integer direction, Integer headDirection, String actions, Integer carryItem, Integer sign, Integer frame, AvatarRenderType renderType, AvatarCollections collections){
        if(direction < 0 || direction > 7){
            direction = 3;
        }
        if(headDirection < 0 || headDirection > 7){
            headDirection = 3;
        }
        this.Figure = figure;
        this.Direction = direction;
        this.HeadDirection = headDirection;
        this.CarryItem = carryItem;
        this.Sign = sign;
        this.Frame = frame;
        this.Actions = new ArrayList<>();
        this.Actions.addAll(Arrays.asList(actions.split(",")));
        this.RenderType = renderType;
        this.UseHrbFor = new ArrayList<>();
        this.UseHrbFor.add("hh_human_hats.26");
        this.UseHrbFor.add("acc_head_U_ears_muffs");
        this.UseHrbFor.add("hat_U_shade");
        this.PartData = new HashMap<>();
        this.PartDataIds = new HashMap<>();
        this.Rendered = new ArrayList<>();
        this.FigureParts = new HashMap<>();
        this.Parts = new HashMap<>();
        if((this.Actions.contains("respect") || this.Actions.contains("sig")) && !this.Actions.contains("wav")){
            this.Actions.add("wav");
        }
        if(this.Actions.contains("lay")){
            this.AvatarWidth = 110;
            this.AvatarHeight = 64;
            this.AvatarOffset = new Integer[] {-10, 20};
            this.DrawOrder = new String[]{"li", "lh", "ls", "lc", "bd", "lg", "ch", "ca", "cc", "cp", "wa", "rh", "rs", "rc", "hd", "fc", "ey", "hr", "hrb", "fa", "ea", "ha", "he", "ri", "sh"};
        }else if(this.RenderType == AvatarRenderType.HEAD){
            this.AvatarHeight = 64;
            this.AvatarWidth = 64;
            this.AvatarOffset = new Integer[]{-31, 65};
            this.DrawOrder = new String[] {"lg", "ch", "ca", "cc", "cp", "wa", "hd", "fc", "ey", "hr", "hrb", "fa", "ea", "ha", "he", "ri"};
        }else if(this.RenderType == AvatarRenderType.PART){
            this.AvatarHeight = 64;
            this.AvatarWidth = 64;
        }
        this.Collections = collections;
        this.parseFigure();
        if(this.CarryItem > 0){
            this.FigureParts.remove("ri");
            this.FigureParts.put("ri", new Integer[]{this.CarryItem, 0,0});
        }
        if(this.Actions.contains("sig")){
            this.AvatarWidth = 76;
            this.AvatarHeight = 122;
            this.AvatarOffset = new Integer[] {-31, 51};
            this.FigureParts.remove("li");
            this.FigureParts.put("li",new Integer[] { this.Sign, 0,0});
        }
        
        this.Avatar = new BufferedImage(this.AvatarWidth, this.AvatarHeight,BufferedImage.TYPE_INT_ARGB);
        this.Avatar = ImageTools.makeTransparent(this.Avatar);
    }
    
    private void parseFigure(){
        for(String p : this.Figure.split("[.]")){
            String[] parts = p.split("-");
            this.FigureParts.put(parts[0], new Integer[]{Integer.parseInt(parts[1]), (parts.length > 2 ? Integer.parseInt(parts[2]) : 0), (parts.length > 3 ? Integer.parseInt(parts[3]) : 0)});
        }
    }
    
    /**
     * Renders the Avatar, Saves the Image to the Object "Avatar"
     * @throws Exception if anything goes wrong while rendering
     */
    public void render() throws Exception{
        this.FigureParts.keySet().stream().forEach((part) -> {
            this.renderPartData(part);
        });
        
        this.FigureParts.keySet().stream().forEach((part) -> {
            AvatarRenderType rType = this.RenderType;
            if(rType == AvatarRenderType.PART){
                rType = AvatarRenderType.FULL;
            }
            if (this.Collections.getPartSets().hasActivePart(renderTypeToString(rType))) {
                try {
                    this.renderPart(part);
                } catch (IOException ex) {
                    Logger.getLogger(AvatarImage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        Integer dir = this.Direction - 1;
        if(dir == -1){
            dir = 6;
        }
        if(dir > 3 || dir <= 0){
            dir = this.getFlipDirection(dir);
        }
        String[] drawOrder = this.Collections.getDrawOrders().getDrawOrder("std", this.Direction);
        if(this.DrawOrder != null){
            drawOrder = this.DrawOrder;
        }
        List<String> rendered = new ArrayList<>();
        Integer lowestX = 0;
        Integer lowestY = 0;
        Integer highestX = 0;
        Integer highestY = 0;
        for(String part : drawOrder){
            if(this.Parts.containsKey(part)){
                if(this.RenderType == AvatarRenderType.PART){
                    for(Part image : this.Parts.get(part)){
                        if(lowestX <= image.getX() || lowestX == 0){
                            lowestX = image.getX();
                        }
                        if(lowestY <= image.getY() || lowestY == 0){
                            lowestY = image.getY();
                        }
                    }
                    for(Part image : this.Parts.get(part)){
                        image.setX(image.getX() - lowestX);
                        image.setY(image.getY() - lowestY);
                    }
                    for(Part image : this.Parts.get(part)){
                        if(image.getX() < 0 && Math.abs(image.getX()) > highestX){
                            highestX = Math.abs(image.getX());
                        }
                        if(image.getY() < 0 && Math.abs(image.getY()) > highestY){
                            highestY = Math.abs(image.getY());
                        }
                    }
                }
            }
        }
        for(String part : drawOrder){
            if(this.Parts.containsKey(part)){
                if(this.RenderType == AvatarRenderType.PART){
                    for(Part image : this.Parts.get(part)){
                        image.setX(image.getX() + highestX);
                        image.setY(image.getY() + highestY);
                        if(image.getX() + image.getWidth() > this.AvatarWidth || this.AvatarWidth == 64){
                            this.AvatarWidth = image.getX() + image.getWidth();
                        }
                        if(image.getY() + image.getHeight() > this.AvatarHeight || this.AvatarHeight == 64){
                            this.AvatarHeight = image.getY() + image.getHeight();
                        }
                    }
                }
                this.Parts.get(part).stream().filter((image) -> !(rendered.contains(image.getType() + image.getId()) && (image.getType().equals("lg") || image.getType().equals("hr") || image.getType().equals("hrb")))).map((image) -> {
                    rendered.add(image.getType() + image.getId());
                    return image;
                }).forEach((image) -> {
                    ImageTools.drawPartToImage(Avatar, image);
                });
            }
        }
    }
    
    
    private void renderPartData(String p){
        Integer[] partData = this.FigureParts.get(p);
        List<Set> parts = new ArrayList();
        if(p.equals("ri")){
            parts.add(new Set("ri", this.CarryItem, false, 0,0));
        }
        if(p.equals("li")){
            parts.add(new Set("li", this.Sign, false, 0,0));
        }
        if(this.Collections.getSets().getPalette(p) != null){
            parts = this.Collections.getSets().getPalette(p).cloneSets(partData[0]);
        }
        for(Set part : parts){
            if(this.getDirection(part.getType()) == 0 || this.getDirection(part.getType()) > 5){
                if(part.getType().equals("ey") || part.getType().equals("fc")){
                    continue;
                }
            }
            String library = this.Collections.getLibraries().getLibrary(part.getType(), part.getId() + "");
            if(library == null){
                continue;
            }
            this.PartData.put(part.getType(), library);
            this.PartDataIds.put(part.getType(), part.getId());
        }
    }
    
    private void renderPart(String p) throws IOException{
        Boolean doFlip = false;
        Integer flipDir = 2;
        Integer[] partData = this.FigureParts.get(p);
        List<Set> parts = new ArrayList();
        if(p.equals("ri")){
            parts.add(new Set("ri", this.CarryItem, false, 0 ,0));
        }
        if(p.equals("li")){
            parts.add(new Set("li", this.Sign, false, 0, 0));
        }
        if(this.Collections.getSets().getPalette(p) != null){
           parts = this.Collections.getSets().getPalette(p).cloneSets(partData[0]);
        }
        for(Set part : parts){
            String renderAs = part.getType();
            String library = this.Collections.getLibraries().getLibrary(part.getType(), part.getId() + "");
            if(library == null && (part.getType().equals("ls") || part.getType().equals("rs"))){
                library = "hh_human_shirt";
            }
            if(library == null){
                return;
            }
            library = library.replace("_50_", "_");
            List<String> actions = this.getActions(part.getType());
            String action = null;
            for(String act : actions){
                if(this.Actions.contains(act)){
                    action = act;
                    break;
                }
            }
            if(action == null){
                action = actions.get(0);
            }
            Integer direction = this.getDirection(part.getType());
            Integer frame = this.getFrameForPart(part.getType(), action);
            if(direction > -1 && direction < 8){
                doFlip = false;
                Boolean doNotFlip = false;
                String imageFileOne = this.getPartImage(library, action, part.getType(), part.getId(), direction, frame);
                String imageFileTwo = this.getPartImage(library, action, this.Collections.getPartSets().getPartFlip(part.getType()), part.getId(), this.getFlipDirection(direction), frame);
                String imageFileThree = this.getPartImage(library, action, part.getType(), part.getId(), this.getFlipDirection(direction), frame);
               
                String newType = part.getType();
                if(Files.exists(Paths.get(System.getProperty("user.dir"), imageFileOne))){
                    direction = this.getFlipDirection(direction);
                    doNotFlip = true;
                }else if(Files.exists(Paths.get(System.getProperty("user.dir"), imageFileTwo))){
                    newType = this.Collections.getPartSets().getPartFlip(part.getType());
                    doNotFlip = false;
                }else if(Files.exists(Paths.get(System.getProperty("user.dir"), imageFileThree))){
                    doNotFlip = false;
                }else{
                    newType = this.Collections.getPartSets().getPartFlip(part.getType());
                    doNotFlip = false;
                }
                flipDir = this.getFlipDirection(direction);
                doFlip = !doNotFlip;
                renderAs = part.getType();
                part.setType(newType);
                direction = flipDir;
            }
            if((direction == 0 || direction > 5) && (part.getType().equals("ey") || part.getType().equals("fc"))){
                continue;
            }
            if(part.getType().length() == 3 && this.Rendered.contains(part.getType())){
                continue;
            }
            this.Rendered.add(part.getType());
            this.renderPartType(part, p,doFlip, flipDir, partData, direction, action, renderAs);
        }
    }
    
    private Integer getFlipDirection(Integer direction){
        switch(direction){
            case 0:
                return 6;
            case 1:
                return 5;
            case 2:
                return 4;
            case 3:
                return 3;
            case 4:
                return 2;
            case 5:
                return 1;
            case 6:
                return 0;
            case 7:
                return 7;
            default:
                return 3;
        }
    }
    
    private Integer getDirection(String type){
        switch(type){
            case "ea":
            case "ey":
            case "fa":
            case "fc":
            case "hr":
            case "hrb":
            case "he":
            case "ha":
            case "hd":
                return this.HeadDirection;
            default:
                return this.Direction;
        }
    }
    
    private List<String> getActions(String type)
    {
        List<String> actions = new ArrayList<>();
        actions.add("std");
        switch(type)
        {
          case "bd":
            actions.add("sit");
            actions.add("lay");
            actions.add("wlk");
            break;
          case "hd":
            actions.add("lsp");
            actions.add("lay");
            actions.add("spk");
            break;
          case "fa":
            actions.add("spk");
            actions.add("lsp");
            actions.add("lay");
            break;
          case "ea":
            actions.add("lay");
            break;
          case "ey":
            actions.add("agr");
            actions.add("sad");
            actions.add("sml");
            actions.add("srp");
            actions.add("lag");
            actions.add("lsp");
            actions.add("lay");
            actions.add("eyb");
            break;
          case "fc":
            actions.add("agr");
            actions.add("blw");
            actions.add("sad");
            actions.add("spk");
            actions.add("srp");
            actions.add("sml");
            actions.add("lsp");
            actions.add("lay");
            break;
          case "hr":
          case "hrb":
            actions.add("lay");
            break;
          case "he":
            actions.add("lay");
            break;
          case "ha":
            actions.add("lay");
            break;
          case "ch":
            actions.add("lay");
            break;
          case "cc":
            actions.add("lay");
            break;
          case "ca":
            actions.add("lay");
            break;
          case "lg":
            actions.add("sit");
            actions.add("wlk");
            actions.add("lay");
            break;
          case "lh":
            actions.add("respect");
            actions.add("sig");
            actions.add("wav");
            actions.add("wlk");
            actions.add("lay");
            break;
          case "ls":
            actions.add("wav");
            actions.add("wlk");
            actions.add("lay");
            break;
          case "lc":
            actions.add("wav");
            actions.add("wlk");
            actions.add("lay");
            break;
          case "rh":
            actions.add("blw");
            actions.add("drk");
            actions.add("crr");
            actions.add("wlk");
            actions.add("lay");
            break;
          case "rs":
            actions.add("blw");
            actions.add("drk");
            actions.add("crr");
            actions.add("wlk");
            actions.add("lay");
            break;
          case "rc":
            actions.add("blw");
            actions.add("drk");
            actions.add("crr");
            actions.add("wlk");
            actions.add("lay");
            break;
          case "ri":
            actions.add("crr");
            actions.add("drk");
            break;
          case "li":
            actions.add("sig");
            break;
          case "sh":
            actions.add("sit");
            actions.add("wlk");
            actions.add("lay");
            break;
          default:
            break;
        }
        return actions;
    }
    
    private String renderTypeToString(AvatarRenderType renderType){
        switch(renderType){
            case FULL:
                return "figure";
            case PART:
                return "part";
            case HEAD:
                return "head";
            default:
                return "figure";
        }
    }
    
    private String getPartImage(String library, String action, String partType, int partId, int direction, int frame){
        String imageFile = this.Collections.getImageBase().replace("{libraryName}", library);
        imageFile += "h_" + action + "_" + partType + "_" + partId + "_" + direction + "_" + frame + ".png";
        return imageFile;
    }
    
    private Integer getFrameForPart(String part, String action){
        if(part.equals("ls") && action.equals("wav") && (this.Direction == 3 || this.getFlipDirection(this.Direction) == 3)){
            return 1;
        }
        if(this.Collections.getAnimations().hasAnimation(action) && this.Collections.getAnimations().getAnimation(action).hasPart(part)){
            for(int i : this.Collections.getAnimations().getAnimation(action).getFrames(part)){
                if(i == this.Frame){
                    return this.Frame;
                }
            }
            Double d = Math.ceil(this.Frame / 2);
            return d.intValue() - 1;
        }
        return 0;
    }

    private void renderPartType(Set part, String p, Boolean doFlip, Integer flipDir, Integer[] partData, Integer direction, String action, String renderAs) throws IOException {
        String library = this.Collections.getLibraries().getLibrary(part.getType(), part.getId() + "");
        if(library == null && (part.getType().equals("ls") || part.getType().equals("rs"))){
            library = "hh_human_shirt";
        }
        if(library == null){
            return;
        }
        //part.setType(this.hasHat(part.getType()));
        library = library.replace("_50_", "_");
        HashMap<String, Integer[]> manifest = this.Collections.getManifest().parse(library);
        List<Color> colors = new ArrayList<>();
        if(part.isColorable()){
            if(partData[1] != 0){
                colors.add(this.Collections.getColors().getColor(partData[1]));
            }
            if(partData[2] != 0){
                colors.add(this.Collections.getColors().getColor(partData[2]));
            }
        }
        boolean colored1 = false;
        boolean colored2 = false;
        for(int i = 0; i < ((colors.size() > 1) ? 3 : 1); i++){
            Integer dir = direction;
            String imageFile = this.Collections.getImageBase().replace("{libraryName}", library);
            String partFile = "h_" + action + "_" + part.getType() + "_" + (part.getId() + i) + "_" + dir + "_" + this.getFrameForPart(part.getType(), action);
            imageFile = imageFile + partFile + ".png";
            if(!Files.exists(Paths.get(imageFile))){
                System.out.println(imageFile + " does not exist!");
                continue;
            }
            BufferedImage partImage = ImageIO.read(new File(imageFile));
            Integer pImageWidth = partImage.getWidth();
            Integer pImageHeight = partImage.getHeight();
            Integer offsetX = null;
            if(doFlip){
                partImage = ImageTools.flipImage(partImage);
                offsetX = this.AvatarWidth - pImageWidth + 3;
            }
            Integer pImagePosXas = 0;
            Integer pImagePosYas = 0;
            if(!doFlip && direction > 3 && direction < 7){
                pImagePosXas = -1;
            }
            if(offsetX != null){
                pImagePosXas = offsetX -(((this.AvatarWidth / 2) - manifest.get(partFile)[0]) + this.AvatarOffset[0]);
            }else{
                pImagePosXas += (((this.AvatarWidth / 2) - manifest.get(partFile)[0]) + this.AvatarOffset[0]);
            }
            pImagePosYas += (((this.AvatarHeight / 2) -  manifest.get(partFile)[1]) + this.AvatarOffset[1]);
            if(part.isColorable() && !part.getType().equals("ey")){
                Color c = null;
                if(!colored1 && colors.size() > 0){
                    c = colors.get(0);
                    colored1 = true;
                }else if(!colored2 && colors.size() > 1){
                    c = colors.get(1);
                    colored2 = true;
                }
                if(c != null){
                    
                    partImage = ImageTools.recolorImage(partImage, c);
                }
            }
            if(!this.Parts.containsKey(renderAs)){
                this.Parts.put(renderAs, new ArrayList<>());
            }
            this.Parts.get(renderAs).add(new Part(partImage, pImagePosXas, pImagePosYas, pImageWidth, pImageHeight, part.getType(), i));
            
        }
    }
    
    
    private String hasHat(String partType){
        java.util.Set<String> partTypes = this.PartData.keySet();
        if(partType.equals("hr")){
            String newType = "";
            for(Entry<String, String> part : this.PartData.entrySet()){
                if(this.UseHrbFor.contains(part.getValue())){
                    newType = "hrb";
                    break;
                }else if(this.UseHrbFor.contains(part.getValue() + "." + this.PartDataIds.get(part.getKey()))){
                    newType = "hrb";
                    break;
                }else{
                    newType = partTypes.contains("ha") && !this.UseHrbFor.contains(part.getValue()) ? "hr" : "hrb";
                }
            }
            return newType;
        }
        return partType;
    }
    
    public BufferedImage getImage(){
        return this.Avatar;
    }
}
