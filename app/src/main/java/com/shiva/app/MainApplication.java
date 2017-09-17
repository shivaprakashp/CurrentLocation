package com.shiva.app;

import android.app.Application;
import android.content.Context;
import android.location.Location;

/**
 * Created by OM on 17-09-2017.
 */

public class MainApplication extends Application {

    private static Context context;

    public static Location mCurrentLocation;
    public static String locationProvider;
    public static Location oldLocation;
    public static String locationTime;


    public static boolean activityVisible = true; // Variable that will check the

    public void onCreate() {
        super.onCreate();

        MainApplication.context = getApplicationContext();

    }

}
