package com.antandbuffalo.homelightrp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SessionRequest {

    @SerializedName("application_id")
    @Expose
    private Integer applicationId;
    @SerializedName("auth_key")
    @Expose
    private String authKey;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("nonce")
    @Expose
    private String nonce;
    @SerializedName("signature")
    @Expose
    private String signature;

    public UserLogin getUser() {
        return user;
    }

    public void setUser(UserLogin user) {
        this.user = user;
    }

    @SerializedName("user")
    @Expose
    private UserLogin user;

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
