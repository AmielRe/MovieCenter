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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MovieDetailsFragment extends Fragment {

    PostDetailsRecyclerAdapter adapter;
    MovieDetailsFragmentBinding binding;

    MovieDetailsViewModel movieDetailsViewModel;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        binding = MovieDetailsFragmentBinding.inflate(inflater, parent, false);

        return binding.getRoot();
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        movieDetailsViewModel = new ViewModelProvider(this, new ViewModelFactory(requireActivity().getApplication(), MovieDetailsFragmentArgs.fromBundle(getArguments()).getId())).get(MovieDetailsViewModel.class);

        binding.movieDetailsRecyclerView.setHasFixedSize(true);

        adapter = new PostDetailsRecyclerAdapter(new ArrayList<>());
        binding.movieDetailsRecyclerView.setAdapter(adapter);

        // Set adapter to recycler view
        binding.movieDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        updatePosts();

        binding.movieDetailsMovieName.setText(MovieDetailsFragmentArgs.fromBundle(getArguments()).getName());
        binding.movieDetailsMovieYear.setText(String.valueOf(MovieDetailsFragmentArgs.fromBundle(getArguments()).getYear()));

        Bitmap movieBitmap = MovieDetailsFragmentArgs.fromBundle(getArguments()).getImage();
        if(movieBitmap != null) {
            binding.movieDetailsMovieImage.setImageBitmap(movieBitmap);
        } else if(!MovieDetailsFragmentArgs.fromBundle(getArguments()).getImageurl().isEmpty()) {
            Picasso.get().load(MovieDetailsFragmentArgs.fromBundle(getArguments()).getImageurl()).placeholder(R.drawable.default_post_placeholder).into(binding.movieDetailsMovieImage);
        } else {
            binding.movieDetailsMovieImage.setImageResource(R.drawable.default_post_placeholder);
        }

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

    @Override
    public void onResume() {
        super.onResume();
        updatePosts();
    }

    public void updatePosts() {
        movieDetailsViewModel.getPosts().observe(getViewLifecycleOwner(), postsMap -> {
            adapter.clear();
            adapter.addAll(postsMap);
        });
    }
}
