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


public class ShoppingEntry extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_entry);
        AutoCompleteTextView food_tv = (AutoCompleteTextView) findViewById(R.id.shoppingTextView);
        UIUtils.setupTextViewWithFoodNames(this, food_tv);
        NumberPicker pound_picker = (NumberPicker) findViewById(R.id.poundPicker);
        NumberPicker ounce_picker = (NumberPicker) findViewById(R.id.ouncePicker);
        pound_picker.setMinValue(0);
        ounce_picker.setMinValue(0);
        ounce_picker.setMaxValue(15);
        pound_picker.setMaxValue(100);
        food_tv.requestFocusFromTouch();
        UIUtils.ShowAds(this);
        UIUtils.ShowKeyboard(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shopping_entry, menu);
        return true;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            ((AutoCompleteTextView) findViewById(R.id.shoppingTextView)).requestFocusFromTouch();
            UIUtils.ShowKeyboard(this);
        }
    }

    public void onSaveShopping(View view) {
        AutoCompleteTextView food_tv = (AutoCompleteTextView) findViewById(R.id.shoppingTextView);
        NumberPicker pound_picker = (NumberPicker) findViewById(R.id.poundPicker);
        NumberPicker ounce_picker = (NumberPicker) findViewById(R.id.ouncePicker);
        String food_name = food_tv.getText().toString();
        int weight_in_ounces = pound_picker.getValue() * 16 + ounce_picker.getValue();
        NutritionData new_data = new NutritionData(food_name, weight_in_ounces);
        new_data.saveShopping();
        Intent intent = new Intent(this, NutritionInformationDisplay.class);
        UIUtils.HideKeyboard(this, food_tv);
        intent.putExtra(FoodEntry.FOOD_NAME, food_tv.getText().toString());
        intent.putExtra(FoodEntry.WEIGHT_IN_OUNCES, weight_in_ounces);
        intent.putExtra(NutritionInformationDisplay.ENTRY_FROM,
                NutritionInformationDisplay.SHOPPING_ENTRY);
        startActivity(intent);
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
