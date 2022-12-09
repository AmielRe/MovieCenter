package com.amiel.moviecenter.DB.Model;

public class Post {

    public long id;
    public String text;
    public long movieID;
    public float rating;
    public byte[] image;

    public Post()
    {

    }

    public Post(String text, long movieID, float rating, byte[] image, long id)
    {
        this.text = text;
        this.movieID = movieID;
        this.rating = rating;
        this.image = image;
        this.id = id;
    }
}
