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
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity  {

    private GamePlay gameplay;
    private TextView woordTextView, translationTextView, scoreTextView, livesTextView, multiplierTextView, timerTextView, correctTextView, incorrectTextView;
    private ImageView item1ImageView, item2ImageView, item3ImageView, item4ImageView, item5ImageView,
            item6ImageView, item7ImageView, item8ImageView, item9ImageView, item10ImageView;
    public ImageView finishedImageView;
    public Button deButton, hetButton;
    private TextToSpeech t1;
    private static final int MY_DATA_CHECK_CODE = 1234;
    private boolean textToSpeechStatus, translationStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameplay = new GamePlay(MainActivity.this);

        // Load the dictionary.
        try {
            gameplay.loadDictionary();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Prepare game components and mascot.
        initializeGameComponents();
        initializeBruin();

        // Check sharedpreferences for useroptions.
        checkSharedPreferences();
        checkForTTS();
    }

    /**
     * Gives gameplay class access to interface elements.
     */
    public void initializeGameComponents()
    {
        // initialize layout components
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


        Button[] buttonArray = new Button[]{
                deButton = (Button) findViewById(R.id.de_button),
                hetButton = (Button) findViewById(R.id.het_button)
        };

        ArrayList<Button> buttonList = new ArrayList<Button>();
        buttonList.addAll(Arrays.asList(buttonArray));


        ImageView[] imageviewArray = new ImageView[]{
                finishedImageView = (ImageView) findViewById(R.id.finishedImageView)
        };

        ArrayList<ImageView> imageviewList = new ArrayList<ImageView>();
        imageviewList.addAll(Arrays.asList(imageviewArray));

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.finishedLayout);

        gameplay.setTextViews(textViewList);
        gameplay.setButtons(buttonList);
        gameplay.setImageViews(imageviewList);
        gameplay.setLayout(frameLayout);

        SharedPreferences useroptions = getSharedPreferences("settings", this.MODE_PRIVATE);
        gameplay.setSharedPreferences(useroptions);
        gameplay.initializeGame();

    }

    /**
     * Creates the game's mascot, Bruin.
     */
    public void initializeBruin(){

        Bruin bruin = new Bruin();

        // set sharedpreferences
        SharedPreferences storedachievements = getSharedPreferences("storedachievements", this.MODE_PRIVATE);
        bruin.setSharedPreferences(storedachievements);

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
        bruin.checkEquipped();
    }

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

    /**
     * Interface redirecting methods.
     */
    public void displayHintScreen(View view) {
        Intent intent = new Intent(this, HintsActivity.class);
        startActivity(intent);
    }

    public void startNewGame(View view) {
        checkSharedPreferences();
        gameplay.startNewGame();
    }

    public void goToOptions(View view) {
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }

    public void goToShop(View view) {
        Intent intent = new Intent(this, ShopActivity.class);
        startActivity(intent);
    }

    public void onWordClick(View view){

        if(textToSpeechStatus) {
            String speech = woordTextView.getText().toString();
            t1.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
        }
        if (translationStatus){
            gameplay.showTranslation();
        }
    }

    public void displayAchievementScreen(View view) {
        Intent intent = new Intent(this, HighscoreActivity.class);
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