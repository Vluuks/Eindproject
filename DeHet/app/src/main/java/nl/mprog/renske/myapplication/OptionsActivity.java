package nl.mprog.renske.myapplication;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class OptionsActivity extends AppCompatActivity {

    private Switch gamemodeSwitch;
    public String chosenGametype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        gamemodeSwitch = (Switch) findViewById(R.id.switch1);
        loadOptions();

        if (gamemodeSwitch.isChecked()){
            gamemodeSwitch.setText("ON");
        }
        else
            gamemodeSwitch.setText("OFF");

        gamemodeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    chosenGametype = "CHILL";
                    gamemodeSwitch.setText("ON");
                } else {
                    chosenGametype = "NORMAL";
                    gamemodeSwitch.setText("OFF");
                }
                saveOptions();
            }
        });

    }

    public void saveOptions(){
        SharedPreferences settings = this.getSharedPreferences("settings",
                this.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("GAMETYPE", chosenGametype);
        editor.commit();

        System.out.println("CHOSEN GAME MODE: " + chosenGametype);
    }


    public void loadOptions(){

        SharedPreferences useroptions = getSharedPreferences("settings", this.MODE_PRIVATE);
        chosenGametype = useroptions.getString("GAMETYPE", "NORMAL");

        if (chosenGametype.equals("NORMAL"))
            gamemodeSwitch.setChecked(false);
        else
            gamemodeSwitch.setChecked(true);
    }
}
