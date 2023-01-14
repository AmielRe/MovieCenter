package com.amiel.moviecenter.DB.DAO;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

public interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(T[] obj);

    @Update
    int update(T[] obj);
}