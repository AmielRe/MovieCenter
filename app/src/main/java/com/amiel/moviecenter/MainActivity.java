package com.amiel.moviecenter;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // List View object
    ListView listView;

    // Define array adapter for ListView
    MovieListAdapter adapter;

    // Define array List for List View data
    List<MovieListItem> moviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialise ListView with id
        listView = findViewById(R.id.listView);

        // Add items to Array List
        moviesList = new ArrayList<>();
        moviesList.add(new MovieListItem("Joker", "2019", R.drawable.joker));
        moviesList.add(new MovieListItem("Inception", "2010", R.drawable.inception));
        moviesList.add(new MovieListItem("Black Panther", "2018", R.drawable.blackpanther));
        moviesList.add(new MovieListItem("Jaws", "1975", R.drawable.jaws));

        // Set adapter to ListView
        adapter = new MovieListAdapter(this, moviesList);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu with items using MenuInflator
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        // Initialise menu item search bar
        // with id and take its object
        MenuItem searchViewItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);

        // attach setOnQueryTextListener
        // to search view defined above
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Override onQueryTextSubmit method which is call when submit query is searched
            @Override
            public boolean onQueryTextSubmit(String query) {
                // If the list contains the search query than filter the adapter
                // using the filter method with the query as its argument
                if (moviesList.stream().anyMatch(curr -> curr.movieName.toLowerCase().contains(query.toLowerCase()))) {
                    adapter.getFilter().filter(query);
                } else {
                    // Search query not found in List View
                    Toast.makeText(MainActivity.this, "Not found", Toast.LENGTH_LONG).show();
                }
                return false;
            }

            // This method is overridden to filter the adapter according
            // to a search query when the user is typing search
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}