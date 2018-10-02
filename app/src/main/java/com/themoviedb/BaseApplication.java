package com.themoviedb;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
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
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
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
