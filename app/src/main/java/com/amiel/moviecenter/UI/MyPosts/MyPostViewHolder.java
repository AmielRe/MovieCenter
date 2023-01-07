package com.amiel.moviecenter.UI.MyPosts;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amiel.moviecenter.Utils.ImageUtils;
import com.amiel.moviecenter.Utils.OnMyPostRowItemClickListener;
import com.amiel.moviecenter.databinding.MyPostRowItemBinding;

import java.text.SimpleDateFormat;

public class MyPostViewHolder extends RecyclerView.ViewHolder{
    MyPostRowItemBinding binding;

    public MyPostViewHolder(@NonNull View itemView, OnMyPostRowItemClickListener listener) {
        super(itemView);
        binding = MyPostRowItemBinding.bind(itemView);

//        itemView.setOnClickListener(view -> {
//            int pos = getAdapterPosition();
//            toggleEditText(binding.myPostRowItemPostText, true);
//            binding.myPostRowItemPostText.setOnFocusChangeListener((view1, inFocus) -> {
//                if(!inFocus) {
//                    toggleEditText(binding.myPostRowItemPostText, false);
//                    listener.onItemClick(pos, binding.myPostRowItemPostText.getText().toString());
//                }
//            });
//        });

        binding.myPostRowItemPostText.setOnFocusChangeListener((view1, inFocus) -> {
            int pos = getAdapterPosition();
            if(!inFocus) {
                toggleEditText(binding.myPostRowItemPostText, false);
                listener.onItemClick(pos, binding.myPostRowItemPostText.getText().toString());
            }
        });
    }

    public void bind(MyPostRowItem post, int pos) {
        binding.myPostRowItemPostRating.setRating(post.post.getRating());
        binding.myPostRowItemPostText.setText(post.post.getText());
        binding.myPostRowItemMovieNameAndYear.setText(post.postMovie.getName() + " (" + post.postMovie.getYear() + ")");
        binding.myPostRowItemPostImage.setImageBitmap(ImageUtils.getBitmap(post.post.getImage()));
        binding.myPostRowItemPostDate.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(post.post.postDate));
    }

    private void toggleEditText(EditText editText, boolean isEnabled) {
        editText.setCursorVisible(isEnabled);
    }
}