package nl.mprog.renske.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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

/**
 * Created by Renske on 7-1-2016.
 */
public class GamePlay {

    private Context activityContext;
    private TextView woordTextView, scoreTextView, livesTextView, multiplierTextView, timerTextView, correctTextView, incorrectTextView, translationTextView;
    public ImageView finishedImageView;
    private Button deButton, hetButton;
    private String lidwoord, znw, gameType, pickedWord, pickedWordTranslation, gameVersion;
    private int score, lives, multiplier, maxmultiplier, correctcount, incorrectcount, storedtimervalue;
    private ArrayList<String> keylist;
    private Stopwatch gameTimer;
    private boolean gameStatus;
    private SharedPreferences useroptions;
    private Map<String, String> dictionarymap = new HashMap<String, String>();
    private String dictValue, dictKey;
    private FrameLayout finishedLayout;


    /**
     * Obtains context from MainActivity layout.
     */
    public GamePlay (Context context){
        this.activityContext = context;
    }

    /**
     * Obtains TextViews from MainActivity layout.
     */
    public void setTextViews(ArrayList<TextView> textViewList){

        this.woordTextView = textViewList.get(0);
        this.scoreTextView = textViewList.get(1);
        this.livesTextView = textViewList.get(2);
        this.multiplierTextView = textViewList.get(3);
        this.timerTextView = textViewList.get(4);
        this.correctTextView = textViewList.get(5);
        this.incorrectTextView = textViewList.get(6);
        this.translationTextView = textViewList.get(7);
    }

    /**
     * Obtains ImageViews from MainActivity layout.
     */
    public void setImageViews(ArrayList<ImageView> imageViewList) {
        this.finishedImageView = imageViewList.get(0);
    }

    /**
     * Obtains buttons from MainActivity layout.
     */
    public void setButtons(ArrayList<Button> buttonList){
        this.deButton = buttonList.get(0);
        this.hetButton = buttonList.get(1);
    }


    public void setLayout(FrameLayout framefinishedLayout){
        finishedLayout = framefinishedLayout;
    }

