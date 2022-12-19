package com.amiel.moviecenter.UI.Profile;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.amiel.moviecenter.DB.DatabaseRepository;
import com.amiel.moviecenter.DB.Model.User;

public class ProfileViewModel extends AndroidViewModel {

    private LiveData<User> user;
    DatabaseRepository mRepository;

    public ProfileViewModel(Application app, String userEmail) {
        super(app);
        mRepository = new DatabaseRepository(app);
        user = mRepository.getUserByEmail(userEmail);
        //user = mRepository.getUserByEmail(userEmail);
    }

    public LiveData<User> getUser() {
        return user;
    }

    public void updateUser(User updateUser) {
        mRepository.updateUserTask(updateUser);
    }
}
