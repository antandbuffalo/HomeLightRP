package com.antandbuffalo.homelightrp;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.antandbuffalo.homelightrp.handlers.ApiHandler;
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

public class MainActivityViewModel extends ViewModel {
    Retrofit retrofit;
    RpService rpService;
    ApiHandler apiHandler;
    String ipAddress;

    public void initIpAddress(Context context) {
        ipAddress = StorageService.shared(context).getString("ipAddress");
    }

    public boolean isSameIp(Context context) {
        return StorageService.shared(context).getString("ipAddress").equalsIgnoreCase(ipAddress);
    }

    public void initRetrofit() {
        if(ipAddress == null) {
            ipAddress = "192.168.1.230";
        }
        retrofit = new Retrofit.Builder()
                .baseUrl("http://" + ipAddress + ":3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        rpService = retrofit.create(RpService.class);
    }

     public void changeLightStatus(Light light) {
        rpService.changeStatus(light)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Light>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Light lightSuccess) {
                        Log.d("changeStatus-onSuccess", lightSuccess.getStatus());
                        apiHandler.lightStatusChanged(lightSuccess);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("changeStatus-onError", e.getMessage());
                    }
                });
     }

    public void changeLightSpeed(Light light) {
        rpService.changeSpeed(light)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Light>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Light lightSuccess) {
                        Log.d("Speed-onSuccess", lightSuccess.getStatus());
                        apiHandler.lightStatusChanged(lightSuccess);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("Speed-onError", e.getMessage());
                    }
                });
    };

    public void changeMode(Mode mode) {
        rpService.changeMode(mode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Light>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Light cmSuccess) {
                        Log.d("Mode-onSuccess", cmSuccess.getMode());
                        apiHandler.onModeChanged(cmSuccess);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("Mode-onError", e.getMessage());
                    }
                });
    }

    public void getLightStatus() {
        rpService.getStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Light>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Light lightSuccess) {
                        Log.d("getLight-onSuccess", lightSuccess.getStatus());
                        apiHandler.onGetLightStatus(lightSuccess);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("getLight-onError", e.getMessage());
                        apiHandler.onGetLightStatus(null);
                    }
                });
    }

    public Light buildSpeedRequest(int speed) {
        Light light = new Light();
        light.setSpeed(speed);
        return light;
    }

    public Mode buildModeRequest(String modeType) {
        Mode mode = new Mode();
        mode.setMode(modeType);
        return mode;
    }

    public Light buildLightRequest(String status, Integer speed) {
        Light light = new Light();
        light.setStatus(status);
        light.setSpeed(speed);
        return light;
    }

    public boolean saveLightStatus(Context context, Light light) {
        return StorageService.shared(context).setLight(light);
    }

    public Light getLightStatus(Context context) {
        return StorageService.shared(context).getLight();
    }

}
