package nl.mprog.renske.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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
    public TextView woordTextView, scoreTextView, livesTextView, multiplierTextView;
    public String lidwoord, znw;
    public Button de_button, het_button;
    public int score, lives, multiplier;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            loadDictionary();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        scoreTextView = (TextView) findViewById(R.id.scoreTextview);
        livesTextView = (TextView) findViewById(R.id.livesTextview);
        multiplierTextView = (TextView) findViewById(R.id.multiplierTextview);

        initializeGame();


        pickWord();



    }

    public void initializeGame()
    {
        score = 0;
        lives = 3;
        multiplier = 0;

        scoreTextView.setText(Integer.toString(score));
        livesTextView.setText("LIVES " + Integer.toString(lives));
        multiplierTextView.setText(" ");
    }
    public void displayHintScreen(View view) {
        // would like to avoid starting a new activity if possible
    }

    public void displayTranslation(View view) {
        // would like to avoid starting a new activity if possible
    }

    public void checkArticle(View view) {

        if (view.getId() == R.id.de_button) {
            if(lidwoord.equals("[de]")){
                multiplier++;
                multiplierTextView.setText("COMBO x" + Integer.toString(multiplier));
            }

            else {
                lives--;
                multiplier = 0;
                multiplierTextView.setText(" ");
            }
        }
        else if (view.getId() == R.id.het_button) {
            if(lidwoord.equals("[het]")){
                multiplier++;
                multiplierTextView.setText("COMBO x" + Integer.toString(multiplier));
            }

            else {
                lives--;
                multiplier = 0;
                multiplierTextView.setText(" ");
            }
        }

        //calculate score increase
        score = score + multiplier;

        // update layout elements
        scoreTextView.setText(Integer.toString(score));
        livesTextView.setText("LIVES " + Integer.toString(lives));



        if(lives > 0){
            pickWord();
        }
        // else go to achievement screen/game over screen
        else {
            Toast toast = Toast.makeText(this, "HAHAHA GIT GUD MATE.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

            Intent intent = new Intent(this, HighscoreActivity.class);
            startActivity(intent);
        }

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
        Random randomizer = new Random();
        String pickedWord = keylist.get(randomizer.nextInt(keylist.size()));
        System.out.println("Gekozen woord raw:" + pickedWord);
        String pickedWordTranslation = dictionarymap.get(pickedWord);

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
