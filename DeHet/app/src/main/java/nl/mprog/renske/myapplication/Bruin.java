package nl.mprog.renske.myapplication;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Renske on 18-1-2016.
 */
public class Bruin {

    private ShopItem item1, item2, item3, item4, item5, item6, item7, item8, item9, item10;
    private ImageView item1ImageView, item2ImageView, item3ImageView, item4ImageView, item5ImageView,
            item6ImageView, item7ImageView, item8ImageView, item9ImageView, item10ImageView;
    private SharedPreferences savedShopItems;
    private String jsonstring;
    private ArrayList<ShopItem> equippedItems;

    public Bruin(){

        // turn json string back to objects

        // loop over objects

        // get information about which items are equipped from objects in json

        // if a certain object has true as value for equipped, show appropriate image

        // if a certain object has false, hide the appropriate image

        // all are hidden by default

        // upon relaunch of the app reload also the users chosesn accessories

        // the first time the app launches the jsonstring is null, should check for that

        // only after the user visits the shop at least once it will exist


    }


    public void setSharedPreferences(SharedPreferences savedPreferences){

        if(savedPreferences != null)
            jsonstring = savedPreferences.getString("jsonshop", null);
        else
            jsonstring = null;
    }


    public void setItemImageViews(ArrayList<ImageView> imageViewList){

        this.item1ImageView = imageViewList.get(0);
        this.item2ImageView = imageViewList.get(1);
        this.item3ImageView = imageViewList.get(2);
        this.item4ImageView = imageViewList.get(3);
        this.item5ImageView = imageViewList.get(4);
        this.item6ImageView = imageViewList.get(5);
        this.item7ImageView = imageViewList.get(6);
        this.item8ImageView = imageViewList.get(7);
        this.item9ImageView = imageViewList.get(8);
        this.item10ImageView = imageViewList.get(9);


        // set all to invisible initially
        this.item1ImageView.setVisibility(View.GONE);
        this.item2ImageView.setVisibility(View.GONE);
        this.item3ImageView.setVisibility(View.GONE);
        this.item4ImageView.setVisibility(View.GONE);
        this.item5ImageView.setVisibility(View.GONE);
        this.item6ImageView.setVisibility(View.GONE);
        this.item7ImageView.setVisibility(View.GONE);
        this.item8ImageView.setVisibility(View.GONE);
        this.item9ImageView.setVisibility(View.GONE);
        this.item10ImageView.setVisibility(View.GONE);

    }



    public void checkEquipped(){

        if(jsonstring != null) {
            loadShopItems(jsonstring);

            for (ShopItem item : equippedItems) {

                // if an item is marked as equipped, set the right imageviews to visible
                if(item.equipped == true){
                    switch(item.id){
                        case 1:
                            item1ImageView.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            item2ImageView.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            item3ImageView.setVisibility(View.VISIBLE);
                            break;
                        case 4:
                            item4ImageView.setVisibility(View.VISIBLE);
                            break;
                        case 5:
                            item5ImageView.setVisibility(View.VISIBLE);
                            break;
                        case 6:
                            item6ImageView.setVisibility(View.VISIBLE);
                            break;
                        case 7:
                            item7ImageView.setVisibility(View.VISIBLE);
                            break;
                        case 8:
                            item8ImageView.setVisibility(View.VISIBLE);
                            break;
                        case 9:
                            item9ImageView.setVisibility(View.VISIBLE);
                            break;
                        case 10:
                            item10ImageView.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }
        }
    }




    public void loadShopItems(String jsonstring){

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
        equippedItems = new ArrayList<ShopItem>();
        equippedItems.addAll(Arrays.asList(savedItemList));

    }

}
