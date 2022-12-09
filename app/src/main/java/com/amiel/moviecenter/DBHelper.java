package com.amiel.moviecenter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // Tables Name
    public static final String POSTS_TABLE_NAME = "POSTS";
    public static final String MOVIES_TABLE_NAME = "MOVIES";
    public static final String USERS_TABLE_NAME = "USERS";

    // Movies table columns
    public static final String MOVIE_ID = "id";
    public static final String MOVIE_NAME = "name";
    public static final String MOVIE_YEAR = "year";
    public static final String MOVIE_RATING = "rating";
    public static final String MOVIE_PLOT = "plot";
    public static final String MOVIE_POSTER = "poster";

    // Users table columns
    public static final String USER_ID = "id";
    public static final String USER_USERNAME = "username";
    public static final String USER_IMAGE = "image";
    public static final String USER_EMAIL = "email";

    // Posts table columns
    public static final String POST_ID = "id";
    public static final String POST_RATING = "rating";
    public static final String POST_TEXT = "text";
    public static final String POST_IMAGE = "image";
    public static final String POST_RELATED_MOVIE_ID = "movieID";

    // Database Information
    static final String DB_NAME = "MOVIE_CENTER.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating movies table query
    private static final String CREATE_MOVIES_TABLE = "create table " + MOVIES_TABLE_NAME + "(" + MOVIE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + MOVIE_NAME + " TEXT NOT NULL, " + MOVIE_YEAR + " INTEGER NOT NULL, "
        + MOVIE_RATING + " REAL NOT NULL, " + MOVIE_PLOT + " TEXT NOT NULL, " + MOVIE_POSTER + " BLOB NOT NULL);";

    private static final String CREATE_USERS_TABLE = "create table " + USERS_TABLE_NAME + "(" + USER_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USER_USERNAME + " TEXT NOT NULL, " + USER_EMAIL + " TEXT NOT NULL, "
            + USER_IMAGE + " BLOB NOT NULL);";

    private static final String CREATE_POSTS_TABLE = "create table " + POSTS_TABLE_NAME + "(" + POST_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + POST_RATING + " REAL NOT NULL, " + POST_TEXT + " TEXT NOT NULL, "
            + POST_IMAGE + " BLOB NOT NULL, " + POST_RELATED_MOVIE_ID + " INTEGER NOT NULL);";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MOVIES_TABLE);
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_POSTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MOVIES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + POSTS_TABLE_NAME);
        onCreate(db);
    }
}