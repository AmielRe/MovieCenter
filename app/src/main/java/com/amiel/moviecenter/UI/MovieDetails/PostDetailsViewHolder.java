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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

class PostDetailsViewHolder extends RecyclerView.ViewHolder{

    MoviePostRowItemBinding binding;

    public PostDetailsViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        binding = MoviePostRowItemBinding.bind(itemView);
    }

    public void bind(PostDetailsItem post, int pos) {
        binding.moviePostRowItemPostDate.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(post.post.getPostDate()));
        binding.moviePostRowItemUserName.setText(itemView.getContext().getString(R.string.username, post.postUser.getUsername()));
        binding.moviePostRowItemPostText.setText(post.post.getText());
        binding.moviePostRowItemPostRating.setRating(post.post.getRating());
        Picasso.get().load(post.postUser.getProfileImageUrl()).placeholder(R.drawable.default_profile_image).into(binding.moviePostRowItemUserImage);
        Picasso.get().load(post.post.getPostImageUrl()).placeholder(R.drawable.default_post_placeholder).into(binding.moviePostRowItemPostImage);
    }
}