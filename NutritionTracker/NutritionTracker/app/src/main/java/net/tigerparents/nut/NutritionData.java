package net.tigerparents.nut;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by xiaoqin on 4/10/2014.
 */
public class NutritionData {
    public String food_name;
    public int weightInOunces;
    PersonProfile profile;

    public NutritionData(PersonProfile prof, String food_name, int weightInOunces) {
        this.food_name = food_name;
        this.weightInOunces = weightInOunces;
        this.profile = prof;
    }

    public static NutritionData getNutritionDataFor(String food_name, int weightInOunces) {
        PersonProfile prof = PersonProfile.getPersonProfile();
        return new NutritionData(prof, food_name, weightInOunces);
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

        Cursor nutrNameCursor = database.rawQuery("select * from NUTR_DEF", null);

        int i = 0;

        String sql = "select * from FOOD_NUT_DATA where Long_Desc = \'" + food_name + "\';";
        Cursor dbCursor = database.rawQuery(sql, null);
        Cursor stdValueCursor = getSTDValue(profile);

        nutrNameCursor.moveToFirst();
        do {
            String nutrNameString = nutrNameCursor.getString(1);

            String sql_nutr = "select * from NUTR_DEF where Nutr_No = \'"
                    + nutrNameString + "\';";
            Cursor nutrCursor = database.rawQuery(sql_nutr, null);
            String nutrName = nutrCursor.getString(4) + " " + nutrCursor.getString(2);

            int index = dbCursor.getColumnIndexOrThrow(nutrNameString);
            double value = dbCursor.getDouble(index) * 28.35 / 100;

            /* get the std value */
            int stdIndex = stdValueCursor.getColumnIndexOrThrow(nutrNameString);
            double stdValue = value * 100 / (stdValueCursor.getDouble(stdIndex));

            NutritionInformation ni = new NutritionInformation(nutrName, value, 0.0);
            info.add(ni);
        } while (dbCursor.moveToNext());

        return info;
    }

    /* std_dvi_table: (gender, status), age */
    public Cursor getSTDValue(PersonProfile prof) {
        String status = prof.getStatus();
        String ageGroup = prof.getAgeGroup();

        String sql = "select * STD_DVI_TAB where STATUS = \'" + status + "\'  \'" +
                ageGroup + "\';";
        SQLiteDatabase database = NutritionTrackerApp.getDatabaseHelper().getDataBase();
        Cursor dbCursor = database.rawQuery(sql, null);

        return dbCursor;
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
        double percentageFDA;

        NutritionInformation(String nutritionDescription, double weightValue,
                             double percentageFDA) {
            this.nutritionDescription = nutritionDescription;
            this.weightValue = weightValue;
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
