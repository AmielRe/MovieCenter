package com.amiel.moviecenter.DB.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.amiel.moviecenter.DB.Model.User;

@Dao
public interface UserDao extends BaseDao<User>{

    @Query("SELECT * FROM Users WHERE email=:email")
    LiveData<User> getUserByEmail(String email);
}
