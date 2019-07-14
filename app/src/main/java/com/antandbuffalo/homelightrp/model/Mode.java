package com.antandbuffalo.homelightrp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mode {

    @SerializedName("mode")
    @Expose
    private String mode;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
