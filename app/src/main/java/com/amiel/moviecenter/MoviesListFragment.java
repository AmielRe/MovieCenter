package com.amiel.moviecenter;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.MenuItemCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amiel.moviecenter.DB.DBManager;
import com.amiel.moviecenter.DB.Model.Movie;
import com.amiel.moviecenter.DB.Model.Post;
import com.amiel.moviecenter.DB.Model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class MoviesListFragment extends Fragment {

    MovieRecyclerAdapter adapter;

    // Recycler View object
    RecyclerView list;

    FloatingActionButton newPostFab;
    ImageView movieImageImageView;
    ImageView moviePosterImageView;

    private static final int GALLERY_REQUEST_CODE_POSTER = 2;
    private static final int GALLERY_REQUEST_CODE_IMAGE = 3;
    private static final String BASE_MOVIE_URL = "https://imdb-api.com/API/AdvancedSearch/k_4wqqdznf?title=%s&has=plot";

    private DBManager dbManager;
    String moviePlot;
    byte[] moviePosterByteArray;
    ProgressDialog progressDialog;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        dbManager = new DBManager(getActivity());
        dbManager.open();
        return inflater.inflate(R.layout.movie_list_fragment, parent, false);
    }

    @Override
    public void onDestroyView() {
        dbManager.close();
        super.onDestroyView();
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // initialise ListView with id
        list = view.findViewById(R.id.main_recycler_list_movies);
        list.setHasFixedSize(true);
        newPostFab = view.findViewById(R.id.movie_list_fab);

        if(!FirebaseAuthHandler.getInstance().isUserLoggedIn()) {
            newPostFab.setVisibility(View.INVISIBLE);
        } else {
            newPostFab.setVisibility(View.VISIBLE);
        }

        // Add items to Array List
        ArrayList<Movie> allMovies = dbManager.getAllMovies();

        // Set adapter to recycler view
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MovieRecyclerAdapter(allMovies);
        list.setAdapter(adapter);

        list.addItemDecoration(new DividerItemDecoration(list.getContext(), DividerItemDecoration.VERTICAL));

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Bundle dataToAdd = new Bundle();
                dataToAdd.putString("name", adapter.getItemAtPosition(pos).name);
                dataToAdd.putInt("year", adapter.getItemAtPosition(pos).year);
                dataToAdd.putByteArray("image", adapter.getItemAtPosition(pos).poster);
                dataToAdd.putLong("id", adapter.getItemAtPosition(pos).id);
                dataToAdd.putFloat("rating", adapter.getItemAtPosition(pos).rating);
                dataToAdd.putString("plot", adapter.getItemAtPosition(pos).plot);
                FragmentUtils.loadFragment(MoviesListFragment.this, null, new MovieDetailsFragment(), R.id.activity_main_frame_layout, dataToAdd);
            }
        });

        newPostFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewPostDialog();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

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
                if (adapter.filteredData.stream().anyMatch(curr -> curr.name.toLowerCase().contains(query.toLowerCase()))) {
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    private void openNewPostDialog()
    {
        final NestedScrollView scrollViewLayout = (NestedScrollView) getLayoutInflater().inflate(R.layout.new_post_layout, null);

        final TextInputEditText movieName = scrollViewLayout.findViewById(R.id.new_post_movie_name_edit_text);
        final TextInputEditText movieYear = scrollViewLayout.findViewById(R.id.new_post_movie_year_edit_text);
        final TextInputLayout movieNameLayout = scrollViewLayout.findViewById(R.id.new_post_movie_name_input_layout);
        final TextInputLayout movieYearLayout = scrollViewLayout.findViewById(R.id.new_post_movie_year_input_layout);
        final ImageView movieImage = scrollViewLayout.findViewById(R.id.new_post_movie_image_upload_image);
        final ImageView moviePoster = scrollViewLayout.findViewById(R.id.new_post_movie_poster_upload_image);
        final RatingBar movieRating = scrollViewLayout.findViewById(R.id.new_post_movie_rating);
        final EditText movieExperienceText = scrollViewLayout.findViewById(R.id.new_post_how_was_your_experience_edit_text);
        movieImageImageView = scrollViewLayout.findViewById(R.id.new_post_movie_image_image_view);
        moviePosterImageView = scrollViewLayout.findViewById(R.id.new_post_movie_poster_image_view);

        movieName.setError(getString(R.string.error_invalid_movie_name));
        movieYear.setError(getString(R.string.error_invalid_movie_year));

        movieName.addTextChangedListener(new TextValidator(movieName) {
            @Override public void validate(TextView textView, String text) {
                if(text.length() <= 0) {
                    movieNameLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                    movieName.setError(getString(R.string.error_invalid_movie_name));
                } else {
                    movieName.setError(null);
                    movieNameLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                    movieNameLayout.setEndIconDrawable(R.drawable.ic_check_circle_black_24dp);
                }
            }
        });

        movieYear.addTextChangedListener(new TextValidator(movieYear) {
            @Override public void validate(TextView textView, String text) {
                if (text.length() <= 0) {
                    movieYearLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                    movieYear.setError(getString(R.string.error_invalid_movie_year));
                } else {
                    movieYear.setError(null);
                    movieYearLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                    movieYearLayout.setEndIconDrawable(R.drawable.ic_check_circle_black_24dp);
                }
            }
        });

        movieName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if ( !hasFocus && movieName.getText().toString().length() > 0 ) {

                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("");
                    progressDialog.setTitle("Searching for movie...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    if(!dbManager.isMovieExist(movieName.getText().toString())) {

                        OkHttpClient client = new OkHttpClient();

                        Request request = new Request.Builder()
                                .url(String.format(BASE_MOVIE_URL, movieName.getText().toString()))
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Could not find movie...", Toast.LENGTH_SHORT).show();
                                call.cancel();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                final String myResponse = response.body().string();
                                JSONObject json = new JSONObject();
                                try {
                                    json = new JSONObject(myResponse);
                                    JSONObject resultsJSON = (JSONObject) json.getJSONArray("results").get(0);
                                    moviePlot = resultsJSON.getString("plot");
                                    String year = resultsJSON.getString("description").replaceAll("[^0-9]", "");
                                    String moviePosterUrl = resultsJSON.getString("image");
                                    String title = resultsJSON.getString("title");

                                    Request request = new Request.Builder()
                                            .url(moviePosterUrl)
                                            .build();

                                    client.newCall(request).enqueue(new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getActivity(), "Could not find movie...", Toast.LENGTH_SHORT).show();
                                            call.cancel();
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            InputStream inputStream = response.body().byteStream();
                                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                            moviePosterByteArray = ImageUtils.getBytes(bitmap);
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    moviePosterImageView.setImageBitmap(bitmap);
                                                    moviePosterImageView.setBackground(null);
                                                    moviePoster.setOnClickListener(null);
                                                    movieYear.setText(year);
                                                    movieYear.setEnabled(false);
                                                    movieName.setText(title);
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getActivity(), "Found matching movie!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    });

                                } catch (JSONException e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "Could not find movie...", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } else {
                    if(movieName.getText().toString().length() > 0 && movieYear.getText().toString().length() > 0) {
                        try {
                            Movie movie = dbManager.getMovieByNameAndYear(movieName.getText().toString(), Integer.parseInt(movieYear.getText().toString()));
                            moviePosterImageView.setImageBitmap(ImageUtils.getBitmap(movie.poster));
                            moviePosterImageView.setBackground(null);
                            moviePlot = movie.plot;
                            moviePoster.setOnClickListener(null);
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Found matching movie!", Toast.LENGTH_SHORT).show();
                        } catch(Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Could not find movie...", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        movieYear.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && movieName.getText().toString().length() > 0 && movieYear.getText().toString().length() > 0) {
                    try {
                        Movie movie = dbManager.getMovieByNameAndYear(movieName.getText().toString(), Integer.parseInt(movieYear.getText().toString()));
                        moviePosterImageView.setImageBitmap(ImageUtils.getBitmap(movie.poster));
                        moviePosterImageView.setBackground(null);
                        moviePlot = movie.plot;
                        moviePoster.setOnClickListener(null);
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Found matching movie!", Toast.LENGTH_SHORT).show();
                    } catch(Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Could not find movie...", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
        });

        movieImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isMissingPermissions()) {
                    requestAppPermission(GALLERY_REQUEST_CODE_IMAGE);
                } else {
                    selectImage(GALLERY_REQUEST_CODE_IMAGE);
                }
            }
        });

        moviePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isMissingPermissions()) {
                    requestAppPermission(GALLERY_REQUEST_CODE_POSTER);
                } else {
                    selectImage(GALLERY_REQUEST_CODE_POSTER);
                }
            }
        });

        //Finally building an AlertDialog
        final AlertDialog builder = new AlertDialog.Builder(getActivity())
                .setPositiveButton("Post", null)
                .setNegativeButton("Cancel", null)
                .setView(scrollViewLayout)
                .setCancelable(false)
                .create();
        builder.show();

        //Setting up OnClickListener on positive button of AlertDialog
        builder.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(movieName.getError() == null && movieYear.getError() == null)
                {
                    // If new movie - insert it
                    if(!dbManager.isMovieExist(movieName.getText().toString())) {
                        Movie newMovie = new Movie(movieName.getText().toString(), Integer.parseInt(movieYear.getText().toString().replaceAll("[^0-9]", "")), movieRating.getRating(), moviePlot,  ImageUtils.getBytes(((BitmapDrawable) moviePosterImageView.getDrawable()).getBitmap()), 0);
                        dbManager.insertMovie(newMovie);
                    }

                    final float rating = movieRating.getRating();
                    final String text = movieExperienceText.getText().toString();
                    final byte[] image = ImageUtils.getBytes(((BitmapDrawable) movieImageImageView.getDrawable()).getBitmap());
                    final Movie movie = dbManager.getMovieByNameAndYear(movieName.getText().toString(), Integer.parseInt(movieYear.getText().toString()));
                    final User user = dbManager.getUserByEmail(FirebaseAuthHandler.getInstance().getCurrentUserEmail());

                    // Insert new post
                    Post newPost = new Post(text, movie.id, rating, image, user.id, 0); // ID is 0 because were not setting it, it's used just for retrieval
                    dbManager.insertPost(newPost);

                    // Update movie rating
                    Movie updatedMovie = new Movie(movie.name, movie.year, (movie.rating + movieRating.getRating()) / 2, movie.plot, movie.poster, movie.id);
                    dbManager.updateMovie(movie.id, updatedMovie);
                    adapter.updateMovieRating(updatedMovie);
                    adapter.notifyDataSetChanged();
                    builder.dismiss();
                }
            }
        });
    }

    private void selectImage(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage(requestCode);
        }
    }

    private void requestAppPermission(int requestCode) {
        List<String> missingPermissions = new ArrayList<>();
        if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            missingPermissions.add(Manifest.permission.CAMERA);
        if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            missingPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            missingPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(!missingPermissions.isEmpty()) {
            requestPermissions(missingPermissions.toArray(new String[missingPermissions.size()]), requestCode);
        }
    }

    private boolean isMissingPermissions() {
        return (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                Bitmap res = ImageUtils.handleSamplingAndRotationBitmap(getActivity(), selectedImage);
                if (requestCode == GALLERY_REQUEST_CODE_POSTER) {
                    moviePosterImageView.setImageBitmap(res);
                    moviePosterImageView.setBackground(null);
                } else if(requestCode == GALLERY_REQUEST_CODE_IMAGE) {
                    movieImageImageView.setImageBitmap(res);
                    movieImageImageView.setBackground(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
