package com.amiel.moviecenter.UI.MovieDetails;

public class PostDetailsItem {

    public String username;
    public String postText;
    public byte[] userImage;
    public float rating;

    public PostDetailsItem(String username, String postText, byte[] userImage, float rating)
    {
        this.username = username;
        this.postText = postText;
        this.userImage = userImage;
        this.rating = rating;
    }
}
