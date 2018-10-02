package com.themoviedb.presenters;

import android.util.Log;

import com.themoviedb.BaseApplication;
import com.themoviedb.apis.entity.responses.CombineResult;
import com.themoviedb.apis.entity.responses.Credit;
import com.themoviedb.apis.entity.responses.Recommendation;
import com.themoviedb.models.MovieDetailModel;
import com.themoviedb.repositories.MovieRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Ajay Kumar Maheshwari.
 */

public class MovieDetailPresenter {

    @Inject
    MovieRepository repository;

    private MovieDetailView movieDetailView;

    private MovieDetailModel movieDetail;

    private Credit credit;
    private Recommendation recommendation;

    private int movieId;

    public MovieDetailPresenter() {

        BaseApplication.getInstance().getApplicationComponent().inject(this);
    }

    public void startNow(MovieDetailView view, int movieId) {
        this.movieDetailView = view;
        this.movieId = movieId;
        if (movieDetail != null) {
            Log.d("MovieDetailPresenter", "Showing cashed ");
            view.showMovieDetail(movieDetail, credit, recommendation);
            return;
        }
        fetchMovie(movieId);
    }

    public void fetchMovie(int id) {

        Log.d("MovieDetailPresenter", "Fetching from API");

        Observable<CombineResult> observable = repository.getMovieDetail(id);
        observable.subscribe(new Observer<CombineResult>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                movieDetailView.showLoadingProgress();
            }

            @Override
            public void onNext(@NonNull CombineResult model) {

                if (model == null) {
                    return;
                }
                movieDetail=  new MovieDetailModel(model.getMovieDetailParser());
                credit = model.getCredit();
                recommendation= model.getRecommendation();

//                movieDetail = model;
                movieDetailView.showMovieDetail(movieDetail,credit,recommendation);
            }

            @Override
            public void onError(@NonNull Throwable e) {

                movieDetailView.onError(e);
            }

            @Override
            public void onComplete() {
                movieDetailView.hideLoadingProgress();
            }
        });
    }

    public void cleanUp() {
        movieId = 0;
        movieDetail = null;
        credit=null;
        movieDetailView=null;
        recommendation=null;
    }

    public interface MovieDetailView {

        void showMovieDetail(MovieDetailModel movieDetailModel, Credit credit, Recommendation recommendation);

        void showLoadingProgress();

        void hideLoadingProgress();

        void onError(Throwable e);
    }
}
