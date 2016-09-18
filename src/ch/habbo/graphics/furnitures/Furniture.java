package ch.habbo.graphics.furnitures;

import ch.habbo.graphics.furnitures.visualization.Visualization;
import ch.habbo.graphics.furnitures.visualization.VisualizationData;
import ch.habbo.graphics.tools.ImageTools;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import javax.imageio.ImageIO;

public class Furniture {
    
    private final Assets assets;
    private final Dimension dimension;
    private final VisualizationData visualizations;
    private final String Classname;
    private Integer Frame;
    private final Integer Animation;
    private Integer Direction;
    private final FurniSize DrawType;
    private BufferedImage Image;
    private final String ImageBase;
    private final String XMLBase;
    private final String FileNameBase;
    private Integer Width;
    private Integer Height;
    private final LinkedHashMap<Asset, BufferedImage> Images;
    
    /**
     * Constructor
     * @param classname Classname (example: "throne")
     * @param direction Direction (0-7)
     * @param frame Frame number
     * @param animation Animation Id
     * @param type What should be drawn (ICON / NORMAL / ZOOMOUT -> does only resize the image to the half of normal size)
     * @param xmlBase XML Base (example "furni/assets/{classname}/{classname}_{classname}_{file}.xml")
     * @param fileNameBase File Name base (example "{classname}_{size}_{part}_{direction}_{frame}")
     * @param iconBase Icon Base (example "furni/assets/{classname}/{classname}_{classname}_icon.png")
     * @param pngBase Image Base (example "furni/assets/{classname}/{classname}_{filename}.png")
     * @throws Exception If any XML Parsing goes wrong
     */
    public Furniture(String classname, Integer direction, Integer frame, Integer animation, FurniSize type, String xmlBase, String fileNameBase, String iconBase, String pngBase) throws Exception{
        this.Classname = classname;
        this.Direction = direction;
        this.Frame = frame;
        this.Animation = animation;
        this.DrawType = type;
        this.assets = new Assets(xmlBase.replace("{classname}", classname).replace("{file}", "assets"));
        this.dimension = Dimension.parse(xmlBase.replace("{classname}", classname).replace("{file}", "logic"));
        this.visualizations = new VisualizationData(xmlBase.replace("{classname}", classname).replace("{file}", "visualization"));
        this.ImageBase = pngBase;
        this.XMLBase = xmlBase;
        this.FileNameBase = fileNameBase;
        this.Width = 0;
        this.Height = 0;
        this.Images = new LinkedHashMap<>();
    }
    
    /**
     * Renders the Furniture!
     * @throws Exception When it fails to read / write an Image
     */
    public void render() throws Exception{
        Visualization visual = this.visualizations.getVisualization(this.DrawType);
        if(!visual.hasDirection(this.Direction)){
            this.Direction = visual.getDefaultDirection();
        }
        if(visual.getAnimation(this.Animation) == null){
            this.Frame = 0;
        }
        for(int i = 0; i < visual.getLayers(); i++){
            int frame = 0;
            if(visual.getAnimation(this.Animation) != null){
                frame = visual.getAnimation(this.Animation).getLayer(i).getFrame(this.Frame);
            }
            Asset asset = this.assets.getAsset(this.FileNameBase
                    .replace("{classname}", this.Classname)
                    .replace("{size}", this.getSize() +"")
                    .replace("{part}", this.getCharForNumber(i))
                    .replace("{direction}", this.Direction +"")
                    .replace("{frame}", frame + ""));
            if(asset == null){
                continue;
            }
            if(!Files.exists(Paths.get(this.ImageBase.replace("{classname}", this.Classname).replace("{filename}", asset.getSource())))){
                continue;
            }
            BufferedImage partImage = ImageIO.read(new File(this.ImageBase.replace("{classname}", this.Classname).replace("{filename}", asset.getSource())));
            if(asset.flipHorizontal()){
                partImage = ImageTools.flipImage(partImage);
            }
            if(visual.getLayer(i) != null && visual.getLayer(i).getAlpha() > 0){
                partImage = ImageTools.alphaImage(partImage, visual.getLayer(i).getAlpha());
            }
            this.Images.put(asset, partImage);
        }
        this.calculateXY();
        this.Image = new BufferedImage(this.Width, this.Height,BufferedImage.TYPE_INT_ARGB);
        this.Image = ImageTools.makeTransparent(this.Image);
        this.Images.entrySet().stream().forEach((entry) -> {
            ImageTools.drawPartToImage(this.Image, entry.getValue(), entry.getKey());
        });
    }
    
