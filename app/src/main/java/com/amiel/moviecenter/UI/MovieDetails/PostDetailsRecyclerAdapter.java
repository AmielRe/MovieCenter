package com.amiel.moviecenter.UI.MovieDetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amiel.moviecenter.R;

import java.util.ArrayList;
import java.util.List;

class PostDetailsRecyclerAdapter extends RecyclerView.Adapter<PostDetailsViewHolder> {
    List<PostDetailsItem> data;

    public PostDetailsRecyclerAdapter(List<PostDetailsItem> originalData) {
        this.data = new ArrayList<>();
        this.data.addAll(originalData);
    }

    @NonNull
    @Override
    public PostDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_post_row_item, parent, false);
        return new PostDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostDetailsViewHolder holder, int position) {
        PostDetailsItem post = data.get(position);
        holder.bind(post, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}