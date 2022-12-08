package com.amiel.moviecenter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MoviesListFragment extends Fragment {

    // Recycler View object
    RecyclerView list;

    // Define array List for Recycler View data
    private List<MovieListItem> originalData;

    MovieRecyclerAdapter adapter;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.movie_list_fragment, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // initialise ListView with id
        list = view.findViewById(R.id.main_recycler_list_movies);
        list.setHasFixedSize(true);

        // Add items to Array List
        originalData = new ArrayList<>();
        originalData.add(new MovieListItem("Joker", "2019", R.drawable.joker));
        originalData.add(new MovieListItem("Inception", "2010", R.drawable.inception));
        originalData.add(new MovieListItem("Black Panther", "2018", R.drawable.blackpanther));
        originalData.add(new MovieListItem("Jaws", "1975", R.drawable.jaws));

        // Set adapter to recycler view
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MovieRecyclerAdapter(originalData);
        list.setAdapter(adapter);

        list.addItemDecoration(new DividerItemDecoration(list.getContext(), DividerItemDecoration.VERTICAL));

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Bundle dataToAdd = new Bundle();
                dataToAdd.putString("name", adapter.getItemAtPosition(pos).movieName);
                dataToAdd.putString("year", adapter.getItemAtPosition(pos).movieYear);
                dataToAdd.putInt("image", adapter.getItemAtPosition(pos).imageResID);
                FragmentUtils.loadFragment(MoviesListFragment.this, null, new MovieDetailsFragment(), R.id.activity_main_frame_layout, dataToAdd);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate menu with items using MenuInflator
        inflater.inflate(R.menu.menu, menu);

        if (FirebaseAuthHandler.getInstance().isUserLoggedIn()) {
            menu.findItem(R.id.menu_login).setVisible(false);
        } else {
            menu.findItem(R.id.menu_profile).setVisible(false);
            menu.findItem(R.id.menu_my_posts).setVisible(false);
            menu.findItem(R.id.menu_logout).setVisible(false);
        }

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
                if (originalData.stream().anyMatch(curr -> curr.movieName.toLowerCase().contains(query.toLowerCase()))) {
                    adapter.getFilter().filter(query);
                } else {
                    // Search query not found in List View
                    Toast.makeText(getActivity(), "Not found", Toast.LENGTH_LONG).show();
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

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.menu_login:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;

            case R.id.menu_profile:
                FragmentUtils.loadFragment(null, getActivity(), new ProfileFragment(), R.id.activity_main_frame_layout, null);
                break;

            case R.id.menu_my_posts:
                // Here will come my_posts fragment
                break;

            case R.id.menu_logout:
                FirebaseAuthHandler.getInstance().logoutCurrentUser();
                getActivity().invalidateOptionsMenu();

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        
        if (FirebaseAuthHandler.getInstance().isUserLoggedIn()) {
            menu.findItem(R.id.menu_login).setVisible(false);
        } else {
            menu.findItem(R.id.menu_profile).setVisible(false);
            menu.findItem(R.id.menu_my_posts).setVisible(false);
            menu.findItem(R.id.menu_logout).setVisible(false);
        }
    }
}
