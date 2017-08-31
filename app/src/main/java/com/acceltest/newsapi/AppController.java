package com.acceltest.newsapi;

import android.app.Application;
import android.content.Context;

/**
 * Created by karthi on 31/07/15.
 */
public class AppController extends Application {

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static Context getAppContext() {
        return mInstance.getApplicationContext();
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }


}
