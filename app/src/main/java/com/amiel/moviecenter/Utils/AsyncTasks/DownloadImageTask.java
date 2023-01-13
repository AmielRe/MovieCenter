package com.amiel.moviecenter.Utils.AsyncTasks;

import androidx.appcompat.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    WeakReference<ImageView> movieImage;

    public DownloadImageTask(ImageView movieImage) {
        this.movieImage = new WeakReference<>(movieImage);
    }

    protected Bitmap doInBackground(String... urls) {

        final OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(urls[0])
                .build();

        Response response = null;
        Bitmap mIcon11 = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response.isSuccessful()) {
            try {
                mIcon11 = BitmapFactory.decodeStream(response.body().byteStream());
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        if(result != null) {
            movieImage.get().setImageBitmap(result);
            movieImage.get().setBackground(null);
        }
    }
}
