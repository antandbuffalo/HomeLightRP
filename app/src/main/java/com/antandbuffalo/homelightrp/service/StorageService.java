package com.antandbuffalo.homelightrp.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.antandbuffalo.homelightrp.model.Light;

public class StorageService {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    static StorageService storageService;

    private StorageService(){}

    public static StorageService shared(Context context) {
        if(storageService == null) {
            storageService = new StorageService();
            storageService.pref = context.getSharedPreferences("HomeLight", Context.MODE_PRIVATE); // 0 - for private mode
            storageService.editor = storageService.pref.edit();
        }
        return storageService;
    }

    public boolean putInt(String key, int data) {
        storageService.editor.putInt(key, data); // Storing integer
        return storageService.editor.commit();
    }

    public int getInt(String key) {
        return  storageService.pref.getInt(key, 0);
    }

    public boolean putString(String key, String value) {
        storageService.editor.putString(key, value);
        return storageService.editor.commit();
    }

    public String getString(String key) {
        return  storageService.pref.getString(key, "192.168.1.230");
    }

    public boolean setLight(Light light) {
        storageService.editor.putString("status", light.getStatus());
        storageService.editor.putString("mode", light.getMode());
        storageService.editor.putInt("startTime", light.getStartTime());
        storageService.editor.putInt("stopTime", light.getStopTime());
        storageService.editor.putInt("speed", light.getSpeed());
        return storageService.editor.commit();
    }
    public Light getLight() {
        Light light = new Light();
        light.setStatus(storageService.pref.getString("status", "off"));
        light.setMode(storageService.pref.getString("mode", "default"));
        light.setStartTime(storageService.pref.getInt("startTime", 18));
        light.setStopTime(storageService.pref.getInt("stopTime", 22));
        light.setSpeed(storageService.pref.getInt("speed", 0));
        return light;
    }
}
