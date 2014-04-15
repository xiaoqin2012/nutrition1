package net.tigerparents.nut;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by xiaoqin on 4/11/2014.
 */
public class PersonProfile {
    String name;
    int birth; //19751127
    String gender;
    String status = null;
    double weight;
    String notes;
    //calculated:
    int age;
    String measure; //year or month all less than 1 year, treat as 1 year old.
    String ageGroup;

    public PersonProfile(String name, int birth, String gender, boolean isPregnancy, boolean isLactation, double weight) {
        this.name = name;
        this.birth = birth;
        this.gender = gender;
        if (isPregnancy)
            this.status = "pregnancy";
        else if (isLactation)
            this.status = "lactation";
        else status = gender;
        this.weight = weight;
        setAgeInfo();
    }

    public PersonProfile(String name, int birth, String gender, String status, double weight) {
        this.name = name;
        this.birth = birth;
        this.gender = gender;
        this.status = status;
        this.weight = weight;
        setAgeInfo();
    }

    public static boolean profileEntered() {
        String sql = "select * from PERSON_PROFILE_TABLE";
        try {
            Cursor dbCursor = NutritionTrackerApp.getDatabaseHelper().getDataBase().rawQuery(sql, null);
            if (dbCursor.moveToFirst() && dbCursor != null) {
                return true;
            }
        } catch (Exception e) {
            Log.e(e.getClass().getName(), e.getMessage(), e);
        }

        return false;
    }

    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    public static PersonProfile getPersonProfile() {
        try {
        /* query personprofile database by default, using the first one */
            String sql = "select * from PERSON_PROFILE_TABLE";
            SQLiteDatabase database = NutritionTrackerApp.getDatabaseHelper().getDataBase();

            Cursor dbCursor = database.rawQuery(sql, null);
            if (dbCursor.moveToFirst() && dbCursor != null) {
                return new PersonProfile(dbCursor.getString(0),
                        dbCursor.getInt(1),
                        dbCursor.getString(2),
                        dbCursor.getString(3),
                        dbCursor.getDouble(4));
            } else return null;
        } catch (Exception e) {
            Log.e(e.getClass().getName(), e.getMessage(), e);
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public int getBirth() {
        return birth;
    }

    public String getGender() {
        return gender;
    }

    public double getWeight() {
        return weight;
    }

    public String getStatus() {
        return gender;

        /*if (status != null)
            return status;
        else return gender; */
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeInfo() {
        int currYear = Calendar.getInstance().get(Calendar.YEAR);
        age = currYear - birth;

        if (isBetween(age, 0, 3)) ageGroup = "1-3";
        else if (isBetween(age, 4, 8)) ageGroup = "4-8";
        else if (isBetween(age, 9, 13)) ageGroup = "9-13";
        else if (isBetween(age, 14, 18)) ageGroup = "14-18";
        else if (isBetween(age, 19, 30)) ageGroup = "19-30";
        else if (isBetween(age, 31, 50)) ageGroup = "31-50";
        else if (isBetween(age, 51, 70)) ageGroup = "51-70";
        else ageGroup = ">70";
        return;
    }

    public void savePersonProfile() {
        String sql = "insert into PERSON_PROFILE_TABLE (_name, birth, gender, status, weight) " +
                "values ( " + "\'" + name + "\', " +
                +birth + ", " +
                "\'" + gender + "\', " +
                "\'" + status + "\', " +
                +weight +
                " ); ";

        NutritionTrackerApp.getDatabaseHelper().execSQL(sql, null);
    }
}
