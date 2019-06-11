package com.antandbuffalo.homelightrp.service;

import com.antandbuffalo.homelightrp.handlers.SessionHandler;
import com.antandbuffalo.homelightrp.model.Message;
import com.antandbuffalo.homelightrp.model.Person;
import com.antandbuffalo.homelightrp.model.SessionRequest;
import com.antandbuffalo.homelightrp.model.SessionResponse;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("session.json")
    Single<SessionResponse> createSession(@Body SessionRequest sessionRequest);

    @Headers("Content-Type: application/json")
    @POST("/chat/Message.json")
    Single<Message> createMessage(@Header("QB-Token") String qbToken, @Body Message message);
}
