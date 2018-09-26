package com.themoviedb.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.themoviedb.apis.entity.responses.ProductionCompanyParser;
import com.themoviedb.apis.entity.responses.ProductionCountryParser;

/**
 * Created by Ajay Kumar Maheshwari .
 */

public class ProductionCountryModel {

    private String iso31661;

    private String name;

    public ProductionCountryModel() {
    }

    public ProductionCountryModel(ProductionCountryParser parser) {

        if(parser == null) {
            return;
        }

        this.iso31661 = parser.getIso31661();
        this.name = parser.getName();
    }

    public String getIso31661() {
        return iso31661;
    }

    public void setIso31661(String iso31661) {
        this.iso31661 = iso31661;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
