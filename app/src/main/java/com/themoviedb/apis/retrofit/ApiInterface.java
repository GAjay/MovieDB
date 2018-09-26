package com.themoviedb.apis.retrofit;

import com.themoviedb.apis.entity.responses.Credit;
import com.themoviedb.apis.entity.responses.DiscoverResponseParser;
import com.themoviedb.apis.entity.responses.MovieDetailParser;
import com.themoviedb.apis.entity.responses.Recommendation;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {

    @GET("discover/movie")
    Observable<Response<DiscoverResponseParser>> discover(@Query("api_key") String apiKey,
                                                          @Query("page") int page,
                                                          @Query("sort_by") String sortBy,
                                                          @Query("primary_release_year") String releaseDateLte,
                                                          @Query("language") String withOriginalLanguage);

    @GET("search/movie")
    Observable<Response<DiscoverResponseParser>> searchMovie(@Query("api_key") String apiKey,
                                                          @Query("page") int page,
                                                          @Query("query") String query,
                                                          @Query("language") String withOriginalLanguage);

    @GET("movie/{id}")
    Observable<Response<MovieDetailParser>> getMovieDetail(@Path("id") int id,
                                                           @Query("append_to_response")String response,
                                                           @Query("api_key") String apiKey);


    @GET("movie/{id}/credits")
    Observable<Response<Credit>> getMovieCredits(@Path("id") int id,
                                                 @Query("api_key") String apiKey);


    @GET("movie/{id}/recommendations")
    Observable<Response<Recommendation>> getRecommendations(@Path("id") int id,
                                                            @Query("api_key") String apiKey);

}