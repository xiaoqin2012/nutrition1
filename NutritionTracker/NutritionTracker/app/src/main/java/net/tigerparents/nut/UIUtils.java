package net.tigerparents.nut;


import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import net.tigerparents.nut.nutritioninfo.NutritionInformation;

import java.util.ArrayList;

/**
 * Created by piaw on 4/15/2014.
 */
public class UIUtils {
    public static PersonProfile s_PP;
    private static ArrayList<String> s_foodNames;

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

    public static void HideKeyboard(Activity activity, EditText edit_text) {
        ((InputMethodManager) activity.getSystemService(
                Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                edit_text.getWindowToken(), 0);
    }

    public static void ShowKeyboard(Activity activity) {
        ((InputMethodManager) activity.getSystemService(
                Context.INPUT_METHOD_SERVICE)).toggleSoftInput(
                InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static ArrayList<String> memoizedGetFoodNames() {
        if (s_foodNames == null)
            s_foodNames = NutritionTrackerApp.getFoodNames();
        return s_foodNames;
    }

    public static void setupTextViewWithFoodNames(Activity activity, AutoCompleteTextView food_tv) {
        ArrayList<String> food_names = memoizedGetFoodNames();
        String[] template = new String[10];
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1,
                        food_names.toArray(template));
        food_tv.setAdapter(adapter);
        food_tv.selectAll();
    }

    public static void ShowAds(Activity activity) {
        // Look up the AdView as a resource and load a request.
        AdView adView = (AdView) activity.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
}
