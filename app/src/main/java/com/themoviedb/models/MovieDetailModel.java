package com.themoviedb.models;

import com.themoviedb.apis.entity.responses.Cast;
import com.themoviedb.apis.entity.responses.GenreParser;
import com.themoviedb.apis.entity.responses.MovieDetailParser;
import com.themoviedb.apis.entity.responses.ProductionCompanyParser;
import com.themoviedb.apis.entity.responses.ProductionCountryParser;
import com.themoviedb.apis.entity.responses.SpokenLanguageParser;
import com.themoviedb.apis.entity.responses.Videos;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Created by Ajay Kumar Maheshwari .
 */
public class MovieDetailModel {

    private int id;

    private Videos videos;

    private MovieModel movie;

    private int budget;
    private boolean adult;
    @NonNull
    private List<GenreModel> genres;

    private String homepage;
    @NonNull
    private List<ProductionCompanyModel> productionCompanies = null;
    @NonNull
    private List<ProductionCountryModel> productionCountries = null;

    private int revenue;

    private int runtime;

    private int color;
    @NonNull
    private List<SpokenLanguageModel> spokenLanguages = null;

    private String status;

    private String tagline;
    private List<Cast> casts;

    public Videos getVideos() {
        return videos;
    }

    public void setVideos(Videos videos) {
        this.videos = videos;
    }

    public MovieDetailModel() {
    }

    public MovieDetailModel(MovieDetailParser parser) {

        if (parser == null) {
            return;
        }

        this.movie = new MovieModel();

        this.movie.setId(parser.getId());
        this.movie.setVideo(false);
        this.movie.setVoteCount(parser.getVoteCount());
        this.movie.setVoteAverage(parser.getVoteAverage());
        this.movie.setTitle(parser.getTitle());
        this.movie.setPopularity(parser.getPopularity());
        this.movie.setPosterPath(parser.getPosterPath());
        this.movie.setBackdropPath(parser.getBackdropPath());
        this.movie.setOriginalLanguage(parser.getOriginalLanguage());
        this.movie.setAdult(parser.isAdult());
        this.movie.setOverview(parser.getOverview());
        this.movie.setReleaseDate(parser.getReleaseDate());
        this.movie.setAdult(parser.isAdult());
        this.videos = parser.getVideo();

        this.budget = parser.getBudget();
        this.genres = new ArrayList<>();
        List<GenreParser> genreParsers = parser.getGenres();
        for (GenreParser genreParser : genreParsers) {
            genres.add(new GenreModel(genreParser));
        }
        this.casts = parser.getCast();

        this.homepage = parser.getHomepage();

        this.productionCompanies = new ArrayList<>();
        List<ProductionCompanyParser> productionCompanyParsers = parser.getProductionCompanies();
        for (ProductionCompanyParser productionCompanyParser : productionCompanyParsers) {
            productionCompanies.add(new ProductionCompanyModel(productionCompanyParser));
        }

        this.productionCountries = new ArrayList<>();
        List<ProductionCountryParser> productionCountryParsers = parser.getProductionCountries();
        for (ProductionCountryParser countryParser : productionCountryParsers) {
            productionCountries.add(new ProductionCountryModel(countryParser));
        }

        this.revenue = parser.getRevenue();
        this.runtime = parser.getRuntime();

        this.spokenLanguages = new ArrayList<>();
        List<SpokenLanguageParser> spokenLanguageParsers = parser.getSpokenLanguages();
        for (SpokenLanguageParser spokenLanguageParser : spokenLanguageParsers) {
            spokenLanguages.add(new SpokenLanguageModel(spokenLanguageParser));
        }

        this.status = parser.getStatus();
        this.tagline = parser.getTagline();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Cast> getCasts() {
        return casts;
    }

    public void setCasts(List<Cast> casts) {
        this.casts = casts;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public MovieModel getMovie() {
        return movie;
    }

    public void setMovie(MovieModel movie) {
        this.movie = movie;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public List<GenreModel> getGenres() {
        return genres;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setGenres(List<GenreModel> genres) {
        this.genres = genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public List<ProductionCompanyModel> getProductionCompanies() {
        return productionCompanies;
    }

    public void setProductionCompanies(List<ProductionCompanyModel> productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    public List<ProductionCountryModel> getProductionCountries() {
        return productionCountries;
    }

    public void setProductionCountries(List<ProductionCountryModel> productionCountries) {
        this.productionCountries = productionCountries;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public List<SpokenLanguageModel> getSpokenLanguages() {
        return spokenLanguages;
    }

    public void setSpokenLanguages(List<SpokenLanguageModel> spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }
}
