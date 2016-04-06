package com.mememachine.mike.laboratorynotehelper.general;

import android.app.Application;
import android.content.Context;

public class GenAppContext extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        GenAppContext.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return GenAppContext.context;
    }
}