package com.themoviedb.apis.request;

import java.util.Calendar;

/**
 * Created by Ajay Kumar Maheshwari .
 */

public class DiscoveryRequest extends BaseRequest {

    public static final int MIN_YEAR = 1900;
    public static final int THIS_YEAR = Calendar.getInstance().get(Calendar.YEAR);
    public static final int MAX_YEAR = THIS_YEAR ;

    public static final String SORT_RELEASE_DATE_DESC = "release_date.desc";
    public static final String SORT_RELEASE_DATE_Asc = "release_date.asc";

    private int page;
    private String sortBy;
    private String releaseDateLte;
    private String query;
    private String withOriginalLanguage;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getReleaseDateLte() {
        return releaseDateLte;
    }

    public void setReleaseDateLte(String releaseDateLte) {
        this.releaseDateLte = releaseDateLte;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getWithOriginalLanguage() {
        return withOriginalLanguage;
    }

    public void setWithOriginalLanguage(String withOriginalLanguage) {
        this.withOriginalLanguage = withOriginalLanguage;
    }
}
