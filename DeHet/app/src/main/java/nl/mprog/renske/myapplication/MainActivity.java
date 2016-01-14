package nl.mprog.renske.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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

    public GamePlay gameplay;
    public TextView woordTextView, scoreTextView, livesTextView, multiplierTextView, timerTextView, correctTextView, incorrectTextView;
    public ImageView finishedImageView;
    public Button deButton, hetButton;
    private TextToSpeech t1;
    private static final int MY_DATA_CHECK_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameplay = new GamePlay(MainActivity.this);

        // load the dictionary
        try {
            gameplay.loadDictionary();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        initializeGameComponents();
        gameplay.initializeGame();


        // TTS check
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);

    }


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
            incorrectTextView = (TextView) findViewById(R.id.incorrectcounter)
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameplay.resumeGame();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameplay.pauseGame();
    }

    public void displayHintScreen(View view) {
        Intent intent = new Intent(this, HintsActivity.class);
        startActivity(intent);
    }

    public void startNewGame(View view) {
        gameplay.startNewGame();
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
        gameplay.checkArticle(view);
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


