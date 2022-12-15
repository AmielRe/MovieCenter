package com.amiel.moviecenter;

public class MyPostRowItem {

    public String postText;
    public float rating;
    public String postMovieName;

    public MyPostRowItem()
    {

    }

    public MyPostRowItem(String postText, float rating, String postMovieName)
    {
        this.postText = postText;
        this.rating = rating;
        this.postMovieName = postMovieName;
    }
}
