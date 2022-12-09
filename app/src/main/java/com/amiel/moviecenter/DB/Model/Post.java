package com.amiel.moviecenter.DB.Model;

public class Post {

    public String text;
    public long movieID;
    public float rating;
    public byte[] image;

    public Post()
    {

    }

    public Post(String text, long movieID, float rating, byte[] image)
    {
        this.text = text;
        this.movieID = movieID;
        this.rating = rating;
        this.image = image;
    }
}
