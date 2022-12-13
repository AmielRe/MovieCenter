package com.amiel.moviecenter;

import android.graphics.Bitmap;
import android.media.Image;
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

public class MovieDetailsFragment extends Fragment {

    TextView movieName;
    TextView movieYear;
    ImageView movieImage;
    RatingBar movieRating;
    TextView moviePlot;

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

        movieName.setText(getArguments().getString("name"));
        movieYear.setText(getArguments().getString("year"));
        Bitmap movieBitmap = ImageUtils.getBitmap(getArguments().getByteArray("image"));
        if(movieBitmap != null) {
            movieImage.setImageBitmap(movieBitmap);
        }
        movieRating.setRating(getArguments().getFloat("rating"));
        moviePlot.setText(getArguments().getString("plot"));
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
