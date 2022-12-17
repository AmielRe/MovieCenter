package com.amiel.moviecenter.DB;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.amiel.moviecenter.DB.AsyncTask.DeleteAsyncTask;
import com.amiel.moviecenter.DB.AsyncTask.InsertAsyncTask;
import com.amiel.moviecenter.DB.AsyncTask.UpdateAsyncTask;
import com.amiel.moviecenter.DB.Model.Movie;
import com.amiel.moviecenter.DB.Model.Post;
import com.amiel.moviecenter.DB.Model.User;

import java.util.List;

public class DatabaseRepository {

    private final AppDatabase mDatabase;

    public DatabaseRepository(Context context) {
        mDatabase = AppDatabase.getInstance(context);
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

    public LiveData<Movie> getMovieById(long id) {
        return mDatabase.movieDao().getMovieById(id);
    }

    public LiveData<Movie> getMovieByName(String name) {
        return mDatabase.movieDao().getMovieByName(name);
    }

    public void deleteMovieTask(Movie movie){
        new DeleteAsyncTask<>(mDatabase.movieDao()).execute(movie);
    }


    public MutableLiveData<long[]> insertPostTask(Post post){
        MutableLiveData<long[]> liveData = new MutableLiveData<>();
        new InsertAsyncTask<>(liveData, mDatabase.postDao()).execute(post);
        return liveData;
    }

    public void updatePostTask(Post post){
        new UpdateAsyncTask<>(mDatabase.postDao()).execute(post);
    }

    public void deletePostTask(Post post){
        new DeleteAsyncTask<>(mDatabase.postDao()).execute(post);
    }

    public LiveData<List<Post>> getAllPostsForMovie(long movieId) {
        return mDatabase.postDao().getAllPostsForMovie(movieId);
    }

    public LiveData<List<Post>> getAllPostsOfUser(long userId) {
        return mDatabase.postDao().getAllPostsOfUser(userId);
    }


    public MutableLiveData<long[]> insertUserTask(User user){
        MutableLiveData<long[]> liveData = new MutableLiveData<>();
        new InsertAsyncTask<>(liveData, mDatabase.userDao()).execute(user);
        return liveData;
    }

    public void updateUserTask(User user){
        new UpdateAsyncTask<>(mDatabase.userDao()).execute(user);
    }

    public void deleteUserTask(User user){
        new DeleteAsyncTask<>(mDatabase.userDao()).execute(user);
    }

    public LiveData<User> getUserByEmail(String email) {
        return mDatabase.userDao().getUserByEmail(email);
    }

    public LiveData<User> getUserById(long id) {
        return mDatabase.userDao().getUserById(id);
    }
}
