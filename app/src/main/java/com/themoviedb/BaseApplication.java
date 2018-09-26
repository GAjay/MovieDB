package com.themoviedb;

import android.app.Application;

import com.themoviedb.components.ApplicationComponent;
import com.themoviedb.components.DaggerApplicationComponent;
import com.themoviedb.modules.ApplicationModule;

/**
 * Created by Ajay Kumar Maheshwari.
 */

public class BaseApplication extends Application {

    private static BaseApplication instance;

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule())
                .build();

    }

    public static BaseApplication getInstance() {
        return instance;
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