    /**
     * Calculates the Position of every Asset & the Size of the "Canvas"
     */
    private void calculateXY(){
        int lowestX = 0;
        int lowestY = 0;
        int highestX = 0;
        int highestY = 0;
        boolean hasMinusOne = false;
        for(Asset asset : this.Images.keySet()){
            if(lowestX <= asset.getX() || lowestX == 0){
                lowestX = asset.getX();
            }
            if(lowestY <= asset.getY() || lowestY == 0){
                lowestY = asset.getY();
            }
            if(asset.getX() == -1){
                hasMinusOne = true;
            }
        }
        for(Asset asset : this.Images.keySet()){
            asset.setX(asset.getX() - lowestX);
            asset.setY(asset.getY() - lowestY);
        }
        for(Asset asset: this.Images.keySet()){
            if(asset.getX() < 0 && Math.abs(asset.getX()) < highestX){
                highestX = Math.abs(asset.getX());
            }
            if(asset.getY() < 0 && Math.abs(asset.getY()) > highestY){
                highestY = Math.abs(asset.getY());
            }
        }
        for(Entry<Asset, BufferedImage> entry : this.Images.entrySet()){
            entry.getKey().setX(entry.getKey().getX() + highestX);
            entry.getKey().setY(entry.getKey().getY() + highestY);
            if(entry.getValue().getWidth() > this.Width || this.Width == 0){
                this.Width = entry.getValue().getWidth();
            }
            if(entry.getValue().getHeight() > this.Height || this.Height == 0){
                this.Height = entry.getValue().getHeight();
            }
        }
        for(Entry<Asset, BufferedImage> entry : this.Images.entrySet()){
            Integer pImageWidth = entry.getValue().getWidth();
            Integer pImageHeight = entry.getValue().getHeight();
            Integer offsetX = null;
            Integer pImagePosXas = 0;
            Integer pImagePosYas = 0;
            if(entry.getKey().flipHorizontal() && this.Direction > 3 && this.Direction < 7){
                offsetX = this.Width - pImageWidth + 3;
            }
            if(!entry.getKey().flipHorizontal() && this.Direction > 3 && this.Direction < 7){
                pImagePosXas = -1;
            }
            if(offsetX != null){
                pImagePosXas = offsetX - (((this.Width / 2) - entry.getKey().originalX()) + (hasMinusOne ? 1 : 0));
            }else{
                pImagePosXas += (((this.Width / 2) - entry.getKey().originalX()) + (hasMinusOne ? 1 : 0));
            }
            pImagePosYas += ((this.Height / 2) - entry.getKey().getY());
            entry.getKey().setX(pImagePosXas);
            entry.getKey().setY(pImagePosYas);
        }
        for(Entry<Asset, BufferedImage> entry : this.Images.entrySet()){
            if(this.Width < (Math.abs(entry.getKey().getX()) + entry.getValue().getWidth())){
                this.Width = Math.abs(entry.getKey().getX()) + entry.getValue().getWidth();
            }
            if(this.Height < (Math.abs(entry.getKey().getY()) + entry.getValue().getHeight())){
                this.Height = Math.abs(entry.getKey().getY()) + entry.getValue().getHeight();
            }
        }
    }
    
    public BufferedImage getImage(){
        return this.Image;
    }
    
    /**
     * Returns the Letter for a Layer (0=a, 1=b, 2=c ... 26=z)
     * @param i Layer number
     * @return Letter (a-z)
     */
    private String getCharForNumber(int i) {
        return i >= 0 && i < 26 ? String.valueOf((char)(i + 97)) : null;
    }
    
    private Integer getSize(){
        switch(this.DrawType){
            case NORMAL:
                return 64;
            case ZOOMOUT:
                return 32;
            case ICON:
                return 1;
            default:
                return 64;
        }
    }
}
