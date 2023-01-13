package com.amiel.moviecenter.DB.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.Map;

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

    @NonNull
    @ColumnInfo(name = "posterUrl")
    private String posterUrl;

    public static final String NAME = "name";
    public static final String ID = "id";
    public static final String YEAR = "year";
    public static final String RATING = "rating";
    public static final String PLOT = "plot";
    public static final String POSTER_URL = "posterUrl";
    public static final String COLLECTION = "Movies";

    public Movie(String name, int year, float rating, String plot, byte[] poster, long id, String posterUrl)
    {
        this.name = name;
        this.year = year;
        this.rating = rating;
        this.plot = plot;
        this.poster = poster;
        this.id = id;
        this.posterUrl = posterUrl;
    }

    @Ignore
    public Movie() {

    }

    @NonNull
    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(@NonNull String posterUrl) {
        this.posterUrl = posterUrl;
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

    public static Movie fromJson(Map<String,Object> json){
        long id = (long) json.get(ID);
        String name = (String)json.get(NAME);
        int year = (int) json.get(YEAR);
        float rating = (float) json.get(RATING);
        String plot = (String) json.get(PLOT);
        String posterUrl = (String) json.get(POSTER_URL);
        return new Movie(name, year, rating, plot, null, id, posterUrl);
    }

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put(ID, getId());
        json.put(NAME, getName());
        json.put(YEAR, getYear());
        json.put(RATING, getRating());
        json.put(PLOT, getPlot());
        json.put(POSTER_URL, getPosterUrl());
        return json;
    }
}
