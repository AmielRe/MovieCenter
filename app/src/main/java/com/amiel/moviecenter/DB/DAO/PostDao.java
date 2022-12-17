package com.amiel.moviecenter.DB.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.amiel.moviecenter.DB.Model.Post;

import java.util.List;

@Dao
public interface PostDao extends BaseDao<Post>{

    @Query("SELECT * FROM Posts WHERE movieId=:movieId")
    LiveData<List<Post>> getAllPostsForMovie(long movieId);

    @Query("SELECT * FROM Posts WHERE userId=:userId")
    LiveData<List<Post>> getAllPostsOfUser(long userId);
}
