package nl.mprog.renske.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Arrays;

public class HighscoreActivity extends AppCompatActivity {

    public int finalscore, finalmultiplier, finallives, coinsAmount, correctcounter;
    public Achievement
            beginner_achiev1, beginner_achiev2, beginner_achiev3, beginner_achiev4,
            novice_achiev1, novice_achiev2, novice_achiev3, novice_achiev4,
            intermediate_achiev1, intermediate_achiev2, intermediate_achiev3, intermediate_achiev4,
            master_achiev1, master_achiev2, master_achiev3,
            ultimate_achiev1, ultimate_achiev2, ultimate_achiev3;
    public ArrayList<Achievement> achievements;
    public TextView coinsTextView;
    private boolean resetvalue;
    private String jsonstring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        // set listview
        ListView listView = (ListView) findViewById(R.id.listView);
        coinsTextView = (TextView) findViewById(R.id.coins);
        achievements = new ArrayList<Achievement>();

        // check for sharedpreferences
        SharedPreferences savedprefs = this.getSharedPreferences("storedachievements", MODE_PRIVATE);
        jsonstring = savedprefs.getString("jsonachievements", null);
        coinsAmount = savedprefs.getInt("coinsamount", 0);


        // if it's the first time the app is loaded or achievements have been reset create achievement objects
        if(savedprefs == null || jsonstring == null  || checkForReset()) {

            if(checkForReset() && jsonstring != null){
                jsonstring = null;
            }

            coinsAmount = 0;
            achievements = createAchievements();
            listView.setAdapter(new UserItemAdapter(this, android.R.layout.simple_list_item_1, achievements));
        }

        // if sharedpreferences exist, use those instead
        else {
            achievements = loadAchievements(jsonstring);
            listView.setAdapter(new UserItemAdapter(this, android.R.layout.simple_list_item_1, achievements));
            System.out.println("LOADING FROM SHAREDPREFS SUCCESFULL");

        }

