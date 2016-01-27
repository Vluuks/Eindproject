package nl.mprog.renske.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Renske on 7-1-2016.
 */
public class GamePlay implements TimerHandler {

    private Context activityContext;
    private TextView woordTextView, scoreTextView, livesTextView, multiplierTextView, timerTextView, correctTextView, incorrectTextView, translationTextView;
    public ImageView finishedImageView;
    private Button deButton, hetButton;
    private String lidwoord, znw, gameType, pickedWord, pickedWordTranslation, gameVersion;
    private int score, lives, multiplier, maxMultiplier, correctCount, incorrectCount, storedTimerValue;
    private ArrayList<String> keylist;
    private Stopwatch gameTimer;
    private boolean gameStatus;
    private SharedPreferences userOptions;
    private Map<String, String> dictionaryMap = new HashMap<String, String>();
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

    /**
     * Obtains framelayout from MainActivity.
     */
    public void setLayout(FrameLayout framefinishedLayout){
        finishedLayout = framefinishedLayout;
    }

    /**
     * Loads the dicitionary from an XDXF XML file.
     */
    public void loadDictionary() {

        // Load the dictionary.
        Dictionary dictionary = new Dictionary(activityContext);
        try {
            dictionaryMap = dictionary.loadDictionaryFromXML();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        keylist = dictionary.getKeyList();
    }

    /**
     * Gets SharedPreferences from MainActivity.
     */
    public void setSharedPreferences(SharedPreferences pickedOptions){
        userOptions = pickedOptions;
    }

    /**
     * Initializes the values and elements used in the game.
     */
    public void initializeGame()
    {
        // Set values.
        gameStatus = true;
        gameType = userOptions.getString("GAMETYPE", "NORMAL");
        gameVersion = userOptions.getString("GAMEVERSION", "DEHET");
        score = 0;
        lives = 3;
        multiplier = 0;
        maxMultiplier = -1;
        correctCount = 0;
        incorrectCount = 0;

        System.out.println(gameVersion);

        // Set views accordingly.
        finishedLayout.setVisibility(View.GONE);
        finishedImageView.setVisibility(View.GONE);
        woordTextView.setVisibility(View.VISIBLE);
        correctTextView.setVisibility(View.VISIBLE);
        incorrectTextView.setVisibility(View.VISIBLE);
        correctTextView.setText(R.string.standardtextright);
        incorrectTextView.setText(R.string.standardtextwrong);
        woordTextView.setText(R.string.standardtextword);

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
        livesTextView.setText(activityContext.getString(R.string.lives) + Integer.toString(lives));
        multiplierTextView.setText(" ");
        initializeTimer(180);
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
        deButton.setText(R.string.thesethosethatthis1);
        hetButton.setText(R.string.thesethosethatthis2);
    }

    /**
     * Sets the game mode to de/het basic mode.
     */
    private void setArticleMode(){
        deButton.setText(R.string.articlebuttonde);
        hetButton.setText(R.string.articlebuttonhet);
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
        Intent intent = new Intent(activityContext, AchievementActivity.class);
        if(gameType.equals("NORMAL")) {
            intent.putExtra("GAMETYPE", gameType);
            intent.putExtra("SCORE", score);
            intent.putExtra("LIVES", lives);
            intent.putExtra("MAXMULTIPLIER", maxMultiplier);
            intent.putExtra("CORRECTCOUNT", correctCount);
            intent.putExtra("TIMERSECONDS", Integer.parseInt(timerTextView.getText().toString()));
        }

        activityContext.startActivity(intent);
    }

    /**
     * Initializes the game's timer.
     */
    private void initializeTimer(int chosentime){
        gameTimer = new Stopwatch(chosentime, timerTextView, this);
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

        while(pickedWord == null) {
            pickedWord = keylist.get(randomizer.nextInt(keylist.size()));
        }

        pickedWordTranslation = dictionaryMap.get(pickedWord);

        // Split keystring so that you have the article and the noun.
        String[] wordparts = pickedWord.split(" ", 2);
        lidwoord = wordparts[0];
        String znwparts = wordparts[1];

        // If the dictionary entries comes with multiple nouns, only use the first.
        if(znwparts.indexOf(';') != -1) {
            String[] znwpartsarray = znwparts.split(";", 2);
            znw = znwpartsarray[0].trim();
        }

        else
            znw = znwparts.trim();

        woordTextView.setText(znw);

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

        // Calculate score increase and update layout elements.
        score = score + multiplier;
        scoreTextView.setText(Integer.toString(score));
        livesTextView.setText(activityContext.getString(R.string.levens) + Integer.toString(lives));

        // Check whether the game should continue or stop.
        if(lives > 0 || lives < 0){
            pickWord();
        }
        if(lives == 0)
            onLose();
    }

    /**
     * To be executed when the guess is correct.
     */
    public void ifCorrect(){
        keylist.remove(pickedWord);
        correctCount++;
        correctTextView.setText(Integer.toString(correctCount)
                + activityContext.getString(R.string.correctguesses));

        if(gameType.equals("NORMAL")) {
            multiplier++;
            multiplierTextView.setText(activityContext.getString(R.string.combo) + Integer.toString(multiplier));

            // Check whether the multiplier has increased.
            if (multiplier > maxMultiplier)
                maxMultiplier = multiplier;
        }
    }

    /**
     * To be executed when the guess is incorrect.
     */
    public void ifIncorrect(){
        keylist.add(pickedWord);
        incorrectCount++;
        incorrectTextView.setText(activityContext.getString(R.string.incorrectguesses)
                + Integer.toString(incorrectCount));

        if(gameType.equals("NORMAL")) {
            lives--;
            multiplier = 0;
            multiplierTextView.setText(" ");
        }
    }

    /**
     * Methods handling the game's end.
     */
    @Override
    public void onTimerFinish() {
        if(gameType.equals("NORMAL") && correctCount > 0)
            onWin();
        else
            initializeGame();
    }

    private void onLose(){
        finishedImageView.setImageResource(R.drawable.onlosebruin);
        stopGame();
    }

    private void onWin(){
        if(gameType.equals("NORMAL"))
            gameTimer.cancelTimer();

        finishedImageView.setImageResource(R.drawable.onwinbruin);
        stopGame();
    }

    /**
     * Methods managing the game's lifecycle.
     */
    public void pauseGame(){
        if(gameType.equals("NORMAL") && !timerTextView.getText().equals("0")) {
            storedTimerValue = gameTimer.pauseTimer();
        }
    }

    public void resumeGame(){
        if(gameStatus != false) {
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
        }
        initializeGame();
    }

    /**
     * Translation.
     */
    public void showTranslation(){
        pickedWordTranslation = dictionaryMap.get(pickedWord);
        translationTextView.setText(pickedWordTranslation);
    }
}