package com.amiel.moviecenter;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amiel.moviecenter.DB.DatabaseRepository;
import com.amiel.moviecenter.DB.Model.Post;
import com.amiel.moviecenter.DB.Model.User;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailsFragment extends Fragment {

    TextView movieName;
    TextView movieYear;
    ImageView movieImage;
    RatingBar movieRating;
    TextView moviePlot;

    // Recycler View object
    RecyclerView list;
    PostDetailsRecyclerAdapter adapter;

    private DatabaseRepository db;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        db = new DatabaseRepository(getActivity());
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

        list = view.findViewById(R.id.movie_details_recycler_view);
        list.setHasFixedSize(true);

        // Set adapter to recycler view
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        db.getAllPostsForMovie(MovieDetailsFragmentArgs.fromBundle(getArguments()).getId()).observe(getActivity(), posts -> {
            List<PostDetailsItem> postsItems = new ArrayList<>();
            for(Post post : posts) {
                db.getUserById(post.getUserID()).observe(getActivity(), user -> {
                    postsItems.add(new PostDetailsItem(user.getUsername(), post.getText(), user.getProfileImage(), post.getRating()));
                    adapter = new PostDetailsRecyclerAdapter(postsItems);
                    list.setAdapter(adapter);
                });
            }
        });

        movieName.setText(MovieDetailsFragmentArgs.fromBundle(getArguments()).getName());
        movieYear.setText(String.valueOf(MovieDetailsFragmentArgs.fromBundle(getArguments()).getYear()));
        Bitmap movieBitmap = MovieDetailsFragmentArgs.fromBundle(getArguments()).getImage();
        if(movieBitmap != null) {
            movieImage.setImageBitmap(movieBitmap);
        }
        movieRating.setRating(MovieDetailsFragmentArgs.fromBundle(getArguments()).getRating());
        moviePlot.setText(MovieDetailsFragmentArgs.fromBundle(getArguments()).getPlot());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}
