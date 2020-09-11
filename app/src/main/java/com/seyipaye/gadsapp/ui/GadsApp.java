package com.seyipaye.gadsapp.ui;

import android.app.Application;

public class GadsApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public AppRepository getRepository() {
        return AppRepository.getRepository();
    }
}
