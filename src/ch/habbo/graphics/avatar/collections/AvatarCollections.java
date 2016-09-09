package ch.habbo.graphics.avatar.collections;

import ch.habbo.graphics.avatar.collections.sets.Sets;
import ch.habbo.graphics.avatar.collections.animations.Animations;

public class AvatarCollections {
    
    private final Animations animations;
    private final Colors colors;
    private final DrawOrders drawOrders;
    private final Libraries libraries;
    private final PartSets partSets;
    private final Sets sets;
    private final String imageBase;
    private final Manifest manifest;
    /**
     * Initializes every collections
     * @param pathAnimations Path to the avatar_animations.json File
     * @param pathColors Path to the avatar_colors.json File
     * @param pathDrawOrders Path to the avatar_draworder.json File
     * @param pathLibraries Path to the avatar_parts.json File
     * @param pathPartSets Path to the avatar_partsets.json File
     * @param pathSets Path to the avatar_sets.json File
     * @param imgBase Base for every Directory
     * @param manifestBase Base for the Manifest
     * @throws Exception If any collection has problems to initialize (for example invalid JSON Data)
     */
    public AvatarCollections(String pathAnimations, String pathColors, String pathDrawOrders, String pathLibraries, String pathPartSets, String pathSets, String imgBase, String manifestBase) throws Exception{
        animations = new Animations(pathAnimations);
        colors = new Colors(pathColors);
        drawOrders = new DrawOrders(pathDrawOrders);
        libraries = new Libraries(pathLibraries);
        partSets = new PartSets(pathPartSets);
        sets = new Sets(pathSets);
        imageBase = imgBase;
        manifest = new Manifest(manifestBase);
    }
    
    public Animations getAnimations(){
        return animations;
    }
    
    public Colors getColors(){
        return colors;
    }
    
    public DrawOrders getDrawOrders(){
        return drawOrders;
    }
    
    public Libraries getLibraries(){
        return libraries;
    }
    
    public PartSets getPartSets(){
        return partSets;
    }
    
    public Sets getSets(){
        return sets;
    }
    
    public String getImageBase(){
        return imageBase;
    }
    
    public Manifest getManifest(){
        return manifest;
    }
}
