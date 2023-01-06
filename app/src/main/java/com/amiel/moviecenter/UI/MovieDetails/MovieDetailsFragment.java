package com.amiel.moviecenter.UI.MovieDetails;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.amiel.moviecenter.DB.Model.Post;
import com.amiel.moviecenter.DB.Model.User;
import com.amiel.moviecenter.R;
import com.amiel.moviecenter.Utils.ViewModelFactory;
import com.amiel.moviecenter.databinding.MovieDetailsFragmentBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MovieDetailsFragment extends Fragment {

    PostDetailsRecyclerAdapter adapter;
    MovieDetailsFragmentBinding binding;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        binding = MovieDetailsFragmentBinding.inflate(inflater, parent, false);

        return binding.getRoot();
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        MovieDetailsViewModel movieDetailsViewModel = new ViewModelProvider(this, new ViewModelFactory(requireActivity().getApplication(), MovieDetailsFragmentArgs.fromBundle(getArguments()).getId())).get(MovieDetailsViewModel.class);

        binding.movieDetailsRecyclerView.setHasFixedSize(true);

        // Set adapter to recycler view
        binding.movieDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        movieDetailsViewModel.getPosts().observe(getViewLifecycleOwner(), postsMap -> {
            List<PostDetailsItem> postsRowItems = new ArrayList<>();
            for(Map.Entry<User, List<Post>> currEntry : postsMap.entrySet()) {
                for(Post currPost : currEntry.getValue()) {
                    postsRowItems.add(new PostDetailsItem(currEntry.getKey().getUsername(), currPost.getText(), currEntry.getKey().getProfileImage(), currPost.getRating()));
                }
                adapter = new PostDetailsRecyclerAdapter(postsRowItems);
                binding.movieDetailsRecyclerView.setAdapter(adapter);
            }
        });

        binding.movieDetailsMovieName.setText(MovieDetailsFragmentArgs.fromBundle(getArguments()).getName());
        binding.movieDetailsMovieYear.setText(String.valueOf(MovieDetailsFragmentArgs.fromBundle(getArguments()).getYear()));
        Bitmap movieBitmap = MovieDetailsFragmentArgs.fromBundle(getArguments()).getImage();
        binding.movieDetailsMovieImage.setImageBitmap(movieBitmap);
        binding.movieDetailsMovieRating.setRating(MovieDetailsFragmentArgs.fromBundle(getArguments()).getRating());
        binding.movieDetailsMovieDescription.setText(MovieDetailsFragmentArgs.fromBundle(getArguments()).getPlot());

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.findItem(R.id.search_bar).setVisible(false);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }

            @Override
            public void onPrepareMenu(@NonNull Menu menu) {}
        });
    }
}
