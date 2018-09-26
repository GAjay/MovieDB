package com.themoviedb;

import android.support.test.runner.AndroidJUnit4;

import com.themoviedb.apis.entity.responses.Credit;
import com.themoviedb.apis.entity.responses.DiscoverResponseParser;
import com.themoviedb.apis.entity.responses.MovieParser;
import com.themoviedb.apis.entity.responses.Recommendation;
import com.themoviedb.apis.request.DiscoveryRequest;
import com.themoviedb.apis.retrofit.RestClient;
import com.themoviedb.models.DiscoverModel;
import com.themoviedb.models.MovieDetailModel;
import com.themoviedb.models.MovieModel;
import com.themoviedb.presenters.HomePresenter;
import com.themoviedb.presenters.MovieDetailPresenter;
import com.themoviedb.repositories.MovieRepository;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AppInstrumentedTest {

    private MovieRepository movieRepository;

    public AppInstrumentedTest() {

        //BaseApplication appContext = (BaseApplication) InstrumentationRegistry.getTargetContext();
        movieRepository = new MovieRepository();
    }

    @Test
    public void testHomePresenter() throws Exception {

        HomePresenter homePresenter = new HomePresenter();
        HomePresenter.HomeView homeView = new HomePresenter.HomeView() {

            @Override
            public void showMovies(List<MovieModel> movies, int minYear, int maxYear, String query) {

                assertNotEquals(minYear, 0);
                assertNotEquals(maxYear, 0);

                assertNotNull(movies);

                if(movies.size() > 0) {
                    assertNotNull(movies.get(0));
                }
            }

            @Override
            public void notifyMoviesListChanged() {

            }

            @Override
            public void showLoadingProgress() {

            }

            @Override
            public void hideLoadingProgress() {

            }

            @Override
            public void onError(Throwable e) {

            }
        };
        homePresenter.startNow(homeView);
    }

    @Test
    public void testMovieDetailPresenter() throws Exception {

        MovieDetailPresenter presenter = new MovieDetailPresenter();

        MovieDetailPresenter.MovieDetailView view = new MovieDetailPresenter.MovieDetailView() {
            @Override
            public void showMovieDetail(MovieDetailModel movieDetailModel, Credit credit, Recommendation recommendation) {

                assertNotNull(movieDetailModel);
            }

            @Override
            public void showLoadingProgress() {

            }

            @Override
            public void hideLoadingProgress() {

            }

            @Override
            public void onError(Throwable e) {

            }
        };
        presenter.startNow(view, 550);
    }

    @Test
    public void testTheMovieRepository() throws Exception {

        Observable<DiscoverModel> discoverObservable = movieRepository.discover(DiscoveryRequest.MIN_YEAR, DiscoveryRequest.MAX_YEAR, 1,null,null);

        discoverObservable.subscribe(new Observer<DiscoverModel>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(DiscoverModel discoverModel) {

                assertNotNull(discoverModel);

                List<MovieModel> movieParsers = discoverModel.getMovies();
                assertNotNull(movieParsers);

                if(movieParsers.size() > 0) {
                    assertNotNull(movieParsers.get(0));
                }
            }

            @Override
            public void onError(Throwable e) {

                fail("testTheMovieRepository thrown exception : " + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Test
    public void testTheMovieDbDiscoverApiResponse() {

        String apiKey = "1e457e1a40b0d8133c7ef44f74260961";
        int page = 1;
        String sortBy = "release_date.desc";
        String releaseDateLte = "2017-10-02";
        String releaseDateGte = "1900-01-01";
        String withOriginalLanguage = "en";

        Observable<Response<DiscoverResponseParser>> discoverObservable = RestClient.get().discover(apiKey, page,
                sortBy, releaseDateLte, withOriginalLanguage);

        discoverObservable = discoverObservable.subscribeOn(Schedulers.newThread());
        discoverObservable = discoverObservable.observeOn(AndroidSchedulers.mainThread());

        discoverObservable.subscribe(new Observer<Response<DiscoverResponseParser>>() {

            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Response<DiscoverResponseParser> response) {

                assertNotNull(response);

                DiscoverResponseParser body = response.body();
                assertNotNull(body);

                List<MovieParser> movieParsers = body.getMovieParsers();
                assertNotNull(movieParsers);

                if(movieParsers.size() > 0) {
                    assertNotNull(movieParsers.get(0));
                }
            }

            @Override
            public void onError(Throwable e) {

                fail("testTheMovieDbDiscoverApiResponse thrown exception : " + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
