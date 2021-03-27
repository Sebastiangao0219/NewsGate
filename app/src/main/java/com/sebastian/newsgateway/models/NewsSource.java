package com.sebastian.newsgateway.models;

import java.io.Serializable;

public class NewsSource implements Serializable {
    public String id;
    public String name;
    public String category;
    public String language;
    public String country;

    public NewsSource(String id, String name, String category, String language, String country) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.language = language;
        this.country = country;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getLanguage() {
        return language;
    }

    public String getCountry() {
        return country;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
