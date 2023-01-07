package com.amiel.moviecenter.DB;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.amiel.moviecenter.DB.AsyncTask.InsertAsyncTask;
import com.amiel.moviecenter.DB.AsyncTask.UpdateAsyncTask;
import com.amiel.moviecenter.DB.Model.Movie;
import com.amiel.moviecenter.DB.Model.Post;
import com.amiel.moviecenter.DB.Model.User;

import java.util.List;
import java.util.Map;

public class DatabaseRepository {

    private final AppLocalDatabase mDatabase;

    public DatabaseRepository(Context context) {
        mDatabase = AppLocalDatabase.getInstance(context);
    }

    public MutableLiveData<long[]> insertMovieTask(Movie movie){
        MutableLiveData<long[]> liveData = new MutableLiveData<>();
        new InsertAsyncTask<>(liveData, mDatabase.movieDao()).execute(movie);
        return liveData;
    }

    public void updateMovieTask(Movie movie){
        new UpdateAsyncTask<>(mDatabase.movieDao()).execute(movie);
    }

    public LiveData<List<Movie>> getAllMoviesTask() {
        return mDatabase.movieDao().getAll();
    }

    public LiveData<Movie> getMovieByNameAndYear(String name, int year) {
        return mDatabase.movieDao().getMovieByNameAndYear(name, year);
    }

    public LiveData<Movie> getMovieByName(String name) {
        return mDatabase.movieDao().getMovieByName(name);
    }

    public MutableLiveData<long[]> insertPostTask(Post post){
        MutableLiveData<long[]> liveData = new MutableLiveData<>();
        new InsertAsyncTask<>(liveData, mDatabase.postDao()).execute(post);
        return liveData;
    }

    public void updatePostTask(Post post){
        new UpdateAsyncTask<>(mDatabase.postDao()).execute(post);
    }

    public LiveData<Map<User,List<Post>>> getAllPostsForMovieWithUser(long movieId) {
        return mDatabase.postDao().getAllPostsForMovieWithUser(movieId);
    }

    public LiveData<Map<Movie, List<Post>>> getAllPostsOfUserWithMovie(String userEmail) {
        return mDatabase.postDao().getAllPostsOfUserWithMovie(userEmail);
    }

    public MutableLiveData<long[]> insertUserTask(User user){
        MutableLiveData<long[]> liveData = new MutableLiveData<>();
        new InsertAsyncTask<>(liveData, mDatabase.userDao()).execute(user);
        return liveData;
    }

    public void updateUserTask(User user){
        new UpdateAsyncTask<>(mDatabase.userDao()).execute(user);
    }

    public LiveData<User> getUserByEmail(String email) {
        return mDatabase.userDao().getUserByEmail(email);
    }
}
