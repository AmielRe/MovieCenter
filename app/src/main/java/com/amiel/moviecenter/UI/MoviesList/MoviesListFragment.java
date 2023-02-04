package com.amiel.moviecenter.UI.MoviesList;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.amiel.moviecenter.DB.Model.User;
import com.amiel.moviecenter.R;
import com.amiel.moviecenter.UI.Authentication.FirebaseAuthHandler;
import com.amiel.moviecenter.DB.Model.Movie;
import com.amiel.moviecenter.DB.Model.Post;
import com.amiel.moviecenter.Utils.DialogUtils;
import com.amiel.moviecenter.Utils.FirebaseStorageHandler;
import com.amiel.moviecenter.Utils.ImageUtils;
import com.amiel.moviecenter.Utils.LoadingState;
import com.amiel.moviecenter.Utils.MovieRemoteApi.MovieModel;
import com.amiel.moviecenter.Utils.PermissionUtils;
import com.amiel.moviecenter.Utils.TextValidator;
import com.amiel.moviecenter.databinding.MovieListFragmentBinding;
import com.amiel.moviecenter.databinding.NewPostLayoutBinding;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Calendar;
import java.util.function.Supplier;
import java.util.stream.Stream;

import androidx.appcompat.widget.SearchView;

public class MoviesListFragment extends Fragment {

    MoviesListViewModel moviesListViewModel;
    MoviesListRecyclerAdapter adapter;
    MovieListFragmentBinding binding;

    AlertDialog progressDialog;
    ActivityResultLauncher<String[]> moviePosterResult;
    ActivityResultLauncher<String[]> movieImageResult;
    ActivityResultLauncher<String> galleryResultLauncherMovieImage;
    ActivityResultLauncher<String> galleryResultLauncherMoviePoster;
    NewPostLayoutBinding newPostBinding;

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

        movieImageResult = PermissionUtils.registerForActivityResult(this, isGranted -> {
            // If permission granted
            if(!isGranted.containsValue(false)) {
                galleryResultLauncherMovieImage.launch("image/*");
            }
        });

        moviePosterResult = PermissionUtils.registerForActivityResult(this, isGranted -> {
            // If permission granted
            if(!isGranted.containsValue(false)) {
                galleryResultLauncherMoviePoster.launch("image/*");
            }
        });

