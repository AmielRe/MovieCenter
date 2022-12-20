package com.amiel.moviecenter.Utils;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.amiel.moviecenter.UI.MovieDetails.MovieDetailsViewModel;
import com.amiel.moviecenter.UI.MyPosts.MyPostsViewModel;
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
        } else if (modelClass == MyPostsViewModel.class) {
            return (T) new MyPostsViewModel(mApplication, (String) mParams[0]);
        } else if (modelClass == MovieDetailsViewModel.class) {
            return (T) new MovieDetailsViewModel(mApplication, (long) mParams[0]);
        } else {
            return super.create(modelClass);
        }
    }
}