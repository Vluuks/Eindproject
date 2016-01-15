package nl.mprog.renske.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ShopActivity extends AppCompatActivity {

    private TextView coinsTextView;
    private int coins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);


        coinsTextView = (TextView) findViewById(R.id.cointsTextView);

        SharedPreferences prefs = this.getSharedPreferences("storedachievements", Context.MODE_PRIVATE);
        coins = prefs.getInt("coinsamount", 0);

        coinsTextView.setText("Coins: " + Integer.toString(coins));





    }
}
