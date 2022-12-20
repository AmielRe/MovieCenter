package com.amiel.moviecenter.UI.MoviesList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amiel.moviecenter.DB.Model.Movie;
import com.amiel.moviecenter.OnItemClickListener;
import com.amiel.moviecenter.R;

import java.util.ArrayList;
import java.util.List;

class MoviesListRecyclerAdapter extends RecyclerView.Adapter<MoviesListViewHolder> implements Filterable {

    OnItemClickListener listener;
    List<Movie> originalData;
    List<Movie> filteredData;

    public MoviesListRecyclerAdapter(List<Movie> originalData) {
        this.originalData = new ArrayList<>();
        this.filteredData = new ArrayList<>();
        this.originalData.addAll(originalData);
        this.filteredData.addAll(originalData);
    }

    void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public MoviesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item,parent,false);
        return new MoviesListViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesListViewHolder holder, int position) {
        Movie movie = filteredData.get(position);
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
                    results.values = new ArrayList<>(originalData);
                    results.count = originalData.size();
                }
                else
                {
                    ArrayList<Movie> filterResultsData = new ArrayList<>();

                    for(Movie data : originalData)
                    {
                        // In this loop, you'll filter through originalData and compare each item to charSequence.
                        // If you find a match, add it to your new ArrayList
                        // I'm not sure how you're going to do comparison, so you'll need to fill out this conditional
                        if(data.getName().toLowerCase().contains(charSequence.toString().toLowerCase()))
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
                filteredData = (ArrayList<Movie>)filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public Movie getItemAtPosition(int pos) {
        return filteredData.get(pos);
    }

    public void updateMovieRating(Movie updatedMovie) {
        for(Movie data : originalData)
        {
            if(data.getName().toLowerCase().contains(updatedMovie.getName().toLowerCase()))
            {
                originalData.set(originalData.indexOf(data), updatedMovie);
                return;
            }
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        originalData.clear();
        filteredData.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Movie> list) {
        originalData.addAll(list);
        filteredData.addAll(list);
        notifyDataSetChanged();
    }
}
