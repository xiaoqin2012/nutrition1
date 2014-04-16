package net.tigerparents.nut.nutritioninfo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.tigerparents.nut.DataBaseHelper;
import net.tigerparents.nut.NutritionTrackerApp;
import net.tigerparents.nut.PersonProfile;

import java.util.ArrayList;
import java.util.Calendar;

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
        int date = Calendar.getInstance().get(Calendar.DATE);
        return year * 10000 + month * 100 + date;
    }

    public void save() {
        int date = getTodayValue();
        String table_name = NutritionTrackerApp.getDatabaseHelper().daily_food_log;
        String sql = "insert into " + table_name + " (_date, food_name, weight) " +
                "values ( " + date + ", " +
                "\'" + food_name + "\', " + weightInOunces + ");";
        NutritionTrackerApp.getDatabaseHelper().execSQL(sql, table_name);
    }

    public ArrayList<NutritionInformation> getNutritionInformation(boolean is_std_needed) {
        ArrayList<NutritionInformation> info = new ArrayList<NutritionInformation>();
        SQLiteDatabase database = NutritionTrackerApp.getDatabaseHelper().getDataBase();

        Cursor nutrNameCursor;

        try {
            int i = 0;
            String sql = "select * from " + DataBaseHelper.food_nutr_tab_name + " where Long_Desc = \'" + food_name + "\';";
            Cursor dbCursor = database.rawQuery(sql, null);
            dbCursor.moveToFirst();

            sql = "select * from " + DataBaseHelper.daily_std_tab_name +
                    " where " +
                    " _status = " + "\"" + profile.getStatus() + "\"" + " and " +
                    "age_group = " + "\"" + profile.getAgeGroup() + "\"" + ";";
            Cursor stdValueCursor = database.rawQuery(sql, null);

            stdValueCursor.moveToFirst();

            for (i = 4; i < dbCursor.getColumnCount(); i++) {
                String nuID = dbCursor.getColumnName(i);

                /* get nu name string from nutr_def table */
                String sql_nu = "select * from " + DataBaseHelper.nutr_desc_tab_name +
                        " where _Nutr_No = \'"
                        + nuID + "\';";
                Cursor nuCursor = database.rawQuery(sql_nu, null);
                nuCursor.moveToFirst();
                String nuName = nuCursor.getString(3) + " ";
                if (nuID.endsWith("208")) {
                    nuName = "Calories";
                }

                double value = dbCursor.getDouble(i);
                value *= weightInOunces * 28.35 / 100;

                /* get the std value */
                double stdValue = -1;
                if (is_std_needed)
                    stdValue = getSTDValue(stdValueCursor, profile, nuID, value);

                NutritionInformation ni = new NutritionInformation(nuName, value,
                        nuCursor.getString(1), stdValue, value * 100 / stdValue);
                info.add(ni);
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            Log.e("NutritionData.class", "getNutritionInformation()", e);
        }
        return info;
    }

    /* std_dvi_table: (gender, status), age */
    public double getSTDValue(Cursor stdValueCursor, PersonProfile prof, String nuID, double value) {
        String status = prof.getStatus();
        String ageGroup = prof.getAgeGroup();
        double stdValue = -1;

        try {
            int stdIndex = stdValueCursor.getColumnIndex(nuID);
            if (stdIndex < 0) return stdValue;
            stdValue = stdValueCursor.getDouble(stdIndex);

            if (nuID.equals("203")) {//203 is protein
                stdValue *= profile.getWeight() * 453.59 / 1000;
            }

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            Log.e("NutritionData.class", "getSTDValue()", e);
        }

        return stdValue;
    }

    public enum ReportTypes {
        DAILY, WEEKLY, MONTHLY
    }


}