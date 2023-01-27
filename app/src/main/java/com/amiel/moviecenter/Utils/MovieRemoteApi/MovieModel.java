package com.amiel.moviecenter.Utils.MovieRemoteApi;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.amiel.moviecenter.DB.Model.Movie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieModel {

    final String BASE_URL = "https://www.omdbapi.com/";
    Retrofit retrofit;
    MovieApi movieApi;

    private static final class InstanceHolder {
        static final MovieModel INSTANCE = new MovieModel();
    }

    public static MovieModel getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private MovieModel() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        movieApi = retrofit.create(MovieApi.class);
    }

    public LiveData<Movie> GetMovieByTitle(String title) {
        MutableLiveData<Movie> result = new MutableLiveData<>();
        Call<Movie> call = movieApi.getMovieByTitle(title);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()){
                    Movie res = response.body();
                    result.setValue(res);
                } else{
                    Log.d("TAG","----- getMoviesByTitle response error");
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }
        });

        return result;
    }
}
