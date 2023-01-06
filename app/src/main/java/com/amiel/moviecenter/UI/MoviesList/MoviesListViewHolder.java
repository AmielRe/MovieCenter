package com.amiel.moviecenter.UI.MoviesList;

import android.graphics.Bitmap;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amiel.moviecenter.DB.Model.Movie;
import com.amiel.moviecenter.Utils.OnItemClickListener;
import com.amiel.moviecenter.Utils.ImageUtils;
import com.amiel.moviecenter.databinding.RowItemBinding;

class MoviesListViewHolder extends RecyclerView.ViewHolder{
    RowItemBinding binding;

    public MoviesListViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        binding = RowItemBinding.bind(itemView);

        itemView.setOnClickListener(view -> {
            int pos = getAdapterPosition();
            listener.onItemClick(pos);
        });
    }

    public void bind(Movie movie, int pos) {
        binding.rowItemMovieName.setText(movie.getName());
        binding.rowItemMovieYear.setText(String.valueOf(movie.getYear()));
        Bitmap movieBitmap = ImageUtils.getBitmap(movie.getPoster());
        if(movieBitmap != null) {
            binding.rowItemMovieImage.setImageBitmap(movieBitmap);
        }
    }
}
