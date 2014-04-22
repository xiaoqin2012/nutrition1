package net.tigerparents.nut;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.tigerparents.nut.nutritioninfo.NutritionData;
import net.tigerparents.nut.nutritioninfo.NutritionReport;

import java.util.ArrayList;


public class ShowRecentEntries extends Activity {

    public static final String RECENT_ENTRIES_TYPE = "recent_entries_type";
    public static final String SHOPPING_TYPE = "shopping";
    public static final String EATING_TYPE = "eating";

    NutritionData.ReportTypes _entries_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recent_entries);
        Intent intent = getIntent();
        String recent_entries_type = intent.getStringExtra(RECENT_ENTRIES_TYPE);
        if (recent_entries_type.equals(SHOPPING_TYPE)) {
            _entries_type = NutritionData.ReportTypes.WEEKLY_SHOPPING;
        } else if (recent_entries_type.equals(EATING_TYPE)) {
            _entries_type = NutritionData.ReportTypes.DAILY;
        } else {
            Log.e("ShowRecentEntries", "Unknown Recent Entries Type:" + recent_entries_type);
            assert (false); // crash the program
        }
        final ListView lv = (ListView) findViewById(R.id.recentListView);
        lv.setOnItemClickListener(new ItemClickListener(this));
        findAndShowRecentEntries(_entries_type);
    }

    public void deleteItem(String food_description) {
        NutritionReport.deleteItem(_entries_type, food_description);
        findAndShowRecentEntries(_entries_type);
    }

    public void findAndShowRecentEntries(NutritionData.ReportTypes type) {
        ArrayList<NutritionReport.FoodLogEntry> entries =
                NutritionReport.getRecentEntries(type);
        if (entries == null) return; // invalid report
        ListView listview = (ListView) findViewById(R.id.recentListView);
        ArrayList<String> listview_data = new ArrayList<String>();
        for (NutritionReport.FoodLogEntry i : entries) {
            String row_text = String.format("%s: %.2f%s",
                    i.getFoodName(),
                    i.getWeight(),
                    i.getWeightUnit());
            listview_data.add(row_text);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, listview_data);
        listview.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.show_recent_entries, menu);
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

    class ItemClickListener implements AdapterView.OnItemClickListener {
        ShowRecentEntries _activity;

        ItemClickListener(ShowRecentEntries activity) {
            super();
            _activity = activity;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String row_text = (String) adapterView.getItemAtPosition(i);
            String[] row_text_split = row_text.split(":");
            final String food_description = row_text_split[0];
            AlertDialog.Builder delete_dialog_builder = new AlertDialog.Builder(_activity);
            delete_dialog_builder.setTitle("Delete this entry?");
            delete_dialog_builder.setMessage("Click yes to delete!")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            _activity.deleteItem(food_description);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog delete_dialog = delete_dialog_builder.create();
            delete_dialog.show();
        }
    }

}
