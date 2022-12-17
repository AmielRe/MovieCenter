package com.amiel.moviecenter.DB.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.amiel.moviecenter.DB.Model.Movie;

import java.util.List;

@Dao
public interface MovieDao extends BaseDao<Movie> {

    @Query("SELECT * FROM Movies")
    LiveData<List<Movie>> getAll();

    @Query("SELECT * FROM Movies WHERE name=:name AND year=:year")
    LiveData<Movie> getMovieByNameAndYear(String name, int year);

    @Query("SELECT * FROM Movies WHERE id=:id")
    LiveData<Movie> getMovieById(long id);

    @Query("SELECT * FROM Movies WHERE name=:name")
    LiveData<Movie> getMovieByName(String name);
}
