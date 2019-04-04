package com.helpa;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

@ReportsCrashes(
        mailTo = "name.surname@appinventiv.com",
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text)

    public class MyApplication extends Application {
    private static MyApplication mApplicationInsatnce;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);
        mApplicationInsatnce = this;

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("SourceSansPro-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    public static MyApplication getInstance(){
        return mApplicationInsatnce;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}
