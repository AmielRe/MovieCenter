package com.amiel.moviecenter.UI.MyPosts;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amiel.moviecenter.DB.Model.Post;
import com.amiel.moviecenter.Utils.ImageUtils;
import com.amiel.moviecenter.Utils.OnItemClickListener;
import com.amiel.moviecenter.R;
import com.amiel.moviecenter.Utils.OnMyPostRowItemClickListener;

public class MyPostViewHolder extends RecyclerView.ViewHolder{
    RatingBar postRating;
    EditText postText;
    TextView postMovieNameAndYear;
    ImageView postImage;

    public MyPostViewHolder(@NonNull View itemView, OnMyPostRowItemClickListener listener) {
        super(itemView);
        postRating = itemView.findViewById(R.id.my_post_row_item_post_rating);
        postText = itemView.findViewById(R.id.my_post_row_item_post_text);
        postImage = itemView.findViewById(R.id.my_post_row_item_post_image);
        postMovieNameAndYear = itemView.findViewById(R.id.my_post_row_item_movie_name_and_year);

        itemView.setOnClickListener(view -> {
            int pos = getAdapterPosition();
            toggleEditText(postText, true);
            postText.setOnFocusChangeListener((view1, inFocus) -> {
                if(!inFocus) {
                    toggleEditText(postText, false);
                    listener.onItemClick(pos, postText.getText().toString());
                }
            });
        });
    }

    public void bind(MyPostRowItem post, int pos) {
        postRating.setRating(post.post.getRating());
        postText.setText(post.post.getText());
        postMovieNameAndYear.setText(post.postMovie.getName() + " (" + post.postMovie.getYear() + ")");
        postImage.setImageBitmap(ImageUtils.getBitmap(post.post.getImage()));
    }

    private void toggleEditText(EditText editText, boolean isEnabled) {
        editText.setFocusable(isEnabled);
        editText.setFocusableInTouchMode(isEnabled);
        editText.setEnabled(isEnabled);
        editText.setCursorVisible(isEnabled);
        editText.setBackgroundColor(isEnabled ? Color.WHITE : Color.TRANSPARENT);
    }
}