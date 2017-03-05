package com.ateam.funshoppers.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Home on 11/30/2016.
 */

public class BusinessPictures {
    @SerializedName("Url")
    private String Url;
    @SerializedName("Id")
    private int Id;
    @SerializedName("Name")
    private String Name;

    public String getName() {
        return Name;
    }

    public String getUrl() {
        return Url;
    }

    public int getId() {
        return Id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
