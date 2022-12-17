package com.example.servicemotor;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
public class SessionManager {
    private static String TAG = SessionManager.class.getSimpleName();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "AndroidHiveLogin";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public void setLogin(boolean isLoggedIn){
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.commit();
        Log.d(TAG, "User Login Session Modified");
    }
    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }
}