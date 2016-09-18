package ch.habbo.graphics.tools;

import ch.habbo.graphics.avatar.Part;
import ch.habbo.graphics.furnitures.Asset;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ImageTools {
    
    /**
     * Flips the image vertically
     * @param input the image which should be flipped
     * @return the flipped image
     */
    public static BufferedImage flipImage(BufferedImage input){
        BufferedImage newImage = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(input, 0 + input.getWidth(), 0, -input.getWidth(), input.getHeight(), null);
        return newImage;
    }
    
    /**
     * Recolors a Image
     * @param image Image which should be recolored
     * @param dest which color the image should have
     * @return 
     */
    public static BufferedImage recolorImage(BufferedImage image, Color dest){
        int width = image.getWidth();
        int height = image.getHeight();
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                Color oldColor = new Color(image.getRGB(x,y), true);
                Color newColor = new Color(
                        ImageTools.colorConvert(dest.getRed(), oldColor.getRed()).intValue(),
                        ImageTools.colorConvert(dest.getGreen(), oldColor.getGreen()).intValue(),
                        ImageTools.colorConvert(dest.getBlue(), oldColor.getBlue()).intValue(),
                        oldColor.getAlpha());
                image.setRGB(x, y, newColor.getRGB());
            }
        }
        return image;
    }
    
    /**
     * Make a Image full transparent
     * @param image Image that should be transparent
     * @return transparent image
     */
    public static BufferedImage makeTransparent(BufferedImage image){
        image.createGraphics().setBackground(new Color(0, true));
        return image;
    }
    
    /**
     * Converts a HEX String to RGB Color
     * @param color hex color (example: #FFFFFF is White)
     * @return RGB Color
     */
    public static Color hexToRGB(String color){
        color = color.replace("#", "");
        return new Color(
            Integer.valueOf(color.substring( 0, 2), 16 ),
            Integer.valueOf(color.substring( 2, 4 ), 16 ),
            Integer.valueOf(color.substring( 4, 6 ), 16 ));
    }
    
    /**
     * draws the Part on the Avatar Image
     * @param image Avatar image
     * @param part part
     */
    public static void drawPartToImage(BufferedImage image, Part part){
        image.createGraphics().drawImage(part.getImage(), null, part.getX(), part.getY());
    }
    
    /**
     * Does some magic
     * @param newC new Color
     * @param oldC old Color
     * @return magic new Color
     */
    private static Double colorConvert(int newC, int oldC){
        return ((double)newC / 255) * (double)oldC;
    }
    
    /**
     * Draws a Furni Part to the "Canvas"
     * @param image the "canvas"
     * @param toDraw the image which should be drawn
     * @param asset Asset contains things like Position
     */
    public static void drawPartToImage(BufferedImage image, BufferedImage toDraw, Asset asset){
        image.createGraphics().drawImage(toDraw,null, Math.abs(asset.getX()),Math.abs(asset.getY()));
    }
    
    /**
     * Alpha "Mask"
     * @param image The Image
     * @param alpha Alpha Mask (0-255)
     * @return 
     */
    public static BufferedImage alphaImage(BufferedImage image, Integer alpha){
        for(int height = 0; height < image.getHeight(); height++){
            for(int width = 0; width < image.getWidth(); width++){
                Color c = new Color(image.getRGB(width, height));
                if(c.getAlpha() < alpha){
                    image.setRGB(width, height, new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha() - alpha).getRGB());
                }
            }
        }
        return image;
    }
}
