package com.amiel.moviecenter.UI.MovieDetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amiel.moviecenter.DB.Model.Movie;
import com.amiel.moviecenter.DB.Model.Post;
import com.amiel.moviecenter.DB.Model.User;
import com.amiel.moviecenter.R;
import com.amiel.moviecenter.UI.MyPosts.MyPostRowItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(Map<User, List<Post>> list) {
        List<PostDetailsItem> postsRowItems = new ArrayList<>();
        for(Map.Entry<User, List<Post>> currEntry : list.entrySet()) {
            for(Post currPost : currEntry.getValue()) {
                if(!currPost.getDeleted()) {
                    postsRowItems.add(new PostDetailsItem(currEntry.getKey(), currPost));
                }
            }
        }
        data.addAll(postsRowItems);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}