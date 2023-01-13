package com.amiel.moviecenter.DB.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "Posts")
public class Post {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    public String id;

    @NonNull
    @ColumnInfo(name = "text")
    public String text;

    @NonNull
    @ColumnInfo(name = "movieId")
    public String movieID;

    @NonNull
    @ColumnInfo(name = "rating")
    public float rating;

    @NonNull
    @ColumnInfo(name = "image")
    public byte[] image;

    @NonNull
    @ColumnInfo(name = "userId")
    public String userID;

    @NonNull
    @ColumnInfo(name = "postDate")
    public Date postDate;

    @NonNull
    @ColumnInfo(name = "postImageUrl")
    public String postImageUrl;

    public static final String TEXT = "text";
    public static final String ID = "id";
    public static final String MOVIE_ID = "movieID";
    public static final String RATING = "rating";
    public static final String USER_ID = "userID";
    public static final String DATE = "postDate";
    public static final String POST_IMAGE_URL = "postImageUrl";
    public static final String COLLECTION = "Posts";

    public Post(String text, String movieID, float rating, byte[] image, String userID, String id, Date postDate, String postImageUrl)
    {
        this.text = text;
        this.movieID = movieID;
        this.rating = rating;
        this.image = image;
        this.userID = userID;
        this.id = id;
        this.postDate = postDate;
        this.postImageUrl = postImageUrl;
    }

    @Ignore
    public Post()
    {

    }

    @NonNull
    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(@NonNull String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    public String getMovieID() {
        return movieID;
    }

    public void setMovieID(@NonNull String movieID) {
        this.movieID = movieID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @NonNull
    public String getText() {
        return text;
    }

    public void setText(@NonNull String text) {
        this.text = text;
    }

    @NonNull
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @NonNull
    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(@NonNull Date postDate) {
        this.postDate = postDate;
    }

    public static Post fromJson(Map<String,Object> json){
        String id = (String)json.get(ID);
        String movieId = (String)json.get(MOVIE_ID);
        String userId = (String) json.get(USER_ID);
        float rating = (float) json.get(RATING);
        String text = (String) json.get(TEXT);
        Date postDate = (Date) json.get(DATE);
        String postImageUrl = (String) json.get(POST_IMAGE_URL);
        return new Post(text, movieId, rating, null, userId, id, postDate, postImageUrl);
    }

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put(ID, getId());
        json.put(MOVIE_ID, getMovieID());
        json.put(USER_ID, getUserID());
        json.put(RATING, getRating());
        json.put(TEXT, getText());
        json.put(DATE, getPostDate());
        json.put(POST_IMAGE_URL, getPostImageUrl());
        return json;
    }
}
