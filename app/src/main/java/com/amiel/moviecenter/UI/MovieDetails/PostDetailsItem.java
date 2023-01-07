package com.amiel.moviecenter.UI.MovieDetails;

import java.util.Date;

public class PostDetailsItem {

    public String username;
    public String postText;
    public byte[] userImage;
    public float rating;
    public Date postDate;
    public byte[] postImage;

    public PostDetailsItem(String username, String postText, byte[] userImage, float rating, Date postDate, byte[] postImage)
    {
        this.username = username;
        this.postText = postText;
        this.userImage = userImage;
        this.rating = rating;
        this.postDate = postDate;
        this.postImage = postImage;
    }
}
