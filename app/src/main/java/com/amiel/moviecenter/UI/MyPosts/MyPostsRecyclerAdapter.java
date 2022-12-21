package com.amiel.moviecenter.UI.MyPosts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amiel.moviecenter.R;
import com.amiel.moviecenter.Utils.OnMyPostRowItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MyPostsRecyclerAdapter extends RecyclerView.Adapter<MyPostViewHolder> {

    OnMyPostRowItemClickListener listener;
    List<MyPostRowItem> data;

    public MyPostsRecyclerAdapter(List<MyPostRowItem> originalData) {
        this.data = new ArrayList<>();
        this.data.addAll(originalData);
    }

    void setOnItemClickListener(OnMyPostRowItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_post_row_item, parent, false);
        return new MyPostViewHolder(view, listener);
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
}