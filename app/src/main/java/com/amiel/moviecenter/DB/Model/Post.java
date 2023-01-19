package com.amiel.moviecenter.DB.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "Posts")
public class Post {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = ID)
    public String id;

    @NonNull
    @ColumnInfo(name = TEXT)
    public String text;

    @NonNull
    @ColumnInfo(name = MOVIE_ID)
    public String movieID;

    @NonNull
    @ColumnInfo(name = RATING)
    public float rating;

    @ColumnInfo(name = "image")
    public byte[] image;

    @NonNull
    @ColumnInfo(name = USER_ID)
    public String userID;

    @NonNull
    @ColumnInfo(name = DATE)
    public Date postDate;

    @NonNull
    @ColumnInfo(name = POST_IMAGE_URL)
    public String postImageUrl;

    @ColumnInfo(name = LAST_UPDATED)
    private Long lastUpdated;

    public static final String TEXT = "text";
    public static final String ID = "post_id";
    public static final String MOVIE_ID = "movieID";
    public static final String RATING = "post_rating";
    public static final String USER_ID = "userID";
    public static final String DATE = "postDate";
    public static final String POST_IMAGE_URL = "postImageUrl";
    public static final String LAST_UPDATED = "postLastUpdated";
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

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public static Post fromJson(Map<String,Object> json){
        String id = (String)json.get(ID);
        String movieId = (String)json.get(MOVIE_ID);
        String userId = (String) json.get(USER_ID);
        float rating = ((Double) json.get(RATING)).floatValue();
        String text = (String) json.get(TEXT);
        Date postDate = ((Timestamp) json.get(DATE)).toDate();
        String postImageUrl = (String) json.get(POST_IMAGE_URL);
        Post post = new Post(text, movieId, rating, null, userId, id, postDate, postImageUrl);

        try{
            Timestamp time = (Timestamp) json.get(LAST_UPDATED);
            post.setLastUpdated(time.getSeconds());
        }catch(Exception ignored){}

        return post;
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
        json.put(LAST_UPDATED, FieldValue.serverTimestamp());
        return json;
    }
}
