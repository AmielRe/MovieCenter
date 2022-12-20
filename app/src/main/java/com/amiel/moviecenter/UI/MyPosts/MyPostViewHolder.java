package com.amiel.moviecenter.UI.MyPosts;

import android.view.View;
import android.widget.EditText;

import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amiel.moviecenter.OnItemClickListener;
import com.amiel.moviecenter.R;
import com.amiel.moviecenter.UI.MyPosts.MyPostRowItem;

public class MyPostViewHolder extends RecyclerView.ViewHolder{
    RatingBar postRating;
    EditText postText;
    TextView postMovieName;

    public MyPostViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        postRating = itemView.findViewById(R.id.my_post_row_item_post_rating);
        postText = itemView.findViewById(R.id.my_post_row_item_post_text);
        postMovieName = itemView.findViewById(R.id.my_post_row_item_post_movie_name);

        itemView.setOnClickListener(view -> {
            int pos = getAdapterPosition();
            listener.onItemClick(pos);
        });
    }

    public void bind(MyPostRowItem post, int pos) {
        postRating.setRating(post.rating);
        postText.setText(post.postText);
        postMovieName.setText(post.postMovieName);
    }
}