package nl.mprog.renske.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
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
import java.util.HashSet;
import java.util.Set;

public class ShopActivity extends AppCompatActivity {

    private TextView coinsTextView;
    private int coins;
    private ArrayList<ShopItem> shopItems;
    private boolean resetvalue;
    private ShopItem item1, item2, item3, item4, item5, item6, item7, item8, item9, item10;
    private ListView listView;
    private String jsonstring, itemsownedstring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        // set listview and other layout components
        listView = (ListView) findViewById(R.id.listViewShop);
        shopItems = new ArrayList<ShopItem>();
        coinsTextView = (TextView) findViewById(R.id.cointsTextView);

        // get sharedpreferences
        // check for sharedpreferences
        SharedPreferences savedprefs = this.getSharedPreferences("storedachievements", MODE_PRIVATE);
        jsonstring = savedprefs.getString("jsonshop", null);
        coins = savedprefs.getInt("coinsamount", 0);
        itemsownedstring = savedprefs.getString("owneditems", "");


        // if it's the first time the app is loaded or achievements have been reset create shopitems from scratch
        if(savedprefs == null || jsonstring == null) {
            shopItems = createShopItems();
            listView.setAdapter(new UserItemAdapter(this, android.R.layout.simple_list_item_1, shopItems));
            coinsTextView.setText("Coins: " + Integer.toString(coins));
        }

        // if sharedpreferences exist, use those instead
        else {
            checkForReset();
            //shopItems = loadShopItems(jsonstring);
            shopItems = createShopItems();
            listView.setAdapter(new UserItemAdapter(this, android.R.layout.simple_list_item_1, shopItems));
            System.out.println("LOADING ITEMS FROM SHAREDPREFS SUCCESFULL");
            coinsTextView.setText("Coins: " + Integer.toString(coins));
        }

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

    public void buyItem(ShopItem theitem){
        if(theitem.status == 1) {
            Toast.makeText(ShopActivity.this, "You already own this!", Toast.LENGTH_SHORT).show();
        }
        else {
            if(coins - theitem.price < 0)
                Toast.makeText(ShopActivity.this, "You cannot afford this!", Toast.LENGTH_SHORT).show();
            else {
                coins = coins - theitem.price;
                theitem.price = 0;
                listView.setAdapter(new UserItemAdapter(this, android.R.layout.simple_list_item_1, shopItems));
                coinsTextView.setText("Coins: " + Integer.toString(coins));

                // add the id of the bought item to string in sharedpreferences
                StringBuilder sb = new StringBuilder(itemsownedstring);
                sb.append(" " + Integer.toString(theitem.id));
                sb.toString();

            }
        }
    }

    // Check if the user has opted to reset the coins and achievements.
    public boolean checkForReset(){
        SharedPreferences useroptions = getSharedPreferences("settings", this.MODE_PRIVATE);
        resetvalue = useroptions.getBoolean("RESET", false);

        if(resetvalue == true) {
            coins = 0;
        }

        return resetvalue;
    }

    // Creates achievements from scratch.
    public ArrayList<ShopItem> createShopItems(){

        // create list to put everything inside array in bulk
        ShopItem[] shopItemList = new ShopItem[]{

                item1 = new ShopItem("Powdery Pastel Scarf", "@drawable/bruinscarf", 1, 50),
                item2 = new ShopItem("Searing Summer Hat", "@mipmap/icontest", 2, 50),
                item3 = new ShopItem("Surely Some Oversized Sock", "@mipmap/icontest", 3, 150),
                item4 = new ShopItem("Warm Wooly Earmuffs", "@mipmap/icontest", 4, 150),
                item5 = new ShopItem("Fantastic Fluffy Flowercrown", "@mipmap/icontest", 5, 250),
                item6 = new ShopItem("Sporty Shades of Supercoolness", "@mipmap/icontest", 6, 250),
                item7 = new ShopItem("Tie With Tons of Triangles", "@mipmap/icontest", 7, 300),
                item8 = new ShopItem("Halo of Helpfulness", "@mipmap/icontest", 8, 500),
                item9 = new ShopItem("Iridescent Wings of Indecision", "@drawable/bruinwings", 9, 1000),
                item10 = new ShopItem("Royal Crown of Masterminds", "@mipmap/icontest", 10, 10000)
        };

        // add the array created above to the arraylist and set the listadapter on this arraylist
        ArrayList<ShopItem> newShopItems = new ArrayList<ShopItem>();
        newShopItems.addAll(Arrays.asList(shopItemList));
        return newShopItems;
    }

    public void saveShopItems(ArrayList<ShopItem> itemsToSave){

        Gson gson = new Gson();

        JsonElement element =
                gson.toJsonTree(itemsToSave, new TypeToken<ArrayList<ShopItem>>() {}.getType());

        JsonArray jsonArray = element.getAsJsonArray();
        String jsonArrayString = jsonArray.toString();

        SharedPreferences prefs = this.getSharedPreferences("storedachievements", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        edit.putString("jsonshop", jsonArrayString);
        edit.putString("owneditems", itemsownedstring);
        edit.putInt("coinsamount", coins);
        edit.commit();
    }

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

    // source: http://codehenge.net/blog/2011/05/customizing-android-listview-item-layout/
    // and http://android-developers.blogspot.nl/2009/02/android-layout-tricks-1.html
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

            // iterate over the items in achievement arraylist
            ShopItem item = shopItems.get(position);
            if (item != null) {

                // initialize layout components from the listitem
                TextView itemName = (TextView) v.findViewById(R.id.itemname);
                TextView itemPrice = (TextView) v.findViewById(R.id.itemprice);
                ImageView itemIcon = (ImageView) v.findViewById(R.id.icon);

                // go over the components of the object, name and progress

                // set textview to show the item's name and price
                if (itemName != null)
                    itemName.setText(item.itemname);

                if(itemPrice != null) {

                    // if the item has been bought
                    if (item.price == 0) {
                        itemPrice.setText("You own this item!");
                        item.status = 1;
                    }
                    else
                        itemPrice.setText(Integer.toString(item.price));
                }
                if(itemIcon != null) {
                    itemIcon.setImageDrawable(getResources().getDrawable(R.mipmap.testicon2));
                }

                saveShopItems(shopItems);

            }
            return v;
        }
    }
}
