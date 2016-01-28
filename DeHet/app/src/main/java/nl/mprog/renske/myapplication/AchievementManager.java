// Renske Talsma, UvA 10896503, vluuks@gmail.com

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
 * AchievementManager makes sure that achievements are properly created, saved and loaded and checks
 * whether the user is eligible for one or more achievements and updates achievements accordingly.
 */
public class AchievementManager {

    private Achievement
            beginner_achiev1, beginner_achiev2, beginner_achiev3, beginner_achiev4,
            novice_achiev1, novice_achiev2, novice_achiev3, novice_achiev4,
            intermediate_achiev1, intermediate_achiev2, intermediate_achiev3, intermediate_achiev4,
            master_achiev1, master_achiev2, master_achiev3,
            ultimate_achiev1, ultimate_achiev2, ultimate_achiev3;

    private Context activityContext;
    private boolean eligibleCoins;
    private ArrayList<Achievement> achievementList;

    /**
     * Constructor.
     */
    public AchievementManager(Context context){
        this.activityContext = context;
    }

    /**
     * Get/set methods.
     */
    public void setEligibleCoins(boolean eligibleCoins) {
        this.eligibleCoins = eligibleCoins;
    }

    public boolean isEligibleCoins(){
        return eligibleCoins;
    }

    /**
     * Create achievements from scratch.
     */
    public ArrayList<Achievement> createAchievements() {

        // Create list to put everything inside array in bulk.
        Achievement[] achievementlist = new Achievement[]{

                // Beginner achievements
                beginner_achiev1 = new Achievement(activityContext.getString(R.string.beginnerachievement1),
                        activityContext.getString(R.string.beginnertype)),
                beginner_achiev2 = new Achievement(activityContext.getString(R.string.beginnerachievement2),
                        activityContext.getString(R.string.beginnertype)),
                beginner_achiev3 = new Achievement(activityContext.getString(R.string.beginnerachievement3),
                        activityContext.getString(R.string.beginnertype)),
                beginner_achiev4 = new Achievement(activityContext.getString(R.string.beginnerachievement4),
                        activityContext.getString(R.string.beginnertype)),

                // Novice achievements
                novice_achiev1 = new Achievement(activityContext.getString(R.string.noviceachievement1),
                        activityContext.getString(R.string.novicetype)),
                novice_achiev2 = new Achievement(activityContext.getString(R.string.noviceachievement2),
                        activityContext.getString(R.string.novicetype)),
                novice_achiev3 = new Achievement(activityContext.getString(R.string.noviceachievement3),
                        activityContext.getString(R.string.novicetype)),
                novice_achiev4 = new Achievement(activityContext.getString(R.string.noviceachievement4),
                        activityContext.getString(R.string.novicetype)),

                // Intermediate achievements
                intermediate_achiev1 = new Achievement(activityContext.getString(R.string.intermediateachievement1),
                        activityContext.getString(R.string.intermediatetype)),
                intermediate_achiev2 = new Achievement(activityContext.getString(R.string.intermediateachievement2),
                        activityContext.getString(R.string.intermediatetype)),
                intermediate_achiev3 = new Achievement(activityContext.getString(R.string.intermediateachievement3),
                        activityContext.getString(R.string.intermediatetype)),
                intermediate_achiev4 = new Achievement(activityContext.getString(R.string.intermediateachievement4),
                        activityContext.getString(R.string.intermediatetype)),

                // Master achievements
                master_achiev1 = new Achievement(activityContext.getString(R.string.masterachievement1),
                        activityContext.getString(R.string.mastertype)),
                master_achiev2 = new Achievement(activityContext.getString(R.string.masterachievement2),
                        activityContext.getString(R.string.mastertype)),
                master_achiev3 = new Achievement(activityContext.getString(R.string.masterachievement3),
                        activityContext.getString(R.string.mastertype)),

                // Ultimate achievements
                ultimate_achiev1 = new Achievement(activityContext.getString(R.string.ultimateachievement1),
                        activityContext.getString(R.string.ultimatetype)),
                ultimate_achiev2 = new Achievement(activityContext.getString(R.string.ultimateachievement2),
                        activityContext.getString(R.string.ultimatetype)),
                ultimate_achiev3 = new Achievement(activityContext.getString(R.string.ultimateachievement3),
                        activityContext.getString(R.string.ultimatetype))
        };

        // Add the array created above to the arraylist and set the listadapter on this arraylist.

        ArrayList<Achievement> achievementList = new ArrayList<Achievement>();
        achievementList.addAll(Arrays.asList(achievementlist));
        return achievementList;
    }

