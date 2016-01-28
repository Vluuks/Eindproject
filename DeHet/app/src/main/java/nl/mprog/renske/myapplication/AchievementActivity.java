// Renske Talsma, UvA 10896503, vluuks@gmail.com

package nl.mprog.renske.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Activity that shows the user a list of all achievements with their according status. Accessed
 * directly after finishing a game and by the main menu.
 */
public class AchievementActivity extends AppCompatActivity {

    public int finalScore, finalMultiplier, finalLives, coinsAmount, correctCounter;
    public ArrayList<Achievement> achievements;
    public TextView coinsTextView;
    private String jsonString;
    private AchievementManager achievementManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        // Set listview and other layout components.
        ListView listView = (ListView) findViewById(R.id.listView);
        coinsTextView = (TextView) findViewById(R.id.coins);
        achievements = new ArrayList<Achievement>();
        achievementManager = new AchievementManager(AchievementActivity.this);

        // Obtain SharedPreferences.
        SharedPreferences savedprefs = this.getSharedPreferences("storedachievements", MODE_PRIVATE);
        jsonString = savedprefs.getString("jsonachievements", null);
        coinsAmount = savedprefs.getInt("coinsamount", 0);

        // If there are not SharedPreferences or user did a reset, create achievements from scratch.
        if (savedprefs == null || jsonString == null || checkForReset()) {

            // If the user has opted to reset, also reset json string.
            if (checkForReset() && jsonString != null) {
                jsonString = null;
            }
            coinsAmount = 0;
            achievements = achievementManager.createAchievements();
            listView.setAdapter(new UserItemAdapter(this, android.R.layout.simple_list_item_1, achievements));
        }

        // If sharedpreferences exist, use those instead.
        else {
            achievements = achievementManager.loadAchievements(jsonString);
            listView.setAdapter(new UserItemAdapter(this, android.R.layout.simple_list_item_1, achievements));
        }

        // Check where the user came from, the menu or a game.
        //achievementManager.saveAchievements(achievements, coinsAmount);
        checkSource();
    }

    /**
     * Check if the user has opted to reset achievements and coins.
     */
    public boolean checkForReset() {
        SharedPreferences useroptions = getSharedPreferences("settings", this.MODE_PRIVATE);
        boolean resetValue = useroptions.getBoolean("RESET", false);

        if (resetValue == true) {
            SharedPreferences.Editor editor = useroptions.edit();
            editor.putBoolean("RESET", false);
            editor.commit();

            // Reset coins to 0.
            SharedPreferences prefs = this.getSharedPreferences("storedachievements", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putInt("coinsamount", 0);
            edit.commit();
        }
        return resetValue;
    }

    /**
     * Check where the user came from and redirect accordingly.
     */
    public void checkSource() {

        // If the user came from the game, obtain data from intent.
        if (getIntent().getExtras() != null) {
            Intent intent = getIntent();
            finalScore = intent.getExtras().getInt("SCORE");
            finalMultiplier = intent.getExtras().getInt("MAXMULTIPLIER");
            finalLives = intent.getExtras().getInt("LIVES");
            correctCounter = intent.getExtras().getInt("CORRECTCOUNTER");

            // Check if the user is eligible for achievements.
            achievementManager.checkForAchievement(finalScore, finalMultiplier, finalLives,
                    correctCounter, coinsAmount);
        } else
            coinsTextView.setText(getString(R.string.coins) + Integer.toString(coinsAmount));
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
                TextView achievementName = (TextView) v.findViewById(R.id.name);
                TextView achievementProgress = (TextView) v.findViewById(R.id.status);
                ImageView achievementIcon = (ImageView) v.findViewById(R.id.icon);

                // Set TextView to show achievement name.
                if (achievementName != null)
                    achievementName.setText(achievement.getName());

                if (achievementProgress != null) {
                    // If the achievement is completed, set text to complete and update picture.
                    if (achievement.getStatus() == 1) {
                        achievementProgress.setText(R.string.achievementcomplete);
                        Drawable myDrawable = getResources().getDrawable(R.drawable.achievementdone);
                        achievementIcon.setImageDrawable(myDrawable);

                        System.out.println(achievement.getCounter());
                        System.out.println(achievementManager.isEligibleCoins());

                        // Check if the achievement is repeated and award coins appropriately.
                        if (achievement.getCounter() > 1) {

                            // waarom doet deze het niet
                            achievementProgress.setText(getString(R.string.completedpart1) + Integer.toString(achievement.getCounter()) + getString(R.string.completedpart2));

                            if  (achievementManager.isEligibleCoins()){
                            switch (achievement.getType()) {
                                case ("Beginner"):
                                    coinsAmount = coinsAmount + 500;
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
                            }
                            // Update TextView to show the amount of coins the user has.
                            coinsTextView.setText(getString(R.string.coins) + Integer.toString(coinsAmount));
                            achievementManager.setEligibleCoins(false);
                        }
                    }
                    // Otherwise just show the type of achievement as text.
                    else {
                        achievementProgress.setText(achievement.type);
                        Drawable myDrawable = getResources().getDrawable(R.drawable.achievementtodo);
                        achievementIcon.setImageDrawable(myDrawable);
                    }
                }
            }

            achievementManager.saveAchievements(achievements, coinsAmount);
            return v;
        }
    }
}