package com.themoviedb.apis.retrofit;

public class AppUrls {

    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String SortPopDesc = "Popularity Descending";
    public static final String SortPopASC = "Popularity Ascending";
    public static final String SortRateDesc = "Rating Descending";
    public static final String SortRateAsc = "Rating Ascending";
    /* default */ static final String DEFAULT_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    /**
     * Default image path in the URL, use original image size.
     */
    /* default */ static final String DEFAULT_IMAGE_PATH = DEFAULT_IMAGE_BASE_URL + "w300/";
    public static final String IMAGES_BASE_URL = DEFAULT_IMAGE_PATH;
    public static final String LARGER_IMAGES_BASE_URL = DEFAULT_IMAGE_BASE_URL + "w780/";
    public static final String IMG_YouTUBE= "https://img.youtube.com/vi/";
    public static final String IMG_YOUTUBE_TYPE="/sddefault.jpg";
    public static final String YOUTUBELINK ="http://www.youtube.com/watch?v=" ;
    public static final String KEYURL = "URL";
}
