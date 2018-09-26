package com.themoviedb.components;

import com.themoviedb.activities.HomeActivity;
import com.themoviedb.activities.MovieDetailActivity;
import com.themoviedb.modules.ApplicationModule;
import com.themoviedb.presenters.HomePresenter;
import com.themoviedb.presenters.MovieDetailPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    void inject(HomeActivity homeActivity);
    void inject(HomePresenter homePresenter);
    void inject(MovieDetailPresenter movieDetailPresenter);
    void inject(MovieDetailActivity movieDetailActivity);
}
