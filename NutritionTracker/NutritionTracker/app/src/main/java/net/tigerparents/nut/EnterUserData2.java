package net.tigerparents.nut;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;


public class EnterUserData2 extends Activity implements AdapterView.OnItemSelectedListener {

    NumberPicker _height_picker;
    NumberPicker _weight_picker;
    private int activity_level;

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        activity_level = pos;
    }


    public void onNothingSelected(AdapterView<?> parent) {
        // do nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_user_data2);
        _height_picker = (NumberPicker) findViewById(R.id.heightPicker);
        _weight_picker = (NumberPicker) findViewById(R.id.weightPicker);
        Spinner activitySpinner = (Spinner) findViewById(R.id.activity_spinner);
        _height_picker.setMinValue(12);
        _height_picker.setMaxValue(100);
        _height_picker.setValue(UIUtils.s_PP.getHeight());
        _weight_picker.setMinValue(20);
        _weight_picker.setMaxValue(500);
        _weight_picker.setValue((int) UIUtils.s_PP.getWeight());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.activity_levels, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        activitySpinner.setAdapter(adapter);
        activitySpinner.setOnItemSelectedListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.enter_user_data2, menu);
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

    public void onSave(View view) {
        PersonProfile pp = UIUtils.s_PP;

        pp.setHeight(_height_picker.getValue());
        pp.setWeight(_weight_picker.getValue());
        double activityMap[] = {0.0, 20.0, 30.0, 40.0, 50.0, 60.0};
        pp.setWorkout(activityMap[activity_level]);
        pp.setDailyKcal();
        pp.savePersonProfile();
        Intent intent = new Intent(this, StartScreen.class);
        startActivity(intent);
    }

}
