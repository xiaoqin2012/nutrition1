package net.tigerparents.nut;

import android.app.Application;
import android.content.Context;

import net.tigerparents.nut.DataBaseHelper.LogDataBaseHelper;
import net.tigerparents.nut.DataBaseHelper.USDADataBaseHelper;

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

