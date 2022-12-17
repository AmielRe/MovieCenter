package com.amiel.moviecenter.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;

import com.amiel.moviecenter.DB.DAO.MovieDao;
import com.amiel.moviecenter.DB.DAO.PostDao;
import com.amiel.moviecenter.DB.DAO.UserDao;
import com.amiel.moviecenter.DB.Model.Movie;
import com.amiel.moviecenter.DB.Model.Post;
import com.amiel.moviecenter.DB.Model.User;

@Database(entities = {Movie.class, Post.class, User.class}, version = 1)
public abstract class AppDatabase extends androidx.room.RoomDatabase {

    public static final String DATABASE_NAME = "MOVIE_CENTER.DB";

    public abstract UserDao userDao();
    public abstract MovieDao movieDao();
    public abstract PostDao postDao();

    private static AppDatabase instance;

    static AppDatabase getInstance(final Context context){
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance =
                            Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class,
                                    DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }
}
