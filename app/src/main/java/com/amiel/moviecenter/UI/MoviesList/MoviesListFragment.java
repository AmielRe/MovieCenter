package com.amiel.moviecenter.UI.MoviesList;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.amiel.moviecenter.R;
import com.amiel.moviecenter.UI.Authentication.FirebaseAuthHandler;
import com.amiel.moviecenter.DB.Model.Movie;
import com.amiel.moviecenter.DB.Model.Post;
import com.amiel.moviecenter.Utils.AsyncTasks.GetMovieDataTask;
import com.amiel.moviecenter.Utils.DialogUtils;
import com.amiel.moviecenter.Utils.FirebaseStorageHandler;
import com.amiel.moviecenter.Utils.ImageUtils;
import com.amiel.moviecenter.Utils.PermissionHelper;
import com.amiel.moviecenter.Utils.TextValidator;
import com.amiel.moviecenter.databinding.MovieListFragmentBinding;
import com.amiel.moviecenter.databinding.NewPostLayoutBinding;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.widget.SearchView;

public class MoviesListFragment extends Fragment {

    MoviesListViewModel moviesListViewModel;
    MoviesListRecyclerAdapter adapter;
    MovieListFragmentBinding binding;

    AlertDialog progressDialog;
    ActivityResultLauncher<String[]> moviePosterResult;
    ActivityResultLauncher<String[]> movieImageResult;
    NewPostLayoutBinding newPostBinding;

    private static final String BASE_IMDB_MOVIE_URL = "https://imdb-api.com/API/AdvancedSearch/k_4wqqdznf?title=%s&title_type=feature,tv_movie&has=plot&release_date=,%s";

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Disable and hide back button from movies list fragment
        ActionBar actionBar = ((AppCompatActivity)requireActivity()).getSupportActionBar();
        if( actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.show();
        }

        binding = MovieListFragmentBinding.inflate(inflater, parent, false);

        movieImageResult = new PermissionHelper().registerForActivityResult(this, isGranted -> {
            // If permission granted
            if(!isGranted.containsValue(false)) {
                galleryResultLauncherMovieImage.launch("image/*");
            }
        });

