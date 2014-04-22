package net.tigerparents.nut;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;

import net.tigerparents.nut.DataBaseHelper.LogDataBaseHelper;
import net.tigerparents.nut.DataBaseHelper.USDADataBaseHelper;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by piaw on 4/8/2014.
 */
public class NutritionTrackerApp extends Application {
    private static Context context;
    private static USDADataBaseHelper usda_db_helper;
    private static LogDataBaseHelper log_db_helper;

    public static void setupAppDatabase() {
        usda_db_helper = new USDADataBaseHelper(context);
        log_db_helper = new LogDataBaseHelper(context);
    }

    public static USDADataBaseHelper getUSDADatabaseHelper() {
        return usda_db_helper;
    }

    public static LogDataBaseHelper getLogDatabaseHelper() {
        return log_db_helper;
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
            Cursor cursor = usda_db_helper.getDataBase().rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    String food_name = cursor.getString(1);
                    if (!food_name.startsWith("Babyfood"))
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

