package com.antandbuffalo.homelightrp.handlers;

import com.antandbuffalo.homelightrp.model.Light;
import com.antandbuffalo.homelightrp.model.Message;
import com.antandbuffalo.homelightrp.model.Session;

public interface SessionHandler {
    default  public void sessionCreated(Session session) {};

    default public void messageCreated(Message message) {};

    default public void lightStatusChanged(Light light) {};

    default public void onGetLightStatus(Light light) {};
}
