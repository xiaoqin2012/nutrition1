package net.tigerparents.nut;


import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.tigerparents.nut.nutritioninfo.NutritionInformation;

import java.util.ArrayList;

/**
 * Created by piaw on 4/15/2014.
 */
public class UIUtils {
    public static void showNutritionInfo(Activity activity, ListView listview,
                                         ArrayList<NutritionInformation> info,
                                         String header_text) {
        ArrayList<String> listview_data = new ArrayList<String>();
        listview_data.add(header_text);
        for (NutritionInformation i : info) {
            String row_text = String.format("%s: %.2f%s %.1f",
                    i.getNutritionDescription(),
                    i.getWeightValue(),
                    i.getWeightUnit(),
                    i.getPercentageFDA());
            listview_data.add(row_text);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                activity, android.R.layout.simple_list_item_1, listview_data);
        listview.setAdapter(arrayAdapter);
    }
}
