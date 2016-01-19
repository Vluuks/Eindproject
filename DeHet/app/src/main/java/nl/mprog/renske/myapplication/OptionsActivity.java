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
    private boolean resetvalue, textToSpeech;

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


                switch (buttonView.getId()) {
                    case R.id.switch1:
                        if (isChecked) {
                            chosenGametype = "CHILL";
                            gamemodeSwitch.setText("ON");
                        } else {
                            chosenGametype = "NORMAL";
                            gamemodeSwitch.setText("OFF");
                        }
                        break;
                    case R.id.switch2:
                        if (isChecked) {
                            textToSpeech = true;
                            gamemodeSwitch.setText("ON");
                        } else {
                            textToSpeech = false;
                            gamemodeSwitch.setText("OFF");
                        }
                        break;



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
        editor.putBoolean("TTS", textToSpeech);
        editor.commit();

        System.out.println("CHOSEN GAME MODE: " + chosenGametype);
    }


    public void loadOptions(){

        SharedPreferences useroptions = getSharedPreferences("settings", this.MODE_PRIVATE);
        chosenGametype = useroptions.getString("GAMETYPE", "NORMAL");
        chosenGameversion = useroptions.getString("GAMEVERSION", "DEHET");
        textToSpeech = useroptions.getBoolean("TTS", true);

        if (chosenGametype.equals("NORMAL"))
            gamemodeSwitch.setChecked(false);
        else
            gamemodeSwitch.setChecked(true);

        if(chosenGameversion.equals("DEMPRO"))
            dezedieditdatRadioButton.setChecked(true);
        else
            dehetRadioButton.setChecked(true);

        if(textToSpeech)
            gamemodeSwitch.setChecked(true);
        else
            gamemodeSwitch.setChecked(false);

        resetCheckBox. setChecked(false);
    }
}
