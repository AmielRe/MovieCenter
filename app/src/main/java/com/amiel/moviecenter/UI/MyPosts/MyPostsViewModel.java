package com.amiel.moviecenter.UI.MyPosts;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.amiel.moviecenter.DB.DatabaseRepository;
import com.amiel.moviecenter.DB.Model.Movie;
import com.amiel.moviecenter.DB.Model.Post;
import com.amiel.moviecenter.Utils.LoadingState;

import java.util.List;
import java.util.Map;

public class MyPostsViewModel extends AndroidViewModel {

    private LiveData<Map<Movie, List<Post>>> posts;
    DatabaseRepository mRepository;

    private final String email;

    public MyPostsViewModel(Application app, String email) {
        super(app);
        mRepository = new DatabaseRepository(app);
        this.email = email;
        posts = mRepository.getAllPostsOfUserWithMovie(email);
    }

    public LiveData<Map<Movie, List<Post>>> getPosts() {
        posts = mRepository.getAllPostsOfUserWithMovie(email);
        return posts;
    }

    public void updatePost(Post updatedPost) {
        mRepository.updatePostTask(updatedPost, data -> {

        });
    }

    public MutableLiveData<LoadingState> getPostsLoadingStatus() {
        return mRepository.getEventPostsListLoadingState();
    }
}