    /**
     * Loads existing achievements from SharedPreferences.
     */
    public ArrayList<Achievement> loadAchievements(String jsonstring) {

        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray jArray = parser.parse(jsonstring).getAsJsonArray();
        ArrayList<Achievement> sharedprefslist = new ArrayList<Achievement>();

        // Iterate over objects in Json Array.
        for (JsonElement obj : jArray) {
            Achievement ach = gson.fromJson(obj, Achievement.class);
            sharedprefslist.add(ach);
        }

        // Create list to put everything inside array in bulk.
        Achievement[] achievementList = new Achievement[]{

                beginner_achiev1 = sharedprefslist.get(0),
                beginner_achiev2 = sharedprefslist.get(1),
                beginner_achiev3 = sharedprefslist.get(2),
                beginner_achiev4 = sharedprefslist.get(3),

                novice_achiev1 = sharedprefslist.get(4),
                novice_achiev2 = sharedprefslist.get(5),
                novice_achiev3 = sharedprefslist.get(6),
                novice_achiev4 = sharedprefslist.get(7),

                intermediate_achiev1 = sharedprefslist.get(8),
                intermediate_achiev2 = sharedprefslist.get(9),
                intermediate_achiev3 = sharedprefslist.get(10),
                intermediate_achiev4 = sharedprefslist.get(11),

                master_achiev1 = sharedprefslist.get(12),
                master_achiev2 = sharedprefslist.get(13),
                master_achiev3 = sharedprefslist.get(14),

                ultimate_achiev1 = sharedprefslist.get(15),
                ultimate_achiev2 = sharedprefslist.get(16),
                master_achiev3 = sharedprefslist.get(17)
        };

        // Add the list created above to the actual arraylist
        ArrayList<Achievement> savedAchievementList = new ArrayList<Achievement>();
        savedAchievementList.addAll(Arrays.asList(achievementList));
        return savedAchievementList;
    }

    /**
     * Store achievements in SharedPreferences.
     */
    public void saveAchievements(ArrayList<Achievement> achievements, int coins) {
        Gson gson = new Gson();

        JsonElement element =
                gson.toJsonTree(achievements, new TypeToken<ArrayList<Achievement>>() {
                }.getType());

        JsonArray jsonArray = element.getAsJsonArray(); // waarom crasht ie hier?? TODO
        String jsonArrayString = jsonArray.toString();

        SharedPreferences prefs = activityContext.getSharedPreferences("storedachievements", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        edit.putString("jsonachievements", jsonArrayString);
        edit.putInt("coinsamount", coins);
        edit.commit();
    }

    /**
     * Checks if the user is eligible for one ore more achievements.
     */
    public void checkForAchievement(int finalscore, int finalmultiplier, int finallives, int correctcounter, int currentcoins) {

        setEligibleCoins(true);

        // Beginner achievements.
        if (finalscore >= 50) {
            beginner_achiev1.setStatus(1);
            beginner_achiev1.setCounterPlusOne();
        }
        if (finalmultiplier >= 5) {
            beginner_achiev2.setStatus(1);
            beginner_achiev2.setCounterPlusOne();
        }
        if (finallives >= 1) {
            beginner_achiev3.setStatus(1);
            beginner_achiev3.setCounterPlusOne();
        }
        if (correctcounter >= 25) {
            beginner_achiev4.setStatus(1);
            beginner_achiev4.setCounterPlusOne();
        }

        // Novice achievements.
        if (finalscore >= 100) {
            novice_achiev1.setStatus(1);
            novice_achiev1.setCounterPlusOne();
        }
        if (finalmultiplier >= 10) {
            novice_achiev2.setStatus(1);
            novice_achiev2.setCounterPlusOne();
        }
        if (finallives >= 2) {
            novice_achiev3.setStatus(1);
            novice_achiev3.setCounterPlusOne();
        }
        if (correctcounter >= 50) {
            novice_achiev4.setStatus(1);
            novice_achiev4.setCounterPlusOne();
        }

        // Intermediate achievements.
        if (finalscore >= 250) {
            intermediate_achiev1.setStatus(1);
            intermediate_achiev1.setCounterPlusOne();
        }
        if (finalmultiplier >= 25) {
            intermediate_achiev2.setStatus(1);
            intermediate_achiev2.setCounterPlusOne();
        }
        if (finallives == 3) {
            intermediate_achiev3.setStatus(1);
            intermediate_achiev3.setCounterPlusOne();
        }
        if (correctcounter >= 75) {
            intermediate_achiev4.setStatus(1);
            intermediate_achiev4.setCounterPlusOne();
        }

        // Master achievements.
        if (finalscore >= 750) {
            master_achiev1.setStatus(1);
            master_achiev1.setCounterPlusOne();
        }
        if (finalmultiplier >= 50) {
            master_achiev2.setStatus(1);
            master_achiev2.setCounterPlusOne();
        }
        if (finalmultiplier >= 100) {
            master_achiev3.setStatus(1);
            master_achiev3.setCounterPlusOne();
        }

        // Ultimate achievements.
        if (finalscore >= 1500) {
            ultimate_achiev1.setStatus(1);
            ultimate_achiev1.setCounterPlusOne();
        }
        if (finalmultiplier >= 100) {
            ultimate_achiev2.setStatus(1);
            ultimate_achiev2.setCounterPlusOne();
        }
        if (finalmultiplier >= 150) {
            ultimate_achiev3.setStatus(1);
            ultimate_achiev3.setCounterPlusOne();
        }
    }
}
