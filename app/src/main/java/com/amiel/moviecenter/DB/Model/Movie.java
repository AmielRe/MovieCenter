package com.amiel.moviecenter.DB.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Movies")
public class Movie {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "year")
    private int year;

    @NonNull
    @ColumnInfo(name = "rating")
    private float rating;

    @NonNull
    @ColumnInfo(name = "plot")
    private String plot;

    @NonNull
    @ColumnInfo(name = "poster")
    private byte[] poster;

    public Movie(String name, int year, float rating, String plot, byte[] poster, long id)
    {
        this.name = name;
        this.year = year;
        this.rating = rating;
        this.plot = plot;
        this.poster = poster;
        this.id = id;
    }

    @Ignore
    public Movie() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @NonNull
    public String getPlot() {
        return plot;
    }

    public void setPlot(@NonNull String plot) {
        this.plot = plot;
    }

    @NonNull
    public byte[] getPoster() {
        return poster;
    }

    public void setPoster(byte[] poster) {
        this.poster = poster;
    }
}
