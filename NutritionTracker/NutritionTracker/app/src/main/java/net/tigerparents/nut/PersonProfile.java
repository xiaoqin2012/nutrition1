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
    boolean isPregnant;
    boolean isLactating;
    double weight;
    //calculated:
    int age;
    String ageGroup;
    String status;
    double daily_kcal;
    private int height;
    private double workout;

    public PersonProfile(String name, int birth, boolean gender, boolean isPregnant,
                         boolean isLactating, double weight, int height, double workout) {
        this.name = name;
        this.birth = birth;
        this.gender = gender ? "male" : "female";
        this.isPregnant = isPregnant;
        this.isLactating = isLactating;
        this.weight = weight;
        this.setHeight(height);
        this.setWorkout(workout);
        status = isLactating ? "lactation" : (isPregnant ? "pregnancy" : this.gender);
        setAgeInfo();
        setDailyKcal();
    }

    /*
    http://weightloss.about.com/od/eatsmart/a/blcalintake.htm
    calculation:
    If you are sedentary : BMR x 20 percent
    If you are lightly active: BMR x 30 percent
    If you are moderately active (You exercise most days a week.): BMR x 40 percent
    If you are very active (You exercise intensely on a daily basis or for prolonged periods.): BMR x 50 percent
    If you are extra active (You do hard labor or are in athletic training.): BMR x 60 percent
    Add this number to your BMR.
    */

    public static boolean profileEntered() {
        String sql = "select * from PERSON_PROFILE_TABLE";
        try {
            Cursor dbCursor = NutritionTrackerApp.getLogDatabaseHelper().getDataBase().rawQuery(sql, null);
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
            SQLiteDatabase database = NutritionTrackerApp.getLogDatabaseHelper().getDataBase();

            Cursor dbCursor = database.rawQuery(sql, null);
            if (dbCursor.moveToFirst() && dbCursor != null) {
                String name = dbCursor.getString(0);
                int birth_year = dbCursor.getInt(1);
                String gender = dbCursor.getString(2);
                String status = dbCursor.getString(3);
                double weight = dbCursor.getDouble(4);
                int height = dbCursor.getInt(5);
                double workout = dbCursor.getDouble(6);

                return new PersonProfile(name, birth_year, gender == "male",
                        status == "lactation", status == "pregnancy", weight, height, workout);
            } else
                return null;
        } catch (Exception e) {
            Log.e(e.getClass().getName(), e.getMessage(), e);
            return null;
        }
    }

    public boolean isPregnant() {
        return isPregnant;
    }

    public boolean isLactating() {
        return isLactating;
    }

    public String getName() {
        return name;
    }

    public int getBirth() {
        return birth;
    }

    public boolean getGender() {
        return gender == "male";
    }

    public double getWeight() {
        return weight;
    }


    public String getAgeGroup() {
        return ageGroup;
    }

    public double getDailyKcal() {
        return daily_kcal;
    }

    void setDailyKcal() {
        if (this.gender == "female") {
            daily_kcal = 665 + 5.3 * weight + 4.7 * height - 4.7 * age;
        } else {
            daily_kcal = 660 + 6.3 * weight + 12.9 * height - 6.8 * age;
        }

        daily_kcal *= (1 + workout / 100);
    }

    public void setAgeInfo() {
        int currYear = Calendar.getInstance().get(Calendar.YEAR);
        age = currYear - birth;

        if (isBetween(age, 0, 3)) {
            ageGroup = "3";
            status = "child";
        } else if (isBetween(age, 4, 8)) {
            ageGroup = "8";
            status = "child";
        } else if (isBetween(age, 9, 13)) ageGroup = "13";
        else if (isBetween(age, 14, 18)) ageGroup = "18";
        else if (isBetween(age, 19, 30)) ageGroup = "30";
        else if (isBetween(age, 31, 50)) ageGroup = "50";
        else if (isBetween(age, 51, 70)) ageGroup = "70";
        else ageGroup = "71";
        return;
    }

    public void savePersonProfile() {
        String status = getStatus();
        String sql = "delete from PERSON_PROFILE_TABLE; ";

        NutritionTrackerApp.getLogDatabaseHelper().execSQL(sql, null);

        sql = "insert or replace into PERSON_PROFILE_TABLE (_name, birth, gender, status, weight, height, workout) " +
                "values ( " + "\'" + name + "\', " +
                +birth + ", " +
                "\'" + gender + "\', " +
                "\'" + status + "\', " +
                +weight + ", " +
                +getHeight() + ", " +
                +getWorkout() +
                " ); ";

        NutritionTrackerApp.getLogDatabaseHelper().execSQL(sql, null);
    }

    public String getStatus() {
        return status;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getWorkout() {
        return workout;
    }

    public void setWorkout(double workout) {
        this.workout = workout;
    }
}
