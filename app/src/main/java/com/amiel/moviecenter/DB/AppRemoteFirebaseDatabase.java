package com.amiel.moviecenter.DB;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.amiel.moviecenter.DB.Model.Movie;
import com.amiel.moviecenter.DB.Model.Post;
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

    public void addUser(User newUser, GenericListener<Void> listener) {
        db.collection(User.COLLECTION).document(String.valueOf(newUser.getId())).set(newUser.toJson())
                .addOnCompleteListener(task -> listener.onComplete(null));
    }

    public void updateUser(User updatedUser, GenericListener<Void> listener) {
        db.collection(User.COLLECTION).document(String.valueOf(updatedUser.getId())).update(updatedUser.toJson())
                .addOnCompleteListener(task -> listener.onComplete(null));
    }

    public void addPost(Post newPost, GenericListener<Void> listener) {
        db.collection(Post.COLLECTION).document(String.valueOf(newPost.getId())).set(newPost.toJson())
                .addOnCompleteListener(task -> listener.onComplete(null));
    }

    public void updatePost(Post updatedPost, GenericListener<Void> listener) {
        db.collection(Post.COLLECTION).document(String.valueOf(updatedPost.getId())).update(updatedPost.toJson())
                .addOnCompleteListener(task -> listener.onComplete(null));
    }

    public void addMovie(Movie newMovie,GenericListener<Void> listener) {
        db.collection(Movie.COLLECTION).document(String.valueOf(newMovie.getId())).set(newMovie.toJson())
                .addOnCompleteListener(task -> listener.onComplete(null));
    }

    public void updateMovie(Movie updatedMovie,GenericListener<Void> listener) {
        db.collection(Movie.COLLECTION).document(String.valueOf(updatedMovie.getId())).update(updatedMovie.toJson())
                .addOnCompleteListener(task -> listener.onComplete(null));
    }
}
