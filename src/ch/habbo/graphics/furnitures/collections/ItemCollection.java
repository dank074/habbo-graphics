package ch.habbo.graphics.furnitures.collections;

import ch.habbo.graphics.furnitures.collections.items.FloorItem;
import ch.habbo.graphics.furnitures.collections.items.WallItem;
import java.util.ArrayList;
import java.util.List;

public class ItemCollection {

    private final List<FloorItem> floorItems;
    private final List<WallItem> wallItems;
    
    public ItemCollection(){
        this.floorItems = new ArrayList();
        this.wallItems = new ArrayList();
    }
    
    public List<WallItem> getWallItems(){
        return this.wallItems;
    }
    
    public List<FloorItem> getFloorItems(){
        return this.floorItems;
    }
    
    public void add(FloorItem item){
        this.floorItems.add(item);
    }
    
    public void add(WallItem item){
        this.wallItems.add(item);
    }
}
