package nl.mprog.renske.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Parcelable;
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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Arrays;

public class ShopActivity extends AppCompatActivity {

    private TextView coinsTextView;
    private int coins;
    private ArrayList<ShopItem> shopItems;
    private boolean resetvalue;
    private ShopItem item1, item2, item3, item4, item5, item6, item7, item8, item9, item10;
    private ListView listView;
    private String jsonstring;

    private Parcelable state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        // Set listview and other layout components.
        listView = (ListView) findViewById(R.id.listViewShop);
        shopItems = new ArrayList<ShopItem>();
        coinsTextView = (TextView) findViewById(R.id.cointsTextView);

        // Obtain SharedPreferences.
        SharedPreferences savedprefs = this.getSharedPreferences("storedachievements", MODE_PRIVATE);
        jsonstring = savedprefs.getString("jsonshop", null);
        coins = savedprefs.getInt("coinsamount", 0);

        // If there are not SharedPreferences, create shop items from scratch.
        if(savedprefs == null || jsonstring == null) {
            coins = 0;
            shopItems = createShopItems();
            listView.setAdapter(new UserItemAdapter(this, android.R.layout.simple_list_item_1, shopItems));
            coinsTextView.setText(getString(R.string.coins) + Integer.toString(coins));
        }

        // If sharedpreferences exist, use those instead.
        else {
            checkForReset();
            shopItems = loadShopItems(jsonstring);
            listView.setAdapter(new UserItemAdapter(this, android.R.layout.simple_list_item_1, shopItems));
            coinsTextView.setText(getString(R.string.coins) + Integer.toString(coins));
        }

        /**
         * Create the listview, set the adapter and onclicklistener.
         */
        listView.setAdapter(new UserItemAdapter(this, android.R.layout.simple_list_item_1, shopItems));
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                switch(position){
                    case 0:
                        buyItem(item1);
                        break;
                    case 1:
                        buyItem(item2);
                        break;
                    case 2:
                        buyItem(item3);
                        break;
                    case 3:
                        buyItem(item4);
                        break;
                    case 4:
                        buyItem(item5);
                        break;
                    case 5:
                        buyItem(item6);
                        break;
                    case 6:
                        buyItem(item7);
                        break;
                    case 7:
                        buyItem(item8);
                        break;
                    case 8:
                        buyItem(item9);
                        break;
                    case 9:
                        buyItem(item10);
                        break;
                }
                return true;
            }
        });
    }


    /**
     * Buy or equip an item.
     */
    public void buyItem(ShopItem theitem){

        state = listView.onSaveInstanceState();

        // If the user owns the item, equip or unequip it depending on current status.
        if(theitem.status == 1) {

            if(!theitem.equipped)
                theitem.equipped = true;
            else
                theitem.equipped = false;

            listView.setAdapter(new UserItemAdapter(this, android.R.layout.simple_list_item_1, shopItems));
            System.out.println(theitem.equipped);
        }
        // If the user doesn't own the item, check if they can afford it and if so, buy it.
        else {
            if(coins - theitem.price < 0)
                Toast.makeText(ShopActivity.this, R.string.cannotafford, Toast.LENGTH_SHORT).show();
            else {
                coins = coins - theitem.price;
                theitem.price = 0;
                listView.setAdapter(new UserItemAdapter(this, android.R.layout.simple_list_item_1, shopItems));
                coinsTextView.setText(getString(R.string.coins) + Integer.toString(coins));
                Toast.makeText(ShopActivity.this, R.string.itemboughtconfirmation, Toast.LENGTH_SHORT).show();
            }
        }
        saveShopItems(shopItems);
    }

    /**
     * Check whether the user has opted to reset the amount of coins.
     */
    public boolean checkForReset(){
        SharedPreferences useroptions = getSharedPreferences("settings", this.MODE_PRIVATE);
        resetvalue = useroptions.getBoolean("RESET", false);

        if(resetvalue == true) {
            coins = 0;
        }
        return resetvalue;
    }

    /**
     * Create all shop items from scratch.
     */
    public ArrayList<ShopItem> createShopItems(){

        // create list to put everything inside array in bulk
        ShopItem[] shopItemList = new ShopItem[]{

                item1 = new ShopItem(getString(R.string.bruinscarf), "bruinscarf", 1, 50),
                item2 = new ShopItem(getString(R.string.bruinhat), "bruinhat", 2, 50),
                item3 = new ShopItem(getString(R.string.bruinsock), "bruinsock", 3, 150),
                item4 = new ShopItem(getString(R.string.bruinearmuffs), "bruinearmuffs", 4, 150),
                item5 = new ShopItem(getString(R.string.bruinflower), "bruinflower", 5, 250),
                item6 = new ShopItem(getString(R.string.bruinshades), "bruinshades", 6, 250),
                item7 = new ShopItem(getString(R.string.bruintie), "bruintie", 7, 300),
                item8 = new ShopItem(getString(R.string.bruinhalo), "bruinhalo", 8, 500),
                item9 = new ShopItem(getString(R.string.bruinwings), "bruinwings", 9, 1000),
                item10 = new ShopItem(getString(R.string.bruincrown), "bruincrown", 10, 10000)
        };

        // add the array created above to the arraylist and set the listadapter on this arraylist
        ArrayList<ShopItem> newShopItems = new ArrayList<ShopItem>();
        newShopItems.addAll(Arrays.asList(shopItemList));
        return newShopItems;
    }

    /**
     * Load shop items and their status from SharedPreferences.
     */
    public ArrayList<ShopItem> loadShopItems(String jsonstring){

        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray jArray = parser.parse(jsonstring).getAsJsonArray();
        ArrayList<ShopItem> sharedprefslist = new ArrayList<ShopItem>();

        for(JsonElement obj : jArray )
        {
            ShopItem savedItem = gson.fromJson( obj , ShopItem.class);
            sharedprefslist.add(savedItem);
        }

        // create list to put everything inside array in bulk
        ShopItem[] savedItemList = new ShopItem[]{

                item1 = sharedprefslist.get(0),
                item2 = sharedprefslist.get(1),
                item3 = sharedprefslist.get(2),
                item4 = sharedprefslist.get(3),
                item5 = sharedprefslist.get(4),
                item6 = sharedprefslist.get(5),
                item7 = sharedprefslist.get(6),
                item8 = sharedprefslist.get(7),
                item9 = sharedprefslist.get(8),
                item10 = sharedprefslist.get(9)
        };

        // add the list created above to the actual arraylist
        ArrayList<ShopItem> savedShopItems = new ArrayList<ShopItem>();
        savedShopItems.addAll(Arrays.asList(savedItemList));
        return savedShopItems;

    }

    /**
     * Save shop items and their status to SharedPreferences.
     */
    public void saveShopItems(ArrayList<ShopItem> itemsToSave){

        Gson gson = new Gson();

        JsonElement element =
                gson.toJsonTree(itemsToSave, new TypeToken<ArrayList<ShopItem>>() {}.getType());

        JsonArray jsonArray = element.getAsJsonArray();
        String jsonArrayString = jsonArray.toString();

        SharedPreferences prefs = this.getSharedPreferences("storedachievements", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        edit.putString("jsonshop", jsonArrayString);
        edit.putInt("coinsamount", coins);
        edit.commit();
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

            if(state != null)
                listView.onRestoreInstanceState(state);
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
                saveShopItems(shopItems);
            }
            return v;
        }

    }
}
