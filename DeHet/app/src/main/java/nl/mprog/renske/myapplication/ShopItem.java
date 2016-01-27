package nl.mprog.renske.myapplication;

/**
 * Created by Renske on 18-1-2016.
 */
public class ShopItem {

    public String itemname, imageviewpath;
    public int status, price, id;
    public boolean equipped;

    public ShopItem (String the_itemname, String the_imageviewpath, int the_id, int the_price){
        this.itemname = the_itemname;
        this.imageviewpath = the_imageviewpath;
        this.id = the_id;
        this.status = 0;
        this.price = the_price;
        this.equipped = false;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setEquipped(boolean equipped) {
        this.equipped = equipped;
    }

    public boolean isEquipped(){
        return equipped;
    }
}
