package com.amiel.moviecenter.UI.MyPosts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amiel.moviecenter.DB.GenericListener;
import com.amiel.moviecenter.DB.Model.Movie;
import com.amiel.moviecenter.DB.Model.Post;
import com.amiel.moviecenter.R;
import com.amiel.moviecenter.Utils.OnMyPostRowItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyPostsRecyclerAdapter extends RecyclerView.Adapter<MyPostViewHolder> {

    OnMyPostRowItemClickListener listener;
    List<MyPostRowItem> data;

    GenericListener<MyPostViewHolder> changeImageListener;

    public MyPostsRecyclerAdapter(List<MyPostRowItem> originalData) {
        this.data = new ArrayList<>();
        this.data.addAll(originalData);
    }

    void setOnItemClickListener(OnMyPostRowItemClickListener listener) {
        this.listener = listener;
    }

    void setChangeImageListener(GenericListener<MyPostViewHolder> listener) {
        this.changeImageListener = listener;
    }

    @NonNull
    @Override
    public MyPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_post_row_item, parent, false);
        return new MyPostViewHolder(view, listener, changeImageListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostViewHolder holder, int position) {
        MyPostRowItem post = data.get(position);
        holder.bind(post, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public MyPostRowItem getItemAtPosition(int pos) {
        return data.get(pos);
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(Map<Movie, List<Post>> list) {
        List<MyPostRowItem> postsRowItems = new ArrayList<>();
        for (Map.Entry<Movie, List<Post>> currEntry : list.entrySet()) {
            for (Post currPost : currEntry.getValue()) {
                MyPostRowItem postRowItem = new MyPostRowItem(currPost, currEntry.getKey());
                postsRowItems.add(postRowItem);
            }
        }
        data.addAll(postsRowItems);
        notifyDataSetChanged();
    }
}