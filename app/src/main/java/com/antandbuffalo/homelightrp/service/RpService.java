package com.antandbuffalo.homelightrp.service;

import com.antandbuffalo.homelightrp.model.Light;
import com.antandbuffalo.homelightrp.model.Message;
import com.antandbuffalo.homelightrp.model.SessionRequest;
import com.antandbuffalo.homelightrp.model.SessionResponse;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RpService {

    @POST("light")
    Single<Light> changeStatus(@Body Light light);

    @POST("speed")
    Single<Light> changeSpeed(@Body Light light);

    @POST("mode")
    Single<Light> changeMode(@Body Light light);

    @GET("light")
    Single<Light> getStatus();
}
