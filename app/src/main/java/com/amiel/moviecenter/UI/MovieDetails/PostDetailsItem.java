package com.amiel.moviecenter.UI.MovieDetails;

import com.amiel.moviecenter.DB.Model.Post;
import com.amiel.moviecenter.DB.Model.User;

public class PostDetailsItem {

    User postUser;
    Post post;

    public PostDetailsItem(User postUser, Post post)
    {
        this.postUser = postUser;
        this.post = post;
    }
}
