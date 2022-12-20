package com.amiel.moviecenter.UI.MovieDetails;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amiel.moviecenter.DB.DatabaseRepository;
import com.amiel.moviecenter.DB.Model.Movie;
import com.amiel.moviecenter.DB.Model.Post;
import com.amiel.moviecenter.DB.Model.User;
import com.amiel.moviecenter.R;
import com.amiel.moviecenter.UI.MyPosts.MyPostRowItem;
import com.amiel.moviecenter.UI.MyPosts.MyPostsRecyclerAdapter;
import com.amiel.moviecenter.UI.Profile.ProfileViewModel;
import com.amiel.moviecenter.Utils.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MovieDetailsFragment extends Fragment {

    TextView movieName;
    TextView movieYear;
    ImageView movieImage;
    RatingBar movieRating;
    TextView moviePlot;

    // Recycler View object
    RecyclerView list;
    PostDetailsRecyclerAdapter adapter;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.movie_details_fragment, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        movieName = view.findViewById(R.id.movie_details_movie_name);
        movieYear = view.findViewById(R.id.movie_details_movie_year);
        movieImage = view.findViewById(R.id.movie_details_movie_image);
        movieRating = view.findViewById(R.id.movie_details_movie_rating);
        moviePlot = view.findViewById(R.id.movie_details_movie_description);
        MovieDetailsViewModel movieDetailsViewModel = new ViewModelProvider(this, new ViewModelFactory(requireActivity().getApplication(), MovieDetailsFragmentArgs.fromBundle(getArguments()).getId())).get(MovieDetailsViewModel.class);

        list = view.findViewById(R.id.movie_details_recycler_view);
        list.setHasFixedSize(true);

        // Set adapter to recycler view
        list.setLayoutManager(new LinearLayoutManager(requireActivity()));
        movieDetailsViewModel.getPosts().observe(getViewLifecycleOwner(), postsMap -> {
            List<PostDetailsItem> postsRowItems = new ArrayList<>();
            for(Map.Entry<User, List<Post>> currEntry : postsMap.entrySet()) {
                for(Post currPost : currEntry.getValue()) {
                    postsRowItems.add(new PostDetailsItem(currEntry.getKey().getUsername(), currPost.getText(), currEntry.getKey().getProfileImage(), currPost.getRating()));
                }
                adapter = new PostDetailsRecyclerAdapter(postsRowItems);
                list.setAdapter(adapter);
            }
        });

        movieName.setText(MovieDetailsFragmentArgs.fromBundle(getArguments()).getName());
        movieYear.setText(String.valueOf(MovieDetailsFragmentArgs.fromBundle(getArguments()).getYear()));
        Bitmap movieBitmap = MovieDetailsFragmentArgs.fromBundle(getArguments()).getImage();
        movieImage.setImageBitmap(movieBitmap);
        movieRating.setRating(MovieDetailsFragmentArgs.fromBundle(getArguments()).getRating());
        moviePlot.setText(MovieDetailsFragmentArgs.fromBundle(getArguments()).getPlot());

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
