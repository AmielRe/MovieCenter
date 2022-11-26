package com.amiel.moviecenter;

public class MovieListItem {
    String movieName;
    String movieYear;
    Integer imageResID;

    public MovieListItem(String movieName, String movieYear, Integer imageResID)
    {
        this.movieName = movieName;
        this.movieYear = movieYear;
        this.imageResID = imageResID;
    }
}
