package com.helpa.fcm;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.helpa.utils.AppSharedPreference;

/**
 * Created by nitin on 5/9/16.
 */


//Class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";
    public String refreshedToken;

    @Override
    public void onTokenRefresh() {
        //Getting registration token
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Displaying token on logcat
        AppSharedPreference.getInstance().putString(this, AppSharedPreference.DEVICE_TOKEN, refreshedToken);
    }
}