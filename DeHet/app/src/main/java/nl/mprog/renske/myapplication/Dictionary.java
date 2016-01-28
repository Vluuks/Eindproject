// Renske Talsma, UvA 10896503, vluuks@gmail.com

package nl.mprog.renske.myapplication;

import android.content.Context;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Dictionary loads the dictionary from an XML file using XML parsing and convertis it to a map of
 * String, String format. After that a list of the keys of the map is constructed to facilitiate
 * pseudorandom picking of words.
 */
public class Dictionary {

    private Map<String, String> dictionarymap = new HashMap<String, String>();
    private String dictValue, dictKey;
    private Context activityContext;
    private ArrayList<String> keyList;

    /**
     * Constructor.
     */
    public Dictionary(Context context){
        activityContext = context;
    }

    /**
     * Obtain dictionary from XML file.
     */
    public Map<String, String> loadDictionaryFromXML()
            throws XmlPullParserException, IOException {

        // Create the xmlpullparser and factory.
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(false);
        XmlPullParser xpp = factory.newPullParser();

        // Set the input stream.
        Reader myReader = new InputStreamReader(activityContext.getAssets().open("dictionary2.xml"), "UTF-8");
        xpp.setInput(myReader);

        // Loop over the XML file.
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {

            // Obtain the tag's name.
            String name = xpp.getName();

            switch (eventType) {
                case XmlPullParser.START_TAG:

                    // Obtain the word/article combo.
                    if (name.equals("k")) {
                        if (xpp.next() == XmlPullParser.TEXT)
                            dictKey = xpp.getText();
                    }

                    // Obtain the translation.
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
        return dictionarymap;
    }

    /**
     * Obtain list of dictionary's keys which consists of all Dutch words.
     */
    public ArrayList<String> getKeyList(){

        // Store keys in arraylist to facilitate random picking.
        keyList = new ArrayList<String>();

        for (Map.Entry<String, String> entry : dictionarymap.entrySet()) {
            String theWord = entry.getKey();
            keyList.add(theWord);
        }
        return keyList;
    }
}
