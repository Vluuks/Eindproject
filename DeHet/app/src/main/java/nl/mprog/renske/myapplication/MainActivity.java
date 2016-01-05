package nl.mprog.renske.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

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
    }

    public void displayHintScreen(View view) {
        // would like to avoid starting a new activity if possible
    }

    public void displayTranslation(View view) {
        // would like to avoid starting a new activity if possible
    }

    public void checkArticle(View view) {
        // would like to avoid starting a new activity if possible
    }


    // cannot be put in another class due to getassets not working?
    public Map<String, String> map = new HashMap<String, String>();
    public String dictValue, dictKey;


    public void loadDictionary()
            throws XmlPullParserException, IOException
    {
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

            switch (eventType){
                case XmlPullParser.START_TAG:
                    // if the tag's name is <k>
                    if(name.equals("k")){
                        if (xpp.next() == XmlPullParser.TEXT)
                            dictKey = xpp.getText();
                            System.out.println(dictKey);
                    }
                    break;

                case XmlPullParser.END_TAG:
                    break;
            }
            map.put(dictKey, dictValue);
            eventType = xpp.next();
        }
        System.out.println("End document");
        Log.d("TEST", "Blabla");
    }
}
