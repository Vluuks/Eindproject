package nl.mprog.renske.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity  {

    public ArrayList<String> keylist;
    public TextView woordTextView, scoreTextView, livesTextView, multiplierTextView, timerTextView, correctTextView, incorrectTextView;
    public String lidwoord, znw, gameType, pickedWord, pickedWordTranslation;
    public int score, lives, multiplier, maxmultiplier, correctcount, incorrectcount;
    private long timervalue, storedtimervalue;
    private CountDownTimer gameTimer;
    private boolean gamestatus, timerstatus;
    private TextToSpeech t1;
    private static final int MY_DATA_CHECK_CODE = 1234;

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
        //wait a bit so that the dictionary has time to load
        try {
            Thread.sleep(1000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }


        initializeGame();

        // TTS check
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);

    }


    public void initializeGame()
    {
        // initialize layout components
        scoreTextView = (TextView) findViewById(R.id.scoreTextview);
        livesTextView = (TextView) findViewById(R.id.livesTextview);
        multiplierTextView = (TextView) findViewById(R.id.multiplierTextview);
        timerTextView = (TextView) findViewById(R.id.timerTextview);
        woordTextView = (TextView) findViewById(R.id.woordTextview);
        correctTextView = (TextView) findViewById(R.id.correctcounter);
        incorrectTextView = (TextView) findViewById(R.id.incorrectcounter);

        gamestatus = true;
        SharedPreferences useroptions = getSharedPreferences("settings", this.MODE_PRIVATE);
        gameType = useroptions.getString("GAMETYPE", "NORMAL");
        score = 0;
        lives = 3;
        multiplier = 0;
        maxmultiplier = -1;
        correctcount = 0;
        incorrectcount = 0;

        woordTextView.setVisibility(View.VISIBLE);
        correctTextView.setVisibility(View.VISIBLE);
        incorrectTextView.setVisibility(View.VISIBLE);
        correctTextView.setText("RIGHT 0");
        incorrectTextView.setText("WRONG 0");
        woordTextView.setText(" ");

        if (gameType.equals("CHILL"))
            setChillMode();
        else
            setNormalMode();

        pickWord();
    }





    // source: http://www.jameselsey.co.uk/blogs/techblog/android-a-really-easy-tutorial-on-how-to-use-text-to-speech-tts-and-how-you-can-enter-text-and-have-it-spoken/
    public void startTTS() {
        // create new TTS
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                Locale[] locales = Locale.getAvailableLocales();
                List<Locale> localeList = new ArrayList<Locale>();
                for (Locale locale : locales) {
                    int res = t1.isLanguageAvailable(locale);
                    if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                        localeList.add(locale);
                        System.out.println(locale);
                    }
                }


                if (status != TextToSpeech.ERROR) {
                    Locale loc = new Locale("fr", "FR");
                    int result = t1.setLanguage(loc);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {

                        System.out.println("LANGUAGE NOT SUPPORTED");


                    }
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        timerstatus = true;

        if(gamestatus != false) {
            //resume the timer where it left off
            if (gameType.equals("NORMAL"))
                gameTimer.cancel();
            initializeTimer(storedtimervalue, 1000);
            System.out.println("RESUMED" + storedtimervalue);
        }
        else
            initializeGame();


    }

    @Override
    protected void onPause() {
        super.onPause();
        // stop the timer and store the value
        if(gameType.equals("NORMAL") && !timerTextView.getText().equals("Time's up!")) {
            storedtimervalue = timervalue;
            gameTimer.cancel();
            System.out.println("PAUSE" + timervalue);
            timerstatus = false;

        }
    }

    @Override
    public void onDestroy()
    {
        // Don't forget to shutdown!
        if (t1 != null)
        {
            t1.stop();
            t1.shutdown();
        }
        super.onDestroy();
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
        System.out.println("NEW TIMER STARTED");
    }

    public void stopGame(){

        // set game status to false so back button in achievementactivity works properly
        gamestatus = false;

        // hide all game elements
        livesTextView.setVisibility(View.INVISIBLE);
        scoreTextView.setVisibility(View.INVISIBLE);
        multiplierTextView.setVisibility(View.INVISIBLE);
        timerTextView.setVisibility(View.INVISIBLE);
        woordTextView.setVisibility(View.INVISIBLE);
        correctTextView.setVisibility(View.INVISIBLE);
        incorrectTextView.setVisibility(View.INVISIBLE);


        //LinearLayout layout =(LinearLayout)findViewById(R.id.mainlayout);
        //display appropriate message
       // if(lives == 0)
            //layout.setBackgroundResource(R.drawable.testplaatjeonlose);
       // else
            //layout.setBackgroundResource(R.drawable.testplaatjeonwin);



        // collect data for highscores/achievements
        Intent intent = new Intent(this, HighscoreActivity.class);
        if(gameType.equals("NORMAL")) {
            intent.putExtra("GAMETYPE", gameType);
            intent.putExtra("SCORE", score);
            intent.putExtra("LIVES", lives);
            intent.putExtra("MAXMULTIPLIER", maxmultiplier);
        }
        startActivity(intent);

    }

    public void initializeTimer(long time, long interval){
        gameTimer = new CountDownTimer(time, interval) {

            public void onTick(long millisUntilFinished) {

                if(gamestatus != false) {
                    timerTextView.setText(Long.toString(millisUntilFinished / 1000));
                    System.out.println(millisUntilFinished);
                    timervalue = millisUntilFinished;
                }
                else
                    System.out.println("PAUSED");
            }

            public void onFinish() {
                timerTextView.setText("Time's up!");

                // if user actually played, trigger onwin, else just start a new game
                if(score > 0)
                    onWin();
                else
                    initializeGame();
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

        if(gameType.equals("NORMAL")) {
            gameTimer.cancel();
            System.out.println("TIMER CANCELLED: NEW GAME INITIATED");
        }

        initializeGame();
    }

    public void displayHintScreen(View view) {
        Intent intent = new Intent(this, HintsActivity.class);
        startActivity(intent);
    }

    public void goToOptions(View view) {
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }

    public void onWordClick(View view){
        String speech = woordTextView.getText().toString();
        t1.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void displayAchievementScreen(View view) {
        Intent intent = new Intent(this, HighscoreActivity.class);
        startActivity(intent);
    }


    public void checkArticle(View view) {

        if (view.getId() == R.id.de_button) {
            if (lidwoord.equals("[de]")) {
                ifCorrect();
            } else {
                ifIncorrect();
            }
        } else if (view.getId() == R.id.het_button) {
            if (lidwoord.equals("[het]")) {
                ifCorrect();
            } else {
                ifIncorrect();
            }
        }

        //calculate score increase
        score = score + multiplier;

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



    public void ifCorrect(){

        keylist.remove(pickedWord);
        correctcount++;
        correctTextView.setText("RIGHT " + Integer.toString(correctcount));

        if(gameType.equals("NORMAL")) {
            multiplier++;
            multiplierTextView.setText("COMBO x" + Integer.toString(multiplier));

            //decide if this multiplier is bigger than he ones before
            if (multiplier > maxmultiplier)
                maxmultiplier = multiplier;

        }
        else {
            Toast toast = Toast.makeText(this, "CORRECT!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }


    public void ifIncorrect(){
        keylist.add(pickedWord);
        incorrectcount++;
        incorrectTextView.setText("WRONG " + Integer.toString(incorrectcount));

        if(gameType.equals("NORMAL")) {
            lives--;
            multiplier = 0;
            multiplierTextView.setText(" ");
        }
        else{
            Toast toast = Toast.makeText(this, "INCORRECT!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }




    public void onLose(){
        Toast toast = Toast.makeText(this, "NOOB", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();

        stopGame();
    }

    public void onWin(){

        if(gameType.equals("NORMAL"))
            gameTimer.cancel();

        Toast toast = Toast.makeText(this, "AWESOME", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();

        stopGame();
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

        while(pickedWord == null) {
            pickedWord = keylist.get(randomizer.nextInt(keylist.size()));
            System.out.println("Gekozen woord raw:" + pickedWord);
        }

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
            znw = znwpartsarray[0].trim();
        }

        else
            znw = znwparts.trim();

        woordTextView =(TextView) findViewById(R.id.woordTextview);
        woordTextView.setText(znw);


        System.out.println(znw);

    }

    /**
     * This is the callback from the TTS engine check, if a TTS is installed we
     * create a new TTS instance (which in turn calls onInit), if not then we will
     * create an intent to go off and install a TTS engine
     * @param requestCode int Request code returned from the check for TTS engine.
     * @param resultCode int Result code returned from the check for TTS engine.
     * @param data Intent Intent returned from the TTS check.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == MY_DATA_CHECK_CODE)
        {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
            {
                startTTS();
            }
            else
            {
                // missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(
                        TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }



}


