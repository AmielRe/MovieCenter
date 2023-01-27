package com.amiel.moviecenter.Utils.MovieRemoteApi;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.amiel.moviecenter.DB.Model.Movie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
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
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
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
                result.setValue(null);
            }
        });

        return result;
    }
}
