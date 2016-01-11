package nl.mprog.renske.myapplication;

import android.content.Context;
import android.content.Intent;
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
import java.util.Arrays;

public class HighscoreActivity extends AppCompatActivity {

    public int finalscore, finalmultiplier, finallives;
    public Achievement beginner_achiev1, beginner_achiev2, beginner_achiev3, novice_achiev1,
            novice_achiev2, novice_achiev3, intermediate_achiev1, intermediate_achiev2,
            intermediate_achiev3, master_achiev1, master_achiev2, ultimate_achiev1, ultimate_achiev2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        ArrayList<Achievement> achievements = new ArrayList<Achievement>();

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

        achievements.addAll(Arrays.asList(achievementlist));
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new UserItemAdapter(this, android.R.layout.simple_list_item_1, achievements));

        checkSource();
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

            checkForAchievement();
            }

        //else
            // load from sharedpreferences
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
    }


    public void loadAchievements(){
        // obtain achievement arraylist from sharedpreferences

    }

    public void updateAchievements(){
        // put achievemenet arraylist in sharedpreferences

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

            Achievement achievement = achievements.get(position);
            if (achievement != null) {
                TextView achievementname = (TextView) v.findViewById(R.id.name);
                TextView achievementprogress =(TextView) v.findViewById(R.id.status);
                ImageView achievementicon = (ImageView) v.findViewById(R.id.icon);

                if (achievementname != null) {
                    achievementname.setText(achievement.name);
                }

                if(achievementprogress != null) {

                    if(achievement.status == 1){
                        achievementprogress.setText("Complete!");
                        achievementicon.setImageDrawable(getResources().getDrawable(R.mipmap.testicon2));
                    }
                    else
                        achievementprogress.setText(achievement.type);
                }
            }
            return v;
        }
    }
}
