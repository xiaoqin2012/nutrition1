package net.tigerparents.nut;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import net.tigerparents.nut.nutritioninfo.NutritionData;
import net.tigerparents.nut.nutritioninfo.NutritionInformation;

import java.util.ArrayList;


public class NutritionInformationDisplay extends Activity {

    public static final String ENTRY_FROM = "entry from";
    public static final String FOOD_ENTRY = "food entry";
    public static final String SHOPPING_ENTRY = "shopping entry";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_information_display);
        ListView nv = (ListView) findViewById(R.id.nutrition_listview);
        Intent intent = getIntent();
        NutritionData nut_data =
                NutritionData.getNutritionDataFor(intent.getStringExtra(FoodEntry.FOOD_NAME),
                        intent.getIntExtra(FoodEntry.WEIGHT_IN_OUNCES, 0));
        ArrayList<NutritionInformation> info = nut_data.getNutritionInformation(true);

        String header_text = String.format("Name: %s", intent.getStringExtra(FoodEntry.FOOD_NAME));
        UIUtils.showNutritionInfo(this, nv, info, header_text);
    }

    public void enterMoreData(View view) {
        Intent intent;

        String entry_from = getIntent().getStringExtra(ENTRY_FROM);
        if (entry_from.equals(FOOD_ENTRY)) {
            intent = new Intent(this, FoodEntry.class);
        } else if (entry_from.equals(SHOPPING_ENTRY)) {
            intent = new Intent(this, ShoppingEntry.class);
        } else {
            Log.e("NutritionInformationDisplay", "Unknown Entry From:" + entry_from);
            return;
        }
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nutrition_information_display, menu);
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
