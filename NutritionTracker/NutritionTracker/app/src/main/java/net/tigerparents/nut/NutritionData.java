package net.tigerparents.nut;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by xiaoqin on 4/10/2014.
 */
public class NutritionData {
    public String food_name;
    public int weightInOunces;
    PersonProfile profile;

    private final String LOG_TAG = "NutritionData";

    public NutritionData(PersonProfile prof, String food_name, int weightInOunces) {
        this.food_name = food_name;
        this.weightInOunces = weightInOunces;
        this.profile = prof;
    }

    public static NutritionData getNutritionDataFor(String food_name, int weightInOunces) {
        PersonProfile prof = PersonProfile.getPersonProfile();
        return new NutritionData(prof, food_name, weightInOunces);
    }

    public static  void peekTable(Cursor cursor) {


        while (!cursor.isAfterLast()) {
            Double test = cursor.getDouble(6);
            System.out.print(test);
            cursor.moveToNext();
        }
    }

    /*ql = "create table DAILY_FOOD_LOG (" +
                    "_name STRING, " +
                    "date INT, " +
                    "food_name STRING, " +
                    "weight DOUBLE )";*/
    public void save() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int date = Calendar.getInstance().get(Calendar.DATE);

        date = year * 10000 + month * 100 + date;

        String sql = "insert into DAILY_FOOD_LOG (date, food_name, weight) " +
                "values ( " + date + " " +
                "\'" + food_name + "\' " + weightInOunces + ");";
        SQLiteDatabase database = NutritionTrackerApp.getDatabaseHelper().getDataBase();
        database.execSQL(sql, null);
    }

    public ArrayList<NutritionInformation> getNutritionInformation() {
        ArrayList<NutritionInformation> info = new ArrayList<NutritionInformation>();
        SQLiteDatabase database = NutritionTrackerApp.getDatabaseHelper().getDataBase();

        Cursor nutrNameCursor;
        PersonProfile person = PersonProfile.getPersonProfile();


        try {
            int i = 0;
            String sql = "select * from FOOD_NUT_DATA where Long_Desc = \'" + food_name + "\';";
            Cursor dbCursor = database.rawQuery(sql, null);
            dbCursor.moveToFirst();

            sql = "select * from DAILY_STD_NUTR_TABLE" +
                    " where " +
                    " _status = " + "\"" + person.getStatus() + "\"" + " and " +
                    "age_group = " + "\"" + person.getAgeGroup() + "\"" + ";";
            Cursor stdValueCursor = database.rawQuery(sql, null);

            stdValueCursor.moveToFirst();
            //peekTable(stdValueCursor);

            //Cursor stdValueCursor = getSTDValue(profile);

            for (i = 4; i < dbCursor.getColumnCount(); i++) {
                String nuID = dbCursor.getColumnName(i);

                /* get nu name string from nutr_def table */
                String sql_nu = "select * from NUTR_DEF where _Nutr_No = \'"
                        + nuID + "\';";
                Cursor nuCursor = database.rawQuery(sql_nu, null);
                nuCursor.moveToFirst();
                String nuName = nuCursor.getString(3) + " " + nuCursor.getString(1);

                double value = dbCursor.getDouble(i);
                value *= weightInOunces * 28.35 / 100;

                /* get the std value */
                double stdValue = 0;
                try {
                    int stdIndex = stdValueCursor.getColumnIndexOrThrow(nuID);
                    stdValue = stdValueCursor.getDouble(stdIndex);
                    stdValue = value * 100 / (stdValueCursor.getDouble(stdIndex));
                    if (nuID == "203") {//203 is protein
                        stdValue *= person.getWeight() * 453.59 / 1000;
                    }
                } catch (Exception e) {
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    Log.e("NutritionData.class", "getNutritionInformation()", e);
                }

                NutritionInformation ni = new NutritionInformation(nuName, value, nuCursor.getString(1), stdValue);
                info.add(ni);
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            Log.e("NutritionData.class", "getNutritionInformation()", e);
        }
        return info;
    }

    /* std_dvi_table: (gender, status), age */
    public Cursor getSTDValue(PersonProfile prof) {
        String status = prof.getStatus();
        String ageGroup = prof.getAgeGroup();

        String sql = "select * STD_DVI_TAB where STATUS = \'" + status + "\'  \'" +
                ageGroup + "\';";
        SQLiteDatabase database = NutritionTrackerApp.getDatabaseHelper().getDataBase();

        try {
            Cursor dbCursor = database.rawQuery(sql, null);
            return dbCursor;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            Log.e("NutritionData.class", "getSTDValue()", e);
        }

        return null;
    }

    public double getCalories() {
        return 0;

    }

    public ArrayList<NutritionInformation> getDailyNutritionInformation() {

        ArrayList<NutritionInformation> sumNutritionList;


        /* construct the time string */
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int date = Calendar.getInstance().get(Calendar.DATE);

        date = year * 10000 + month * 100 + date;

        /* query database get all today the food list */

        /*for each food, do the following and sum them up*/
        PersonProfile prof = PersonProfile.getPersonProfile();
        return (new NutritionData(prof, food_name, weightInOunces)).getNutritionInformation();
    }

    class NutritionInformation {
        String nutritionDescription;
        double weightValue;
        String weightUnit;
        double percentageFDA;


        NutritionInformation(String nutritionDescription, double weightValue, String weightUnit,
                             double percentageFDA) {
            this.nutritionDescription = nutritionDescription;
            this.weightValue = weightValue;
            this.weightUnit = weightUnit;
            this.percentageFDA = percentageFDA;
        }

        public String getNutritionDescription() {
            return nutritionDescription;
        }

        public void setNutritionDescription(String nutritionDescription) {
            this.nutritionDescription = nutritionDescription;
        }

        public double getWeightValue() {
            return weightValue;
        }

        public void setWeightValue(double weightValue) {
            this.weightValue = weightValue;
        }

        public double getPercentageFDA() {
            return percentageFDA;
        }

        public void setPercentageFDA(double percentageFDA) {
            this.percentageFDA = percentageFDA;
        }
    }
}
