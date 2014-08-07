package net.tigerparents.nut;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;

import java.util.Calendar;


public class EnterUserData extends Activity {
    EditText _name;
    NumberPicker _birthyear;
    Switch _gender;
    Switch _pregnant;
    Switch _lactating;

    boolean _person_gender; // female = false, male = true
    boolean _is_person_pregnant; // ! pregnant = false, pregnant = true
    boolean _is_person_lactating; // ! breast feeding = false, breastfeeding = true

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_user_data);
        _name = (EditText) findViewById(R.id.name);
        _birthyear = (NumberPicker) findViewById(R.id.birthYear);
        _gender = (Switch) findViewById(R.id.gender);
        _pregnant = (Switch) findViewById(R.id.pregnant);
        _lactating = (Switch) findViewById(R.id.lactate);

        setDefaultPersonProfile();

        if (PersonProfile.profileEntered()) {
            readDefaultPersonProfile();
        } else {
            UIUtils.s_PP = new PersonProfile("Azhu", 1970, false, false, false, 100, 50, 0);
        }

        _name.requestFocusFromTouch();

        UIUtils.ShowKeyboard(this);
    }

    private void readDefaultPersonProfile() {
        UIUtils.s_PP = PersonProfile.getPersonProfile();

        _name.setText(UIUtils.s_PP.getName());
        _birthyear.setValue(UIUtils.s_PP.getBirth());
        _gender.setChecked(UIUtils.s_PP.getGender());
        _is_person_pregnant = UIUtils.s_PP.isPregnant();
        _is_person_lactating = UIUtils.s_PP.isLactating();
    }

    private void setDefaultPersonProfile() {
        _name.selectAll();
        Calendar cal = Calendar.getInstance();
        int currentyear = cal.get(Calendar.YEAR);
        _birthyear.setMaxValue(currentyear);
        _birthyear.setMinValue(currentyear - 150);
        _birthyear.setValue(currentyear - 50); // everybody wants to be 25
        _gender.setTextOn("male");
        _gender.setTextOff("female");
        _pregnant.setTextOn("Pregnant");
        _pregnant.setTextOff("Not Pregnant");
        _lactating.setTextOn("Breast Feeding");
        _lactating.setTextOff("Not Breast Feeding");
        _gender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton == _gender) {
                    _person_gender = isChecked;
                    return;
                }

                if (compoundButton == _pregnant) {
                    _is_person_pregnant = isChecked;
                    return;
                }

                if (compoundButton == _lactating) {
                    _is_person_lactating = isChecked;
                    return;
                }

                Log.e("EnterUserData", "CheckChanged Incorrect");

            }
        });
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            ((EditText) findViewById(R.id.name)).requestFocusFromTouch();
            UIUtils.ShowKeyboard(this);
        }
    }

    public void userSave(View view) {
        String name = _name.getText().toString();
        int year = _birthyear.getValue();
        UIUtils.s_PP = new PersonProfile(name, year, _person_gender, _is_person_pregnant,
                _is_person_lactating, UIUtils.s_PP.getWeight(), UIUtils.s_PP.getHeight(),
                UIUtils.s_PP.getWorkout());
        UIUtils.HideKeyboard(this, _name);
        Intent intent = new Intent(this, EnterUserData2.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.enter_user_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
