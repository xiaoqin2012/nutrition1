package net.tigerparents.nut;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by piaw on 4/8/2014.
 */
public class NutritionTrackerApp extends Application {
    private static Context context;
    private static SQLiteDatabase db;

    public static void setupAppDatabase() {
        DataBaseHelper myDbHelper = new DataBaseHelper(context);
        try {
            myDbHelper.createDataBase();
        } catch (IOException e) {
            Log.e("NutritionTrackerApp", "setupAppDatabase unable to create", e);
            throw new Error("Unable to create database");
        }

        try {
            myDbHelper.openDataBase(SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLException e) {
            Log.e("NutritionTrackerApp", "setupAppDatabase unable to open", e);
            throw e;
        }

        db = myDbHelper.getWritableDatabase();
        createAllTables();
    }

    public static void createAllTables() {
        String sql;
        try{
            /* create people info table */
            sql = "create table PEP_INFO_DATA (" +
                    "_name STRING PRIMARY KEY, " +
                    "birth INT, " +
                    "status STRING, " +
                    "gender STRING, " +
                    "notes STRING )";
            db.execSQL(sql);

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            Log.e("NutritionTrackerApp", "createAllTables", e);
        }

        try {  /* create daily food log table */
            sql = "create table DAILY_FOOD_LOG (" +
                    "_name STRING, " +
                    "date INT, " +
                    "food_id STRING, " +
                    "weight DOUBLE )";
            db.execSQL(sql);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            Log.e("NutritionTrackerApp", "createAllTables", e);
        }

        try {
          /* create weekly food bag log table */
          /* date:food_id:weight */
            sql = "create table WEEKLY_FOOD_LOG (" +
                    "date INT, " +
                    "food_id STRING, " +
                    "weight DOUBLE )";
            db.execSQL(sql);
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            Log.e("NutritionTrackerApp", "createAllTables", e);
        }

        System.out.println("Table created successfully");
    }


    public static Context getAppContext() {
        return NutritionTrackerApp.context;
    }

    public static ArrayList<String> getFoodNames() {
        ArrayList<String> food_list = new ArrayList<String>();
        String sql;
        ResultSet rs;

        try {
            sql = "SELECT _id, Long_Desc FROM FOOD_NUT_DATA;";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    food_list.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return food_list;
        }

        System.out.println("successfully");
        return food_list;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NutritionTrackerApp.context = getApplicationContext();
        setupAppDatabase();
    }

    /* query people_info_table show the current people and show the dialog to add more people */
    public void setupAccountShow() {
    }
}

