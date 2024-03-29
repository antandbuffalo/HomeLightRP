package com.antandbuffalo.homelightrp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Light {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("interval")
    @Expose
    private String interval;

    @SerializedName("speed")
    @Expose
    private Integer speed;

    @SerializedName("mode")
    @Expose
    private String mode;

    @SerializedName("startTime")
    @Expose
    private Integer startTime;

    @SerializedName("stopTime")
    @Expose
    private Integer stopTime;

    public String getStatus() {
        if(status == null) {
            status = "off";
        }
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public String getMode() {
        if(mode == null) {
            mode = "default";
        }
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getStopTime() {
        return stopTime;
    }

    public void setStopTime(Integer stopTime) {
        this.stopTime = stopTime;
    }
}
