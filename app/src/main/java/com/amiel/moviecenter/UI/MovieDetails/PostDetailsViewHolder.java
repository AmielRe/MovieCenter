package com.amiel.moviecenter.UI.MovieDetails;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amiel.moviecenter.R;
import com.amiel.moviecenter.Utils.ImageUtils;
import com.amiel.moviecenter.databinding.MoviePostRowItemBinding;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

class PostDetailsViewHolder extends RecyclerView.ViewHolder{

    MoviePostRowItemBinding binding;

    public PostDetailsViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = MoviePostRowItemBinding.bind(itemView);
    }

    public void bind(PostDetailsItem post, int pos) {
        binding.moviePostRowItemPostDate.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(post.post.getPostDate()));
        binding.moviePostRowItemUserName.setText(itemView.getContext().getString(R.string.username, post.postUser.getUsername()));
        binding.moviePostRowItemPostText.setText(post.post.getText());
        binding.moviePostRowItemPostRating.setRating(post.post.getRating());
        byte[] postImage = post.post.getImage();
        byte[] postUserImage = post.postUser.getProfileImage();

        if(postImage != null) {
            binding.moviePostRowItemPostImage.setImageBitmap(ImageUtils.getBitmap(postImage));
        } else if(!post.post.getPostImageUrl().isEmpty()) {
            Picasso.get().load(post.post.getPostImageUrl()).placeholder(R.drawable.default_post_placeholder).into(binding.moviePostRowItemPostImage);
        }

        if(postUserImage != null) {
            binding.moviePostRowItemUserImage.setImageBitmap(ImageUtils.getBitmap(postUserImage));
        } else if(!post.postUser.getProfileImageUrl().isEmpty()) {
            Picasso.get().load(post.postUser.getProfileImageUrl()).placeholder(R.drawable.default_profile_image).into(binding.moviePostRowItemUserImage);
        }
    }
}