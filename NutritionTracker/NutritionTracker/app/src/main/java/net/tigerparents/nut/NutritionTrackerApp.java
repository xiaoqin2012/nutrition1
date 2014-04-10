package net.tigerparents.nut;

import android.app.Application;
import android.content.Context;

/**
 * Created by piaw on 4/8/2014.
 */
public class NutritionTrackerApp extends Application {
    private static Context context;

    @Override
    public void onCreate(){
        super.onCreate();
        NutritionTrackerApp.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return NutritionTrackerApp.context;
    }
}
