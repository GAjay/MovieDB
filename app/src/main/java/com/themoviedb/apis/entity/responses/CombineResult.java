package com.themoviedb.apis.entity.responses;

import com.themoviedb.models.MovieDetailModel;

public class CombineResult {

    private MovieDetailParser movieDetailParser;
    private Credit credit;
    private Recommendation recommendation;

    public CombineResult(MovieDetailParser movieDetailParser, Credit credit, Recommendation parserRecommendation) {
        this.movieDetailParser = movieDetailParser;
        this.credit = credit;
        this.recommendation= parserRecommendation;

    }

    public MovieDetailParser getMovieDetailParser() {
        return movieDetailParser;
    }

    public void setMovieDetailParser(MovieDetailParser movieDetailParser) {
        this.movieDetailParser = movieDetailParser;
    }

    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    public Recommendation getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(Recommendation recommendation) {
        this.recommendation = recommendation;
    }
}
