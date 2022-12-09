package com.amiel.moviecenter.DB.Model;

public class Movie {

    public String name;
    public int year;
    public float rating;
    public String plot;
    public byte[] poster;

    public Movie()
    {

    }

    public Movie(String name, int year, float rating, String plot, byte[] poster)
    {
        this.name = name;
        this.year = year;
        this.rating = rating;
        this.plot = plot;
        this.poster = poster;
    }
}