        // check where the user came from (if not from game, then no need to check for achievements)
        saveAchievements(achievements);
        checkSource();
    }



    // Check if the user has opted to reset the achievements.
    public boolean checkForReset(){
        SharedPreferences useroptions = getSharedPreferences("settings", this.MODE_PRIVATE);
        resetvalue = useroptions.getBoolean("RESET", false);

        if(resetvalue == true) {
            SharedPreferences.Editor editor = useroptions.edit();
            editor.putBoolean("RESET", false);
            editor.commit();

            SharedPreferences prefs = this.getSharedPreferences("storedachievements", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();

            edit.putInt("currentcoins", 0);
            edit.commit();
        }

        return resetvalue;
    }

    // Creates achievements from scratch.
    public ArrayList<Achievement> createAchievements(){

        // create list to put everything inside array in bulk
        Achievement[] achievementlist = new Achievement[]{

                // beginner achievements
                beginner_achiev1 = new Achievement("Score 50 points or more", "@mipmap/icontest", 0, "Beginner"),
                beginner_achiev2 = new Achievement("Score a combo of at least 5", "@mipmap/icontest", 0, "Beginner"),
                beginner_achiev3 = new Achievement("Finish a game with at least 1 life left", "@mipmap/icontest", 0, "Beginner"),
                beginner_achiev4 = new Achievement("Correctly assign 25 articles", "@mipmap/icontest", 0, "Beginner"),

                // novice achievements
                novice_achiev1 = new Achievement("Score 100 points or more", "@mipmap/icontest", 0, "Novice"),
                novice_achiev2 = new Achievement("Score a combo of at least 10", "@mipmap/icontest", 0, "Novice"),
                novice_achiev3 = new Achievement("Finish a game with at least 2 lives left", "@mipmap/icontest", 0, "Novice"),
                novice_achiev4 = new Achievement("Correctly assign 50 articles", "@mipmap/icontest", 0, "Novice"),

                // intermediate achievements
                intermediate_achiev1 = new Achievement("Score 250 points or more", "@mipmap/icontest", 0, "Intermediate"),
                intermediate_achiev2 = new Achievement("Score a combo of at least 25", "@mipmap/icontest", 0, "Intermediate"),
                intermediate_achiev3 = new Achievement("Finish a game with 3 lives left", "@mipmap/icontest", 0, "Intermediate"),
                intermediate_achiev4 = new Achievement("Correctly assign 75 articles", "@mipmap/icontest", 0, "Intermediate"),

                // master achievements
                master_achiev1 = new Achievement("Score 750 points or more", "@mipmap/icontest", 0, "Master"),
                master_achiev2 = new Achievement("Score a combo of at least 50", "@mipmap/icontest", 0, "Master"),
                master_achiev3 = new Achievement("Correctly assign 100 articles", "@mipmap/icontest", 0, "Master"),

                // ultimate achievements
                master_achiev1 = new Achievement("Score 1500 points or more", "@mipmap/icontest", 0, "Ultimate"),
                master_achiev2 = new Achievement("Score a combo of at least 100", "@mipmap/icontest", 0, "Ultimate"),
                master_achiev3 = new Achievement("Correctly assign 150 articles", "@mipmap/icontest", 0, "Ultimate")
        };

        // add the array created above to the arraylist and set the listadapter on this arraylist
        ArrayList<Achievement> newAchievements = new ArrayList<Achievement>();
        newAchievements.addAll(Arrays.asList(achievementlist));
        return newAchievements;
    }



    // Checks where the user came from, the game or the menu.
    public void checkSource(){

        // if the user came from the game
        if(getIntent().getExtras()!=null) {
            Intent intent = getIntent();
            finalscore = intent.getExtras().getInt("SCORE");
            finalmultiplier = intent.getExtras().getInt("MAXMULTIPLIER");
            finallives = intent.getExtras().getInt("LIVES");
            correctcounter = intent.getExtras().getInt("CORRECTCOUNTER");

            // check if the user is eligible for achievements
            checkForAchievement();
            }
        else
            coinsTextView.setText("Coins: " + Integer.toString(coinsAmount));
        }


    // Checks if the user is eligible for one or more achievements.
    public void checkForAchievement(){

        // check if they're eligible for an achievement
        if(finalscore >= 50) {
            beginner_achiev1.setStatus(1);
            beginner_achiev1.counter++;
        }
        if(finalmultiplier >= 5) {
            beginner_achiev2.setStatus(1);
            beginner_achiev2.counter++;
        }
        if(finallives >= 1) {
            beginner_achiev3.setStatus(1);
            beginner_achiev3.counter++;
        }
        if(correctcounter >= 25) {
            beginner_achiev4.setStatus(1);
            beginner_achiev4.counter++;
        }


        if(finalscore >= 100) {
            novice_achiev1.setStatus(1);
            novice_achiev1.counter++;
        }
        if(finalmultiplier >= 10) {
            novice_achiev2.setStatus(1);
            novice_achiev2.counter++;
        }
        if(finallives >= 2) {
            novice_achiev3.setStatus(1);
            novice_achiev3.counter++;
        }
        if(correctcounter >= 50) {
            novice_achiev4.setStatus(1);
            novice_achiev4.counter++;
        }


        if(finalscore >= 250) {
            intermediate_achiev1.setStatus(1);
            intermediate_achiev1.counter++;
        }
        if(finalmultiplier >= 25) {
            intermediate_achiev2.setStatus(1);
            intermediate_achiev2.counter++;
        }
        if(finallives == 3) {
            intermediate_achiev3.setStatus(1);
            intermediate_achiev3.counter++;
        }
        if(correctcounter >= 75) {
            intermediate_achiev4.setStatus(1);
            intermediate_achiev4.counter++;
        }


        if(finalscore >= 750) {
            master_achiev1.setStatus(1);
            master_achiev1.counter++;
        }
        if(finalmultiplier >= 50) {
            master_achiev2.setStatus(1);
            master_achiev2.counter++;
        }
        if(finalmultiplier >= 100) {
            master_achiev3.setStatus(1);
            master_achiev3.counter++;
        }


        if(finalscore >= 1500) {
            ultimate_achiev1.setStatus(1);
            ultimate_achiev1.counter++;
        }
        if(finalmultiplier >= 100) {
            ultimate_achiev2.setStatus(1);
            ultimate_achiev2.counter++;
        }
        if(finalmultiplier >= 150) {
            ultimate_achiev3.setStatus(1);
            ultimate_achiev3.counter++;
        }
        // Save the status of the achievements.
        saveAchievements(achievements);
    }


    // Load exisiting achievements from sharedpreferences.
    public ArrayList<Achievement> loadAchievements(String jsonstring){

        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray jArray = parser.parse(jsonstring).getAsJsonArray();
        ArrayList<Achievement> sharedprefslist = new ArrayList<Achievement>();

        for(JsonElement obj : jArray )
        {
            Achievement ach = gson.fromJson( obj , Achievement.class);
            sharedprefslist.add(ach);
        }

        // create list to put everything inside array in bulk
        Achievement[] achievementlist = new Achievement[]{

                beginner_achiev1 = sharedprefslist.get(0),
                beginner_achiev2 = sharedprefslist.get(1),
                beginner_achiev3 = sharedprefslist.get(2),

                novice_achiev1 = sharedprefslist.get(3),
                novice_achiev2 = sharedprefslist.get(4),
                novice_achiev3 = sharedprefslist.get(5),

                intermediate_achiev1 = sharedprefslist.get(6),
                intermediate_achiev2 = sharedprefslist.get(7),
                intermediate_achiev3 = sharedprefslist.get(8),

                master_achiev1 = sharedprefslist.get(9),
                master_achiev2 = sharedprefslist.get(10),

                ultimate_achiev1 = sharedprefslist.get(11),
                ultimate_achiev2 = sharedprefslist.get(12),

        };

        // add the list created above to the actual arraylist
        ArrayList<Achievement> savedAchievements = new ArrayList<Achievement>();
        savedAchievements.addAll(Arrays.asList(achievementlist));
        return savedAchievements;

    }




    // Store achievements in sharedpreferences as Json string.
    public void saveAchievements(ArrayList<Achievement> achievements) {
        Gson gson = new Gson();

        JsonElement element =
                gson.toJsonTree(achievements, new TypeToken<ArrayList<Achievement>>() {}.getType());

        JsonArray jsonArray = element.getAsJsonArray();
        String jsonArrayString = jsonArray.toString();

        SharedPreferences prefs = this.getSharedPreferences("storedachievements", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        edit.putString("jsonachievements", jsonArrayString);
        edit.putInt("coinsamount", coinsAmount);
        edit.commit();
        }















    // source: http://codehenge.net/blog/2011/05/customizing-android-listview-item-layout/
    // and http://android-developers.blogspot.nl/2009/02/android-layout-tricks-1.html
    public class UserItemAdapter extends ArrayAdapter<Achievement> {
        protected ArrayList<Achievement> achievements;
        public UserItemAdapter(Context context, int textViewResourceId, ArrayList<Achievement> achievements) {
            super(context, textViewResourceId, achievements);
            this.achievements = achievements;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.listitem, null);
            }

            // iterate over the items in achievement arraylist
            Achievement achievement = achievements.get(position);
            if (achievement != null) {

                // initialize layout components from the listitem
                TextView achievementname = (TextView) v.findViewById(R.id.name);
                TextView achievementprogress = (TextView) v.findViewById(R.id.status);
                ImageView achievementicon = (ImageView) v.findViewById(R.id.icon);


                // go over the components of the object, name and progress

                // set textview to show achievement name
                if (achievementname != null)
                    achievementname.setText(achievement.name);

                if(achievementprogress != null) {
                    // if the achievement is completed, set text to complete and update picture
                    if(achievement.status == 1){
                        achievementprogress.setText("Complete!");
                        achievementicon.setImageDrawable(getResources().getDrawable(R.mipmap.testicon2));

                        // check if the achievement is repeated and award coins appropriately
                        if(achievement.counter > 1) {
                            System.out.println(coinsAmount);
                            System.out.println("COUNTERNEW IS BIGGER THAN OLD COUNTER");
                            System.out.println(achievement.counter + "|" + achievement.countercurrent);
                            achievementprogress.setText("Completed " + Integer.toString(achievement.counter) + " times");


                            // if statements met equals van maken
                            switch(achievement.type) {
                                case ("Beginner"):
                                    coinsAmount = coinsAmount + 500; //todo terugzetten naar 5
                                    break;

                                case ("Novice"):
                                    coinsAmount = coinsAmount + 10;
                                    break;

                                case ("Intermediate"):
                                    coinsAmount = coinsAmount + 15;
                                    break;

                                case ("Master"):
                                    coinsAmount = coinsAmount + 25;
                                    break;

                                case ("Ultimate"):
                                    coinsAmount = coinsAmount + 50;
                                    break;
                            }

                            // update the amount of coins the user has
                            coinsTextView.setText("Coins: " + Integer.toString(coinsAmount));
                            System.out.println("AMOUNT OF COINS" + coinsAmount);

                            // update the current counter
                            //achievement.countercurrent = achievement.counternew;
                            System.out.println(achievement.counter + achievement.countercurrent);

                        }
                    }
                    // otherwise just show the type of achievement as text
                    else {
                        achievementprogress.setText(achievement.type);
                        achievementicon.setImageDrawable(getResources().getDrawable(R.mipmap.icontest));
                    }
                }

            saveAchievements(achievements);


            }
            return v;
        }
    }
}
