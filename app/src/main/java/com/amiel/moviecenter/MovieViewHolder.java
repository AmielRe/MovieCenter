package com.amiel.moviecenter;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amiel.moviecenter.DB.Model.Movie;

class MovieViewHolder extends RecyclerView.ViewHolder{
    TextView movieName;
    TextView movieYear;
    ImageView movieImage;

    public MovieViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        movieName = itemView.findViewById(R.id.row_item_movie_name);
        movieYear = itemView.findViewById(R.id.row_item_movie_year);
        movieImage = itemView.findViewById(R.id.row_item_movie_image);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = getAdapterPosition();
                listener.onItemClick(pos);
            }
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
