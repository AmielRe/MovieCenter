package com.amiel.moviecenter.UI.MovieDetails;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amiel.moviecenter.Utils.FirebaseStorageHandler;
import com.amiel.moviecenter.Utils.OnItemClickListener;
import com.amiel.moviecenter.R;
import com.amiel.moviecenter.Utils.ImageUtils;
import com.amiel.moviecenter.databinding.MoviePostRowItemBinding;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;

class PostDetailsViewHolder extends RecyclerView.ViewHolder{

    MoviePostRowItemBinding binding;

    public PostDetailsViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        binding = MoviePostRowItemBinding.bind(itemView);
    }

    public void bind(PostDetailsItem post, int pos) {
        binding.moviePostRowItemPostDate.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(post.postDate));
        binding.moviePostRowItemUserName.setText(itemView.getContext().getString(R.string.username, post.username));
        binding.moviePostRowItemPostText.setText(post.postText);
        binding.moviePostRowItemPostRating.setRating(post.rating);

        FirebaseStorageHandler.getInstance().downloadImage(post.userId, bytes -> {
            Bitmap userImageBitmap = ImageUtils.getBitmap(bytes);
            if(userImageBitmap != null) {
                binding.moviePostRowItemUserImage.setImageBitmap(userImageBitmap);
            }
        });

        Bitmap postImageBitmap = ImageUtils.getBitmap(post.postImage);
        if(postImageBitmap != null) {
            binding.moviePostRowItemPostImage.setImageBitmap(postImageBitmap);
        }
    }
}