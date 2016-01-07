package nl.mprog.renske.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity  {

    public ArrayList<String> keylist;
    public TextView woordTextView, scoreTextView, livesTextView, multiplierTextView, timerTextView;
    public String lidwoord, znw, gameType, pickedWord, pickedWordTranslation;
    public int score, lives, multiplier, maxmultiplier, timervalue;
    private CountDownTimer gameTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // load the dictionary
        try {
            loadDictionary();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // initialize layout components
        scoreTextView = (TextView) findViewById(R.id.scoreTextview);
        livesTextView = (TextView) findViewById(R.id.livesTextview);
        multiplierTextView = (TextView) findViewById(R.id.multiplierTextview);
        timerTextView = (TextView) findViewById(R.id.timerTextview);

        initializeGame();
        pickWord();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // resume the timer where it left off
        if(gameType.equals("NORMAL"))
            initializeTimer(timervalue, 1000);
            System.out.println("RESUMED" + timervalue);

    }

    @Override
    protected void onPause() {
        super.onPause();
        // stop the timer and store the value
        if(gameType.equals("NORMAL") && !timerTextView.getText().equals("Time's up!")) {
            timervalue = Integer.valueOf(timerTextView.getText().toString()) * 1000;
            gameTimer.cancel();
            System.out.println("PAUSE" + timervalue);
        }
    }

    public void initializeGame()
    {
        SharedPreferences useroptions = getSharedPreferences("settings", this.MODE_PRIVATE);
        gameType = useroptions.getString("GAMETYPE", "NORMAL");
        score = 0;
        lives = 3;
        multiplier = 0;
        maxmultiplier = 0;

        if (gameType.equals("CHILL"))
            setChillMode();
        else
            setNormalMode();

    }

    public void setNormalMode(){
        livesTextView.setVisibility(View.VISIBLE);
        scoreTextView.setVisibility(View.VISIBLE);
        multiplierTextView.setVisibility(View.VISIBLE);
        timerTextView.setVisibility(View.VISIBLE);

        scoreTextView.setText(Integer.toString(score));
        livesTextView.setText("LIVES " + Integer.toString(lives));
        multiplierTextView.setText(" ");

        initializeTimer(120000, 1000);
    }

    public void initializeTimer(long time, long interval){
        gameTimer = new CountDownTimer(time, interval) {

            public void onTick(long millisUntilFinished) {
                timerTextView.setText(Long.toString(millisUntilFinished / 1000));
                System.out.println(millisUntilFinished);
                timervalue = millisUntilFinished; /// deze gebruiken ipv value van de textview is ws beter
            }

            public void onFinish() {
                timerTextView.setText("Time's up!");
                onWin();
            }
        }.start();
    }

    public void setChillMode(){
        lives = -1;
        livesTextView.setVisibility(View.INVISIBLE);
        scoreTextView.setVisibility(View.INVISIBLE);
        multiplierTextView.setVisibility(View.INVISIBLE);
        timerTextView.setVisibility(View.INVISIBLE);
    }

    public void startNewGame(View view) {

        if(gameType.equals("NORMAL"))
            gameTimer.cancel();

        initializeGame();
        pickWord();
    }

    public void displayHintScreen(View view) {
        Intent intent = new Intent(this, HintsActivity.class);
        startActivity(intent);
    }

    public void goToOptions(View view) {
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }

    public void displayTranslation(View view) {
        // to be done
    }

    public void displayAchievementScreen(View view) {
        Intent intent = new Intent(this, HighscoreActivity.class);
        startActivity(intent);
    }


    public void checkArticle(View view) {

        if (view.getId() == R.id.de_button) {
            if(lidwoord.equals("[de]")){
                keylist.remove(pickedWord);
                multiplier++;
                multiplierTextView.setText("COMBO x" + Integer.toString(multiplier));
            }

            else {
                keylist.add(pickedWord);
                lives--;
                multiplier = 0;
                multiplierTextView.setText(" ");
            }
        }
        else if (view.getId() == R.id.het_button) {
            if(lidwoord.equals("[het]")){
                keylist.remove(pickedWord);
                multiplier++;
                multiplierTextView.setText("COMBO x" + Integer.toString(multiplier));
            }

            else {
                keylist.add(pickedWord);
                lives--;
                multiplier = 0;
                multiplierTextView.setText(" ");
            }
        }

        //calculate score increase
        score = score + multiplier;

        //decide if this multiplier is bigger than he ones before
        if (multiplier > maxmultiplier)
            maxmultiplier = multiplier;

        // update layout elements
        scoreTextView.setText(Integer.toString(score));
        livesTextView.setText("LIVES " + Integer.toString(lives));

        // if the user has lives left or chill mode is active (which sets the start lives to -1)
        if(lives > 0 || lives < 0){
            pickWord();
        }
        // else go to achievement screen/game over screen
        else
            onLose();

    }


    public void sendScore(){
        Intent intent = new Intent(this, HighscoreActivity.class);

        // only send data if the mode is not chill
        if(gameType.equals("NORMAL")) {
            intent.putExtra("GAMETYPE", gameType);
            intent.putExtra("SCORE", score);
            intent.putExtra("LIVES", lives);
            intent.putExtra("MAXMULTIPLIER", maxmultiplier);
        }
        startActivity(intent);
    }


    public void onLose(){
        Toast toast = Toast.makeText(this, "NOOB", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();

        sendScore();
    }

    public void onWin(){

        if(gameType.equals("NORMAL"))
            gameTimer.cancel();

        Toast toast = Toast.makeText(this, "AWESOME", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();

        sendScore();
    }

    // cannot be put in another class due to getassets not working?
    public Map<String, String> dictionarymap = new HashMap<String, String>();
    public String dictValue, dictKey;

    public void loadDictionary()
            throws XmlPullParserException, IOException {
        // create the xmlpullparser and factory
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(false);
        XmlPullParser xpp = factory.newPullParser();

        // set the input stream
        Reader myReader = new InputStreamReader(getAssets().open("dictionary.xdxf.xml"), "UTF-8");
        xpp.setInput(myReader);

        // go over xml file
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {

            // get the name of the tag
            String name = xpp.getName();

            switch (eventType) {
                case XmlPullParser.START_TAG:
                    // if the tag's name is <k>
                    if (name.equals("k")) {
                        if (xpp.next() == XmlPullParser.TEXT)
                            dictKey = xpp.getText();
                        System.out.println(dictKey);
                    }
                    break;

                case XmlPullParser.END_TAG:
                    break;
            }
            dictionarymap.put(dictKey, dictValue);
            eventType = xpp.next();
        }
        System.out.println("Finished loading dictionary");

        // store keys in arraylist to facilitate random picking
        keylist = new ArrayList<String>();

        for (Map.Entry<String, String> entry : dictionarymap.entrySet()) {
            String theWord = entry.getKey();
            keylist.add(theWord);
        }
    }


    public void pickWord() {
        // pick a random key from the arraylist with keys

        //if the list is empty it means the player guessed all words correcly
        if (keylist.isEmpty())
            onWin();

            // get random item from list
        Random randomizer = new Random();
        pickedWord = keylist.get(randomizer.nextInt(keylist.size()));
        System.out.println("Gekozen woord raw:" + pickedWord);
        pickedWordTranslation = dictionarymap.get(pickedWord);

        // split keystring so that you have the article and the rest
        String[] wordparts = pickedWord.split(" ", 2);
        lidwoord = wordparts[0];
        String znwparts = wordparts[1];

        System.out.println("Bijbehorend lidwoord:" + lidwoord);
        System.out.println("Znw:" + znwparts);

        // if the dictionary entries comes with multiple nouns, only use the first
        if(znwparts.indexOf(';') != -1) {
            String[] znwpartsarray = znwparts.split(";", 2);
            znw = znwpartsarray[0];
        }

        else
            znw = znwparts;

        woordTextView =(TextView) findViewById(R.id.woordTextview);
        woordTextView.setText(znw);


        System.out.println(znw);

    }
}