        moviePosterResult = new PermissionHelper().registerForActivityResult(this, isGranted -> {
            // If permission granted
            if(!isGranted.containsValue(false)) {
                galleryResultLauncherMoviePoster.launch("image/*");
            }
        });

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.menu, menu);

                // Initialise menu item search bar
                // with id and take its object
                MenuItem search = menu.findItem(R.id.search_bar);
                SearchView searchView = (SearchView) search.getActionView();

                SearchManager searchManager = (SearchManager) requireActivity().getSystemService(Context.SEARCH_SERVICE);
                searchView.setSearchableInfo( searchManager.getSearchableInfo(requireActivity().getComponentName()));
                searchView.setQueryHint(getResources().getString(R.string.search_movie_hint));

                // attach setOnQueryTextListener
                // to search view defined above
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    // Override onQueryTextSubmit method which is call when submit query is searched
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        // If the list contains the search query than filter the adapter
                        // using the filter method with the query as its argument
                        if (adapter.filteredData.stream().anyMatch(curr -> curr.getName().toLowerCase().contains(query.toLowerCase()))) {
                            adapter.getFilter().filter(query);
                        } else {
                            // Search query not found in List View
                            Toast.makeText(requireActivity(), "Not found", Toast.LENGTH_LONG).show();
                        }
                        return false;
                    }

                    // This method is overridden to filter the adapter according
                    // to a search query when the user is typing search
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if(adapter != null) {
                            adapter.getFilter().filter(newText);
                        }
                        return false;
                    }
                });
            }

            @Override
            public void onPrepareMenu(@NonNull Menu menu) {
                binding.movieListFab.setVisibility(!FirebaseAuthHandler.getInstance().isUserLoggedIn() ? View.GONE : View.VISIBLE);

                MenuProvider.super.onPrepareMenu(menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

                // Handle option Menu Here
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        moviesListViewModel = new ViewModelProvider(this).get(MoviesListViewModel.class);
        progressDialog = DialogUtils.setProgressDialog(getContext(), "Loading...");
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        binding.mainRecyclerListMovies.setHasFixedSize(true);

        if(!FirebaseAuthHandler.getInstance().isUserLoggedIn()) {
            binding.movieListFab.setVisibility(View.INVISIBLE);
        } else {
            binding.movieListFab.setVisibility(View.VISIBLE);
        }

        // Set adapter to recycler view
        moviesListViewModel.getMovies(data -> {
            if(isAdded() && getActivity() != null) {
                binding.mainRecyclerListMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
            adapter = new MoviesListRecyclerAdapter(data);
            binding.mainRecyclerListMovies.setAdapter(adapter);

            binding.mainRecyclerListMovies.addItemDecoration(new DividerItemDecoration(binding.mainRecyclerListMovies.getContext(), DividerItemDecoration.VERTICAL));

            adapter.setOnItemClickListener(pos -> {
                Movie clickedMovie = adapter.getItemAtPosition(pos);
                MoviesListFragmentDirections.ActionMoviesListFragmentToMovieDetailsFragment action = MoviesListFragmentDirections.actionMoviesListFragmentToMovieDetailsFragment(clickedMovie.getId(), clickedMovie.getName(), clickedMovie.getYear(), clickedMovie.getRating(), clickedMovie.getPlot(), clickedMovie.getPosterUrl());
                Navigation.findNavController(view).navigate(action);
            });
            binding.progressBar.setVisibility(View.GONE);
        });

        binding.movieListSwipeRefreshLayout.setOnRefreshListener(() -> {
            updateMovies();
            binding.movieListSwipeRefreshLayout.setRefreshing(false);
        });

        binding.movieListFab.setOnClickListener(v -> openNewPostDialog());
    }

    private void openNewPostDialog()
    {
        newPostBinding = NewPostLayoutBinding.inflate(getLayoutInflater());

        newPostBinding.newPostMovieNameEditText.setError(getString(R.string.error_invalid_movie_name));
        newPostBinding.newPostMovieYearEditText.setError(getString(R.string.error_invalid_movie_year));

        newPostBinding.newPostMovieNameEditText.addTextChangedListener(new TextValidator(newPostBinding.newPostMovieNameEditText) {
            @Override public void validate(TextView textView, String text) {
                if(text.length() <= 0) {
                    newPostBinding.newPostMovieNameInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                    newPostBinding.newPostMovieNameEditText.setError(getString(R.string.error_invalid_movie_name));
                } else {
                    newPostBinding.newPostMovieNameEditText.setError(null);
                    newPostBinding.newPostMovieNameInputLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                    newPostBinding.newPostMovieNameInputLayout.setEndIconDrawable(R.drawable.ic_check_circle_black_24dp);
                }
            }
        });

        newPostBinding.newPostMovieYearEditText.addTextChangedListener(new TextValidator(newPostBinding.newPostMovieYearEditText) {
            @Override public void validate(TextView textView, String text) {
                if (text.length() <= 0) {
                    newPostBinding.newPostMovieYearInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                    newPostBinding.newPostMovieYearEditText.setError(getString(R.string.error_invalid_movie_year));
                } else {
                    newPostBinding.newPostMovieYearEditText.setError(null);
                    newPostBinding.newPostMovieYearInputLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                    newPostBinding.newPostMovieYearInputLayout.setEndIconDrawable(R.drawable.ic_check_circle_black_24dp);
                }
            }
        });

        newPostBinding.newPostMovieNameEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if ( !hasFocus && newPostBinding.newPostMovieNameEditText.getText().toString().length() > 0 ) {

                progressDialog.show();

                moviesListViewModel.getMovieByName(newPostBinding.newPostMovieNameEditText.getText().toString()).observe(getViewLifecycleOwner(), movie -> {
                    if (movie != null && newPostBinding.newPostMovieYearEditText.getText().toString().length() > 0) {
                        try {
                            moviesListViewModel.getMovieByNameAndYear(newPostBinding.newPostMovieNameEditText.getText().toString(), Integer.parseInt(newPostBinding.newPostMovieYearEditText.getText().toString())).observe(getViewLifecycleOwner(), movieByNameAndYear -> {
                                Bitmap movieBitmap = ImageUtils.getBitmap(movieByNameAndYear.getPoster());
                                if (movieBitmap != null) {
                                    newPostBinding.newPostMoviePosterImageView.setImageBitmap(movieBitmap);
                                    newPostBinding.newPostMoviePosterImageView.setBackground(null);
                                }
                                moviesListViewModel.setNewMoviePlot(movieByNameAndYear.getPlot());
                                newPostBinding.newPostMoviePosterUploadImage.setOnClickListener(null);
                                progressDialog.dismiss();
                                Toast.makeText(requireActivity(), "Found matching movie!", Toast.LENGTH_SHORT).show();
                            });
                        } catch (Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(requireActivity(), "Could not find movie...", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String currentDate = df.format(Calendar.getInstance().getTime());
                            new GetMovieDataTask(getActivity(), newPostBinding.newPostMoviePosterImageView, newPostBinding.newPostMovieNameEditText, newPostBinding.newPostMovieYearEditText, moviesListViewModel, progressDialog).execute(String.format(BASE_IMDB_MOVIE_URL, newPostBinding.newPostMovieNameEditText.getText().toString(), currentDate));
                        } catch (Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(requireActivity(), "Could not find movie...", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        newPostBinding.newPostMovieYearEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus && newPostBinding.newPostMovieNameEditText.getText().toString().length() > 0 && newPostBinding.newPostMovieYearEditText.getText().toString().length() > 0) {
                progressDialog.show();
                try {
                    moviesListViewModel.getMovieByNameAndYear(newPostBinding.newPostMovieNameEditText.getText().toString(), Integer.parseInt(newPostBinding.newPostMovieYearEditText.getText().toString())).observe(getViewLifecycleOwner(), movie -> {
                        if(movie != null) {
                            Bitmap movieBitmap = ImageUtils.getBitmap(movie.getPoster());
                            if(movieBitmap != null) {
                                newPostBinding.newPostMoviePosterImageView.setImageBitmap(movieBitmap);
                                newPostBinding.newPostMoviePosterImageView.setBackground(null);
                            }
                            newPostBinding.newPostMoviePosterImageView.setBackground(null);
                            moviesListViewModel.setNewMoviePlot(movie.getPlot());
                            newPostBinding.newPostMoviePosterUploadImage.setOnClickListener(null);
                            progressDialog.dismiss();
                            Toast.makeText(requireActivity(), "Found matching movie!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch(Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(requireActivity(), "Could not find movie...", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        newPostBinding.newPostMovieImageUploadImage.setOnClickListener(v -> {
            if(PermissionHelper.isMissingPermissions(requireActivity(), Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET)) {
                new PermissionHelper().startPermissionRequest(movieImageResult, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET);
            } else {
                galleryResultLauncherMovieImage.launch("image/*");
            }
        });

        newPostBinding.newPostMoviePosterUploadImage.setOnClickListener(v -> {
            if(PermissionHelper.isMissingPermissions(requireActivity(), Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET)) {
                new PermissionHelper().startPermissionRequest(moviePosterResult, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET);
            } else {
                galleryResultLauncherMoviePoster.launch("image/*");
            }
        });

        // Finally building an AlertDialog
        final AlertDialog builder = new AlertDialog.Builder(requireActivity())
                .setPositiveButton("Post", null)
                .setNegativeButton("Cancel", null)
                .setView(newPostBinding.newPostLayout)
                .setCancelable(false)
                .create();
        builder.show();

        builder.setOnDismissListener(dialog -> updateMovies());

        //Setting up OnClickListener on positive button of AlertDialog
        builder.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(view -> {
            if(newPostBinding.newPostMovieNameEditText.getError() == null && newPostBinding.newPostMovieYearEditText.getError() == null)
            {
                progressDialog.show();

                // If new movie - insert it
                boolean isExist = adapter.originalData.stream().anyMatch(currMovie -> currMovie.getName().equals(newPostBinding.newPostMovieNameEditText.getText().toString()) && currMovie.getYear() == Integer.parseInt(newPostBinding.newPostMovieYearEditText.getText().toString()));
                if(!isExist) {
                    final Bitmap movieImageBitmap = ((BitmapDrawable) newPostBinding.newPostMoviePosterImageView.getDrawable()).getBitmap();
                    Movie newMovie = new Movie(newPostBinding.newPostMovieNameEditText.getText().toString(), Integer.parseInt(newPostBinding.newPostMovieYearEditText.getText().toString().replaceAll("[^0-9]", "")), newPostBinding.newPostMovieRating.getRating(), moviesListViewModel.getNewMoviePlot(),  ImageUtils.getBytes(movieImageBitmap), "", "");
                    moviesListViewModel.insertMovie(newMovie).observe(getViewLifecycleOwner(), ids -> {
                        FirebaseStorageHandler.getInstance().uploadMovieImage(movieImageBitmap, String.valueOf(ids[0]), data -> {
                            if(data != null) {
                                newMovie.setPosterUrl(data);
                                moviesListViewModel.updateMovie(newMovie);
                                updateMovies();
                            }
                        });

                        final String text = newPostBinding.newPostHowWasYourExperienceEditText.getText().toString();
                        final Bitmap postImageBitmap = ((BitmapDrawable) newPostBinding.newPostMovieImageImageView.getDrawable()).getBitmap();

                        // Insert new post
                        Post newPost = new Post(text, newMovie.getId(), newMovie.getRating(), ImageUtils.getBytes(postImageBitmap), FirebaseAuthHandler.getInstance().getCurrentUserId(), "", Calendar.getInstance().getTime(), ""); // ID is 0 because were not setting it, it's used just for retrieval
                        moviesListViewModel.insertPost(newPost).observe(getViewLifecycleOwner(), postIds -> FirebaseStorageHandler.getInstance().uploadPostImage(postImageBitmap, String.valueOf(postIds[0]), data -> {
                            if (data != null) {
                                newPost.setPostImageUrl(data);
                                moviesListViewModel.updatePost(newPost);
                            }
                        }));

                        builder.dismiss();
                        progressDialog.dismiss();
                    });
                } else {
                    moviesListViewModel.getMovieByNameAndYear(newPostBinding.newPostMovieNameEditText.getText().toString(), Integer.parseInt(newPostBinding.newPostMovieYearEditText.getText().toString())).observe(getViewLifecycleOwner(), movie -> {
                        final float rating = movie.getRating();
                        final String text = newPostBinding.newPostHowWasYourExperienceEditText.getText().toString();
                        final Bitmap imageBitmap = ((BitmapDrawable) newPostBinding.newPostMovieImageImageView.getDrawable()).getBitmap();

                        // Insert new post
                        Post newPost = new Post(text, movie.getId(), rating, ImageUtils.getBytes(imageBitmap), FirebaseAuthHandler.getInstance().getCurrentUserId(), "", Calendar.getInstance().getTime(), ""); // ID is 0 because were not setting it, it's used just for retrieval
                        moviesListViewModel.insertPost(newPost).observe(getViewLifecycleOwner(), postIds -> FirebaseStorageHandler.getInstance().uploadPostImage(imageBitmap, String.valueOf(postIds[0]), data -> {
                            if (data != null) {
                                newPost.setPostImageUrl(data);
                                moviesListViewModel.updatePost(newPost);
                            }
                        }));

                        // Update movie rating
                        movie.setRating((movie.getRating() + newPostBinding.newPostMovieRating.getRating()) / 2);
                        moviesListViewModel.updateMovie(movie);
                        adapter.updateMovieRating(movie);

                        builder.dismiss();
                        progressDialog.dismiss();
                    });
                }
            }
        });
    }

    private final ActivityResultLauncher<String> galleryResultLauncherMovieImage = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            result -> {
                if (result != null) {
                    try {
                        Bitmap res = ImageUtils.handleSamplingAndRotationBitmap(requireActivity(), result);
                        newPostBinding.newPostMovieImageImageView.setImageBitmap(res);
                        newPostBinding.newPostMovieImageImageView.setBackground(null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

    private final ActivityResultLauncher<String> galleryResultLauncherMoviePoster = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            result -> {
                if (result != null) {
                    try {
                        Bitmap res = ImageUtils.handleSamplingAndRotationBitmap(requireActivity(), result);
                        newPostBinding.newPostMoviePosterImageView.setImageBitmap(res);
                        newPostBinding.newPostMoviePosterImageView.setBackground(null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

    public void updateMovies() {
        binding.progressBar.setVisibility(View.VISIBLE);
        moviesListViewModel.getMovies((mvList)->{
            moviesListViewModel.setMovies(mvList);
            adapter.clear();
            adapter.addAll(mvList);
            binding.progressBar.setVisibility(View.GONE);
        });
    }
}