    /**
     * Loads the dicitionary from an XDXF XML file.
     */
    public void loadDictionary()
            throws XmlPullParserException, IOException {
        // create the xmlpullparser and factory
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(false);
        XmlPullParser xpp = factory.newPullParser();

        // set the input stream
        Reader myReader = new InputStreamReader(activityContext.getAssets().open("dictionary2.xml"), "UTF-8");
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
                    if (name.equals("t")) {
                        if (xpp.next() == XmlPullParser.TEXT)
                            dictValue = xpp.getText();
                    }
                    System.out.println(dictValue);
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

    /**
     * Gets SharedPreferences from MainActivity.
     */
    public void setSharedPreferences(SharedPreferences pickedOptions){
        useroptions = pickedOptions;
    }


    /**
     * Initializes the values and elements used in the game.
     */
    public void initializeGame()
    {
        // Set values.
        gameStatus = true;
        gameType = useroptions.getString("GAMETYPE", "NORMAL");
        gameVersion = useroptions.getString("GAMEVERSION", "DEHET");
        score = 0;
        lives = 3;
        multiplier = 0;
        maxmultiplier = -1;
        correctcount = 0;
        incorrectcount = 0;

        System.out.println(gameVersion);

        // Set views accordingly.
        finishedLayout.setVisibility(View.GONE);
        finishedImageView.setVisibility(View.GONE);
        woordTextView.setVisibility(View.VISIBLE);
        correctTextView.setVisibility(View.VISIBLE);
        incorrectTextView.setVisibility(View.VISIBLE);
        correctTextView.setText("0 RIGHT");
        incorrectTextView.setText("WRONG 0");
        woordTextView.setText(" ");

        // Set the right game modes.
        if (gameVersion.equals("DEMPRO"))
            setDemonstrativePronounMode();
        else
            setArticleMode();

        if (gameType.equals("CHILL"))
            setChillMode();
        else
            setNormalMode();

        // Start the game by picking a word.
        pickWord();
    }

    /**
     * Sets the game mode to normal. This includes score, lives and a tinmer.
     */
    private void setNormalMode(){

        livesTextView.setVisibility(View.VISIBLE);
        scoreTextView.setVisibility(View.VISIBLE);
        multiplierTextView.setVisibility(View.VISIBLE);
        timerTextView.setVisibility(View.VISIBLE);

        scoreTextView.setText(Integer.toString(score));
        livesTextView.setText("LIVES " + Integer.toString(lives));
        multiplierTextView.setText(" ");

        initializeTimer(181);
        System.out.println("NEW TIMER STARTED");
    }

    /**
     * Sets the game mode to chill, this removes the timer, lives and score.
     */
    private void setChillMode(){
        lives = -1;
        livesTextView.setVisibility(View.INVISIBLE);
        scoreTextView.setVisibility(View.INVISIBLE);
        multiplierTextView.setVisibility(View.INVISIBLE);
        timerTextView.setVisibility(View.INVISIBLE);
    }


    /**
     * Sets the game mode to deze/die/dit/dat instead of de/het.
     */
    private void setDemonstrativePronounMode(){
        deButton.setText("DEZE / DIE");
        hetButton.setText("DIT / DAT");
    }

    /**
     * Sets the game mode to de/het basic mode.
     */
    private void setArticleMode(){
        deButton.setText("DE");
        hetButton.setText("HET");
    }


    /**
     * Stops the game and redirects user to achievement panel.
     */
    private void stopGame(){
        gameStatus = false;
        finishedLayout.setVisibility(View.VISIBLE);
        finishedImageView.setVisibility(View.VISIBLE);

        // Hide all game elements.
        livesTextView.setVisibility(View.INVISIBLE);
        scoreTextView.setVisibility(View.INVISIBLE);
        multiplierTextView.setVisibility(View.INVISIBLE);
        timerTextView.setVisibility(View.INVISIBLE);
        woordTextView.setVisibility(View.INVISIBLE);
        correctTextView.setVisibility(View.INVISIBLE);
        incorrectTextView.setVisibility(View.INVISIBLE);

        // Collect data for highscores/achievements.
        Intent intent = new Intent(activityContext, HighscoreActivity.class);
        if(gameType.equals("NORMAL")) {
            intent.putExtra("GAMETYPE", gameType);
            intent.putExtra("SCORE", score);
            intent.putExtra("LIVES", lives);
            intent.putExtra("MAXMULTIPLIER", maxmultiplier);
            intent.putExtra("CORRECTCOUNT", correctcount);
            intent.putExtra("TIMERSECONDS", Integer.parseInt(timerTextView.getText().toString()));
        }

        activityContext.startActivity(intent);
    }



    /**
     * Initializes the game's timer.
     */
    private void initializeTimer(int chosentime){
        gameTimer = new Stopwatch(chosentime, timerTextView);
        gameTimer.timerstatus = true;
    }

    /**
     * Pick a word from the keylist and dictionary.
     */
    public void pickWord() {

        translationTextView.setText(" ");

        if (keylist.isEmpty())
            onWin();

        // Obtain random word from keylist.
        Random randomizer = new Random();
        pickedWord = keylist.get(randomizer.nextInt(keylist.size()));
        System.out.println("Gekozen woord raw:" + pickedWord);

        while(pickedWord == null) {
            pickedWord = keylist.get(randomizer.nextInt(keylist.size()));
            System.out.println("Gekozen woord raw:" + pickedWord);
        }

        pickedWordTranslation = dictionarymap.get(pickedWord);

        // Split keystring so that you have the article and the noun.
        String[] wordparts = pickedWord.split(" ", 2);
        lidwoord = wordparts[0];
        String znwparts = wordparts[1];

        System.out.println("Bijbehorend lidwoord:" + lidwoord);
        System.out.println("Znw:" + znwparts);

        // If the dictionary entries comes with multiple nouns, only use the first.
        if(znwparts.indexOf(';') != -1) {
            String[] znwpartsarray = znwparts.split(";", 2);
            znw = znwpartsarray[0].trim();
        }

        else
            znw = znwparts.trim();

        woordTextView.setText(znw);


        System.out.println(znw);

    }

    /**
     * Checks if the article the user chose corresponds to the word's actual article.
     */
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

        if(gameType.equals("NORMAL") && timerTextView.getText().equals("0") && correctcount > 0)
            onWin();

        // else go to achievement screen/game over screen
        if(lives == 0)
            onLose();
    }


    /**
     * To be executed when the guess is correct.
     */
    public void ifCorrect(){

        keylist.remove(pickedWord);
        correctcount++;
        correctTextView.setText(Integer.toString(correctcount) + " RIGHT");

        if(gameType.equals("NORMAL")) {
            multiplier++;
            multiplierTextView.setText("COMBO x" + Integer.toString(multiplier));

            //decide if this multiplier is bigger than he ones before
            if (multiplier > maxmultiplier)
                maxmultiplier = multiplier;

        }
        else {
            Toast toast = Toast.makeText(activityContext, "CORRECT!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }

    /**
     * To be executed when the guess is incorrect.
     */
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
            Toast toast = Toast.makeText(activityContext, "INCORRECT!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }



    private void onLose(){

        finishedImageView.setImageResource(R.drawable.onlosebruin);

        Toast toast = Toast.makeText(activityContext, "NOOB", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();

        stopGame();
    }

    private void onWin(){

        if(gameType.equals("NORMAL"))
            gameTimer.cancelTimer();

        finishedImageView.setImageResource(R.drawable.onwinbruin);
        stopGame();
    }

    public void pauseGame(){

        if(gameType.equals("NORMAL") && !timerTextView.getText().equals("0")) {
            storedtimervalue = gameTimer.pauseTimer();
            System.out.println("PAUSED" + storedtimervalue);

        }

    }


    public void resumeGame(){
        if(gameStatus != false) {
            //resume the timer where it left off
            if (gameType.equals("NORMAL"))
                gameTimer.resumeTimer();

        }
        else
            initializeGame();

    }

    public void startNewGame(){
        if(gameType.equals("NORMAL")) {
            gameTimer.cancelTimer();
            timerTextView.setText(" ");
            System.out.println("TIMER CANCELLED: NEW GAME INITIATED");
        }

        initializeGame();
    }


    public void showTranslation(){
        pickedWordTranslation = dictionarymap.get(pickedWord);
        translationTextView.setText(pickedWordTranslation);
    }
}
