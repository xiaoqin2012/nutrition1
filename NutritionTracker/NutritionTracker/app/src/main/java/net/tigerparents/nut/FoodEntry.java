package net.tigerparents.nut;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.NumberPicker;

import java.util.ArrayList;

public class FoodEntry extends Activity {

    public static String FOOD_NAME = "FoodName";
    public static String WEIGHT_IN_OUNCES = "WeightInOunces";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_entry);
        AutoCompleteTextView food_tv = (AutoCompleteTextView) findViewById(R.id.food_entry_tv);
        ArrayList<String> food_names = NutritionTrackerApp.getFoodNames();
        String[] template = new String[10];
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                        food_names.toArray(template));
        food_tv.setAdapter(adapter);
        food_tv.selectAll();
        NumberPicker picker = (NumberPicker) findViewById(R.id.weightPicker);
        picker.setMinValue(1);
        picker.setMaxValue(64);
        picker.setValue(1);
        food_tv.requestFocusFromTouch();
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(
                InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


    }

    public void onSave(View v) {
        AutoCompleteTextView food_tv = (AutoCompleteTextView) findViewById(R.id.food_entry_tv);
        NumberPicker picker = (NumberPicker) findViewById(R.id.weightPicker);
        String food_name = food_tv.getText().toString();
        int weight_in_ounces = picker.getValue();
        NutritionData new_data = new NutritionData(food_name, weight_in_ounces);
        new_data.save();
        Intent intent = new Intent(this, NutritionInformationDisplay.class);
        ((InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                food_tv.getWindowToken(), 0);
        intent.putExtra(FOOD_NAME, food_tv.getText().toString());
        intent.putExtra(WEIGHT_IN_OUNCES, picker.getValue());
        startActivity(intent);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            ((AutoCompleteTextView) findViewById(R.id.food_entry_tv)).requestFocusFromTouch();
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(
                    InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.food_entry, menu);
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
