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

    public static void createTable(String sql, String table_name) {
        try {
            db_helper.getDataBase().beginTransaction();
            db_helper.getDataBase().execSQL("drop table if exists " + table_name);
            db_helper.getDataBase().setTransactionSuccessful();
            db_helper.getDataBase().endTransaction();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            Log.e("NutritionTrackerApp", "createTable", e);
        }

        try {
            db_helper.getDataBase().beginTransaction();
            db_helper.getDataBase().execSQL(sql);
            db_helper.getDataBase().setTransactionSuccessful();
            db_helper.getDataBase().endTransaction();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            Log.e("NutritionTrackerApp", "createTable", e);
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

    public static void writeDailySTDTable(String table_name) {

        try {
            InputStream input = getAppContext().getAssets().open("std_daily_table.txt");
            BufferedReader br=new BufferedReader(new InputStreamReader(input));
            String line;

            getAllTables();

            db_helper.getDataBase().beginTransaction();

            while((line = br.readLine()) != null) {
                /* get database column names and count */
                ContentValues contentValues = new ContentValues();

                Cursor cursor = db_helper.getDataBase().rawQuery("select * from " + table_name, null);
                String[] col_names = cursor.getColumnNames();

                String[] words = line.split("\t");

                for (int i = 0; i < words.length; i++) {
                    contentValues.put("\'"+col_names[i]+"\'", words[i]);
                }
                System.out.print("test");
                db_helper.getDataBase().insert(table_name, null, contentValues);
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
        String table_name;

        /* create daily std nutrition table */
        table_name = "DAILY_STD_NUTR_TABLE";

        sql = "create table " + table_name + " ( " +
                "_status STRING , " +
                " age_group STRING, " +
                " \"205\" DOUBLE, " +
                " \"291\" DOUBLE, " +
                " \"301\" DOUBLE, " +
                " \"421\" DOUBLE, " +
                " \"203\" DOUBLE, " +
                " \"320\" DOUBLE, " +
                " \"401\" DOUBLE, " +
                " \"328\" DOUBLE, " +
                " \"323\" DOUBLE, " +
                " \"404\" DOUBLE, " +
                " \"405\" DOUBLE, " +
                " \"406\" DOUBLE, " +
                " \"415\" DOUBLE, " +
                " \"432\" DOUBLE, " +
                " \"418\" DOUBLE, " +
                " \"312\" DOUBLE, " +
                " \"002\" DOUBLE, " +
                " \"303\" DOUBLE, " +
                " \"315\" DOUBLE, " +
                " \"003\" DOUBLE, " +
                " \"305\" DOUBLE, " +
                " \"317\" DOUBLE, " +
                " \"309\" DOUBLE " +
                ");";
        createTable(sql, table_name);
        writeDailySTDTable(table_name);

         /* create person profile table */
        table_name = "PERSON_PROFILE_TABLE";
        sql = "create table " + table_name + " (" +
                "_name STRING PRIMARY KEY, " +
                "birth INT, " +
                "gender STRING, " +
                "status STRING, " +
                "weight DOUBLE " +
                ")";
        db_helper.execSQL(sql, table_name);

        /* create daily food log */
        table_name = "DAILY_FOOD_LOG";
        sql = "create table " + table_name + "  (" +
                    "_name STRING, " +
                    "date INT, " +
                    "food_name STRING, " +
                    "weight DOUBLE )";
        db_helper.execSQL(sql, table_name);

        /* create weekly food log */
        table_name = "WEEKLY_FOOD_LOG";
        sql = "create table " + table_name + " (" +
                    "date INT, " +
                    "food_id STRING, " +
                    "weight DOUBLE )";
        db_helper.execSQL(sql, table_name);


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

