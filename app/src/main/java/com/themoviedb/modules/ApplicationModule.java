package com.themoviedb.modules;

import com.themoviedb.presenters.HomePresenter;
import com.themoviedb.presenters.MovieDetailPresenter;
import com.themoviedb.repositories.MovieRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Ajay Kumar Maheshwari.
 */

@Module
public class ApplicationModule {

    @Provides
    @Singleton
    public HomePresenter providesHomePresenter() {
        return new HomePresenter();
    }

    @Provides
    @Singleton
    public MovieDetailPresenter providesMovieDetailPresenter() {
        return new MovieDetailPresenter();
    }

    @Provides
    @Singleton
    public MovieRepository providesMoviesRepository() {
        return new MovieRepository();
    }

}
