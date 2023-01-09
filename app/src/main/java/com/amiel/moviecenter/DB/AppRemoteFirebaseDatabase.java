package com.amiel.moviecenter.DB;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.amiel.moviecenter.DB.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class AppRemoteFirebaseDatabase {

    private FirebaseFirestore db;
    private static AppRemoteFirebaseDatabase instance;

    private AppRemoteFirebaseDatabase(){
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
    }

    static AppRemoteFirebaseDatabase getInstance(){
        if (instance == null) {
            synchronized (AppRemoteFirebaseDatabase.class) {
                if (instance == null) {
                    instance = new AppRemoteFirebaseDatabase();
                }
            }
        }
        return instance;
    }

    public void addUser(User newUser, AddUserListener listener) {
        db.collection(User.COLLECTION).document(String.valueOf(newUser.getId())).set(newUser.toJson())
                .addOnCompleteListener(task -> listener.onComplete());
    }
}
