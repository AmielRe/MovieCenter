package com.amiel.moviecenter.UI.MovieDetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amiel.moviecenter.OnItemClickListener;
import com.amiel.moviecenter.R;

import java.util.ArrayList;
import java.util.List;

class PostDetailsRecyclerAdapter extends RecyclerView.Adapter<PostDetailsViewHolder> {

    OnItemClickListener listener;
    List<PostDetailsItem> data;

    public PostDetailsRecyclerAdapter(List<PostDetailsItem> originalData) {
        this.data = new ArrayList<>();
        this.data.addAll(originalData);
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_post_row_item, parent, false);
        return new PostDetailsViewHolder(view, listener);
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

    public PostDetailsItem getItemAtPosition(int pos) {
        return data.get(pos);
    }
}