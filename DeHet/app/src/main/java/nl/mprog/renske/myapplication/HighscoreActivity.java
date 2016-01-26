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
    private boolean resetValue, eligibleCoins;
    private String jsonstring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        // Set listview and other layout components.
        ListView listView = (ListView) findViewById(R.id.listView);
        coinsTextView = (TextView) findViewById(R.id.coins);
        achievements = new ArrayList<Achievement>();

        // Obtain SharedPreferences.
        SharedPreferences savedprefs = this.getSharedPreferences("storedachievements", MODE_PRIVATE);
        jsonstring = savedprefs.getString("jsonachievements", null);
        coinsAmount = savedprefs.getInt("coinsamount", 0);
        System.out.println(coinsAmount + "from sharedprefs");

        // If there are not SharedPreferences or user did a reset, create achievements from scratch.
        if (savedprefs == null || jsonstring == null || checkForReset()) {

            if (checkForReset() && jsonstring != null) {
                jsonstring = null;
            }
            coinsAmount = 0;
            achievements = createAchievements();
            listView.setAdapter(new UserItemAdapter(this, android.R.layout.simple_list_item_1, achievements));
        }

        // If sharedpreferences exist, use those instead.
        else {
            achievements = loadAchievements(jsonstring);
            listView.setAdapter(new UserItemAdapter(this, android.R.layout.simple_list_item_1, achievements));
        }

        // Check where the user came from, the menu or a game.
        saveAchievements(achievements);
        checkSource();
    }

    /**
     * Check if the user has opted to reset achievements and coins.
     */
    public boolean checkForReset() {
        SharedPreferences useroptions = getSharedPreferences("settings", this.MODE_PRIVATE);
        resetValue = useroptions.getBoolean("RESET", false);

        System.out.println(resetValue);

        if (resetValue == true) {
            SharedPreferences.Editor editor = useroptions.edit();
            editor.putBoolean("RESET", false);
            editor.commit();

            SharedPreferences prefs = this.getSharedPreferences("storedachievements", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();

            // reset coins to 0
            edit.putInt("coinsamount", 0);
            edit.commit();
            System.out.println("RESET ACHIEVEMENTS AND COINS");
        }

        return resetValue;
    }

    /**
     * Create achievements from scratch.
     */
    public ArrayList<Achievement> createAchievements() {

        // Create list to put everything inside array in bulk.
        Achievement[] achievementlist = new Achievement[]{

                // Beginner achievements
                beginner_achiev1 = new Achievement(getString(R.string.beginnerachievement1),
                        getString(R.string.beginnertype)),
                beginner_achiev2 = new Achievement(getString(R.string.beginnerachievement2),
                        getString(R.string.beginnertype)),
                beginner_achiev3 = new Achievement(getString(R.string.beginnerachievement3),
                        getString(R.string.beginnertype)),
                beginner_achiev4 = new Achievement(getString(R.string.beginnerachievement4),
                        getString(R.string.beginnertype)),

                // Novice achievements
                novice_achiev1 = new Achievement(getString(R.string.noviceachievement1),
                        getString(R.string.novicetype)),
                novice_achiev2 = new Achievement(getString(R.string.noviceachievement2),
                        getString(R.string.novicetype)),
                novice_achiev3 = new Achievement(getString(R.string.noviceachievement3),
                        getString(R.string.novicetype)),
                novice_achiev4 = new Achievement(getString(R.string.noviceachievement4),
                        getString(R.string.novicetype)),

                // Intermediate achievements
                intermediate_achiev1 = new Achievement(getString(R.string.intermediateachievement1),
                        getString(R.string.intermediatetype)),
                intermediate_achiev2 = new Achievement(getString(R.string.intermediateachievement2),
                        getString(R.string.intermediatetype)),
                intermediate_achiev3 = new Achievement(getString(R.string.intermediateachievement3),
                        getString(R.string.intermediatetype)),
                intermediate_achiev4 = new Achievement(getString(R.string.intermediateachievement4),
                        getString(R.string.intermediatetype)),

                // Master achievements
                master_achiev1 = new Achievement(getString(R.string.masterachievement1),
                        getString(R.string.mastertype)),
                master_achiev2 = new Achievement(getString(R.string.masterachievement2),
                        getString(R.string.mastertype)),
                master_achiev3 = new Achievement(getString(R.string.masterachievement3),
                        getString(R.string.mastertype)),

                // Ultimate achievements
                ultimate_achiev1 = new Achievement(getString(R.string.ultimateachievement1),
                        getString(R.string.ultimatetype)),
                ultimate_achiev2 = new Achievement(getString(R.string.ultimateachievement2),
                        getString(R.string.ultimatetype)),
                ultimate_achiev3 = new Achievement(getString(R.string.ultimateachievement3),
                        getString(R.string.ultimatetype))
        };

        // Add the array created above to the arraylist and set the listadapter on this arraylist.
        ArrayList<Achievement> newAchievements = new ArrayList<Achievement>();
        newAchievements.addAll(Arrays.asList(achievementlist));
        return newAchievements;
    }

    /**
     * Loads existing achievements from SharedPreferences.
     */
    public ArrayList<Achievement> loadAchievements(String jsonstring) {

        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray jArray = parser.parse(jsonstring).getAsJsonArray();
        ArrayList<Achievement> sharedprefslist = new ArrayList<Achievement>();

        for (JsonElement obj : jArray) {
            Achievement ach = gson.fromJson(obj, Achievement.class);
            sharedprefslist.add(ach);
        }

        // Ccreate list to put everything inside array in bulk.
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

        // Add the list created above to the actual arraylist
        ArrayList<Achievement> savedAchievements = new ArrayList<Achievement>();
        savedAchievements.addAll(Arrays.asList(achievementlist));
        return savedAchievements;
    }

    /**
     * Check where the user came from and redirect accordingly.
     */
    public void checkSource() {

        // If the user came from the game, obtain data from intent.
        if (getIntent().getExtras() != null) {
            System.out.println("INTENT IS NOT NULL, SHOULD BE COMING FROM GAME");
            Intent intent = getIntent();
            finalscore = intent.getExtras().getInt("SCORE");
            finalmultiplier = intent.getExtras().getInt("MAXMULTIPLIER");
            finallives = intent.getExtras().getInt("LIVES");
            correctcounter = intent.getExtras().getInt("CORRECTCOUNTER");

            // Check if the user is eligible for achievements.
            checkForAchievement();
        } else
            coinsTextView.setText(getString(R.string.coins) + Integer.toString(coinsAmount));
    }

    /**
     * Checks if the user is eligible for one ore more achievements.
     */
    public void checkForAchievement() {

        eligibleCoins = true;

        // Beginner achievements.
        if (finalscore >= 50) {
            beginner_achiev1.setStatus(1);
            beginner_achiev1.counter++;
        }
        if (finalmultiplier >= 5) {
            beginner_achiev2.setStatus(1);
            beginner_achiev2.counter++;
        }
        if (finallives >= 1) {
            beginner_achiev3.setStatus(1);
            beginner_achiev3.counter++;
        }
        if (correctcounter >= 25) {
            beginner_achiev4.setStatus(1);
            beginner_achiev4.counter++;
        }

        // Novice achievements.
        if (finalscore >= 100) {
            novice_achiev1.setStatus(1);
            novice_achiev1.counter++;
        }
        if (finalmultiplier >= 10) {
            novice_achiev2.setStatus(1);
            novice_achiev2.counter++;
        }
        if (finallives >= 2) {
            novice_achiev3.setStatus(1);
            novice_achiev3.counter++;
        }
        if (correctcounter >= 50) {
            novice_achiev4.setStatus(1);
            novice_achiev4.counter++;
        }

        // Intermediate achievements.
        if (finalscore >= 250) {
            intermediate_achiev1.setStatus(1);
            intermediate_achiev1.counter++;
        }
        if (finalmultiplier >= 25) {
            intermediate_achiev2.setStatus(1);
            intermediate_achiev2.counter++;
        }
        if (finallives == 3) {
            intermediate_achiev3.setStatus(1);
            intermediate_achiev3.counter++;
        }
        if (correctcounter >= 75) {
            intermediate_achiev4.setStatus(1);
            intermediate_achiev4.counter++;
        }

        // Master achievements.
        if (finalscore >= 750) {
            master_achiev1.setStatus(1);
            master_achiev1.counter++;
        }
        if (finalmultiplier >= 50) {
            master_achiev2.setStatus(1);
            master_achiev2.counter++;
        }
        if (finalmultiplier >= 100) {
            master_achiev3.setStatus(1);
            master_achiev3.counter++;
        }

        // Ultimate achievements.
        if (finalscore >= 1500) {
            ultimate_achiev1.setStatus(1);
            ultimate_achiev1.counter++;
        }
        if (finalmultiplier >= 100) {
            ultimate_achiev2.setStatus(1);
            ultimate_achiev2.counter++;
        }
        if (finalmultiplier >= 150) {
            ultimate_achiev3.setStatus(1);
            ultimate_achiev3.counter++;
        }

        // Save the status of the achievements.
        saveAchievements(achievements);
    }

    /**
     * Store achievements in SharedPreferences.
     */
    public void saveAchievements(ArrayList<Achievement> achievements) {
        Gson gson = new Gson();

        JsonElement element =
                gson.toJsonTree(achievements, new TypeToken<ArrayList<Achievement>>() {
                }.getType());

        JsonArray jsonArray = element.getAsJsonArray();
        String jsonArrayString = jsonArray.toString();

        SharedPreferences prefs = this.getSharedPreferences("storedachievements", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        edit.putString("jsonachievements", jsonArrayString);
        edit.putInt("coinsamount", coinsAmount);
        edit.commit();
    }

    /**
     * The adapter of the listview, handles all changes made to the shopitem objects.
     * source: http://codehenge.net/blog/2011/05/customizing-android-listview-item-layout/
     * and http://android-developers.blogspot.nl/2009/02/android-layout-tricks-1.html
     */
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
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.listitem, null);
            }

            // Iterate over the items in achievement arraylist.
            Achievement achievement = achievements.get(position);
            if (achievement != null) {

                // Initialize layout components from the listitem.
                TextView achievementname = (TextView) v.findViewById(R.id.name);
                TextView achievementprogress = (TextView) v.findViewById(R.id.status);
                ImageView achievementicon = (ImageView) v.findViewById(R.id.icon);

                // set textview to show achievement name
                if (achievementname != null)
                    achievementname.setText(achievement.name);

                if (achievementprogress != null) {
                    // if the achievement is completed, set text to complete and update picture
                    if (achievement.status == 1) {
                        achievementprogress.setText(R.string.achievementcomplete);
                        achievementicon.setImageDrawable(getResources().getDrawable(R.mipmap.testicon2));

                        // check if the achievement is repeated and award coins appropriately
                        if (achievement.counter > 1 && eligibleCoins) {
                            achievementprogress.setText(getString(R.string.completedpart1) + Integer.toString(achievement.counter) + getString(R.string.completedpart2));

                            switch (achievement.type) {
                                case ("Beginner"):
                                    coinsAmount = coinsAmount + 500; //todo terugzetten naar 5
                                    System.out.println("COINS ADDED");
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
                            coinsTextView.setText(getString(R.string.coins) + Integer.toString(coinsAmount));
                            eligibleCoins = false;
                        }
                    }
                    // otherwise just show the type of achievement as text
                    else {
                        achievementprogress.setText(achievement.type);
                        achievementicon.setImageDrawable(getResources().getDrawable(R.mipmap.icontest));
                    }
                }
            }

            saveAchievements(achievements);
            return v;
        }
    }
}

