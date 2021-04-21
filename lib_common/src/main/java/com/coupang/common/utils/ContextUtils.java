package com.coupang.common.utils;

import android.app.Application;
import android.content.Context;


import org.jetbrains.annotations.NotNull;

public class ContextUtils {
    private static Context context;
    private static Application application;

    @NotNull
    public static Context getSharedContext() {
        return context;
    }

    @NotNull
    public static Application getApplication() {
        return application;
    }

    public static void init(Application application) {
        context = application.getApplicationContext();
        ContextUtils.application = application;
    }

}
