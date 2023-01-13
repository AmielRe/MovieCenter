package com.amiel.moviecenter.Utils.AsyncTasks;

import androidx.appcompat.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.widget.ImageView;

import com.amiel.moviecenter.DB.Model.Movie;
import com.amiel.moviecenter.UI.MoviesList.MoviesListViewModel;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetMovieDataTask extends AsyncTask<String, Void, Pair<Movie, String>> {
    WeakReference<ImageView> movieImage;
    WeakReference<TextInputEditText> movieName;
    WeakReference<TextInputEditText> movieYear;
    MoviesListViewModel moviesListViewModel;
    WeakReference<AlertDialog> loadingDialog;

    public GetMovieDataTask(ImageView movieImage, TextInputEditText movieName, TextInputEditText movieYear, MoviesListViewModel moviesListViewModel, AlertDialog loadingDialog) {
        this.movieImage = new WeakReference<>(movieImage);
        this.movieName = new WeakReference<>(movieName);
        this.movieYear = new WeakReference<>(movieYear);
        this.moviesListViewModel = moviesListViewModel;
        this.loadingDialog = new WeakReference<>(loadingDialog);
    }

    protected Pair<Movie, String> doInBackground(String... urls) {

        final OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(urls[0])
                .build();

        Response response = null;
        Pair<Movie, String> returnValue = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response.isSuccessful()) {
            try {
                JSONObject json = new JSONObject();
                json = new JSONObject(response.body().string());
                JSONObject resultsJSON = (JSONObject) json.getJSONArray("results").get(0);
                String plot = resultsJSON.getString("plot");
                String year = resultsJSON.getString("description").replaceAll("[^0-9]", "");
                String moviePosterUrl = resultsJSON.getString("image");
                String title = resultsJSON.getString("title");
                moviesListViewModel.setNewMoviePlot(plot);
                Movie movie = new Movie(title, Integer.parseInt(year), 0, plot, null, "", "");

                returnValue = Pair.create(movie, moviePosterUrl);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
        }

        return returnValue;
    }

    protected void onPostExecute(Pair<Movie, String> result) {
        if(result != null) {
            new DownloadImageTask(movieImage.get(), loadingDialog.get())
                    .execute(result.second);
            movieName.get().setText(result.first.getName());
            movieYear.get().setText(String.valueOf(result.first.getYear()));
            movieYear.get().setEnabled(false);
        } else {
            loadingDialog.get().dismiss();
        }
    }
}
