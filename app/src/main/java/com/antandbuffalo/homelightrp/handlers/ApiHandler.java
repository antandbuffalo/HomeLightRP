package com.antandbuffalo.homelightrp.handlers;

import com.antandbuffalo.homelightrp.model.Light;
import com.antandbuffalo.homelightrp.model.Message;
import com.antandbuffalo.homelightrp.model.Mode;
import com.antandbuffalo.homelightrp.model.Session;

public interface ApiHandler {
    default  public void sessionCreated(Session session) {};

    default public void messageCreated(Message message) {};

    default public void lightStatusChanged(Light light) {};

    default public void onModeChanged(Mode mode) {};

    default public void onGetLightStatus(Light light) {};
}
