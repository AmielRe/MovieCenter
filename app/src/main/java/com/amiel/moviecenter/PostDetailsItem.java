package com.amiel.moviecenter;

public class PostDetailsItem {

    public String username;
    public String postText;
    public byte[] userImage;
    public float rating;

    public PostDetailsItem()
    {

    }

    public PostDetailsItem(String username, String postText, byte[] userImage, float rating)
    {
        this.username = username;
        this.postText = postText;
        this.userImage = userImage;
        this.rating = rating;
    }
}
