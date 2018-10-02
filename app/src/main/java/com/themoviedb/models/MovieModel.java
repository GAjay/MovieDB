package com.themoviedb.models;

import com.themoviedb.apis.entity.responses.MovieParser;
import com.themoviedb.apis.entity.responses.ProductCompaine;

import java.util.List;

/**
 * Created by Ajay Kumar Maheshwari .
 */

public class MovieModel {

    private int id;

    private boolean video;

    private int voteCount;

    private float voteAverage;

    private String title;

    private double popularity;

    private String posterPath;

    private String originalLanguage;

    private String originalTitle;
    private List<ProductCompaine> ProductCompaines;

    private List<Integer> genreIds = null;

    private String backdropPath;

    private boolean adult;

    private String overview;
    private int color;
    private int runTime;

    private String releaseDate;

    public MovieModel() {
    }

    public MovieModel(MovieParser parser) {

        if (parser == null) {
            return;
        }

        this.id = parser.getId();
        this.video = parser.isVideo();
        this.voteCount = parser.getVoteCount();
        this.voteAverage = parser.getVoteAverage();
        this.title = parser.getTitle();
        this.popularity = parser.getPopularity();
        this.posterPath = parser.getPosterPath();
        this.originalLanguage = parser.getOriginalLanguage();
        this.adult = parser.isAdult();
        this.overview = parser.getOverview();
        this.ProductCompaines= parser.getProductCompaines();
        this.releaseDate = parser.getReleaseDate();
        this.runTime= parser.getRunTime();
    }

    public int getRunTime() {
        return runTime;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPopularity() {
        return popularity;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public List<ProductCompaine> getProductCompaines() {
        return ProductCompaines;
    }

    public void setProductCompaines(List<ProductCompaine> productCompaines) {
        ProductCompaines = productCompaines;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
            return posterPath;
       }

    public String getLargePosterPath() {

            return   posterPath;

    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public String getBackdropPath() {

            return backdropPath;


    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }


    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

}
