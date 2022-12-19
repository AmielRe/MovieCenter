package com.amiel.moviecenter.Utils;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.amiel.moviecenter.UI.Profile.ProfileViewModel;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory implements ViewModelProvider.Factory {
    private final Application mApplication;
    private final Object[] mParams;

    public ViewModelFactory(Application application, Object... params) {
        mApplication = application;
        mParams = params;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass == ProfileViewModel.class) {
            return (T) new ProfileViewModel(mApplication, (String) mParams[0]);
        } /*else if (modelClass == ViewModel2.class) {
            return (T) new ViewModel2(mApplication, (Integer) mParams[0]);
        } else if (modelClass == ViewModel3.class) {
            return (T) new ViewModel3(mApplication, (Integer) mParams[0], (String) mParams[1]);
        }*/ else {
            return super.create(modelClass);
        }
    }
}