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
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;

public class HighscoreActivity extends AppCompatActivity {

    public int finalscore, finalmultiplier, finallives;
    public Achievement beginner_achiev1, beginner_achiev2, beginner_achiev3, novice_achiev1,
            novice_achiev2, novice_achiev3, intermediate_achiev1, intermediate_achiev2,
            intermediate_achiev3, master_achiev1, master_achiev2, ultimate_achiev1, ultimate_achiev2;
    public ArrayList<Achievement> achievements;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);


        // set listview
        ListView listView = (ListView) findViewById(R.id.listView);
        achievements = new ArrayList<Achievement>();

        // check for sharedpreferences
        SharedPreferences savedprefs = this.getSharedPreferences("storedachievements", MODE_PRIVATE);
        String jsonstring = savedprefs.getString("jsonachievements", null);

        // if it's the first time the app is loaded create achievement objects
        if(savedprefs == null || jsonstring == null) {
            achievements = createAchievements();
            listView.setAdapter(new UserItemAdapter(this, android.R.layout.simple_list_item_1, achievements));
        }

        // if sharedpreferences exist, use those instead
        else {
            achievements = loadAchievements(jsonstring);
            listView.setAdapter(new UserItemAdapter(this, android.R.layout.simple_list_item_1, achievements));
        }

        // check where the user came from (if not from game, then no need to check for achievements)
        checkSource();
    }


    public ArrayList<Achievement> createAchievements(){

        // create list to put everything inside array in bulk
        Achievement[] achievementlist = new Achievement[]{

                // beginner achievements
                beginner_achiev1 = new Achievement("Score 50 points or more", "@mipmap/icontest", 0, "Beginner"),
                beginner_achiev2 = new Achievement("Score a combo of at least 5", "@mipmap/icontest", 0, "Beginner"),
                beginner_achiev3 = new Achievement("Finish a game with at least 1 life left", "@mipmap/icontest", 0, "Beginner"),

                // novice achievements
                novice_achiev1 = new Achievement("Score 100 points or more", "@mipmap/icontest", 0, "Novice"),
                novice_achiev2 = new Achievement("Score a combo of at least 10", "@mipmap/icontest", 0, "Novice"),
                novice_achiev3 = new Achievement("Finish a game with at least 2 lives left", "@mipmap/icontest", 0, "Novice"),

                // intermediate achievements
                intermediate_achiev1 = new Achievement("Score 250 points or more", "@mipmap/icontest", 0, "Intermediate"),
                intermediate_achiev2 = new Achievement("Score a combo of at least 25", "@mipmap/icontest", 0, "Intermediate"),
                intermediate_achiev3 = new Achievement("Finish a game with 3 lives left", "@mipmap/icontest", 0, "Intermediate"),

                // master achievements
                master_achiev1 = new Achievement("Score 750 points or more", "@mipmap/icontest", 0, "Master"),
                master_achiev2 = new Achievement("Score a combo of at least 50", "@mipmap/icontest", 0, "Master"),

                // ultimate achievements
                master_achiev1 = new Achievement("Score 1500 points or more", "@mipmap/icontest", 0, "Ultimate"),
                master_achiev2 = new Achievement("Score a combo of at least 100", "@mipmap/icontest", 0, "Ultimate")
        };

        // add the array created above to the arraylist and set the listadapter on this arraylist
        ArrayList<Achievement> newAchievements = new ArrayList<Achievement>();
        newAchievements.addAll(Arrays.asList(achievementlist));
        return newAchievements;

    }

    public void checkSource(){

        // if the user came from the game
        if(getIntent().getExtras()!=null) {
            Intent intent = getIntent();
            finalscore = intent.getExtras().getInt("SCORE");
            finalmultiplier = intent.getExtras().getInt("MAXMULTIPLIER");
            finallives = intent.getExtras().getInt("LIVES");

            System.out.println(finalscore);
            System.out.println(finalmultiplier);
            System.out.println(finallives);

            // check if the user is eligible for achievements
            checkForAchievement();
            }
        }



    public void checkForAchievement(){

        // check if they're eligible for an achievement
        if(finalscore >= 50)
            beginner_achiev1.setStatus(1);
        if(finalmultiplier >= 5)
            beginner_achiev2.setStatus(1);
        if(finallives >= 1)
            beginner_achiev3.setStatus(1);

        if(finalscore >= 100)
            novice_achiev1.setStatus(1);
        if(finalmultiplier >= 10)
            novice_achiev2.setStatus(1);
        if(finallives >= 2)
            novice_achiev3.setStatus(1);

        if(finalscore >= 250)
            intermediate_achiev1.setStatus(1);
        if(finalmultiplier >= 25)
            intermediate_achiev2.setStatus(1);
        if(finallives == 3)
            intermediate_achiev3.setStatus(1);

        if(finalscore >= 750)
            master_achiev1.setStatus(1);
        if(finalmultiplier >= 50)
            master_achiev2.setStatus(1);

        if(finalscore >= 1500)
            ultimate_achiev1.setStatus(1);
        if(finalmultiplier >= 100)
            ultimate_achiev2.setStatus(1);

        // save the status
        saveAchievements(achievements);
    }


    public ArrayList<Achievement> loadAchievements(String jsonstring){
            Gson gson = new Gson();
            ArrayList<Achievement> savedAchievementList = gson.fromJson(jsonstring, new TypeToken<ArrayList<ArrayList<String>>>() {}.getType());
            return savedAchievementList;
    }

    public void saveAchievements(ArrayList<Achievement> achievements) {
            Gson gson = new Gson();
            StringBuilder sb = new StringBuilder();
            for(Achievement a : achievements) {
                sb.append(gson.toJson(a));
            }

            System.out.println(sb.toString());
            String finaljson = sb.toString();

        SharedPreferences prefs = this.getSharedPreferences("storedachievements", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        edit.putString("jsonachievements", finaljson);
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
                TextView achievementprogress =(TextView) v.findViewById(R.id.status);
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
                    }
                    // otherwise just show the type of achievement as text
                    else {
                        achievementprogress.setText(achievement.type);
                        achievementicon.setImageDrawable(getResources().getDrawable(R.mipmap.icontest));
                    }
                }
            }
            return v;
        }
    }
}
