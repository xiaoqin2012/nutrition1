package net.tigerparents.nut.DataBaseHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.tigerparents.nut.Log;
import net.tigerparents.nut.NutritionTrackerApp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by xiaoqin on 4/20/2014.
 */
public class LogDataBaseHelper extends DataBaseHelper {

    private static final int DATABASE_VERSION = 2;
    public static String person_profile_tab_name = "PERSON_PROFILE_TABLE";
    public static String daily_food_log = "DAILY_FOOD_LOG";
    public static String weekly_food_log = "WEEKLY_FOOD_LOG";
    public ArrayList<String> food_names;
    public Set<String> favorite_food_names;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     *
     * @param context
     */
    public LogDataBaseHelper(Context context) {
        super(context, "user_log.db", DATABASE_VERSION);
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */

    /* override */
    public void createDataBase() {
        try {
            myDataBase = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null, null);
        } catch (Exception e) {
            Log.e(e.getClass().getName(), e.getMessage(), e);
        }
    }

    public void createAllTables() {
        String sql;
        String table_name;

         /* create person profile table */
        table_name = person_profile_tab_name;
        sql = "create table if not exists " + table_name + " (" +
                "_name STRING PRIMARY KEY, " +
                "birth INT, " +
                "gender STRING, " +
                "status STRING, " +
                "weight DOUBLE " +
                ")";
        execSQL(sql, table_name);
        execSQL("ALTER TABLE " + table_name + " ADD COLUMN height INT ", table_name);
        execSQL("ALTER TABLE " + table_name + " ADD COLUMN workout DOUBLE ", table_name);

        /* create daily food log */
        table_name = daily_food_log;
        sql = "create table if not exists " + table_name + "  (" +
                "_date INT, " +
                "time STRING, " +
                "food_name STRING, " +
                "weight DOUBLE )";
        execSQL(sql, table_name);
        execSQL("ALTER TABLE " + table_name + " ADD COLUMN time STRING DEFAULT null", table_name);

        /* create weekly food log */
        table_name = weekly_food_log;
        sql = "create table if not exists " + table_name + " (" +
                "_date INT, " +
                "time STRING, " +
                "food_name STRING, " +
                "weight DOUBLE )";
        execSQL(sql, table_name);
        execSQL("ALTER TABLE " + table_name + " ADD COLUMN time STRING DEFAULT null", table_name);
        setFoodNames();
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("ALTER TABLE " + LogDataBaseHelper.daily_food_log + " ADD COLUMN time STRING DEFAULT null");
            db.execSQL("ALTER TABLE " + LogDataBaseHelper.weekly_food_log + " ADD COLUMN time STRING DEFAULT null");
        }
    }

    public ArrayList<String> getFoodNames() {
        if (food_names != null)
            return food_names;

        setFoodNames();
        return food_names;
    }

    public void setFoodNames() {
        food_names = new ArrayList<String>();
        favorite_food_names = new HashSet<String>();

        getFavoriteFoodNames(daily_food_log);
        getFavoriteFoodNames(weekly_food_log);

        String sql;
        try {
            sql = "SELECT _id, Long_Desc FROM FOOD_NUT_DATA;";
            Cursor cursor = NutritionTrackerApp.getUSDADatabaseHelper().getDataBase().rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    String food_name = cursor.getString(1);
                    if (food_name != null && !food_name.startsWith("Babyfood")
                            && !food_name.startsWith("Infant")
                            && favorite_food_names.contains(food_name) == false) {
                        food_names.add(cursor.getString(1));
                    }
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void getFavoriteFoodNames(String input_name) {
        String sql;

        sql = "select * from " + input_name + ";";
        try {
            sql = "SELECT * FROM " + input_name + " ;";
            Cursor cursor = getDataBase().rawQuery(sql, null);
            int index = cursor.getColumnIndex("food_name");
            if (cursor.moveToFirst()) {
                do {
                    String food_name = cursor.getString(index);
                    if (food_name != null
                            && favorite_food_names.contains(food_name) == false) {
                        favorite_food_names.add(food_name);
                        food_names.add(food_name);
                    }
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void updateFoodNames(String food_name) {
        if (favorite_food_names.contains(food_name))
            return;
        favorite_food_names.add(food_name);
        food_names.remove(food_name);
        food_names.add(0, food_name);
    }
}
