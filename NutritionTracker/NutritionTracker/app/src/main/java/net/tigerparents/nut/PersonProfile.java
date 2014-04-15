package net.tigerparents.nut;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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


    public PersonProfile(String name, int birth, String gender,
                         String status, double weight, String notes) {
        this.name = name;
        this.birth = birth;
        this.gender = gender;
        this.status = status;
        this.weight = weight;
        this.notes = notes;
        setAgeInfo();
        savePersonProfile();
    }

    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    public static PersonProfile getPersonProfile() {
        /* query personprofile database by default, using the first one */
        String sql = "select * from PERSON_PROFILE_TABLE";
        SQLiteDatabase database = NutritionTrackerApp.getDatabaseHelper().getDataBase();

        Cursor dbCursor = database.rawQuery(sql, null);
        if (dbCursor.moveToFirst() && dbCursor != null) {
            return new PersonProfile(dbCursor.getString(1),
                    dbCursor.getInt(2),
                    dbCursor.getString(3),
                    dbCursor.getString(4),

                    dbCursor.getDouble(5),
                    dbCursor.getString(6));
        }
        return new PersonProfile("xiaoqin", 19751127, "female", null, 120, null);
    }

    public String getStatus() {
        if (status != null)
            return status;
        else return gender;
    }

    public double getWeight() {
        return weight;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeInfo() {
        int currYear = Calendar.getInstance().get(Calendar.YEAR);
        int currMon = Calendar.getInstance().get(Calendar.MONTH);

        int year = birth / 10000;
        int month = (birth / 100) % 100;
        boolean lessOneyear = false;

        age = currYear - currMon;
        measure = "year";

        if (age < 1) {
            lessOneyear = true;
            month = currMon - month;
        }

        month = 12 - month + 1 + currMon;

        if (year == 1 && month < 12) lessOneyear = true;

        if (lessOneyear) {
            age = month;
            measure = "month";
            if (month < 6) ageGroup = "0-6m";
            else ageGroup = "6-12m";
            return;
        }

        if (isBetween(age, 1, 3)) ageGroup = "1-3";
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
        /*sql = "create table PERSON_PROFILE (" +
                "_name STRING PRIMARY KEY, " +
                "birth INT, " +
                "gender STRING, " +
                "status STRING, " +
                "weight DOUBLE, " +
                "notes STRING )"; */

        String sql = "insert into PERSON_PROFILE_TABLE (_name, birth, gender, status, weight, notes) " +
                "values ( " + "\'" + name + "\' " +
                +birth + " " +
                "\'" + gender + "\' " +
                +weight +
                "\'" + status + "\' " +
                "\'" + notes + "\'); ";

        NutritionTrackerApp.getDatabaseHelper().execSQL(sql, null);
    }
}
