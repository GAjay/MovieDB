package com.themoviedb.models;

import com.themoviedb.apis.entity.responses.ProductionCompanyParser;

import static com.themoviedb.apis.retrofit.AppUrls.LARGER_IMAGES_BASE_URL;

/**
 * Created by Ajay Kumar Maheshwari .
 */

public class ProductionCompanyModel {

    private String name;

    private int id;

    private String logoPath;

    public ProductionCompanyModel() {
    }

    public ProductionCompanyModel(ProductionCompanyParser parser) {

        if(parser == null) {
            return;
        }

        this.id = parser.getId();
        this.name = parser.getName();
        this.logoPath=parser.getLogoPath();
    }

    public String getLogoPath() {

            return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
