package com.amiel.moviecenter.UI.Profile;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.amiel.moviecenter.DB.DatabaseRepository;
import com.amiel.moviecenter.DB.GenericListener;
import com.amiel.moviecenter.DB.Model.User;

public class ProfileViewModel extends AndroidViewModel {

    private final LiveData<User> user;
    DatabaseRepository mRepository;

    public ProfileViewModel(Application app, String userEmail) {
        super(app);
        mRepository = new DatabaseRepository(app);
        user = mRepository.getUserByEmail(userEmail);
    }

    public LiveData<User> getUser() {
        return user;
    }

    public void updateUser(User updateUser) {
        mRepository.updateUserTask(updateUser, data -> {});
    }
}
