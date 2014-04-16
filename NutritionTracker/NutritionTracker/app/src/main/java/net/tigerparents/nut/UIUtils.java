package net.tigerparents.nut;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by piaw on 4/15/2014.
 */
public class UIUtils {
    public static void showNutritionInfo(Activity activity, ListView listview,
                                         ArrayList<NutritionData.NutritionInformation> info,
                                         String header_text) {
        ArrayList<String> listview_data = new ArrayList<String>();
        listview_data.add(header_text);
        for (NutritionData.NutritionInformation i : info) {
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

    public static void HideKeyboard(Activity activity, EditText edit_text) {
        ((InputMethodManager) activity.getSystemService(
                Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                edit_text.getWindowToken(), 0);
    }

    public static void ShowKeyboard(Activity activity) {
        ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(
                InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}
