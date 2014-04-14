package net.tigerparents.nut;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by piaw on 4/8/2014.
 */
public class NutritionTrackerApp extends Application {
    private static Context context;
    private static DataBaseHelper db_helper;

    public static void setupAppDatabase() {
        db_helper = new DataBaseHelper(context);
        try {
            db_helper.createDataBase();
        } catch (IOException e) {
            Log.e("NutritionTrackerApp", "setupAppDatabase unable to create", e);
            throw new Error("Unable to create database");
        }

        try {
            db_helper.openDataBase(SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLException e) {
            Log.e("NutritionTrackerApp", "setupAppDatabase unable to open", e);
            throw e;
        }

        db_helper.getWritableDatabase();
        createAllTables();
    }

    public static DataBaseHelper getDatabaseHelper() {
        return db_helper;
    }

    public static void execSQL(String sql) {
        try{
            db_helper.getDataBase().beginTransaction();
            db_helper.getDataBase().execSQL(sql);
            db_helper.getDataBase().setTransactionSuccessful();
            db_helper.getDataBase().endTransaction();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            Log.e("NutritionTrackerApp", "createAllTables", e);
        }

    }


    public static void getAllTables() {
        String SQL_GET_ALL_TABLES = "SELECT name FROM " +
                "sqlite_master WHERE type='table' ORDER BY name";
        Cursor cursor = db_helper.getDataBase().rawQuery(SQL_GET_ALL_TABLES, null);
        System.out.print("test");
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                String tablename = cursor.getString(0);
                System.out.println(tablename);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
    }

    public static void writeDailySTDTable(){

        try {
            InputStream input = getAppContext().getAssets().open("std_daily_table.txt");
            BufferedReader br=new BufferedReader(new InputStreamReader(input));
            String line;

            getAllTables();

            db_helper.getDataBase().beginTransaction();

            while((line = br.readLine()) != null) {
                /* get database column names and count */
                ContentValues contentValues = new ContentValues();

                Cursor cursor = db_helper.getDataBase().rawQuery("select * from DAILY_STD_NUTR_TABLE", null);
                String[] col_names = cursor.getColumnNames();

                String[] words = line.split("\t");

                for (int i = 0; i < words.length; i++) {
                    contentValues.put("\'"+col_names[i]+"\'", words[i]);
                }
                System.out.print("test");
                db_helper.getDataBase().insert("DAILY_STD_TABLE", null, contentValues);
            }
            db_helper.getDataBase().setTransactionSuccessful();
            db_helper.getDataBase().endTransaction();


        } catch (Exception e) {
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    Log.e("NutritionTrackerApp", "writeDailySTDTable", e);
        }
    }

    public static void createAllTables() {
        String sql;

        /* create daily std nutrition table */
        sql = "create table DAILY_STD_NUTR_TABLE ( " +
                "_status STRING PRIMARY KEY, " +
                " age_group STRING, " +
                " \"301\" DOUBLE, " +
                " \"001\" DOUBLE, " +
                " \"312\" DOUBLE, " +
                " \"313\" DOUBLE, " +
                " \"002\" DOUBLE, " +
                " \"303\" DOUBLE, " +
                " \"304\" DOUBLE, " +
                " \"315\" DOUBLE, " +
                " \"003\" DOUBLE, " +
                " \"305\" DOUBLE, " +
                " \"317\" DOUBLE, " +
                " \"309\" DOUBLE, " +
                " \"306\" DOUBLE, " +
                " \"307\" DOUBLE, " +
                " \"004\" DOUBLE, " +
                " \"320\" DOUBLE, " +
                " \"401\" DOUBLE, " +
                " \"328\" DOUBLE, " +
                " \"323\" DOUBLE, " +
                " \"430\" DOUBLE, " +
                " \"404\" DOUBLE, " +
                " \"405\" DOUBLE, " +
                " \"406\" DOUBLE, " +
                " \"415\" DOUBLE, " +
                " \"432\" DOUBLE, " +
                " \"418\" DOUBLE, " +
                " \"420\" DOUBLE, " +
                " \"005\" DOUBLE, " +
                " \"421\" DOUBLE " +
                ");";
        execSQL(sql);

         /* create person profile table */
        sql = "create table PERSON_PROFILE_TABLE (" +
                "_name STRING PRIMARY KEY, " +
                "birth INT, " +
                "gender STRING, " +
                "status STRING, " +
                "notes STRING )";
        execSQL(sql);

        /* create daily food log */
        sql = "create table DAILY_FOOD_LOG (" +
                    "_name STRING, " +
                    "date INT, " +
                    "food_name STRING, " +
                    "weight DOUBLE )";
        execSQL(sql);

        /* create weekly food log */
        sql = "create table WEEKLY_FOOD_LOG (" +
                    "date INT, " +
                    "food_id STRING, " +
                    "weight DOUBLE )";
        execSQL(sql);

        writeDailySTDTable();

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
            Cursor cursor = db_helper.getDataBase().rawQuery(sql, null);
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

