package nl.mprog.renske.myapplication;

import android.content.Context;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class Initialization
{
    public static Map<String, String> map = new HashMap<String, String>();
    public static String dictValue, dictKey;



    public void main (String args[])
            throws XmlPullParserException, IOException
    {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        xpp.setInput( this.getAssets().open("dictionary.xdxf.xml") );                               // this cannot be referenced from static context TODO
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = xpp.getName();
            switch (eventType){
                case XmlPullParser.START_TAG:
                    break;

                case XmlPullParser.END_TAG:
                    if(name.equals("ar")){
                        dictValue = xpp.getAttributeValue(null,"value");

                    }

                    if(name.equals("k")){
                        dictKey = xpp.getAttributeValue(null,"value");
                    }
                    break;
            map.put(dictKey, dictValue);
            }
            eventType = xpp.next();
        }
        System.out.println("End document");
    }
}
