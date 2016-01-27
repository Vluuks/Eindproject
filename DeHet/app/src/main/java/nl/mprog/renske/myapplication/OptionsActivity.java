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

    private Switch gamemodeSwitch, textToSpeechSwitch, translationSwitch;
    public String chosenGametype, chosenGameversion;
    private RadioGroup radioGroup;
    private RadioButton dehetRadioButton, dezedieditdatRadioButton;
    private CheckBox resetCheckBox;
    private boolean resetValue, textToSpeech, translationValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        // Initialize layout components.
        gamemodeSwitch = (Switch) findViewById(R.id.switch1);
        textToSpeechSwitch = (Switch) findViewById(R.id.switch2);
        translationSwitch = (Switch) findViewById(R.id.switch3);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        dehetRadioButton = (RadioButton) findViewById(R.id.dehetradioButton);
        dezedieditdatRadioButton = (RadioButton) findViewById(R.id.dezedieditdatradioButton);
        resetCheckBox = (CheckBox) findViewById(R.id.checkBox);

        // Obtain saved options.
        loadOptions();

        // Set listeners.
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

        CompoundButton.OnCheckedChangeListener multipleListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {
                    case R.id.switch1:
                        if (isChecked) {
                            chosenGametype = "CHILL";
                            gamemodeSwitch.setText(R.string.ON);
                        } else {
                            chosenGametype = "NORMAL";
                            gamemodeSwitch.setText(R.string.OFF);
                        }
                        break;
                    case R.id.switch2:
                        if (isChecked) {
                            textToSpeech = true;
                            textToSpeechSwitch.setText(R.string.ON);
                        } else {
                            textToSpeech = false;
                            textToSpeechSwitch.setText(R.string.OFF);
                        }
                        break;
                    case R.id.switch3:
                        if (isChecked) {
                            translationValue = true;
                            translationSwitch.setText(R.string.ON);
                        } else {
                            translationValue = false;
                            translationSwitch.setText(R.string.OFF);
                        }
                        break;
                    case R.id.checkBox:
                        if (isChecked) {
                            resetValue = true;
                        } else {
                            resetValue = false;
                        }
                        break;
                }
                saveOptions();
            }
        };

        gamemodeSwitch.setOnCheckedChangeListener(multipleListener);
        textToSpeechSwitch.setOnCheckedChangeListener(multipleListener);
        translationSwitch.setOnCheckedChangeListener(multipleListener);
        resetCheckBox.setOnCheckedChangeListener(multipleListener);
    }

    /**
     * Store state of options in SharedPreferences.
     */
    public void saveOptions(){

        SharedPreferences useroptions = this.getSharedPreferences("settings",
                this.MODE_PRIVATE);
        SharedPreferences.Editor editor = useroptions.edit();
        editor.putString("GAMETYPE", chosenGametype);
        editor.putString("GAMEVERSION", chosenGameversion);
        editor.putBoolean("RESET", resetValue);
        editor.putBoolean("TTS", textToSpeech);
        editor.putBoolean("TRANSLATION", translationValue);
        editor.commit();
    }

    /**
     * Obtain state of options from SharedPreferences.
     */
    public void loadOptions(){

        SharedPreferences useroptions = getSharedPreferences("settings", this.MODE_PRIVATE);
        chosenGametype = useroptions.getString("GAMETYPE", "NORMAL");
        chosenGameversion = useroptions.getString("GAMEVERSION", "DEHET");
        textToSpeech = useroptions.getBoolean("TTS", true);
        translationValue = useroptions.getBoolean("TRANSLATION", true);

        // Set all layout elements accordingly.
        if (chosenGametype.equals("NORMAL")) {
            gamemodeSwitch.setChecked(false);
            gamemodeSwitch.setText(R.string.OFF);
        }
        else {
            gamemodeSwitch.setChecked(true);
            gamemodeSwitch.setText(R.string.ON);

        }

        if(textToSpeech == true) {
            textToSpeechSwitch.setChecked(true);
            textToSpeechSwitch.setText(R.string.ON);
        }
        else {
            textToSpeechSwitch.setChecked(false);
            textToSpeechSwitch.setText(R.string.OFF);
        }

        if(translationValue == true) {
            translationSwitch.setChecked(true);
            translationSwitch.setText(R.string.ON);
        }
        else {
            translationSwitch.setChecked(false);
            translationSwitch.setText(R.string.OFF);
        }

        if(chosenGameversion.equals("DEMPRO"))
            dezedieditdatRadioButton.setChecked(true);
        else
            dehetRadioButton.setChecked(true);

        resetCheckBox. setChecked(false);
    }
}