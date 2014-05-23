package net.tigerparents.nut;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.NumberPicker;

import net.tigerparents.nut.nutritioninfo.NutritionData;

public class FoodEntry extends Activity {

    public static String FOOD_NAME = "FoodName";
    public static String WEIGHT_IN_OUNCES = "WeightInOunces";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_entry);
        if (PersonProfile.getPersonProfile() == null) {
            // don't let them do anything without entering a profile
            Intent intent = new Intent(this, EnterUserData.class);
            startActivity(intent);
        }
        AutoCompleteTextView food_tv = (AutoCompleteTextView) findViewById(R.id.food_entry_tv);
        UIUtils.setupTextViewWithFoodNames(this, food_tv);
        NumberPicker picker = (NumberPicker) findViewById(R.id.weightPicker);
        picker.setMinValue(1);
        picker.setMaxValue(64);
        picker.setValue(1);
        food_tv.requestFocusFromTouch();
        UIUtils.ShowKeyboard(this);
    }

    private NutritionData createNutritionDataFromTextView() {
        AutoCompleteTextView food_tv = (AutoCompleteTextView) findViewById(R.id.food_entry_tv);
        NumberPicker picker = (NumberPicker) findViewById(R.id.weightPicker);
        String food_name = food_tv.getText().toString();
        int weight_in_ounces = picker.getValue();
        return new NutritionData(food_name, weight_in_ounces);
    }

    private void showNutritionInformationDisplay() {
        AutoCompleteTextView food_tv = (AutoCompleteTextView) findViewById(R.id.food_entry_tv);
        NumberPicker picker = (NumberPicker) findViewById(R.id.weightPicker);
        Intent intent = new Intent(this, NutritionInformationDisplay.class);
        UIUtils.HideKeyboard(this, food_tv);
        intent.putExtra(FOOD_NAME, food_tv.getText().toString());
        intent.putExtra(WEIGHT_IN_OUNCES, picker.getValue());
        intent.putExtra(NutritionInformationDisplay.ENTRY_FROM,
                NutritionInformationDisplay.FOOD_ENTRY);
        startActivity(intent);
    }

    public void onSave(View v) {
        NutritionData new_data = createNutritionDataFromTextView();
        new_data.save();
        showNutritionInformationDisplay();
    }

    public void onLookup(View v) {
        NutritionData new_data = createNutritionDataFromTextView();
        showNutritionInformationDisplay();
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        AutoCompleteTextView tv = (AutoCompleteTextView) findViewById(R.id.food_entry_tv);
        if (hasFocus) {
            tv.requestFocusFromTouch();
            UIUtils.ShowKeyboard(this);
        } else {
            UIUtils.HideKeyboard(this, tv);
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
