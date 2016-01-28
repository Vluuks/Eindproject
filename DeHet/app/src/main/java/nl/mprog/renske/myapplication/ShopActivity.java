// Renske Talsma, UvA 10896503, vluuks@gmail.com

package nl.mprog.renske.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * Activity that displays all items that a user can buy using coins, allowin the user to buy them
 * using long taps and equip/unequip using long taps. The equipped items will show on Bruin.
 */
public class ShopActivity extends AppCompatActivity {

    private TextView coinsTextView;
    private int coinsAmount;
    private ArrayList<ShopItem> shopItems;
    private boolean resetValue;
    private ListView listView;
    private String jsonString;
    private ShopItemLoader shopItemLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        // Set listview and other layout components.
        listView = (ListView) findViewById(R.id.listViewShop);
        shopItems = new ArrayList<ShopItem>();
        coinsTextView = (TextView) findViewById(R.id.cointsTextView);

        // Create or load shopitems, adapter and listener.
        shopItemLoader = new ShopItemLoader(ShopActivity.this);
        initializeShop();
        setListener();
    }

    /**
     * Initializes the shop either from SharedPreferences or from scratch.
     */
    private void initializeShop(){

        // Obtain SharedPreferences.
        SharedPreferences savedprefs = this.getSharedPreferences("storedachievements", MODE_PRIVATE);
        jsonString = savedprefs.getString("jsonshop", null);
        coinsAmount = savedprefs.getInt("coinsamount", 0);

        // If there are not SharedPreferences, create shop items from scratch.
        if(savedprefs == null || jsonString == null) {
            coinsAmount = 0;
            shopItems = shopItemLoader.createShopItems();
            listView.setAdapter(new UserItemAdapter(this, android.R.layout.simple_list_item_1, shopItems));
            coinsTextView.setText(getString(R.string.coins) + Integer.toString(coinsAmount));
        }
        // If SharedPreferences exist, use those instead.
        else {
            checkForReset();
            shopItems = shopItemLoader.loadShopItems(jsonString);
            listView.setAdapter(new UserItemAdapter(this, android.R.layout.simple_list_item_1, shopItems));
            coinsTextView.setText(getString(R.string.coins) + Integer.toString(coinsAmount));
        }
        shopItemLoader.saveShopItems(shopItems, coinsAmount);
    }

    /**
     * Set an OnClickListener for the items in the ListView.
     */
    private void setListener(){
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        buyItem(shopItemLoader.getShopItem(0));
                        break;
                    case 1:
                        buyItem(shopItemLoader.getShopItem(1));
                        break;
                    case 2:
                        buyItem(shopItemLoader.getShopItem(2));
                        break;
                    case 3:
                        buyItem(shopItemLoader.getShopItem(3));
                        break;
                    case 4:
                        buyItem(shopItemLoader.getShopItem(4));
                        break;
                    case 5:
                        buyItem(shopItemLoader.getShopItem(5));
                        break;
                    case 6:
                        buyItem(shopItemLoader.getShopItem(6));
                        break;
                    case 7:
                        buyItem(shopItemLoader.getShopItem(7));
                        break;
                    case 8:
                        buyItem(shopItemLoader.getShopItem(8));
                        break;
                    case 9:
                        buyItem(shopItemLoader.getShopItem(9));
                        break;
                }
                return true;
            }
        });
    }

    /**
     * Buy or equip an item.
     */
    public void buyItem(ShopItem theItem){

        // If the user owns the item, equip or unequip it depending on current status.
        if(theItem.status == 1) {

            if(!theItem.isEquipped()) {
                theItem.setEquipped(true);
                Toast.makeText(ShopActivity.this, R.string.equippedsuccess, Toast.LENGTH_SHORT).show();
            }
            else {
                theItem.setEquipped(false);
                Toast.makeText(ShopActivity.this, R.string.unequippedsuccess, Toast.LENGTH_SHORT).show();
            }
            listView.setAdapter(new UserItemAdapter(this, android.R.layout.simple_list_item_1, shopItems));
        }
        // If the user doesn't own the item, check if they can afford it and if so, buy it.
        else {
            if(coinsAmount - theItem.getPrice() < 0)
                Toast.makeText(ShopActivity.this, R.string.cannotafford, Toast.LENGTH_SHORT).show();
            else {
                coinsAmount = coinsAmount - theItem.getPrice();
                theItem.setPrice(0);
                coinsTextView.setText(getString(R.string.coins) + Integer.toString(coinsAmount));
                Toast.makeText(ShopActivity.this, R.string.itemboughtconfirmation, Toast.LENGTH_SHORT).show();
            }
            listView.setAdapter(new UserItemAdapter(this, android.R.layout.simple_list_item_1, shopItems));
        }
        shopItemLoader.saveShopItems(shopItems, coinsAmount);
    }

    /**
     * Check whether the user has opted to reset the amount of coins.
     */
    public boolean checkForReset(){
        SharedPreferences useroptions = getSharedPreferences("settings", this.MODE_PRIVATE);
        resetValue = useroptions.getBoolean("RESET", false);

        if(resetValue == true) {
            coinsAmount = 0;
        }
        return resetValue;
    }

    /**
     * The adapter of the listview, handles all changes made to the shopitem objects.
     * source: http://codehenge.net/blog/2011/05/customizing-android-listview-item-layout/
     * and http://android-developers.blogspot.nl/2009/02/android-layout-tricks-1.html
     */
    public class UserItemAdapter extends ArrayAdapter<ShopItem> {
        protected ArrayList<ShopItem> shopItems;
        public UserItemAdapter(Context context, int textViewResourceId, ArrayList<ShopItem> shopItems) {
            super(context, textViewResourceId, shopItems);
            this.shopItems = shopItems;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.listitemshop, null);
            }

            // Iterate over the items in shopitem arraylist.
            ShopItem item = shopItems.get(position);
            if (item != null) {

                // Initialize layout components from the listitem.
                TextView itemName = (TextView) v.findViewById(R.id.itemname);
                TextView itemPrice = (TextView) v.findViewById(R.id.itemprice);
                ImageView itemIcon = (ImageView) v.findViewById(R.id.icon);

                // Set textviews to show the item's name and price.
                if (itemName != null)
                    itemName.setText(item.itemname);

                if(itemPrice != null) {
                    // If the item has been bought.
                    if (item.price == 0) {
                        item.status = 1;

                        // Display text according to item's equipped status.
                        if (!item.equipped)
                            itemPrice.setText(R.string.itemowned);
                        else
                            itemPrice.setText(R.string.itemequipped);
                    }

                    // If the item was not bought, just display the price.
                    else
                        itemPrice.setText(Integer.toString(item.price));
                }

                // Set the icon of the item accordingly.
                if(itemIcon != null) {
                    Resources res = getResources();
                    int resourceId = res.getIdentifier(
                            item.imageviewpath, "drawable", getPackageName() );
                    itemIcon.setImageResource(resourceId);
                }

                // Save any changes that the adapter made.
                shopItemLoader.saveShopItems(shopItems, coinsAmount);
            }
            return v;
        }
    }
}