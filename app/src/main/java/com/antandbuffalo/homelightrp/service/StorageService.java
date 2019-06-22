package com.antandbuffalo.homelightrp.service;

import android.content.Context;
import android.content.SharedPreferences;

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

}
