package com.antandbuffalo.homelightrp.service;

import android.content.Context;
import android.content.SharedPreferences;

public class StorageService {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    public static StorageService store = new StorageService();

    public StorageService getPreferenceEditor(Context context) {
        if(store == null) {
            pref = context.getSharedPreferences("HomeLight", 0); // 0 - for private mode
            editor = pref.edit();
            store = new StorageService();
        }
        return store;
    }

    public void putInt(String key, int data) {
        editor.putInt("key_name", data); // Storing integer
        editor.commit();
    }

    public int getInt(String key) {
        return pref.getInt(key, 0);
    }


}
