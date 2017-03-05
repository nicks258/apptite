package com.ateam.funshoppers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Home on 12/29/2016.
 */

public class LoginPref {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "login-cred";

    private static final String SAVE_CRED = "IsFirstTimeLaunch";

    public LoginPref(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void loginsave(boolean isFirstTime) {
        editor.putBoolean(SAVE_CRED, isFirstTime);
        editor.commit();
    }
    public void save(String token){
        //editor.putString("password",pass);
        editor.putString("token",token);
        editor.commit();
    }

public String getToken(){
    return pref.getString("token","1234");
}
    public  String getEmail(){
        return pref.getString("email","abc@123.com");
    }

    public boolean isLoginsaved() {
        return pref.getBoolean(SAVE_CRED, false);
    }
}
