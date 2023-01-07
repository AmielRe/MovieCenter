package com.amiel.moviecenter.DB.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Posts")
public class Post {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @NonNull
    @ColumnInfo(name = "text")
    public String text;

    @NonNull
    @ColumnInfo(name = "movieId")
    public long movieID;

    @NonNull
    @ColumnInfo(name = "rating")
    public float rating;

    @NonNull
    @ColumnInfo(name = "image")
    public byte[] image;

    @NonNull
    @ColumnInfo(name = "userId")
    public long userID;

    @NonNull
    @ColumnInfo(name = "postDate")
    public Date postDate;

    public Post(String text, long movieID, float rating, byte[] image, long userID, long id, Date postDate)
    {
        this.text = text;
        this.movieID = movieID;
        this.rating = rating;
        this.image = image;
        this.id = id;
        this.userID = userID;
        this.postDate = postDate;
    }

    @Ignore
    public Post()
    {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public long getMovieID() {
        return movieID;
    }

    public void setMovieID(@NonNull long movieID) {
        this.movieID = movieID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
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
}
