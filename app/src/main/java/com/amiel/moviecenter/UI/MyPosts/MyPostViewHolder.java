package com.amiel.moviecenter.UI.MyPosts;

import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amiel.moviecenter.R;
import com.amiel.moviecenter.Utils.ImageUtils;
import com.amiel.moviecenter.Utils.OnMyPostRowItemClickListener;
import com.amiel.moviecenter.databinding.MyPostRowItemBinding;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MyPostViewHolder extends RecyclerView.ViewHolder{
    MyPostRowItemBinding binding;

    public MyPostViewHolder(@NonNull View itemView, OnMyPostRowItemClickListener listener) {
        super(itemView);
        binding = MyPostRowItemBinding.bind(itemView);

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
        binding.myPostRowItemMovieNameAndYear.setText(this.itemView.getContext().getString(R.string.movie_name_and_year, post.postMovie.getName(), post.postMovie.getYear()));
        binding.myPostRowItemPostImage.setImageBitmap(ImageUtils.getBitmap(post.post.getImage()));
        binding.myPostRowItemPostDate.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(post.post.postDate));
    }

    private void toggleEditText(EditText editText, boolean isEnabled) {
        editText.setCursorVisible(isEnabled);
    }
}