package com.amiel.moviecenter.UI.MyPosts;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.amiel.moviecenter.DB.DatabaseRepository;
import com.amiel.moviecenter.DB.Model.Movie;
import com.amiel.moviecenter.DB.Model.Post;

import java.util.List;
import java.util.Map;

public class MyPostsViewModel extends AndroidViewModel {

    private final LiveData<Map<Movie, List<Post>>> posts;
    DatabaseRepository mRepository;

    public MyPostsViewModel(Application app, String email) {
        super(app);
        mRepository = new DatabaseRepository(app);
        posts = mRepository.getAllPostsOfUserWithMovie(email);
    }

    public LiveData<Map<Movie, List<Post>>> getPosts() {
        return posts;
    }

    public void updatePost(Post updatedPost) {
        mRepository.updatePostTask(updatedPost);
    }
}
