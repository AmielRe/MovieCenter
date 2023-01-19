package com.amiel.moviecenter.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {

    private static final String PREF_NAME = "com.amiel.moviecenter.sharedPref";
    private static final String LOCAL_USER_LAST_UPDATED_KEY = "com.amiel.moviecenter.userLastUpdated";
    private static final String LOCAL_POST_LAST_UPDATED_KEY = "com.amiel.moviecenter.postLastUpdated";
    private static final String LOCAL_MOVIE_LAST_UPDATED_KEY = "com.amiel.moviecenter.movieLastUpdated";

    private static PreferencesManager sInstance;
    private final SharedPreferences mPref;

    private PreferencesManager(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManager(context);
        }
    }

    public static synchronized PreferencesManager getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(PreferencesManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return sInstance;
    }

    public void setUserLastUpdatedValue(Long value) {
        mPref.edit()
                .putLong(LOCAL_USER_LAST_UPDATED_KEY, value)
                .apply();
    }

    public Long getUserLastUpdatedValue() {
        return mPref.getLong(LOCAL_USER_LAST_UPDATED_KEY, 0);
    }

    public Long getPostLastUpdatedValue() { return mPref.getLong(LOCAL_POST_LAST_UPDATED_KEY, 0); }

    public void setPostLastUpdatedValue(Long value) {
        mPref.edit()
                .putLong(LOCAL_POST_LAST_UPDATED_KEY, value)
                .apply();
    }

    public void setMovieLastUpdatedValue(Long value) {
        mPref.edit()
                .putLong(LOCAL_MOVIE_LAST_UPDATED_KEY, value)
                .apply();
    }

    public Long getMovieLastUpdatedValue() {
        return mPref.getLong(LOCAL_MOVIE_LAST_UPDATED_KEY, 0);
    }

    public void remove(String key) {
        mPref.edit()
                .remove(key)
                .apply();
    }

    public boolean clear() {
        return mPref.edit()
                .clear()
                .commit();
    }
}