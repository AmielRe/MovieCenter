package com.amiel.moviecenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MovieListAdapter extends BaseAdapter implements Filterable {

    Context context;
    private List<MovieListItem> originalData;
    private List<MovieListItem> filteredData;
    private static LayoutInflater inflater = null;

    public MovieListAdapter(Context context, List<MovieListItem> data) {
        this.context = context;
        this.originalData = data;
        this.filteredData = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.row_item, null);

        ImageView movieImage = (ImageView) vi.findViewById(R.id.row_item_movie_image);
        TextView movieName = (TextView) vi.findViewById(R.id.row_item_movie_name);
        TextView movieYear = (TextView) vi.findViewById(R.id.row_item_movie_year);

        MovieListItem item = filteredData.get(position);

        movieName.setText(item.movieName);
        movieYear.setText(item.movieYear);
        movieImage.setImageResource(item.imageResID);

        return vi;
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
}
