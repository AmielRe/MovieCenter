package com.amiel.moviecenter.UI.MoviesList;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.amiel.moviecenter.DB.DatabaseRepository;
import com.amiel.moviecenter.DB.Model.Movie;
import com.amiel.moviecenter.DB.Model.Post;
import com.amiel.moviecenter.DB.Model.User;

import java.util.List;

public class MoviesListViewModel extends AndroidViewModel {

    String newMoviePlot;
    private final LiveData<List<Movie>> movies;
    DatabaseRepository mRepository;

    public MoviesListViewModel(Application app) {
        super(app);
        mRepository = new DatabaseRepository(app);
        movies = mRepository.getAllMoviesTask();
    }

    public void setNewMoviePlot(String newMoviePlot) {
        this.newMoviePlot = newMoviePlot;
    }

    public String getNewMoviePlot() {
        return newMoviePlot;
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public MutableLiveData<long[]> insertMovie(Movie newMovie) {
        return mRepository.insertMovieTask(newMovie);
    }

    public LiveData<Movie> getMovieByName(String movieName) {
        return mRepository.getMovieByName(movieName);
    }

    public LiveData<Movie> getMovieByNameAndYear(String movieName, int movieYear) {
        return mRepository.getMovieByNameAndYear(movieName, movieYear);
    }

    public LiveData<User> getUserByEmail(String email) {
        return mRepository.getUserByEmail(email);
    }

    public void insertPost(Post newPost) {
        mRepository.insertPostTask(newPost);
    }

    public void updateMovie(Movie updatedMovie) {
        mRepository.updateMovieTask(updatedMovie);
    }
}
