package com.amiel.moviecenter.DB.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.amiel.moviecenter.DB.Model.Movie;
import com.amiel.moviecenter.DB.Model.Post;
import com.amiel.moviecenter.DB.Model.User;

import java.util.List;
import java.util.Map;

@Dao
public interface PostDao extends BaseDao<Post>{

    @Query("SELECT * FROM Users " +
            "JOIN Posts ON Posts.userId = Users.id " +
            "INNER JOIN Movies ON Posts.movieId = Movies.id " +
            "WHERE Posts.movieId LIKE :movieId")
    LiveData<Map<User, List<Post>>> getAllPostsForMovieWithUser(long movieId);

    @Query("SELECT * FROM Movies " +
            "JOIN Posts ON Posts.movieId = Movies.id " +
            "INNER JOIN Users ON Posts.userId = Users.id " +
            "WHERE Users.email LIKE :userEmail")
    LiveData<Map<Movie, List<Post>>> getAllPostsOfUserWithMovie(String userEmail);
}
