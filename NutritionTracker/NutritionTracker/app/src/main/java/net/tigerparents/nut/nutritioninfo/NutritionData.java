package net.tigerparents.nut.nutritioninfo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.tigerparents.nut.DataBaseHelper.USDADataBaseHelper;
import net.tigerparents.nut.Log;
import net.tigerparents.nut.NutritionTrackerApp;
import net.tigerparents.nut.PersonProfile;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * Created by xiaoqin on 4/10/2014.
 */

public class NutritionData {
    private final String LOG_TAG = "NutritionData";
    public String food_name;
    public int weightInOunces;
    PersonProfile profile = null;

    public NutritionData(String food_name, int weightInOunces) {
        this.food_name = food_name;
        this.weightInOunces = weightInOunces;
        this.profile = PersonProfile.getPersonProfile();
    }

    public static NutritionData getNutritionDataFor(String food_name, int weightInOunces) {
        return new NutritionData(food_name, weightInOunces);
    }

    public static int getTodayValue() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        return year * 10000 + month * 100 + date;
    }

    public static boolean isForShopping(ReportTypes type) {
        switch (type) {
            case DAILY:
            case WEEKLY:
            case MONTHLY:
                return false;
            case DAILY_SHOPPING:
            case WEEKLY_SHOPPING:
            case MONTHLY_SHOPPING:
                return true;
            default:
                return false;
        }
    }

    public void save() {
        save(NutritionTrackerApp.getLogDatabaseHelper().daily_food_log);
    }

    public void saveShopping() {
        save(NutritionTrackerApp.getLogDatabaseHelper().weekly_food_log);
    }

    public void save(String table_name) {
        int date = getTodayValue();
        String sql;

        Date time = new Date();
        sql = "insert into " + table_name + " (_date, time, food_name, weight) " +
                "values ( " +
                date + ", " +
                "\'" + time.toString() + "\'" + "," +
                "\'" + food_name + "\', " +
                weightInOunces +
                ");";

        NutritionTrackerApp.getLogDatabaseHelper().execSQL(sql, table_name);

        table_name = NutritionTrackerApp.getLogDatabaseHelper().favorite_food_log;
        sql = "insert into " + table_name + " (_foodname) " +
                "values ( " +
                "\'" + food_name + "\' " +
                ");";

        NutritionTrackerApp.getLogDatabaseHelper().execSQL(sql, table_name);
    }

    public ArrayList<NutritionInformation> getNutritionInformation(boolean is_std_needed, boolean toSort) {
        ArrayList<NutritionInformation> info = new ArrayList<NutritionInformation>();
        SQLiteDatabase usda_database = NutritionTrackerApp.getUSDADatabaseHelper().getDataBase();
        SQLiteDatabase log_database = NutritionTrackerApp.getLogDatabaseHelper().getDataBase();

        Cursor nutrNameCursor;

        try {
            int i = 0;
            String sql1 = "select * from " + USDADataBaseHelper.food_nutr_tab_name + " where Long_Desc = \'" + food_name + "\';";
            Cursor dbCursor = usda_database.rawQuery(sql1, null);
            if (dbCursor.moveToFirst() == false)
                return null;

            String sql2 = "select * from " + USDADataBaseHelper.daily_std_tab_name +
                    " where " +
                    " _status = " + "\"" + profile.getStatus() + "\"" + " and " +
                    "age_group = " + "\"" + profile.getAgeGroup() + "\"" + ";";

            Cursor stdValueCursor = usda_database.rawQuery(sql2, null);
            if (stdValueCursor.moveToFirst() == false)
                return null;

            String status = stdValueCursor.getString(0);
            String age_group = stdValueCursor.getString(1);

            for (i = 4; i < dbCursor.getColumnCount(); i++) {
                String nuID = dbCursor.getColumnName(i);

                /* get nu name string from nutr_def table */
                String sql_nu = "select * from " + USDADataBaseHelper.nutr_desc_tab_name +
                        " where _Nutr_No = \'"
                        + nuID + "\';";
                Cursor nuCursor = usda_database.rawQuery(sql_nu, null);
                nuCursor.moveToFirst();
                String nuName = nuCursor.getString(3) + " ";

                if (nuID.equals("208")) {
                    nuName = "Calories";
                }
                double value = dbCursor.getDouble(i);

                value *= weightInOunces * 28.35 / 100;

                /* get the std value */
                double stdValue = -100;

                if (nuID.equals("208")) {
                    nuName = "Calories";
                    stdValue = profile.getDailyKcal();
                } else {
                    stdValue = getSTDValue(stdValueCursor, profile, nuID, value);
                }
                if (stdValue < 0) continue;

                if (nuID.equals("312"))
                    stdValue /= 1000;

                NutritionInformation ni = new NutritionInformation(nuName, nuID, value,
                        nuCursor.getString(1), stdValue, value * 100 / stdValue);
                info.add(ni);
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            Log.e("NutritionData.class", "getNutritionInformation()", e);
        }

        if (toSort) {
            Collections.sort(info, Collections.reverseOrder());
        }
        return info;
    }

    /* std_dvi_table: (gender, status), age */
    public double getSTDValue(Cursor stdValueCursor, PersonProfile prof, String nuID, double value) {
        double stdValue = -100;
        int count = stdValueCursor.getCount();
        System.out.print(count);

        try {
            int stdIndex = stdValueCursor.getColumnIndex(nuID);
            if (stdIndex < 0)
                return stdValue;

            stdValue = stdValueCursor.getDouble(stdIndex);

            if (nuID.equals("203")) {//203 is protein
                stdValue *= profile.getWeight() * 453.59 / 1000;
            }

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            Log.e(e.getClass().getName(), e.getMessage(), e);
        }

        return stdValue;
    }

    public enum ReportTypes {
        DAILY, WEEKLY, MONTHLY, DAILY_SHOPPING, WEEKLY_SHOPPING, MONTHLY_SHOPPING
    }
}
