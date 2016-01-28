// Renske Talsma, UvA 10896503, vluuks@gmail.com

package nl.mprog.renske.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Displays a set of rules and hints to help the user determine when to use "de" or "het".
 */
public class HintsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hints);
    }
}
