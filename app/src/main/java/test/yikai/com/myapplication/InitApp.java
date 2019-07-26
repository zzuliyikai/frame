package test.yikai.com.myapplication;

import android.app.Application;

public class InitApp extends Application {

    private static InitApp mInitApp;

    public static InitApp getInstance(){
        return mInitApp;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mInitApp = this;
    }
}
