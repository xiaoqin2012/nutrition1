package net.tigerparents.nut.DataBaseHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by xiaoqin on 4/20/2014.
 */
public class LogDataBaseHelper extends DataBaseHelper {

    private static final int DATABASE_VERSION = 1;
    public static String person_profile_tab_name = "PERSON_PROFILE_TABLE";
    public static String daily_food_log = "DAILY_FOOD_LOG";
    public static String weekly_food_log = "WEEKLY_FOOD_LOG";

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

        /* create daily food log */
        table_name = daily_food_log;
        sql = "create table if not exists " + table_name + "  (" +
                "_date INT, " +
                "food_name STRING, " +
                "weight DOUBLE )";
        execSQL(sql, table_name);

        /* create weekly food log */
        table_name = weekly_food_log;
        sql = "create table if not exists " + table_name + " (" +
                "_date INT, " +
                "food_name STRING, " +
                "weight DOUBLE )";
        execSQL(sql, table_name);
    }
}
