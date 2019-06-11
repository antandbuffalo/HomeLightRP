package com.antandbuffalo.homelightrp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Person {
    @Expose
    @SerializedName("id")
    public Integer id;
    @Expose
    @SerializedName("userId")
    public Integer userId;
    @Expose
    @SerializedName("title")
    public String title;
    @Expose
    @SerializedName("completed")
    public Boolean completed;

    // bunch of boring getters and setters
}