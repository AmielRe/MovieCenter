package com.amiel.moviecenter.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.amiel.moviecenter.DB.Model.Movie;
import com.amiel.moviecenter.DB.Model.Post;
import com.amiel.moviecenter.DB.Model.User;

public class DBManager {

    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insertPost(Post newPost) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper.POST_RATING, newPost.rating);
        contentValue.put(DBHelper.POST_TEXT, newPost.text);
        contentValue.put(DBHelper.POST_IMAGE, newPost.image);
        contentValue.put(DBHelper.POST_RELATED_MOVIE_ID, newPost.movieID);
        database.insert(DBHelper.POSTS_TABLE_NAME, null, contentValue);
    }

    public int updatePost(long id, Post post) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.POST_RATING, post.rating);
        contentValues.put(DBHelper.POST_TEXT, post.text);
        contentValues.put(DBHelper.POST_IMAGE, post.image);
        contentValues.put(DBHelper.POST_RELATED_MOVIE_ID, post.movieID);
        return database.update(DBHelper.POSTS_TABLE_NAME, contentValues, DBHelper.POST_ID + " = " + id, null);
    }

    public void deletePost(long id) {
        database.delete(DBHelper.POSTS_TABLE_NAME, DBHelper.POST_ID + "=" + id, null);
    }

    public void insertMovie(Movie newMovie) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper.MOVIE_RATING, newMovie.rating);
        contentValue.put(DBHelper.MOVIE_NAME, newMovie.name);
        contentValue.put(DBHelper.MOVIE_PLOT, newMovie.plot);
        contentValue.put(DBHelper.MOVIE_POSTER, newMovie.poster);
        contentValue.put(DBHelper.MOVIE_YEAR, newMovie.year);
        database.insert(DBHelper.MOVIES_TABLE_NAME, null, contentValue);
    }

    public int updateMovie(long id, Movie movie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.MOVIE_RATING, movie.rating);
        contentValues.put(DBHelper.MOVIE_NAME, movie.name);
        contentValues.put(DBHelper.MOVIE_PLOT, movie.plot);
        contentValues.put(DBHelper.MOVIE_POSTER, movie.poster);
        contentValues.put(DBHelper.MOVIE_YEAR, movie.year);
        return database.update(DBHelper.MOVIES_TABLE_NAME, contentValues, DBHelper.MOVIE_ID + " = " + id, null);
    }

    public void deleteMovie(long id) {
        database.delete(DBHelper.MOVIES_TABLE_NAME, DBHelper.MOVIE_ID + "=" + id, null);
    }

    public void insertUser(User newUser) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper.USER_EMAIL, newUser.email);
        contentValue.put(DBHelper.USER_IMAGE, newUser.profileImage);
        contentValue.put(DBHelper.USER_USERNAME, newUser.username);
        database.insert(DBHelper.USERS_TABLE_NAME, null, contentValue);
    }

    public int updateUser(long id, User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.USER_EMAIL, user.email);
        contentValues.put(DBHelper.USER_IMAGE, user.profileImage);
        contentValues.put(DBHelper.USER_USERNAME, user.username);
        return database.update(DBHelper.USERS_TABLE_NAME, contentValues, DBHelper.USER_ID + " = " + id, null);
    }

    public void deleteUser(long id) {
        database.delete(DBHelper.USERS_TABLE_NAME, DBHelper.USER_ID + "=" + id, null);
    }

    public long getMovieIdByNameAndYear(String movieName, int year) {
        Cursor cursor = database.rawQuery("SELECT id FROM " + DBHelper.MOVIES_TABLE_NAME + " WHERE " + DBHelper.MOVIE_NAME + " IN ( '" + movieName + "' ) AND " + DBHelper.MOVIE_YEAR + " IN ( '" + year + "' )" , null);
        cursor.moveToFirst();
        long id = cursor.getLong(0);
        cursor.close();
        return id;
    }

    public String getUserNameByEmail(String email) {
        Cursor cursor = database.rawQuery("SELECT username FROM " + DBHelper.USERS_TABLE_NAME + " WHERE " + DBHelper.USER_EMAIL + " IN ( '" + email + "' )", null);
        cursor.moveToFirst();
        String username = cursor.getString(0);
        cursor.close();
        return username;
    }

    public byte[] getUserImageByEmail(String email) {
        Cursor cursor = database.rawQuery("SELECT image FROM " + DBHelper.USERS_TABLE_NAME + " WHERE " + DBHelper.USER_EMAIL + " IN ( '" + email + "' )", null);
        cursor.moveToFirst();
        byte[] image = cursor.getBlob(0);
        cursor.close();
        return image;
    }
}
