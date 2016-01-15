package nl.mprog.renske.myapplication;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

public class OptionsActivity extends AppCompatActivity {

    private Switch gamemodeSwitch;
    public String chosenGametype, chosenGameversion;
    private RadioGroup radioGroup;
    private RadioButton dehetRadioButton, dezedieditdatRadioButton;
    private CheckBox resetCheckBox;
    private boolean resetvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        gamemodeSwitch = (Switch) findViewById(R.id.switch1);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        dehetRadioButton = (RadioButton) findViewById(R.id.dehetradioButton);
        dezedieditdatRadioButton = (RadioButton) findViewById(R.id.dezedieditdatradioButton);
        resetCheckBox = (CheckBox) findViewById(R.id.checkBox);
        loadOptions();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedButtonId) {
                if(checkedButtonId == R.id.dehetradioButton){
                    chosenGameversion = "DEHET";
                }
                if(checkedButtonId == R.id.dezedieditdatradioButton){
                    chosenGameversion = "DEMPRO";
                }
                saveOptions();
            }
        });


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


        if (gamemodeSwitch.isChecked()){
            gamemodeSwitch.setText("ON");
        }
        else
            gamemodeSwitch.setText("OFF");


        resetCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    resetvalue = true;
                } else {
                    resetvalue = false;
                }
                saveOptions();
            }
        });



    }

    public void saveOptions(){
        SharedPreferences useroptions = this.getSharedPreferences("settings",
                this.MODE_PRIVATE);
        SharedPreferences.Editor editor = useroptions.edit();
        editor.putString("GAMETYPE", chosenGametype);
        editor.putString("GAMEVERSION", chosenGameversion);
        editor.putBoolean("RESET", resetvalue);
        editor.commit();

        System.out.println("CHOSEN GAME MODE: " + chosenGametype);
    }


    public void loadOptions(){

        SharedPreferences useroptions = getSharedPreferences("settings", this.MODE_PRIVATE);
        chosenGametype = useroptions.getString("GAMETYPE", "NORMAL");
        chosenGameversion = useroptions.getString("GAMEVERSION", "DEHET");

        if (chosenGametype.equals("NORMAL"))
            gamemodeSwitch.setChecked(false);
        else
            gamemodeSwitch.setChecked(true);

        if(chosenGameversion.equals("DEMPRO"))
            dezedieditdatRadioButton.setChecked(true);
        else
            dehetRadioButton.setChecked(true);

        resetCheckBox. setChecked(false);
    }
}
