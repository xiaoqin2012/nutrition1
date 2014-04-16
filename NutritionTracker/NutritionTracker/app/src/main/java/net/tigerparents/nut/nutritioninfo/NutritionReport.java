package net.tigerparents.nut.nutritioninfo;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Calendar;

import static net.tigerparents.nut.NutritionTrackerApp.getDatabaseHelper;

/**
 * Created by xiaoqin on 4/16/2014.
 */
public class NutritionReport {
    ArrayList<NutritionInformation> nu_info_list;
    NutritionData.ReportTypes type;

    String sql_daily_log;
    int num_of_days;

    NutritionReport(NutritionData.ReportTypes type) {
        this.nu_info_list = null;
        this.type = type;
        buildQueryString();
    }

    public static ArrayList<NutritionInformation> getNutritionInformationReport(NutritionData.ReportTypes type) {
        NutritionReport nu_report = new NutritionReport(type);
        return nu_report.getReport();
    }

    public void add(ArrayList<NutritionInformation> array) {
        if (nu_info_list == null) {
            nu_info_list = array;
        } else {
            for (int i = 0; i < nu_info_list.size() && i < array.size(); i++) {
                nu_info_list.get(i).weightValue += nu_info_list.get(i).weightValue;
            }
        }
    }

    public ArrayList<NutritionInformation> getReport() {
        if (nu_info_list == null) {
            generateReport();
        }

        return nu_info_list;
    }

    public void generateReport() {
        int num_days = 1;
        Cursor cursor = getDatabaseHelper().getWritableDatabase().rawQuery(sql_daily_log, null);
        if (!cursor.moveToFirst())
            return;

        while (!cursor.isAfterLast()) {
            NutritionData nu_data = new NutritionData(cursor.getString(0), cursor.getInt(1));
            if (nu_info_list == null)
                add(nu_data.getNutritionInformation(true));
            else add(nu_data.getNutritionInformation(false));
            cursor.moveToNext();
        }

        for (int i = 0; i < nu_info_list.size(); i++) {
            double value = nu_info_list.get(i).getWeightValue();
            double stdValue = nu_info_list.get(i).getFDA();
            nu_info_list.get(i).setPercentageFDA(value / stdValue / num_days);
        }
    }

    public void buildQueryString() {
        String table_name = getDatabaseHelper().daily_food_log;
        String sql = "select * from " + table_name + " where ";
        String condition = null;

        int date = NutritionData.getTodayValue();

        switch (type) {
            case DAILY:
            /* query database, get all logs food name, weight */
            /* for each food, call get nutrition_data(food_name, weight).getNutritionInformation
            *  add it to sum value*/
                condition = " _date = " + date + ";";
                num_of_days = 1;
                break;

            case WEEKLY:
                int day_of_week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                int begin_of_week = date - day_of_week;
                condition = " _date >= " + begin_of_week + " and " + "_date < " + date + ";";
                num_of_days = day_of_week;
                break;
            case MONTHLY:
                int year = Calendar.getInstance().get(Calendar.YEAR);
                int month = Calendar.getInstance().get(Calendar.MONTH);
                int begin_date = year * 10000 + month * 100;
                condition = " _date >= " + begin_date + " and " + " _date < " + date + ";";
                num_of_days = date - begin_date;
                break;
            default:
                break;
        }

        sql_daily_log = sql + condition;
    }
}