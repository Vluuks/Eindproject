package nl.mprog.renske.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HighscoreActivity extends AppCompatActivity {

    public int finalscore, finalmultiplier, finallives;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        checkSource();

    }



    public void checkSource(){

        // if the user came from the game
        if(getIntent().getExtras()!=null) {
            Intent intent = getIntent();
            finalscore = intent.getExtras().getInt("SCORE");
            finalmultiplier = intent.getExtras().getInt("MULTIPLIER");
            finallives = intent.getExtras().getInt("LIVES");


            // check if they're eligible for an achievement

        }


        // if the user came from the button
        // just load cheeves from sharedprefs

    }


    public void loadAchievements(){

    }

    public void updateAchievements(){

    }
}
