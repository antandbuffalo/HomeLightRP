package com.antandbuffalo.homelightrp.settings;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.antandbuffalo.homelightrp.handlers.ApiHandler;
import com.antandbuffalo.homelightrp.model.Duration;
import com.antandbuffalo.homelightrp.model.Light;
import com.antandbuffalo.homelightrp.model.Mode;
import com.antandbuffalo.homelightrp.service.RpService;
import com.antandbuffalo.homelightrp.service.StorageService;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class TimeSettingViewModel extends ViewModel {
    Retrofit retrofit;
    RpService rpService;
    ApiHandler sessionHandler;

    public void initRetrofit(Context context) {
        String ipAddress = StorageService.shared(context).getString("ipAddress");
        retrofit = new Retrofit.Builder()
                .baseUrl("http://" + ipAddress + ":3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        rpService = retrofit.create(RpService.class);
    }

    public void changeDuration(Duration duration) {
        rpService.changeDuration(duration)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Duration>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Duration cdSuccess) {
                        Log.d("Mode-onSuccess", cdSuccess.getStartTime() + "");
                        if(sessionHandler != null) {
                            sessionHandler.onDurationChanged(cdSuccess);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("Mode-onError", e.getMessage());
                    }
                });
    }

    public Duration buildChangeDurationRequest(int startTime, int stopTime) {
        Duration duration = new Duration();
        duration.setStartTime(startTime);
        duration.setStopTime(stopTime);
        return duration;
    }

    public Light getLightStatus(Context context) {
        return StorageService.shared(context).getLight();
    }

    public boolean updateLightStatus(Context context, Light light) {
        return StorageService.shared(context).setLight(light);
    }


    public double getStartAngle(Context context) {
        return StorageService.shared(context).getLight().getStartTime() * 15;
    }

    public double getStopAngle(Context context) {
        return StorageService.shared(context).getLight().getStopTime() * 15;
    }

}
