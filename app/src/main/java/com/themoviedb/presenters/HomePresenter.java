package com.themoviedb.presenters;

import android.text.TextUtils;
import android.util.Log;

import com.themoviedb.BaseApplication;
import com.themoviedb.R;
import com.themoviedb.apis.request.DiscoveryRequest;
import com.themoviedb.apis.retrofit.AppUrls;
import com.themoviedb.models.DiscoverModel;
import com.themoviedb.models.MovieModel;
import com.themoviedb.repositories.MovieRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Ajay Kumar Maheshwari.
 */
@NonConfigurationScope
public class HomePresenter {

    @Inject
    MovieRepository repository;
    private int page = 1;
    private int minYear = DiscoveryRequest.THIS_YEAR;
    private int maxYear = DiscoveryRequest.MIN_YEAR;
    private int totalPages = 1;
    private HomeView homeView=null;
    private String sortBy;

    private String query = null;
    //sorting constants
    private String popDesc = "popularity.desc";
    private String popAsc = "popularity.asc";
    private String voteAsc = "vote_average.desc";
    private String voteDesc = "vote_average.asc";
    //end
    private List<MovieModel> movies = new ArrayList<>();
    private boolean loading;
    public HomePresenter() {
        BaseApplication.getInstance().getApplicationComponent().inject(this);
    }

    public void startNow(HomeView homeView) {
        this.homeView = homeView;
        if (movies != null && movies.size() > 0) {
            Log.d("HomePresenter", "Showing cashed minYear : " + minYear + ", maxYear : " + maxYear + ", page : " + page);
            homeView.showMovies(movies, minYear, maxYear, query);
            return;
        }
        fetchFirst();
    }

    public void fetchFirst() {
        resetFilters();
        System.out.println("queryssdsd" + query);
        fetchMovies(minYear, maxYear, page, sortBy, query);
    }

    public void fetchNext() {
        int nextPage = ++page;
        if (nextPage <= totalPages) {
            fetchMovies(minYear, maxYear, nextPage, sortBy, query);
        }
    }

    public void filterMovieList(int startYear, int endYear, String sortBy, String query) {

        if (this.minYear == startYear && this.maxYear == endYear && this.sortBy.equalsIgnoreCase(sortBy) && this.query.equalsIgnoreCase(query)) {
            return;
        }

        resetFilters();
        this.minYear = startYear;
        this.maxYear = endYear;
        this.sortBy = sortBy;
        this.query = query;
        fetchMovies(startYear, endYear, page, sortBy, query);
    }

    /**
     * A method to return sort by value from spinner.
     *
     * @param text
     * @return
     */
    private String getSortBy(String text) {
        String sortBy = null;
        switch (text) {
            case AppUrls.SortPopDesc:
                sortBy = popDesc;
                break;
            case AppUrls.SortPopASC:
                sortBy = popAsc;
                break;
            case AppUrls.SortRateDesc:
                sortBy = voteDesc;
                break;
            case AppUrls.SortRateAsc:
                sortBy = voteAsc;
                break;
            default:
                sortBy = popDesc;
                break;
        }
        return sortBy;
    }


    private void resetFilters() {
        page = 1;

        page = 1;
        minYear = DiscoveryRequest.THIS_YEAR;
        maxYear = DiscoveryRequest.MIN_YEAR;
        sortBy = popDesc;
        query = "";
        movies.clear();
        homeView.notifyMoviesListChanged();
    }

    private void fetchMovies(final int minYear, final int maxYear, final int nextPage, String sortby, final String query) {

        Log.d("HomePresenter", "Fetching from API : minYear : " + minYear + ", maxYear : " + maxYear + ", page : " + nextPage);
        Observable<DiscoverModel> observable = repository.discover(minYear, maxYear, nextPage, getSortBy(sortby), query);


        observable.subscribe(new Observer<DiscoverModel>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                loading = true;
                homeView.showLoadingProgress();

            }

            @Override
            public void onNext(@NonNull DiscoverModel discoverModel) {

                if (discoverModel == null) {
                    return;
                }
                page = discoverModel.getPage();
                totalPages = discoverModel.getTotalPages();

                List<MovieModel> newList = discoverModel.getMovies();
                if (newList != null) {
                    if (movies == null || movies.size() == 0) {
                        movies = newList;
                        homeView.showMovies(movies, minYear, maxYear, query);
                    } else {
                        movies.addAll(newList);
                        homeView.notifyMoviesListChanged();
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

                homeView.onError(e);
            }

            @Override
            public void onComplete() {
                loading = false;
                homeView.hideLoadingProgress();

            }
        });
    }

    public boolean isLoading() {
        return loading;
    }

    public interface HomeView {

        void showMovies(List<MovieModel> movies, int minYear, int maxYear, String query);

        void notifyMoviesListChanged();

        void showLoadingProgress();

        void hideLoadingProgress();

        void onError(Throwable e);
    }

    public void clear(){
        this.homeView=null;
        movies.clear();
        movies=null;

    }
}
