package net.tigerparents.nut;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class NutritionInformationDisplay extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_information_display);
        ListView nv = (ListView) findViewById(R.id.nutrition_listview);
        Intent intent = getIntent();
        NutritionData nut_data =
                NutritionData.getNutritionDataFor(intent.getStringExtra(FoodEntry.FOOD_NAME),
                        intent.getIntExtra(FoodEntry.WEIGHT_IN_OUNCES, 0));
        ArrayList<NutritionData.NutritionInformation> info = nut_data.getNutritionInformation();
        ArrayList<String> listview_data = new ArrayList<String>();
        String header_text = String.format("Name: %s, Calories: %.2f",
                intent.getStringExtra(FoodEntry.FOOD_NAME), nut_data.getCalories());
        listview_data.add(header_text);
        for (NutritionData.NutritionInformation i : info) {
            String row_text = String.format("%s: %.2f %.1f",
                    i.getNutritionDescription(),
                    i.getWeightValue(),
                    i.getPercentageFDA());
            listview_data.add(row_text);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, listview_data);
        nv.setAdapter(arrayAdapter);
    }

    public void enterMoreData(View view) {
        Intent intent = new Intent(this, FoodEntry.class);
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
