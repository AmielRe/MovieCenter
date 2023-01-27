package com.amiel.moviecenter.DB.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RewriteQueriesToDropUnusedColumns;

import com.amiel.moviecenter.DB.Model.Movie;
import com.amiel.moviecenter.DB.Model.Post;
import com.amiel.moviecenter.DB.Model.User;

import java.util.List;
import java.util.Map;

@Dao
public interface PostDao extends BaseDao<Post>{

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM Users " +
            "JOIN Posts ON Posts.userId = Users.user_id " +
            "INNER JOIN Movies ON Posts.movieId = Movies.movie_id " +
            "WHERE Posts.movieId LIKE :movieId")
    LiveData<Map<User, List<Post>>> getAllPostsForMovieWithUser(String movieId);

    @Query("SELECT * FROM Movies " +
            "JOIN Posts ON Posts.movieId = Movies.movie_id " +
            "INNER JOIN Users ON Posts.userId = Users.user_id " +
            "WHERE Users.email LIKE :userEmail")
    LiveData<Map<Movie, List<Post>>> getAllPostsOfUserWithMovie(String userEmail);
}