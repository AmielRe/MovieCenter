package com.amiel.moviecenter.DB;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.amiel.moviecenter.DB.AsyncTask.InsertAsyncTask;
import com.amiel.moviecenter.DB.AsyncTask.UpdateAsyncTask;
import com.amiel.moviecenter.DB.Model.Movie;
import com.amiel.moviecenter.DB.Model.Post;
import com.amiel.moviecenter.DB.Model.User;
import com.amiel.moviecenter.Utils.FirebaseStorageHandler;
import com.amiel.moviecenter.Utils.ImageUtils;
import com.amiel.moviecenter.Utils.LoadingState;
import com.amiel.moviecenter.Utils.PreferencesManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DatabaseRepository {
    private final AppLocalDatabase mDatabase;
    private final AppRemoteFirebaseDatabase remoteFirebaseDatabase;
    private final MutableLiveData<LoadingState> EventMoviesListLoadingState;
    private final MutableLiveData<LoadingState> EventPostsListLoadingState;

    public DatabaseRepository(Context context) {
        mDatabase = AppLocalDatabase.getInstance(context);
        remoteFirebaseDatabase = AppRemoteFirebaseDatabase.getInstance();
        EventMoviesListLoadingState = new MutableLiveData<>(LoadingState.NOT_LOADING);
        EventPostsListLoadingState = new MutableLiveData<>(LoadingState.NOT_LOADING);
        PreferencesManager.initializeInstance(context);
    }

    public MutableLiveData<LoadingState> getEventMoviesListLoadingState() {
        return EventMoviesListLoadingState;
    }

    public MutableLiveData<LoadingState> getEventPostsListLoadingState() {
        return EventPostsListLoadingState;
    }

    public void insertMovieTask(Movie movie, GenericListener<Void> listener){
        movie.setId(remoteFirebaseDatabase.createUniqueId(Movie.COLLECTION));
        remoteFirebaseDatabase.addMovie(movie, (Void) -> {
            refreshAllMovies();
            listener.onComplete(null);
        });
    }

    public void updateMovieTask(Movie movie, GenericListener<Void> listener){
        remoteFirebaseDatabase.updateMovie(movie, (Void) -> {
            new UpdateAsyncTask<>(mDatabase.movieDao()).execute(movie);
            refreshAllMovies();
            listener.onComplete(null);
        });
    }

    public LiveData<List<Movie>> getAllMoviesTask() {
        LiveData<List<Movie>> movies = mDatabase.movieDao().getAll();
        refreshAllMovies();
        return movies;
    }

    public LiveData<Movie> getMovieByNameAndYear(String name, int year) {
        LiveData<Movie> movie = mDatabase.movieDao().getMovieByNameAndYear(name, year);
        refreshAllMovies();
        return movie;
    }

    public LiveData<Movie> getMovieByName(String name) {
        LiveData<Movie> movie = mDatabase.movieDao().getMovieByName(name);
        refreshAllMovies();
        return movie;
    }

    public void insertPostTask(Post post, GenericListener<Void> listener){
        post.setId(remoteFirebaseDatabase.createUniqueId(Post.COLLECTION));
        remoteFirebaseDatabase.addPost(post, (Void) -> {
            refreshAllPosts();
            listener.onComplete(null);
        });
    }

    public void updatePostTask(Post post, GenericListener<Void> listener){
        remoteFirebaseDatabase.updatePost(post, (Void) -> {
            new UpdateAsyncTask<>(mDatabase.postDao()).execute(post);
            refreshAllPosts();
            listener.onComplete(null);
        });
    }

    public LiveData<Map<User,List<Post>>> getAllPostsForMovieWithUser(String movieId) {
        LiveData<Map<User,List<Post>>> postsForMovieWithUser = mDatabase.postDao().getAllPostsForMovieWithUser(movieId);
        refreshAllUsers();
        refreshAllMovies();
        refreshAllPosts();
        return postsForMovieWithUser;
    }

    public LiveData<Map<Movie, List<Post>>> getAllPostsOfUserWithMovie(String userEmail) {
        LiveData<Map<Movie, List<Post>>> postsOfUserWithMovie = mDatabase.postDao().getAllPostsOfUserWithMovie(userEmail);
        refreshAllUsers();
        refreshAllMovies();
        refreshAllPosts();
        return postsOfUserWithMovie;
    }

    public void insertUserTask(User user, GenericListener<Void> listener){
        remoteFirebaseDatabase.addUser(user, (Void) -> {
            refreshAllUsers();
            listener.onComplete(null);
        });
    }

    public void updateUserTask(User user, GenericListener<Void> listener){
        remoteFirebaseDatabase.updateUser(user, (Void) -> {
            new UpdateAsyncTask<>(mDatabase.userDao()).execute(user);
            refreshAllUsers();
            listener.onComplete(null);
        });
    }

    public LiveData<User> getUserByEmail(String email) {
        LiveData<User> user = mDatabase.userDao().getUserByEmail(email);
        refreshAllUsers();
        return user;
    }

    public void refreshAllPosts() {
        EventPostsListLoadingState.setValue(LoadingState.LOADING);
        // get local last update
        Long localLastUpdate = PreferencesManager.getInstance().getPostLastUpdatedValue();
        // get all updated recorde from firebase since local last update
        remoteFirebaseDatabase.getAllPostsSince(localLastUpdate,list->{
            Log.d("TAG", " firebase return : " + list.size());
            Long time = localLastUpdate;
            for(Post ps:list){
                // insert new records into ROOM
                new InsertAsyncTask<>(mDatabase.postDao()).execute(ps);
                if (time < ps.getLastUpdated()){
                    time = ps.getLastUpdated();
                }
            }

            // update local last update
            PreferencesManager.getInstance().setPostLastUpdatedValue(time);
            EventPostsListLoadingState.postValue(LoadingState.NOT_LOADING);
        });
    }

    public void refreshAllUsers() {
        // get local last update
        Long localLastUpdate = PreferencesManager.getInstance().getUserLastUpdatedValue();
        // get all updated recorde from firebase since local last update
        remoteFirebaseDatabase.getAllUsersSince(localLastUpdate,list->{
            Log.d("TAG", " firebase return : " + list.size());
            Long time = localLastUpdate;
            for(User us:list){
                // insert new records into ROOM
                new InsertAsyncTask<>(mDatabase.userDao()).execute(us);
                if (time < us.getLastUpdated()){
                    time = us.getLastUpdated();
                }
            }

            // update local last update
            PreferencesManager.getInstance().setUserLastUpdatedValue(time);
        });
    }

    public void refreshAllMovies() {
        EventMoviesListLoadingState.setValue(LoadingState.LOADING);
        // get local last update
        Long localLastUpdate = PreferencesManager.getInstance().getMovieLastUpdatedValue();
        // get all updated recorde from firebase since local last update
        remoteFirebaseDatabase.getAllMoviesSince(localLastUpdate,list->{
            Log.d("TAG", " firebase return : " + list.size());
            Long time = localLastUpdate;
            for(Movie mv:list){
                // insert new records into ROOM
                new InsertAsyncTask<>(mDatabase.movieDao()).execute(mv);
                if (time < mv.getLastUpdated()){
                    time = mv.getLastUpdated();
                }
            }

            // update local last update
            PreferencesManager.getInstance().setMovieLastUpdatedValue(time);
            EventMoviesListLoadingState.postValue(LoadingState.NOT_LOADING);
        });
    }
}