        galleryResultLauncherMovieImage = ImageUtils.registerForGalleryActivityResult(this, data -> {
            if (data != null) {
                try {
                    Bitmap res = ImageUtils.handleSamplingAndRotationBitmap(requireActivity(), data);
                    newPostBinding.newPostMovieImageImageView.setImageBitmap(res);
                    newPostBinding.newPostMovieImageImageView.setBackground(null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        galleryResultLauncherMoviePoster = ImageUtils.registerForGalleryActivityResult(this, data -> {
            if (data != null) {
                try {
                    Bitmap res = ImageUtils.handleSamplingAndRotationBitmap(requireActivity(), data);
                    newPostBinding.newPostMoviePosterImageView.setImageBitmap(res);
                    newPostBinding.newPostMoviePosterImageView.setBackground(null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
        moviesListViewModel.getMovies().observe(getViewLifecycleOwner(), data -> {
            if(isAdded() && getActivity() != null) {
                binding.mainRecyclerListMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
            adapter = new MoviesListRecyclerAdapter(data);
            binding.mainRecyclerListMovies.setAdapter(adapter);

            binding.mainRecyclerListMovies.addItemDecoration(new DividerItemDecoration(binding.mainRecyclerListMovies.getContext(), DividerItemDecoration.VERTICAL));

            adapter.setOnItemClickListener(pos -> {
                Movie clickedMovie = adapter.getItemAtPosition(pos);
                MoviesListFragmentDirections.ActionMoviesListFragmentToMovieDetailsFragment action = MoviesListFragmentDirections.actionMoviesListFragmentToMovieDetailsFragment(clickedMovie.getId(), clickedMovie.getName(), clickedMovie.getYear(), clickedMovie.getRating(), clickedMovie.getPlot(), ImageUtils.getBitmap(clickedMovie.getPoster()), clickedMovie.getPosterUrl());
                Navigation.findNavController(view).navigate(action);
            });
        });

        binding.movieListSwipeRefreshLayout.setOnRefreshListener(this::updateMovies);

        moviesListViewModel.getMoviesLoadingStatus().observe(getViewLifecycleOwner(), status -> binding.movieListSwipeRefreshLayout.setRefreshing(status == LoadingState.LOADING));

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

                LiveData<Movie> movieLiveData = moviesListViewModel.getMovieByName(newPostBinding.newPostMovieNameEditText.getText().toString());
                movieLiveData.observe(getViewLifecycleOwner(), movie -> {
                    movieLiveData.removeObservers(getViewLifecycleOwner());
                    if(movie != null) {
                        setNewMovieDetails(movie);
                        moviesListViewModel.setNewMoviePlot(movie.getPlot());
                        progressDialog.dismiss();
                    } else {
                        MovieModel.getInstance().GetMovieByTitle(newPostBinding.newPostMovieNameEditText.getText().toString()).observe(getViewLifecycleOwner(), movieFromApi -> {
                            if(movieFromApi == null) {
                                Toast.makeText(requireActivity(), "Error fetching movie", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                return;
                            }

                            moviesListViewModel.setNewMoviePlot(movieFromApi.getPlot());
                            setNewMovieDetails(movieFromApi);
                            progressDialog.dismiss();
                        });
                    }
                });
            }
        });

        newPostBinding.newPostMovieYearEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus && newPostBinding.newPostMovieNameEditText.getText().toString().length() > 0 && newPostBinding.newPostMovieYearEditText.getText().toString().length() > 0) {
                progressDialog.show();
                LiveData<Movie> movieLiveData = moviesListViewModel.getMovieByNameAndYear(newPostBinding.newPostMovieNameEditText.getText().toString(), Integer.parseInt(newPostBinding.newPostMovieYearEditText.getText().toString()));
                movieLiveData.observe(getViewLifecycleOwner(), movie -> {
                    movieLiveData.removeObservers(getViewLifecycleOwner());
                    if(movie != null) {
                        setNewMovieDetails(movie);
                        moviesListViewModel.setNewMoviePlot(movie.getPlot());
                    }
                    progressDialog.dismiss();
                });
            }
        });

        newPostBinding.newPostMovieImageUploadImage.setOnClickListener(v -> galleryResultLauncherMovieImage.launch("image/*"));

        newPostBinding.newPostMoviePosterUploadImage.setOnClickListener(v -> galleryResultLauncherMoviePoster.launch("image/*"));

        // Finally building an AlertDialog
        final AlertDialog builder = new AlertDialog.Builder(requireActivity())
                .setPositiveButton("Post", null)
                .setNegativeButton("Cancel", null)
                .setView(newPostBinding.newPostLayout)
                .setCancelable(false)
                .create();
        builder.show();

        builder.setOnDismissListener(dialog -> updateMovies());

        // Setting up OnClickListener on positive button of AlertDialog
        builder.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(view -> {
            updateMovies();

            if(!validateNewPostInput()) {
                return;
            }

            progressDialog.show();

            final String postText = newPostBinding.newPostHowWasYourExperienceEditText.getText().toString();
            final Bitmap postImageBitmap = ((BitmapDrawable) newPostBinding.newPostMovieImageImageView.getDrawable()).getBitmap();

            // If new movie - insert it

            Supplier<Stream<Movie>> streamSupplier = () -> adapter.originalData.stream();
            Stream<Movie> matchingMovies = streamSupplier.get().filter(currMovie -> currMovie.getName().equals(newPostBinding.newPostMovieNameEditText.getText().toString()) && currMovie.getYear() == Integer.parseInt(newPostBinding.newPostMovieYearEditText.getText().toString()));
            if(!matchingMovies.findAny().isPresent()) {
                final Bitmap movieImageBitmap = ((BitmapDrawable) newPostBinding.newPostMoviePosterImageView.getDrawable()).getBitmap();

                Movie newMovie = new Movie(newPostBinding.newPostMovieNameEditText.getText().toString(), Integer.parseInt(newPostBinding.newPostMovieYearEditText.getText().toString().replaceAll("\\D", "")), newPostBinding.newPostMovieRating.getRating(), moviesListViewModel.getNewMoviePlot(),  ImageUtils.getBytes(movieImageBitmap), "", "");
                moviesListViewModel.insertMovie(newMovie, (Void) -> {
                    FirebaseStorageHandler.getInstance().uploadMovieImage(movieImageBitmap, newMovie.getId(), imageUrl -> {
                        if(imageUrl != null) {
                            newMovie.setPosterUrl(imageUrl);
                            moviesListViewModel.updateMovie(newMovie);
                            updateMovies();
                        }
                    });

                    // Insert new post
                    Post newPost = new Post(postText, newMovie.getId(), newMovie.getRating(), ImageUtils.getBytes(postImageBitmap), FirebaseAuthHandler.getInstance().getCurrentUserId(), "", Calendar.getInstance().getTime(), "", false); // ID is nothing because it will be set later
                    insertNewPost(newPost);

                    builder.dismiss();
                    progressDialog.dismiss();
                });
            } else {
                Movie movie = streamSupplier.get().filter(currMovie -> currMovie.getName().equals(newPostBinding.newPostMovieNameEditText.getText().toString()) && currMovie.getYear() == Integer.parseInt(newPostBinding.newPostMovieYearEditText.getText().toString())).findFirst().get();
                final float rating = newPostBinding.newPostMovieRating.getRating();

                // Insert new post
                Post newPost = new Post(postText, movie.getId(), rating, ImageUtils.getBytes(postImageBitmap), FirebaseAuthHandler.getInstance().getCurrentUserId(), "", Calendar.getInstance().getTime(), "", false); // ID is nothing because it will be set later
                insertNewPost(newPost);

                // Update movie rating
                movie.setRating((movie.getRating() + newPostBinding.newPostMovieRating.getRating()) / 2);
                moviesListViewModel.updateMovie(movie);
                adapter.updateMovieRating(movie);

                builder.dismiss();
                progressDialog.dismiss();
            }
        });
    }

    public void updateMovies() {
        moviesListViewModel.getMovies().observe(getViewLifecycleOwner(), mvList -> {
            adapter.clear();
            adapter.addAll(mvList);
        });
    }

    public boolean validateNewPostInput() {
        boolean isValid = true;

        String movieName = newPostBinding.newPostMovieNameEditText.getText().toString();
        String movieYear = newPostBinding.newPostMovieYearEditText.getText().toString();

        if(movieName.isEmpty()) {
            newPostBinding.newPostMovieNameInputLayout.setError("Please enter movie name");
            isValid = false;
            newPostBinding.newPostMovieNameInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
        } else {
            newPostBinding.newPostMovieNameInputLayout.setError(null);
        }

        if(movieYear.isEmpty()) {
            newPostBinding.newPostMovieYearEditText.setError("Please enter movie year");
            isValid = false;
            newPostBinding.newPostMovieYearInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
        } else {
            newPostBinding.newPostMovieYearInputLayout.setError(null);
        }

        if(newPostBinding.newPostMoviePosterImageView.getDrawable() == null || newPostBinding.newPostMovieImageImageView.getDrawable() == null) {
            Toast.makeText(requireActivity(), "Please upload movie poster and image", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }

    private void setNewMovieDetails(Movie movie) {
        newPostBinding.newPostMovieNameEditText.setText(movie.getName());
        newPostBinding.newPostMovieYearEditText.setText(String.valueOf(movie.getYear()));
        newPostBinding.newPostMovieYearEditText.setEnabled(false);
        byte[] moviePoster = movie.getPoster();
        if(moviePoster != null) {
            newPostBinding.newPostMoviePosterImageView.setImageBitmap(ImageUtils.getBitmap(moviePoster));
        } else if(movie.getPosterUrl() != null && !movie.getPosterUrl().isEmpty()) {
            Picasso.get().load(movie.getPosterUrl()).placeholder(R.drawable.default_post_placeholder).into(newPostBinding.newPostMoviePosterImageView);
        } else {
            newPostBinding.newPostMoviePosterImageView.setImageResource(R.drawable.default_post_placeholder);
        }
        newPostBinding.newPostMoviePosterImageView.setBackground(null);
        newPostBinding.newPostMoviePosterUploadImage.setOnClickListener(null);
    }

    private void insertNewPost(Post newPost) {
        moviesListViewModel.insertPost(newPost, data -> {
            FirebaseStorageHandler.getInstance().uploadPostImage(ImageUtils.getBitmap(newPost.image), newPost.getId(), imageUrl -> {
                if (imageUrl != null) {
                    newPost.setPostImageUrl(imageUrl);
                    moviesListViewModel.updatePost(newPost);
                }
            });
        });
    }
}
