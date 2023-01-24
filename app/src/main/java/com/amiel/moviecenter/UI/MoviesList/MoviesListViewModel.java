package com.amiel.moviecenter.UI.MoviesList;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.amiel.moviecenter.DB.DatabaseRepository;
import com.amiel.moviecenter.Utils.Listeners.GenericListener;
import com.amiel.moviecenter.DB.Model.Movie;
import com.amiel.moviecenter.DB.Model.Post;
import com.amiel.moviecenter.Utils.LoadingState;

import java.util.List;

public class MoviesListViewModel extends AndroidViewModel {

    String newMoviePlot;
    private LiveData<List<Movie>> movies;
    DatabaseRepository mRepository;

    public MoviesListViewModel(Application app) {
        super(app);
        mRepository = new DatabaseRepository(app);
        this.movies = mRepository.getAllMoviesTask();
    }

    public void setNewMoviePlot(String newMoviePlot) {
        this.newMoviePlot = newMoviePlot;
    }

    public String getNewMoviePlot() {
        return newMoviePlot;
    }

    public LiveData<List<Movie>> getMovies() {
        movies = mRepository.getAllMoviesTask();
        return movies;
    }

    public MutableLiveData<LoadingState> getMoviesLoadingStatus() {
        return mRepository.getEventMoviesListLoadingState();
    }

    public void insertMovie(Movie newMovie, GenericListener<Void> listener) {
        mRepository.insertMovieTask(newMovie, listener);
    }

    public LiveData<Movie> getMovieByName(String movieName) {
        return mRepository.getMovieByName(movieName);
    }

    public LiveData<Movie> getMovieByNameAndYear(String movieName, int movieYear) {
        return mRepository.getMovieByNameAndYear(movieName, movieYear);
    }

    public void insertPost(Post newPost, GenericListener<Void> listener) {
        mRepository.insertPostTask(newPost, listener);
    }

    public void updateMovie(Movie updatedMovie) {
        mRepository.updateMovieTask(updatedMovie, data -> {});
    }

    public void updatePost(Post updatedPost) { mRepository.updatePostTask(updatedPost, data -> {}); }
}
