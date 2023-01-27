package com.amiel.moviecenter.Utils.MovieRemoteApi;

import com.amiel.moviecenter.DB.Model.Movie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieApi {

    @GET("/?apikey=5fac8496")
    Call<Movie> getMovieByTitle(@Query("t") String title);
}
