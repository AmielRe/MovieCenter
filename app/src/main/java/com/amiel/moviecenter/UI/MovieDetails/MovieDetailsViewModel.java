package com.amiel.moviecenter.UI.MovieDetails;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.amiel.moviecenter.DB.DatabaseRepository;
import com.amiel.moviecenter.DB.Model.Post;
import com.amiel.moviecenter.DB.Model.User;

import java.util.List;
import java.util.Map;

public class MovieDetailsViewModel extends AndroidViewModel {

    private final LiveData<Map<User, List<Post>>> posts;
    DatabaseRepository mRepository;

    public MovieDetailsViewModel(Application app, String movieId) {
        super(app);
        mRepository = new DatabaseRepository(app);
        posts = mRepository.getAllPostsForMovieWithUser(movieId);
    }

    public LiveData<Map<User, List<Post>>> getPosts() {
        return posts;
    }
}
