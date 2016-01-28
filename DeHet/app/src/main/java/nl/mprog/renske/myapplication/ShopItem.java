// Renske Talsma, UvA 10896503, vluuks@gmail.com

package nl.mprog.renske.myapplication;

/**
 * Object that contains information about shop items such as their appearance, name, price, whether
 * the user owns the item or not and whether the user has the item equipped or not.
 */
public class ShopItem {

    public String itemname, imageviewpath;
    public int status, price, id;
    public boolean equipped;

    /**
     * Constructor.
     */
    public ShopItem (String theItemName, String theImageviewpath, int theId, int thePrice){
        this.itemname = theItemName;
        this.imageviewpath = theImageviewpath;
        this.id = theId;
        this.status = 0;
        this.price = thePrice;
        this.equipped = false;
    }

    /**
     * Get/set methods.
     */
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
