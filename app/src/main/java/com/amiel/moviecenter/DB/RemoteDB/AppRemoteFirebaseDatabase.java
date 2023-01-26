package com.amiel.moviecenter.DB.RemoteDB;

import com.amiel.moviecenter.DB.Model.Movie;
import com.amiel.moviecenter.DB.Model.Post;
import com.amiel.moviecenter.DB.Model.User;
import com.amiel.moviecenter.Utils.Listeners.GenericListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AppRemoteFirebaseDatabase {

    private final FirebaseFirestore db;

    private AppRemoteFirebaseDatabase(){
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
    }

    private static final class InstanceHolder {
        static final AppRemoteFirebaseDatabase instance = new AppRemoteFirebaseDatabase();
    }

    public static AppRemoteFirebaseDatabase getInstance(){
        return InstanceHolder.instance;
    }

    public String createUniqueId(String collectionName) {
        return db.collection(collectionName).document().getId();
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

    public void getAllMoviesSince(Long since, GenericListener<List<Movie>> callback) {
        db.collection(Movie.COLLECTION).whereGreaterThanOrEqualTo(Movie.LAST_UPDATED, new Timestamp(since, 0)).get().addOnCompleteListener(task -> {
            List<Movie> list = new LinkedList<>();
            if (task.isSuccessful()){
                QuerySnapshot jsonsList = task.getResult();
                for (DocumentSnapshot json: jsonsList){
                    Map<String, Object> currJson = json.getData();
                    if(currJson != null) {
                        Movie movie = Movie.fromJson(currJson);
                        list.add(movie);
                    }
                }
            }
            callback.onComplete(list);
        });
    }

    public void getAllPostsSince(Long since, GenericListener<List<Post>> callback) {
        db.collection(Post.COLLECTION).whereGreaterThanOrEqualTo(Post.LAST_UPDATED, new Timestamp(since, 0)).get().addOnCompleteListener(task -> {
            List<Post> list = new LinkedList<>();
            if (task.isSuccessful()){
                QuerySnapshot jsonsList = task.getResult();
                for (DocumentSnapshot json: jsonsList){
                    Map<String, Object> currJson = json.getData();
                    if(currJson != null) {
                        Post post = Post.fromJson(currJson);
                        list.add(post);
                    }
                }
            }
            callback.onComplete(list);
        });
    }

    public void getAllUsersSince(Long since, GenericListener<List<User>> callback) {
        db.collection(User.COLLECTION).whereGreaterThanOrEqualTo(User.LAST_UPDATED, new Timestamp(since, 0)).get().addOnCompleteListener(task -> {
            List<User> list = new LinkedList<>();
            if (task.isSuccessful()){
                QuerySnapshot jsonsList = task.getResult();
                for (DocumentSnapshot json: jsonsList){
                    Map<String, Object> currJson = json.getData();
                    if(currJson != null) {
                        User user = User.fromJson(currJson);
                        list.add(user);
                    }
                }
            }
            callback.onComplete(list);
        });
    }
}
