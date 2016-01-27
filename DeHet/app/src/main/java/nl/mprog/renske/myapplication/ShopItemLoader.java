package nl.mprog.renske.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Renske on 27-1-2016.
 */
public class ShopItemLoader {

    private ShopItem item1, item2, item3, item4, item5, item6, item7, item8, item9, item10;
    private Context activityContext;
    private ArrayList<ShopItem> savedShopItems;

    public ShopItemLoader(Context context){
        this.activityContext = context;
    }

    /**
     * Create all shop items from scratch.
     */
    public ArrayList<ShopItem> createShopItems(){

        // create list to put everything inside array in bulk
        ShopItem[] shopItemList = new ShopItem[]{

                item1 = new ShopItem(activityContext.getString(R.string.bruinscarf), "bruinscarf", 1, 50),
                item2 = new ShopItem(activityContext.getString(R.string.bruinhat), "bruinhat", 2, 50),
                item3 = new ShopItem(activityContext.getString(R.string.bruinsock), "bruinsock", 3, 150),
                item4 = new ShopItem(activityContext.getString(R.string.bruinearmuffs), "bruinearmuffs", 4, 150),
                item5 = new ShopItem(activityContext.getString(R.string.bruinflower), "bruinflower", 5, 250),
                item6 = new ShopItem(activityContext.getString(R.string.bruinshades), "bruinshades", 6, 250),
                item7 = new ShopItem(activityContext.getString(R.string.bruintie), "bruintie", 7, 300),
                item8 = new ShopItem(activityContext.getString(R.string.bruinhalo), "bruinhalo", 8, 500),
                item9 = new ShopItem(activityContext.getString(R.string.bruinwings), "bruinwings", 9, 1000),
                item10 = new ShopItem(activityContext.getString(R.string.bruincrown), "bruincrown", 10, 10000)
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

        // Add the list created above to the actual arraylist.
        savedShopItems = new ArrayList<ShopItem>();
        savedShopItems.addAll(Arrays.asList(savedItemList));
        return savedShopItems;
    }

    public ShopItem getShopItem(int index){
        return savedShopItems.get(index);
    }

    /**
     * Save shop items and their status to SharedPreferences.
     */
    public void saveShopItems(ArrayList<ShopItem> itemsToSave, int coins){

        Gson gson = new Gson();

        JsonElement element =
                gson.toJsonTree(itemsToSave, new TypeToken<ArrayList<ShopItem>>() {}.getType());

        JsonArray jsonArray = element.getAsJsonArray();
        String jsonArrayString = jsonArray.toString();

        SharedPreferences prefs = activityContext.getSharedPreferences("storedachievements", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        edit.putString("jsonshop", jsonArrayString);
        edit.putInt("coinsamount", coins);
        edit.commit();
    }
}