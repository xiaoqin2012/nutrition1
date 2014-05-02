package net.tigerparents.nut.DataBaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import net.tigerparents.nut.Log;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by xiaoqin on 4/20/2014.
 */
public class USDADataBaseHelper extends DataBaseHelper {

    private static final int DATABASE_VERSION = 1;
    public static String food_nutr_tab_name = "FOOD_NUT_DATA";
    public static String nutr_desc_tab_name = "NUTR_DEF";
    public static String daily_std_tab_name = "DAILY_STD_NUTR_TABLE";
    public static String top_food_tab_name = "TOP_FOOD_TABLE";

    public static String usda_file_name = "std_daily_table.txt";
    public static String top_food_file_name = "top_food.txt";

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     *
     * @param context
     */
    public USDADataBaseHelper(Context context) {
        super(context, "food.db", DATABASE_VERSION);
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase() {
        try {
            this.getReadableDatabase();
            InputStream myInput = myContext.getAssets().open(DB_NAME);

            // Path to the just created empty db
            String outFileName = DB_PATH + DB_NAME;
            //Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);
            copyDataBase(myInput, myOutput);
        } catch (IOException e) {
            throw new Error("Error copying database");
        }
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase(InputStream myInput, OutputStream myOutput) throws IOException {

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

    public void createAllTables() {
        createFDATable();
        createTopFoodTable();
    }

    public void createTopFoodTable() {
        String table_name = top_food_tab_name;
        String sql;

        sql = "drop table " + top_food_tab_name + ";";

        sql = "create table if not exists " + table_name + " ( " +
                "_nu_id STRING, " +
                "food_list STRING, " +
                "url1 STRING, " +
                "url2 STRING " + ");";
        execSQL(sql, table_name);
        writeToTable(top_food_file_name, table_name);
    }

    public void writeTopFoodTable() {

    }

    public void createFDATable() {
        String sql;
        /* create daily std nutrition table */
        String table_name = daily_std_tab_name;
        sql = "create table if not exists " + table_name + " ( " +
                "_status STRING , " +
                " age_group STRING, " +
                " \"291\" DOUBLE, " +
                " \"301\" DOUBLE, " +
                " \"205\" DOUBLE, " +
                " \"203\" DOUBLE, " +
                " \"320\" DOUBLE, " +
                " \"401\" DOUBLE, " +
                " \"328\" DOUBLE, " +
                " \"323\" DOUBLE, " +
                " \"404\" DOUBLE, " +
                " \"405\" DOUBLE, " +
                " \"406\" DOUBLE, " +
                " \"415\" DOUBLE, " +
                " \"417\" DOUBLE, " +
                " \"418\" DOUBLE, " +
                " \"312\" DOUBLE, " +
                " \"002\" DOUBLE, " +
                " \"303\" DOUBLE, " +
                " \"304\" DOUBLE, " +
                " \"003\" DOUBLE, " +
                " \"305\" DOUBLE, " +
                " \"317\" DOUBLE, " +
                " \"309\" DOUBLE " +
                ");";
        execSQL(sql, table_name);
        writeToTable(usda_file_name, table_name);
    }

    public void writeToTable(String input_file, String table_name) {

        try {
            InputStream input = myContext.getAssets().open(input_file);
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            String line;

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
                getDataBase().insert(table_name, null, contentValues);
            }
            getDataBase().setTransactionSuccessful();
            getDataBase().endTransaction();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            Log.e(e.getClass().getName(), e.getMessage(), e);
        }
    }
}

