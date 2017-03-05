package com.ateam.funshoppers;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Home on 1/29/2017.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
LoginPref sharedPref;


    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        sharedPref=new LoginPref(getApplicationContext());
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("firebaseInstance", "Refreshed token: " + refreshedToken);
        sharedPref.save(refreshedToken);


        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

    }
}
