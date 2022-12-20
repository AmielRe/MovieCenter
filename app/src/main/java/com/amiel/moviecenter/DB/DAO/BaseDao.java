package com.amiel.moviecenter.DB.DAO;

import androidx.room.Insert;
import androidx.room.Update;

public interface BaseDao<T> {

    @Insert
    long[] insert(T[] obj);

    @Update
    int update(T[] obj);
}