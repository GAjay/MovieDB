package com.themoviedb.models;

import com.themoviedb.apis.entity.responses.ProductionCountryParser;
import com.themoviedb.apis.entity.responses.SpokenLanguageParser;

/**
 * Created by Ajay Kumar Maheshwari .
 */

public class SpokenLanguageModel {

    private String iso6391;

    private String name;

    public SpokenLanguageModel() {
    }

    public SpokenLanguageModel(SpokenLanguageParser parser) {

        if(parser == null) {
            return;
        }

        this.iso6391 = parser.getIso6391();
        this.name = parser.getName();
    }

    public String getIso6391() {
        return iso6391;
    }

    public void setIso6391(String iso6391) {
        this.iso6391 = iso6391;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
