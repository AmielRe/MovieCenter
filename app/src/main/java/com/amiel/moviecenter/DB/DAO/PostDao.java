package com.amiel.moviecenter.DB.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.amiel.moviecenter.DB.Model.Movie;
import com.amiel.moviecenter.DB.Model.Post;

import java.util.List;
import java.util.Map;

@Dao
public interface PostDao extends BaseDao<Post>{

    @Query("SELECT * FROM Posts WHERE movieId=:movieId")
    LiveData<List<Post>> getAllPostsForMovie(long movieId);

    @Query("SELECT * FROM Movies " +
            "JOIN Posts ON Posts.movieId = Movies.id " +
            "INNER JOIN Users ON Posts.userId = Users.id " +
            "WHERE Users.email LIKE :userEmail")
    LiveData<Map<Movie, List<Post>>> getAllPostsOfUserWithMovie(String userEmail);
}
