package com.amiel.moviecenter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieViewHolder> implements Filterable {

    OnItemClickListener listener;
    List<MovieListItem> originalData;
    List<MovieListItem> filteredData;

    public MovieRecyclerAdapter(List<MovieListItem> originalData) {
        this.originalData = originalData;
        this.filteredData = originalData;
    }

    void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item,parent,false);
        return new MovieViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MovieListItem movie = filteredData.get(position);
        holder.bind(movie, position);
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                FilterResults results = new FilterResults();

                // If there's nothing to filter on, return the original data for your list
                if(charSequence == null || charSequence.length() == 0)
                {
                    results.values = originalData;
                    results.count = originalData.size();
                }
                else
                {
                    ArrayList<MovieListItem> filterResultsData = new ArrayList<>();

                    for(MovieListItem data : originalData)
                    {
                        // In this loop, you'll filter through originalData and compare each item to charSequence.
                        // If you find a match, add it to your new ArrayList
                        // I'm not sure how you're going to do comparison, so you'll need to fill out this conditional
                        if(data.movieName.toLowerCase().contains(charSequence.toString().toLowerCase()))
                        {
                            filterResultsData.add(data);
                        }
                    }

                    results.values = filterResultsData;
                    results.count = filterResultsData.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                filteredData = (ArrayList<MovieListItem>)filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public MovieListItem getItemAtPosition(int pos) {
        return filteredData.get(pos);
    }
}
