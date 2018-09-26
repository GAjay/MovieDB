package com.themoviedb.models;

import com.themoviedb.apis.entity.responses.DiscoverResponseParser;
import com.themoviedb.apis.entity.responses.MovieParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ajay Kumar Maheshwari .
 */

public class DiscoverModel {

    private int page;

    private int totalResults;

    private int totalPages;

    private List<MovieModel> movies = null;

    public DiscoverModel() {
    }

    public DiscoverModel(DiscoverResponseParser parser) {

        if(parser == null) {
            return;
        }

        this.page = parser.getPage();
        this.totalResults = parser.getTotalResults();
        this.totalPages = parser.getTotalPages();

        List<MovieParser> movieParsers = parser.getMovieParsers();
        if(movieParsers != null) {
            movies = new ArrayList<>();
            for(MovieParser movieParser : movieParsers){
                this.movies.add(new MovieModel(movieParser));
            }
        }
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<MovieModel> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieModel> movies) {
        this.movies = movies;
    }
}
