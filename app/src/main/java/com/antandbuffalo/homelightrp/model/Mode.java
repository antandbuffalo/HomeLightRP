package com.antandbuffalo.homelightrp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mode {

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("startTime")
    @Expose
    private int startTime;

    @SerializedName("endTime")
    @Expose
    private int endTime;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
}
