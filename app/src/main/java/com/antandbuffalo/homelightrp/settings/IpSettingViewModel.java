package com.antandbuffalo.homelightrp.settings;

import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.antandbuffalo.homelightrp.service.StorageService;

public class IpSettingViewModel extends ViewModel {

    public String getDefaultIp(Context context) {
        return StorageService.shared(context).getString("ipAddress");
    }

}
