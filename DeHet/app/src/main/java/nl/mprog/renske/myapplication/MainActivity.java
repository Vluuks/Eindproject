// Renske Talsma, UvA 10896503, vluuks@gmail.com

package nl.mprog.renske.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * MainActivity is where the actual game takes place. It contains all layout components necessary
 * for the game.
 */
public class MainActivity extends AppCompatActivity  {

    private GamePlay gameplay;
    private TextView woordTextView;
    private TextToSpeech textToSpeech;
    private static final int MY_DATA_CHECK_CODE = 1234;
    private boolean textToSpeechStatus, translationStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create Gameplay instance.
        gameplay = new GamePlay(MainActivity.this);
        gameplay.loadDictionary();

        // Prepare game components and mascot.
        initializeGameComponents();
        initializeBruin();

        // Check sharedpreferences for useroptions.
        checkSharedPreferences();
        checkForTTS();
    }

    /**
     * Gives gameplay class access to interface layout elements.
     */
    public void initializeGameComponents()
    {
        // Pass TextViews on to GamePlay.
        TextView translationTextView, scoreTextView, livesTextView, multiplierTextView,
                timerTextView, correctTextView, incorrectTextView;

        TextView[] textViewArray = new TextView[]{
            woordTextView = (TextView) findViewById(R.id.woordTextview),
            scoreTextView = (TextView) findViewById(R.id.scoreTextview),
            livesTextView = (TextView) findViewById(R.id.livesTextview),
            multiplierTextView = (TextView) findViewById(R.id.multiplierTextview),
            timerTextView = (TextView) findViewById(R.id.timerTextview),
            correctTextView = (TextView) findViewById(R.id.correctcounter),
            incorrectTextView = (TextView) findViewById(R.id.incorrectcounter),
            translationTextView = (TextView) findViewById(R.id.translationTextView)
        };

        ArrayList<TextView> textViewList = new ArrayList<TextView>();
        textViewList.addAll(Arrays.asList(textViewArray));
        gameplay.setTextViews(textViewList);

        // Pass buttons on to GamePlay.
        Button deButton, hetButton;

        Button[] buttonArray = new Button[]{
                deButton = (Button) findViewById(R.id.de_button),
                hetButton = (Button) findViewById(R.id.het_button)
        };

        ArrayList<Button> buttonList = new ArrayList<Button>();
        buttonList.addAll(Arrays.asList(buttonArray));
        gameplay.setButtons(buttonList);

        // Pass ImageViews on to GamePlay.
        ImageView finishedImageView;
        ImageView[] imageviewArray = new ImageView[]{
                finishedImageView = (ImageView) findViewById(R.id.finishedImageView)
        };

        ArrayList<ImageView> imageviewList = new ArrayList<ImageView>();
        imageviewList.addAll(Arrays.asList(imageviewArray));
        gameplay.setImageViews(imageviewList);

        // Pass layout on to GamePlay.
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.finishedLayout);
        gameplay.setLayout(frameLayout);

        // Pass user settings on to GamePlay.
        SharedPreferences useroptions = getSharedPreferences("settings", this.MODE_PRIVATE);
        gameplay.setSharedPreferences(useroptions);
        gameplay.initializeGame();
    }

    /**
     * Creates the game's mascot, Bruin.
     */
    public void initializeBruin(){

        Bruin bruin = new Bruin();

        // Set SharedPreferences for Bruin.
        SharedPreferences storedachievements =
                getSharedPreferences("storedachievements", this.MODE_PRIVATE);
        bruin.setSharedPreferences(storedachievements);

        // Pass ImageViews to Bruin.
        ImageView item1ImageView, item2ImageView, item3ImageView, item4ImageView, item5ImageView,
                item6ImageView, item7ImageView, item8ImageView, item9ImageView, item10ImageView;

        ImageView[] imageViewArray = new ImageView[]{
                item1ImageView = (ImageView) findViewById(R.id.scarfImageView),
                item2ImageView = (ImageView) findViewById(R.id.hatImageView),
                item3ImageView = (ImageView) findViewById(R.id.sockImageView),
                item4ImageView = (ImageView) findViewById(R.id.earmuffsImageView),
                item5ImageView = (ImageView) findViewById(R.id.flowerImageView),
                item6ImageView = (ImageView) findViewById(R.id.shadesImageView),
                item7ImageView = (ImageView) findViewById(R.id.tieImageView),
                item8ImageView = (ImageView) findViewById(R.id.haloImageView),
                item9ImageView = (ImageView) findViewById(R.id.wingsImageView),
                item10ImageView = (ImageView) findViewById(R.id.crownImageView),
        };

        ArrayList<ImageView> imageViewList = new ArrayList<ImageView>();
        imageViewList.addAll(Arrays.asList(imageViewArray));
        bruin.setItemImageViews(imageViewList);

        // Check which items are equipped and update ImageViews accordingly.
        bruin.checkEquipped();
    }

    /**
     * Obtains user's chosen options concerning TextToSpeech and translations.
     */
    public void checkSharedPreferences(){
        SharedPreferences useroptions = getSharedPreferences("settings", this.MODE_PRIVATE);
        textToSpeechStatus = useroptions.getBoolean("TTS", true);
        translationStatus = useroptions.getBoolean("TRANSLATION", true);
    }

    /**
     * Activity lifecycle methods.
     */
    @Override
    protected void onResume() {
        super.onResume();
        checkSharedPreferences();
        gameplay.resumeGame();
        initializeBruin();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameplay.pauseGame();
    }

    @Override
    public void onBackPressed()
    {
        finish();
        System.exit(0);
        super.onBackPressed();
    }

    /**
     * Layout redirecting methods.
     */
    public void displayHintScreen(View view) {
        Intent intent = new Intent(this, HintsActivity.class);
        startActivity(intent);
    }

    public void startNewGame(View view) {
        checkSharedPreferences();
        gameplay.startNewGame();
    }

    public void displayOptions(View view) {
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }

    public void displayShop(View view) {
        Intent intent = new Intent(this, ShopActivity.class);
        startActivity(intent);
    }

    public void onWordClick(View view){
        if(textToSpeechStatus) {
            String speech = woordTextView.getText().toString();
            textToSpeech.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
        }
        if (translationStatus)
            gameplay.showTranslation();

    }

    public void displayAchievementScreen(View view) {
        Intent intent = new Intent(this, AchievementActivity.class);
        startActivity(intent);
    }

    public void checkArticle(View view) {
        gameplay.checkArticle(view);
    }

    public void displayHelpScreen(View view) {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

    /**
     * TextToSpeech implementations.
     * http://www.jameselsey.co.uk/blogs/techblog/android-a-really-easy-tutorial-on-how-to-use-text-
     * to-speech-tts-and-how-you-can-enter-text-and-have-it-spoken/
     */
    public void checkForTTS(){
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
    }

    /**
     * Upon the result of the check, determine if TTS can run as is or is in need of more data.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == MY_DATA_CHECK_CODE)
        {
            // If TTS can be launched without problem.
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
                startTTS();

            // If not, it means there is missing TTS data, install it.
            else
            {
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }

    /**
     * Starts TextToSpeech functionality and attempt to set the language to Dutch.
     */
    public void startTTS() {
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {

            // Set locale/language on intialization.
            @Override
            public void onInit(int status) {
                Locale[] locales = Locale.getAvailableLocales();
                List<Locale> localeList = new ArrayList<Locale>();
                for (Locale locale : locales) {
                    int res = textToSpeech.isLanguageAvailable(locale);
                    if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                        localeList.add(locale);
                    }
                }
                if (status != TextToSpeech.ERROR) {
                    Locale loc = new Locale("nl", "NL");
                    textToSpeech.setLanguage(loc);
                }
            }
        });
    }
}