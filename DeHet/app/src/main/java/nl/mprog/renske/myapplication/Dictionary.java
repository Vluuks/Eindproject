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
 * Created by Renske on 25-1-2016.
 */
public class Dictionary {

    private Map<String, String> dictionarymap = new HashMap<String, String>();
    private String dictValue, dictKey;
    private Context activityContext;
    private ArrayList<String> keylist;

    public Dictionary(Context context){
        activityContext = context;
    }

    public ArrayList<String> getKeyList(){

        // Store keys in arraylist to facilitate random picking.
        keylist = new ArrayList<String>();

        for (Map.Entry<String, String> entry : dictionarymap.entrySet()) {
            String theWord = entry.getKey();
            keylist.add(theWord);
        }
        return keylist;
    }

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

                    // If the tag's name is <k>.
                    if (name.equals("k")) {
                        if (xpp.next() == XmlPullParser.TEXT)
                            dictKey = xpp.getText();
                    }

                    // If the tag's name is <t>.
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
}
