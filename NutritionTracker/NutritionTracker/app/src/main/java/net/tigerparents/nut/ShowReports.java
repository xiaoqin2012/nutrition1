package net.tigerparents.nut;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import net.tigerparents.nut.nutritioninfo.NutritionData;
import net.tigerparents.nut.nutritioninfo.NutritionInformation;
import net.tigerparents.nut.nutritioninfo.NutritionReport;

import java.util.ArrayList;


public class ShowReports extends Activity {

    public static final String REPORTS_PARENT = "reports_parent";
    public static final String REPORTS_FOR_EATING = "eating";
    public static final String REPORTS_FOR_SHOPPING = "shopping";
    EntryType _entry_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_reports);
        Spinner spinner = (Spinner) findViewById(R.id.reportsSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.report_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Intent intent = getIntent();
        if (intent.getStringExtra(REPORTS_PARENT).equals(REPORTS_FOR_EATING)) {
            _entry_type = EntryType.EATING;
        } else if (intent.getStringExtra(REPORTS_PARENT).equals(REPORTS_FOR_SHOPPING)) {
            _entry_type = EntryType.SHOPPING;
        } else {
            Log.e("ShowReports", "Unknown Reports Parent" + intent.getStringExtra(REPORTS_PARENT));
            return;
        }
        spinner.setOnItemSelectedListener(_entry_type == EntryType.EATING ?
                new EatingReportSelectedListener(this) :
                new ShoppingSelectedListener(this));
    }

    void updateReport(String report_type) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.show_reports, menu);
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

    enum EntryType {EATING, SHOPPING}

    private class ShowReportsListener {

        Activity _activity;

        private ShowReportsListener(Activity activity) {
            _activity = activity;
        }

        public void findAndShowReports(String report_type, NutritionData.ReportTypes type) {
            ArrayList<NutritionInformation> report =
                    NutritionReport.getNutritionInformationReport(type);
            if (report == null) {
                // dummy up bogus data
                NutritionInformation dummy = new NutritionInformation();
                report = new ArrayList<NutritionInformation>();
                report.add(dummy);
            }
            ListView listview = (ListView) findViewById(R.id.reports_lv);
            String header_text = String.format("Report: %s", report_type);
            UIUtils.showNutritionInfo(_activity, listview, report, header_text);
            updateReport(report_type);
        }
    }

    private class EatingReportSelectedListener extends ShowReportsListener
            implements AdapterView.OnItemSelectedListener {

        EatingReportSelectedListener(Activity activity) {
            super(activity);
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String report_type = (String) adapterView.getItemAtPosition(i);
            Log.d("report_type selected:", report_type);
            NutritionData.ReportTypes type;
            if (report_type.equals("Daily")) {
                type = NutritionData.ReportTypes.DAILY;
            } else if (report_type.equals("Weekly")) {
                type = NutritionData.ReportTypes.WEEKLY;
            } else if (report_type.equals("Monthly")) {
                type = NutritionData.ReportTypes.MONTHLY;
            } else {
                Log.e("Invalid report type", report_type);
                return;
            }
            findAndShowReports(report_type, type);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    private class ShoppingSelectedListener extends ShowReportsListener
            implements AdapterView.OnItemSelectedListener {

        ShoppingSelectedListener(Activity activity) {
            super(activity);
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String report_type = (String) adapterView.getItemAtPosition(i);
            Log.d("report_type selected:", report_type);
            NutritionData.ReportTypes type;
            if (report_type.equals("Daily")) {
                type = NutritionData.ReportTypes.DAILY_SHOPPING;
            } else if (report_type.equals("Weekly")) {
                type = NutritionData.ReportTypes.WEEKLY_SHOPPING;
            } else if (report_type.equals("Monthly")) {
                type = NutritionData.ReportTypes.MONTHLY_SHOPPING;
            } else {
                Log.e("Invalid report type", report_type);
                return;
            }
            findAndShowReports(report_type, type);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}
