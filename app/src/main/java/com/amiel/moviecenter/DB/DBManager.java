package com.amiel.moviecenter.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.amiel.moviecenter.DB.Model.Movie;
import com.amiel.moviecenter.DB.Model.Post;
import com.amiel.moviecenter.DB.Model.User;

import java.util.ArrayList;

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

    public long insertPost(Post newPost) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper.POST_RATING, newPost.rating);
        contentValue.put(DBHelper.POST_TEXT, newPost.text);
        contentValue.put(DBHelper.POST_IMAGE, newPost.image);
        contentValue.put(DBHelper.POST_RELATED_MOVIE_ID, newPost.movieID);
        contentValue.put(DBHelper.POST_RELATED_USER_ID, newPost.userID);
        return database.insert(DBHelper.POSTS_TABLE_NAME, null, contentValue);
    }

    public int updatePost(long id, Post post) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.POST_RATING, post.rating);
        contentValues.put(DBHelper.POST_TEXT, post.text);
        contentValues.put(DBHelper.POST_IMAGE, post.image);
        contentValues.put(DBHelper.POST_RELATED_MOVIE_ID, post.movieID);
        contentValues.put(DBHelper.POST_RELATED_USER_ID, post.userID);
        return database.update(DBHelper.POSTS_TABLE_NAME, contentValues, DBHelper.POST_ID + " = " + id, null);
    }

    public void deletePost(long id) {
        database.delete(DBHelper.POSTS_TABLE_NAME, DBHelper.POST_ID + "=" + id, null);
    }

    public long insertMovie(Movie newMovie) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper.MOVIE_RATING, newMovie.rating);
        contentValue.put(DBHelper.MOVIE_NAME, newMovie.name);
        contentValue.put(DBHelper.MOVIE_PLOT, newMovie.plot);
        contentValue.put(DBHelper.MOVIE_POSTER, newMovie.poster);
        contentValue.put(DBHelper.MOVIE_YEAR, newMovie.year);
        return database.insert(DBHelper.MOVIES_TABLE_NAME, null, contentValue);
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

    public long insertUser(User newUser) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper.USER_EMAIL, newUser.email);
        contentValue.put(DBHelper.USER_IMAGE, newUser.profileImage);
        contentValue.put(DBHelper.USER_USERNAME, newUser.username);
        return database.insert(DBHelper.USERS_TABLE_NAME, null, contentValue);
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

    public Movie getMovieByNameAndYear(String movieName, int movieYear) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DBHelper.MOVIES_TABLE_NAME + " WHERE " + DBHelper.MOVIE_NAME + " IN ( '" + movieName + "' ) AND " + DBHelper.MOVIE_YEAR + " IN ( '" + movieYear + "' )" , null);
        if(cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(DBHelper.MOVIE_NAME));
            String plot = cursor.getString(cursor.getColumnIndex(DBHelper.MOVIE_PLOT));
            byte[] poster = cursor.getBlob(cursor.getColumnIndex(DBHelper.MOVIE_POSTER));
            long id = cursor.getLong(cursor.getColumnIndex(DBHelper.MOVIE_ID));
            float rating = cursor.getLong(cursor.getColumnIndex(DBHelper.MOVIE_RATING));
            int year = cursor.getInt(cursor.getColumnIndex(DBHelper.MOVIE_YEAR));
            cursor.close();
            return new Movie(name, year, rating, plot, poster, id);
        }
        return null;
    }

    public User getUserByEmail(String email) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DBHelper.USERS_TABLE_NAME + " WHERE " + DBHelper.USER_EMAIL + " IN ( '" + email + "' )", null);
        if(cursor.moveToFirst()) {
            String username = cursor.getString(cursor.getColumnIndex(DBHelper.USER_USERNAME));
            long id = cursor.getLong(cursor.getColumnIndex(DBHelper.USER_ID));
            byte[] profileImage= cursor.getBlob(cursor.getColumnIndex(DBHelper.USER_IMAGE));
            cursor.close();

            return new User(username, email, profileImage, id);
        }

        return null;
    }

    public ArrayList<Movie> getAllMovies() {
        ArrayList<Movie> allMovies = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + DBHelper.MOVIES_TABLE_NAME, null);
        if (cursor.moveToFirst()){
            do {
                String name = cursor.getString(cursor.getColumnIndex(DBHelper.MOVIE_NAME));
                String plot = cursor.getString(cursor.getColumnIndex(DBHelper.MOVIE_PLOT));
                byte[] poster = cursor.getBlob(cursor.getColumnIndex(DBHelper.MOVIE_POSTER));
                long id = cursor.getLong(cursor.getColumnIndex(DBHelper.MOVIE_ID));
                float rating = cursor.getLong(cursor.getColumnIndex(DBHelper.MOVIE_RATING));
                int year = cursor.getInt(cursor.getColumnIndex(DBHelper.MOVIE_YEAR));
                Movie movie = new Movie(name, year, rating, plot, poster, id);
                allMovies.add(movie);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return allMovies;
    }

    public ArrayList<Post> getAllPostsForMovie(long movieId) {
        ArrayList<Post> allPosts = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + DBHelper.POSTS_TABLE_NAME + " WHERE " + DBHelper.POST_RELATED_MOVIE_ID + " IN ( '" + movieId + "' )", null);
        if (cursor.moveToFirst()){
            do {
                String text = cursor.getString(cursor.getColumnIndex(DBHelper.POST_TEXT));
                byte[] image = cursor.getBlob(cursor.getColumnIndex(DBHelper.POST_IMAGE));
                long id = cursor.getLong(cursor.getColumnIndex(DBHelper.POST_ID));
                float rating = cursor.getLong(cursor.getColumnIndex(DBHelper.POST_RATING));
                long userId = cursor.getLong(cursor.getColumnIndex(DBHelper.POST_RELATED_USER_ID));
                Post post = new Post(text, movieId, rating, image, userId, id);
                allPosts.add(post);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return allPosts;
    }

    public boolean isMovieExist(String name) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DBHelper.MOVIES_TABLE_NAME + " WHERE " + DBHelper.MOVIE_NAME + " IN ( '" + name + "' )", null);
        boolean isExist = cursor.moveToFirst();
        cursor.close();
        return isExist;
    }

    public boolean isUserExist(String email) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DBHelper.USERS_TABLE_NAME + " WHERE " + DBHelper.USER_EMAIL + " IN ( '" + email + "' )", null);
        boolean isExist = cursor.moveToFirst();
        cursor.close();
        return isExist;
    }

    public ArrayList<Post> getAllPostsOfUser(String email) {
        User userToSearch = getUserByEmail(email);
        return getAllPostsOfUser(userToSearch.id);
    }

    public ArrayList<Post> getAllPostsOfUser(long userID) {
        ArrayList<Post> allPosts = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + DBHelper.POSTS_TABLE_NAME + " WHERE " + DBHelper.POST_RELATED_USER_ID + " IN ( '" + userID + "' )", null);
        if (cursor.moveToFirst()){
            do {
                String text = cursor.getString(cursor.getColumnIndex(DBHelper.POST_TEXT));
                byte[] image = cursor.getBlob(cursor.getColumnIndex(DBHelper.POST_IMAGE));
                long id = cursor.getLong(cursor.getColumnIndex(DBHelper.POST_ID));
                float rating = cursor.getLong(cursor.getColumnIndex(DBHelper.POST_RATING));
                long movieId = cursor.getLong(cursor.getColumnIndex(DBHelper.POST_RELATED_MOVIE_ID));
                Post post = new Post(text, movieId, rating, image, userID, id);
                allPosts.add(post);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return allPosts;
    }
}
