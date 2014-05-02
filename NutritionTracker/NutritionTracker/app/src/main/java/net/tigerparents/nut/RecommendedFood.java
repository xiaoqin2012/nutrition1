package net.tigerparents.nut;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.tigerparents.nut.nutritioninfo.FoodNutrientInfo;
import net.tigerparents.nut.nutritioninfo.NutritionReport;

import java.util.ArrayList;


public class RecommendedFood extends Activity {

    public static final String NUTRIENT = "Nutrient";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_food);
        String nutrient_name = getIntent().getStringExtra(NUTRIENT);
        FoodNutrientInfo[] recommended_foods = NutritionReport.getFoodFor(nutrient_name);
        ListView lv = (ListView) findViewById(R.id.recommended);
        ArrayList<String> listview_data = new ArrayList<String>();
        for (FoodNutrientInfo i : recommended_foods) {
            String user_view = String.format("%s: %.2f %s", i.get_foodname(),
                    i.get_nutrient_value(), i.get_nutrient_unit());
            listview_data.add(user_view);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, listview_data);
        lv.setAdapter(arrayAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.recommended_food, menu);
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
