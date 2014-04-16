package net.tigerparents.nut;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by xiaoqin on 4/9/2014.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/net.tigerparents.nut/databases/";
    private static String DB_NAME = "food.db";
    private final Context myContext;
    public SQLiteDatabase myDataBase = null;
    String food_nutr_tab_name = "FOOD_NUT_DATA";
    String nutr_desc_tab_name = "NUTR_DEF";
    String daily_std_tab_name = "DAILY_STD_NUTR_TABLE";
    String person_profile_tab_name = "PERSON_PROFILE_TABLE";
    String daily_food_log = "DAILY_FOOD_LOG";
    String weekly_food_log = "WEEKLY_FOOD_LOG";

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     *
     * @param context
     */
    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    public SQLiteDatabase getDataBase() {
        if (myDataBase == null)
            myDataBase = super.getWritableDatabase();
        return myDataBase;
    }

    public Context getContext() {
        return myContext;
    }
    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            //do nothing - database already exist
        } else {
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            //database does't exist yet.
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase(int mode) throws SQLException {
        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, mode);
        createAllTables();
    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();

        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void execSQL(String sql, String table_name) {
        /* give table name for create tables */
        if (table_name != null) {
            try {
                getDataBase().beginTransaction();
                getDataBase().execSQL("drop table if exists " + table_name);
                getDataBase().setTransactionSuccessful();
                getDataBase().endTransaction();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                Log.e("NutritionTrackerApp", "createTable", e);
            }
        }

        try {
            getDataBase().beginTransaction();
            getDataBase().execSQL(sql);
            getDataBase().setTransactionSuccessful();
            getDataBase().endTransaction();

            if (table_name == daily_std_tab_name)
                writeDailySTDTable(table_name);

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            Log.e("NutritionTrackerApp", "createTable", e);
        }
    }

    public void createAllTables() {
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
        execSQL(sql, table_name);

         /* create person profile table */
        table_name = "PERSON_PROFILE_TABLE";
        sql = "create table " + table_name + " (" +
                "_name STRING PRIMARY KEY, " +
                "birth INT, " +
                "gender STRING, " +
                "status STRING, " +
                "weight DOUBLE " +
                ")";
        execSQL(sql, table_name);

        /* create daily food log */
        table_name = "DAILY_FOOD_LOG";
        sql = "create table " + table_name + "  (" +
                "_name STRING, " +
                "date INT, " +
                "food_name STRING, " +
                "weight DOUBLE )";
        execSQL(sql, table_name);

        /* create weekly food log */
        table_name = "WEEKLY_FOOD_LOG";
        sql = "create table " + table_name + " (" +
                "date INT, " +
                "food_id STRING, " +
                "weight DOUBLE )";
        execSQL(sql, table_name);
    }

    public void writeDailySTDTable(String table_name) {

        try {
            InputStream input = myContext.getAssets().open("std_daily_table.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            String line;

            //getAllTables();

            getDataBase().beginTransaction();

            while ((line = br.readLine()) != null) {
                /* get database column names and count */
                ContentValues contentValues = new ContentValues();

                Cursor cursor = getDataBase().rawQuery("select * from " + table_name, null);
                String[] col_names = cursor.getColumnNames();

                String[] words = line.split("\t");

                if (words.length == 0) continue;
                for (int i = 0; i < words.length; i++) {
                    contentValues.put("\'" + col_names[i] + "\'", words[i]);
                }
                System.out.print("test");
                if (contentValues != null)
                    getDataBase().insert(table_name, null, contentValues);
            }
            getDataBase().setTransactionSuccessful();
            getDataBase().endTransaction();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            Log.e("NutritionTrackerApp", "writeDailySTDTable", e);
        }
    }

    public void getAllTables() {
        String SQL_GET_ALL_TABLES = "SELECT name FROM " +
                "sqlite_master WHERE type='table' ORDER BY name";
        Cursor cursor = getDataBase().rawQuery(SQL_GET_ALL_TABLES, null);
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
}
