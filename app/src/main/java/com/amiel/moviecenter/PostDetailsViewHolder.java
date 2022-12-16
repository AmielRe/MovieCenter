package com.amiel.moviecenter;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class PostDetailsViewHolder extends RecyclerView.ViewHolder{
    TextView username;
    TextView postText;
    ImageView userImage;
    RatingBar rating;

    public PostDetailsViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        username = itemView.findViewById(R.id.movie_post_row_item_user_name);
        postText = itemView.findViewById(R.id.movie_post_row_item_post_text);
        userImage = itemView.findViewById(R.id.movie_post_row_item_user_image);
        rating = itemView.findViewById(R.id.movie_post_row_item_post_rating);
    }

    public void bind(PostDetailsItem post, int pos) {
        this.username.setText(itemView.getContext().getString(R.string.username, post.username));
        this.postText.setText(post.postText);
        this.rating.setRating(post.rating);
        Bitmap userImageBitmap = ImageUtils.getBitmap(post.userImage);
        if(userImageBitmap != null) {
            this.userImage.setImageBitmap(userImageBitmap);
        }
    }
}