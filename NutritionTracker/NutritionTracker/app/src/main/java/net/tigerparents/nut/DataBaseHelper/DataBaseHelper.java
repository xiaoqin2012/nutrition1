
package net.tigerparents.nut.DataBaseHelper;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import net.tigerparents.nut.Log;

/**
 * Created by xiaoqin on 4/9/2014.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    public static String SYS_PATH = "/data/data/net.tigerparents.nut/databases/";
    public String DB_NAME;
    public String DB_PATH;
    public SQLiteDatabase myDataBase = null;
    protected Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     *
     * @param context
     */
    public DataBaseHelper(Context context, String DB_NAME, int DATABASE_VERSION) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        this.myContext = context;
        this.DB_NAME = DB_NAME;
        this.DB_PATH = SYS_PATH;
        openOrCreateDataBase(SQLiteDatabase.OPEN_READWRITE);
    }

    public boolean checkDataBase() {
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

    public void createDataBase() {
    }

    public void createAllTables() {

    }

    public SQLiteDatabase getDataBase() {
        if (myDataBase == null)
            openOrCreateDataBase(SQLiteDatabase.OPEN_READWRITE);
        return myDataBase;
    }

    public Context getContext() {
        return myContext;
    }

    public void openOrCreateDataBase(int mode) {
        //Open the database
        String myPath = DB_PATH + DB_NAME;
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            createDataBase();
        }

        if (myDataBase == null) {
            myDataBase = SQLiteDatabase.openDatabase(myPath, null, mode);
        }

        createAllTables();
    }

    public SQLiteDatabase getMyDataBase() {
        if (myDataBase == null)
            openOrCreateDataBase(SQLiteDatabase.OPEN_READWRITE);
        return myDataBase;
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

    public boolean execSQL(String sql, String table_name) {
        /* give table name for create tables */
        try {
            getDataBase().beginTransaction();
            getDataBase().execSQL(sql);
            getDataBase().setTransactionSuccessful();
            getDataBase().endTransaction();
            return true;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            Log.e(e.getClass().getName(), e.getMessage(), e);
            return false;
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

    public void readTable(String table_name) {
        String sql = "select * from " + table_name + ";";
        try {
            Cursor cursor = getDataBase().rawQuery(sql, null);

            int count = cursor.getCount();

            if (!cursor.moveToFirst()) {
                System.out.print(count);
            }

            while (!cursor.isAfterLast()) {
                String status = cursor.getString(0);
                String age_group = cursor.getString(1);
                double val1 = cursor.getDouble(2);
                double val13 = cursor.getDouble(13);
                cursor.moveToNext();
            }
        } catch (Exception e) {
            Log.v(e.getClass().getName(), e.getMessage(), e);
        }
    }
}
