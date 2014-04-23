package net.tigerparents.nut.nutritioninfo;

import android.database.Cursor;

import net.tigerparents.nut.DataBaseHelper.LogDataBaseHelper;
import net.tigerparents.nut.Log;

import java.util.ArrayList;
import java.util.Calendar;

import static net.tigerparents.nut.NutritionTrackerApp.getLogDatabaseHelper;

/**
 * Created by xiaoqin on 4/16/2014.
 */
public class NutritionReport {
    ArrayList<NutritionInformation> nu_info_list;
    NutritionData.ReportTypes type;

    String sql_daily_log;
    int num_of_days;
    String table_name = null;

    NutritionReport(NutritionData.ReportTypes type) {
        this.nu_info_list = null;
        this.type = type;
        table_name = NutritionData.isForShopping(type) ? LogDataBaseHelper.weekly_food_log :
                LogDataBaseHelper.daily_food_log;
        buildQueryString();
    }

    public static ArrayList<FoodLogEntry> getRecentEntries(NutritionData.ReportTypes type) {
        NutritionReport log_report = new NutritionReport(type);
        return log_report.getLog();
    }

    public static void deleteItem(NutritionData.ReportTypes type, String food_description) {
        try {
            String table_name = new String();
            int date = NutritionData.getTodayValue();
            switch (type) {
                case DAILY:
                case WEEKLY:
                case MONTHLY:
                    table_name = LogDataBaseHelper.daily_food_log;
                    break;
                case DAILY_SHOPPING:
                case WEEKLY_SHOPPING:
                case MONTHLY_SHOPPING:
                    table_name = LogDataBaseHelper.weekly_food_log;
                    break;
                default:
                    break;
            }

            String[] parts = food_description.split(" ", 2);
            getLogDatabaseHelper().getDataBase().delete(table_name, " _date = ? and food_name = ? ",
                    new String[]{parts[0], parts[1]});
        } catch (Exception e) {
            Log.e(e.getClass().getName(), e.getMessage(), e);
        }
    }

    public static ArrayList<NutritionInformation> getNutritionInformationReport(NutritionData.ReportTypes type) {
        NutritionReport nu_report = new NutritionReport(type);
        return nu_report.getReport();
    }

    public ArrayList<FoodLogEntry> getLog() {
        ArrayList<FoodLogEntry> food_log = new ArrayList<FoodLogEntry>();
        try {
            Cursor cursor = getLogDatabaseHelper().getDataBase().rawQuery(sql_daily_log, null);
            if (!cursor.moveToFirst())
                return food_log;

            while (!cursor.isAfterLast()) {
                FoodLogEntry log_entry = new FoodLogEntry(Integer.toString(cursor.getInt(0)) +
                        " " + cursor.getString(1),
                        cursor.getDouble(2), type
                );
                food_log.add(log_entry);
                cursor.moveToNext();
            }
        } catch (Exception e) {
            Log.e(e.getClass().getName(), e.getMessage(), e);
        }

        return food_log;
    }

    public void add(ArrayList<NutritionInformation> array) {
        if (nu_info_list == null) {
            nu_info_list = array;
        } else {
            for (int i = 0; i < nu_info_list.size() && i < array.size(); i++) {
                if (i == 5) {
                    double value = nu_info_list.get(i).weightValue;
                    System.out.print(value);
                }
                nu_info_list.get(i).weightValue += array.get(i).weightValue;
            }
        }
    }

    public ArrayList<NutritionInformation> getReport() {
        try {
            Cursor cursor = getLogDatabaseHelper().getDataBase().rawQuery(sql_daily_log, null);
            if (!cursor.moveToFirst())
                return nu_info_list;

            while (!cursor.isAfterLast()) {
                NutritionData nu_data = new NutritionData(cursor.getString(1), cursor.getInt(2));
                if (nu_info_list == null)
                    add(nu_data.getNutritionInformation(true));
                else add(nu_data.getNutritionInformation(false));
                cursor.moveToNext();
            }
        } catch (Exception e) {
            Log.e(e.getClass().getName(), e.getMessage(), e);
        }

        for (int i = 0; i < nu_info_list.size(); i++) {
            double value = nu_info_list.get(i).getWeightValue();
            double stdValue = nu_info_list.get(i).getFDA();
            int real_days = getRealDays(type, num_of_days);
            nu_info_list.get(i).setPercentageFDA(value / (stdValue * real_days) * 100);
        }

        return nu_info_list;
    }

    public int getRealDays(NutritionData.ReportTypes type, int num_of_days) {
        switch (type) {
            case DAILY:
            case WEEKLY:
            case MONTHLY:
            case DAILY_SHOPPING:
                return num_of_days;
            case WEEKLY_SHOPPING:
                return 7;
            case MONTHLY_SHOPPING:
                return Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
            default:
                return 0;
        }
    }

    public void buildQueryString() {
        String sql = "select * from " + table_name + " where ";
        String condition = null;

        int date = NutritionData.getTodayValue();

        switch (type) {
            case DAILY:
            case DAILY_SHOPPING:
            /* query database, get all logs food name, weight */
            /* for each food, call get nutrition_data(food_name, weight).getNutritionInformation
            *  add it to sum value*/
                condition = " _date = " + date + ";";
                num_of_days = 1;
                break;

            case WEEKLY:
            case WEEKLY_SHOPPING:
                int day_of_week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                int begin_of_week = date - day_of_week + 1;
                condition = " _date >= " + begin_of_week + " and " + "_date <= " + date + ";";
                num_of_days = day_of_week;
                break;
            case MONTHLY:
            case MONTHLY_SHOPPING:
                int year = Calendar.getInstance().get(Calendar.YEAR);
                int month = Calendar.getInstance().get(Calendar.MONTH);
                int begin_date = year * 10000 + month * 100;
                condition = " _date >= " + begin_date + " and " + " _date <= " + date + ";";
                num_of_days = date - begin_date;
                break;
            default:
                break;
        }

        sql_daily_log = sql + condition;
    }

    public class FoodLogEntry {
        String food_name;
        double weight;
        String weightUnit;

        FoodLogEntry(String food_name, double weight, NutritionData.ReportTypes type) {
            this.food_name = food_name;
            switch (type) {
                case DAILY:
                case WEEKLY:
                case MONTHLY:
                    this.weight = weight;
                    this.weightUnit = new String("ounce");
                    break;
                case DAILY_SHOPPING:
                case WEEKLY_SHOPPING:
                case MONTHLY_SHOPPING:
                    this.weightUnit = new String("lb");
                    this.weight = weight / 16;
            }
        }

        public String getFoodName() {
            return food_name;
        }

        public double getWeight() {
            return weight;
        }

        public String getWeightUnit() {
            return weightUnit;
        }
    }

}
