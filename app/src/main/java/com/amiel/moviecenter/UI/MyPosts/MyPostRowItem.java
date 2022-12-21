package com.amiel.moviecenter.UI.MyPosts;

import com.amiel.moviecenter.DB.Model.Movie;
import com.amiel.moviecenter.DB.Model.Post;

public class MyPostRowItem {

    Post post;
    Movie postMovie;

    public MyPostRowItem(Post post, Movie postMovie)
    {
        this.post = post;
        this.postMovie = postMovie;
    }
}
