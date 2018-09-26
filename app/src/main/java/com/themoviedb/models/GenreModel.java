package com.themoviedb.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.themoviedb.apis.entity.responses.GenreParser;

/**
 * Created by Ajay Kumar Maheshwari .
 */

public class GenreModel {

    private int id;

    private String name;

    public GenreModel() {
    }

    public GenreModel(GenreParser parser) {

        if(parser == null) {
            return;
        }

        this.id = parser.getId();
        this.name = parser.getName();
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
