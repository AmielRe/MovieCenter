package com.amiel.moviecenter.UI.MoviesList;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amiel.moviecenter.DB.Model.Movie;
import com.amiel.moviecenter.Utils.OnItemClickListener;
import com.amiel.moviecenter.R;
import com.amiel.moviecenter.Utils.ImageUtils;

class MoviesListViewHolder extends RecyclerView.ViewHolder{
    TextView movieName;
    TextView movieYear;
    ImageView movieImage;

    public MoviesListViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        movieName = itemView.findViewById(R.id.row_item_movie_name);
        movieYear = itemView.findViewById(R.id.row_item_movie_year);
        movieImage = itemView.findViewById(R.id.row_item_movie_image);

        itemView.setOnClickListener(view -> {
            int pos = getAdapterPosition();
            listener.onItemClick(pos);
        });
    }

    public void bind(Movie movie, int pos) {
        movieName.setText(movie.getName());
        movieYear.setText(String.valueOf(movie.getYear()));
        Bitmap movieBitmap = ImageUtils.getBitmap(movie.getPoster());
        if(movieBitmap != null) {
            movieImage.setImageBitmap(movieBitmap);
        }
    }
}
