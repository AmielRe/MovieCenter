package com.amiel.moviecenter.UI.MyPosts;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amiel.moviecenter.Utils.ImageUtils;
import com.amiel.moviecenter.Utils.OnMyPostRowItemClickListener;
import com.amiel.moviecenter.databinding.MyPostRowItemBinding;

public class MyPostViewHolder extends RecyclerView.ViewHolder{
    MyPostRowItemBinding binding;

    public MyPostViewHolder(@NonNull View itemView, OnMyPostRowItemClickListener listener) {
        super(itemView);
        binding = MyPostRowItemBinding.bind(itemView);

        itemView.setOnClickListener(view -> {
            int pos = getAdapterPosition();
            toggleEditText(binding.myPostRowItemPostText, true);
            binding.myPostRowItemPostText.setOnFocusChangeListener((view1, inFocus) -> {
                if(!inFocus) {
                    toggleEditText(binding.myPostRowItemPostText, false);
                    listener.onItemClick(pos, binding.myPostRowItemPostText.getText().toString());
                }
            });
        });
    }

    public void bind(MyPostRowItem post, int pos) {
        binding.myPostRowItemPostRating.setRating(post.post.getRating());
        binding.myPostRowItemPostText.setText(post.post.getText());
        binding.myPostRowItemMovieNameAndYear.setText(post.postMovie.getName() + " (" + post.postMovie.getYear() + ")");
        binding.myPostRowItemPostImage.setImageBitmap(ImageUtils.getBitmap(post.post.getImage()));
    }

    private void toggleEditText(EditText editText, boolean isEnabled) {
        editText.setFocusable(isEnabled);
        editText.setFocusableInTouchMode(isEnabled);
        editText.setEnabled(isEnabled);
        editText.setCursorVisible(isEnabled);
        editText.setBackgroundColor(isEnabled ? Color.WHITE : Color.TRANSPARENT);
    }
}