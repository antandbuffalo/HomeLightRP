package com.antandbuffalo.homelightrp;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.antandbuffalo.homelightrp.constants.QBConfig;
import com.antandbuffalo.homelightrp.handlers.ApiHandler;
import com.antandbuffalo.homelightrp.model.Light;
import com.antandbuffalo.homelightrp.model.Message;
import com.antandbuffalo.homelightrp.model.Mode;
import com.antandbuffalo.homelightrp.model.Session;
import com.antandbuffalo.homelightrp.model.SessionRequest;
import com.antandbuffalo.homelightrp.model.SessionResponse;
import com.antandbuffalo.homelightrp.model.UserLogin;
import com.antandbuffalo.homelightrp.service.ApiService;
import com.antandbuffalo.homelightrp.service.RpService;
import com.antandbuffalo.homelightrp.service.StorageService;

import java.util.Date;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivityViewModel extends ViewModel {
    Retrofit retrofit;
    ApiService apiService;
    RpService rpService;
    Session session;
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
    public SessionRequest createSessionRequest() {
        Long timeStamp = new Date().getTime();
        int ts = (int)(timeStamp / 1000);
        int nonce = ts + 1;
        SessionRequest sessionRequest = new SessionRequest();
        sessionRequest.setApplicationId(62936);
        sessionRequest.setAuthKey("Ek5kyf5mfeCAgYN");
        sessionRequest.setNonce(nonce + "");
        sessionRequest.setTimestamp(ts + "");
        UserLogin user = new UserLogin();
        user.setLogin("test1");
        user.setPassword("password");
        sessionRequest.setUser(user);

        String message = "application_id="
                + sessionRequest.getApplicationId()
                + "&auth_key="
                + sessionRequest.getAuthKey()
                + "&nonce="
                + sessionRequest.getNonce()
                + "&timestamp="
                + sessionRequest.getTimestamp()
                + "&user[login]="
                + "test1"
                + "&user[password]="
                + "password";

        sessionRequest.setSignature(HmacSha.hmacSha1(message, "8Mt4MysTODX9k-B"));
        return sessionRequest;
    }

    public void createSession() {
        apiService = retrofit.create(ApiService.class);
        apiService.createSession(createSessionRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SessionResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(SessionResponse sessionResponse) {
                        session = sessionResponse.getSession();
                        System.out.println(sessionResponse.getSession());
                        apiHandler.sessionCreated(session);

                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(e);
                    }
                });
    }

     public void createMessage(String token, Message message) {
            apiService.createMessage(token, message)
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(new SingleObserver<Message>() {
                     @Override
                     public void onSubscribe(Disposable d) {

                     }

                     @Override
                     public void onSuccess(Message message) {
                         System.out.println(message.getMessage());
                         apiHandler.messageCreated(message);
                     }

                     @Override
                     public void onError(Throwable e) {
                         System.out.println(e);
                     }
                 });
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
                .subscribe(new SingleObserver<Mode>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Mode cmSuccess) {
                        Log.d("Mode-onSuccess", cmSuccess.getType());
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
                        Log.d("Speed-onSuccess", lightSuccess.getStatus());
                        apiHandler.onGetLightStatus(lightSuccess);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("Speed-onError", e.getMessage());
                        apiHandler.onGetLightStatus(null);
                    }
                });
    }

    public Message buildMessage(String msg) {
        Message message = new Message();
        message.setChatDialogId(QBConfig.chatDialogId);
        message.setMessage(msg);
        message.setSentToChat(1);
        return message;
    }

    public Light buildLightRequest(String status) {
        Light light = new Light();
        light.setStatus(status);
        return light;
    }

    public Light buildSpeedRequest(int speed) {
        Light light = new Light();
        light.setSpeed(speed);
        return light;
    }

    public Mode buildModeRequest(String modeType) {
        Mode mode = new Mode();
        mode.setType(modeType);
        return mode;
    }

    public Light buildLightRequest(String status, Integer speed) {
        Light light = new Light();
        light.setStatus(status);
        light.setSpeed(speed);
        return light;
    }
}
