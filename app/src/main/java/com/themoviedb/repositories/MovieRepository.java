package com.themoviedb.repositories;

import android.text.TextUtils;
import android.util.Log;

import com.themoviedb.apis.entity.responses.CombineResult;
import com.themoviedb.apis.entity.responses.Credit;
import com.themoviedb.apis.entity.responses.DiscoverResponseParser;
import com.themoviedb.apis.entity.responses.MovieDetailParser;
import com.themoviedb.apis.entity.responses.RecommendateResult;
import com.themoviedb.apis.entity.responses.Recommendation;
import com.themoviedb.apis.request.DiscoveryRequest;
import com.themoviedb.apis.retrofit.RestClient;
import com.themoviedb.models.DiscoverModel;
import com.themoviedb.models.MovieDetailModel;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by Ajay Kumar Maheshwari.
 */

public class MovieRepository {

    private static final String API_KEY = "1e457e1a40b0d8133c7ef44f74260961";

    public Observable<CombineResult> getMovieDetail(int id) {

        Observable<Response<MovieDetailParser>> observableA = RestClient.get().getMovieDetail(id, "videos", API_KEY);

        Observable<Response<Credit>> observableB = RestClient.get().getMovieCredits(id, API_KEY);
        Observable<Response<Recommendation>> observableC = RestClient.get().getRecommendations(id, API_KEY);

        observableA = observableA.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
        observableB = observableB.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
        observableC = observableC.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());


        Observable<CombineResult> responseBodyObservable = Observable.zip(observableA, observableB, observableC,
                new Function3<Response<MovieDetailParser>, Response<Credit>, Response<Recommendation>, CombineResult>() {
                    @Override
                    public CombineResult apply(Response<MovieDetailParser> movieDetailParserResponse, Response<Credit> creditResponse, Response<Recommendation> recommendationResponse) throws Exception {
                        if (movieDetailParserResponse.isSuccessful()) {
                            MovieDetailParser parser = movieDetailParserResponse.body();
                            Credit parserCredit = creditResponse.body();
                            Recommendation parserRecommendation = recommendationResponse.body();
                            return new CombineResult(parser, parserCredit, parserRecommendation);
                        }
                        return null;
                    }
                });

        return responseBodyObservable;
    }


    public Observable<DiscoverModel> discover(int yearStart, int yearEnd, int page, String sortBy, String query) {


        if (yearStart > yearEnd) {
            int temp = yearStart;
            yearEnd = temp;
            sortBy = (null == sortBy) ? "popularity.desc" : sortBy;
        }

        String releaseDateLte = yearEnd + "";
        String withOriginalLanguage = "en";

        DiscoveryRequest request = new DiscoveryRequest();
        request.setPage(page);
        //set searched query
        request.setQuery(query);
        request.setReleaseDateLte(releaseDateLte);
        request.setSortBy(sortBy);
        request.setWithOriginalLanguage(withOriginalLanguage);

        return discover(request);
    }

    private Observable<DiscoverModel> discover(DiscoveryRequest discoveryRequest) {

        String sortBy = discoveryRequest.getSortBy();
        String releaseDateLte = discoveryRequest.getReleaseDateLte();
        String withOriginalLanguage = discoveryRequest.getWithOriginalLanguage();
        //get searching query
        String query = discoveryRequest.getQuery();
        int page = discoveryRequest.getPage();

        Log.d("DiscoveryRequest", "DiscoveryRequest" +
                "\npage : " + page +
                "\nprimary_release_year : " + releaseDateLte +
                "\nlanguage : " + withOriginalLanguage +
                "\nsortBy : " + sortBy);
        Observable<Response<DiscoverResponseParser>> discoverObservable = null;
        if (TextUtils.isEmpty(query)) {
            //discover service calling.
            discoverObservable =
                    RestClient.get().discover(API_KEY, page, sortBy, releaseDateLte, withOriginalLanguage);
        } else {
            //searching service calling
            discoverObservable=RestClient.get().searchMovie(API_KEY,page,query,
                    withOriginalLanguage);
        }
        discoverObservable = discoverObservable.subscribeOn(Schedulers.newThread());
        discoverObservable = discoverObservable.observeOn(AndroidSchedulers.mainThread());

        return discoverObservable.map(new Function<Response<DiscoverResponseParser>, DiscoverModel>() {

            @Override
            public DiscoverModel apply(@NonNull Response<DiscoverResponseParser> response) throws Exception {

                if (response.isSuccessful()) {
                    DiscoverResponseParser parser = response.body();
                    return new DiscoverModel(parser);
                }

                return null;
            }
        });
    }
}
