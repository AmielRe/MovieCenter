package com.amiel.moviecenter.UI.MyPosts;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amiel.moviecenter.Utils.Listeners.GenericListener;
import com.amiel.moviecenter.R;
import com.amiel.moviecenter.Utils.Listeners.OnMyPostRowItemClickListener;
import com.amiel.moviecenter.databinding.MyPostRowItemBinding;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MyPostViewHolder extends RecyclerView.ViewHolder{
    MyPostRowItemBinding binding;

    public MyPostViewHolder(@NonNull View itemView, OnMyPostRowItemClickListener listener, GenericListener<MyPostViewHolder> changeImageListener, GenericListener<Integer> removePostListener) {
        super(itemView);
        binding = MyPostRowItemBinding.bind(itemView);

        binding.myPostRowItemPostText.setOnFocusChangeListener((view1, inFocus) -> {
            int pos = getAdapterPosition();
            if(!inFocus) {
                hideKeyboard(view1);
                toggleEditText(binding.myPostRowItemPostText);
                final Bitmap postImageBitmap = ((BitmapDrawable) binding.myPostRowItemPostImage.getDrawable()).getBitmap();
                listener.onItemClick(pos, binding.myPostRowItemPostText.getText().toString(), postImageBitmap);
            }
        });

        binding.myPostRowItemPostImage.setOnClickListener(view -> changeImageListener.onComplete(this));

        binding.myPostRowItemDeleteButton.setOnClickListener(view -> removePostListener.onComplete(getAdapterPosition()));
    }

    public void bind(MyPostRowItem post, int pos) {
        binding.myPostRowItemPostRating.setRating(post.post.getRating());
        binding.myPostRowItemPostText.setText(post.post.getText());
        binding.myPostRowItemMovieNameAndYear.setText(this.itemView.getContext().getString(R.string.movie_name_and_year, post.postMovie.getName(), post.postMovie.getYear()));
        if(!post.post.postImageUrl.isEmpty()) {
            Picasso.get().load(post.post.postImageUrl).placeholder(R.drawable.default_post_placeholder).into(binding.myPostRowItemPostImage);
        } else {
            Picasso.get().load(R.drawable.default_post_placeholder).into(binding.myPostRowItemPostImage);
        }
        binding.myPostRowItemPostDate.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(post.post.postDate));
    }

    private void toggleEditText(EditText editText) {
        editText.setCursorVisible(false);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void setPostImage(Bitmap bitmap) {
        binding.myPostRowItemPostImage.setImageBitmap(bitmap);
    }
}