package com.antandbuffalo.homelightrp.settings;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.antandbuffalo.homelightrp.handlers.SessionHandler;
import com.antandbuffalo.homelightrp.model.Mode;
import com.antandbuffalo.homelightrp.service.RpService;
import com.antandbuffalo.homelightrp.service.StorageService;

import java.util.Optional;

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
    SessionHandler sessionHandler;

    public void initRetrofit(Context context) {
        String ipAddress = StorageService.shared(context).getString("ipAddress");
        retrofit = new Retrofit.Builder()
                .baseUrl("http://" + ipAddress + ":3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        rpService = retrofit.create(RpService.class);
    }

    public void changeModeTime(Mode mode) {
        rpService.changeModeTime(mode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Mode>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Mode cmSuccess) {
                        Log.d("Mode-onSuccess", cmSuccess.getType());
                        if(sessionHandler != null) {
                            sessionHandler.onModeChanged(cmSuccess);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("Mode-onError", e.getMessage());
                    }
                });
    }

    public Mode buildChangeModeTimeRequest(int startTime, int endTime) {
        Mode mode = new Mode();
        mode.setStartTime(startTime);
        mode.setEndTime(endTime);
        return mode;
    }

}
