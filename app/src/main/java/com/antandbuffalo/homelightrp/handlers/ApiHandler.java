package com.antandbuffalo.homelightrp.handlers;

import com.antandbuffalo.homelightrp.model.Duration;
import com.antandbuffalo.homelightrp.model.Light;
import com.antandbuffalo.homelightrp.model.Message;
import com.antandbuffalo.homelightrp.model.Mode;
import com.antandbuffalo.homelightrp.model.Session;

public interface ApiHandler {
    default public void lightStatusChanged(Light light) {};

    default public void onModeChanged(Light light) {};

    default public void onDurationChanged(Duration duration) {};

    default public void onGetLightStatus(Light light) {};
}
