package com.amiel.moviecenter.UI.MyPosts;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;

import java.io.IOException;

import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.amiel.moviecenter.R;
import com.amiel.moviecenter.UI.Authentication.FirebaseAuthHandler;
import com.amiel.moviecenter.DB.Model.Post;
import com.amiel.moviecenter.Utils.FirebaseStorageHandler;
import com.amiel.moviecenter.Utils.ImageUtils;
import com.amiel.moviecenter.Utils.LoadingState;
import com.amiel.moviecenter.Utils.PermissionHelper;
import com.amiel.moviecenter.Utils.ViewModelFactory;
import com.amiel.moviecenter.databinding.MyPostsFragmentBinding;

import java.util.ArrayList;

public class MyPostsFragment extends Fragment {

    MyPostsRecyclerAdapter adapter;

    MyPostsViewModel myPostsViewModel;
    MyPostsFragmentBinding binding;

    ActivityResultLauncher<String[]> permissionResult;
    ActivityResultLauncher<Void> cameraResult;
    ActivityResultLauncher<String> galleryResult;

    MyPostViewHolder viewHolder;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        binding = MyPostsFragmentBinding.inflate(inflater, parent, false);

        permissionResult = PermissionHelper.registerForActivityResult(this, isGranted -> {
            // If permission granted
            if(!isGranted.containsValue(false)) {
                cameraResult.launch(null);
            }
        });

        cameraResult = ImageUtils.registerForCameraActivityResult(this, data -> {
            if(data != null) {
                viewHolder.setPostImage(data);
            }
        });

        galleryResult = ImageUtils.registerForGalleryActivityResult(this, data -> {
            if (data != null) {
                try {
                    Bitmap res = ImageUtils.handleSamplingAndRotationBitmap(requireActivity(), data);
                    viewHolder.setPostImage(res);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        adapter = new MyPostsRecyclerAdapter(new ArrayList<>());
        binding.myPostsRecyclerView.setAdapter(adapter);
        myPostsViewModel = new ViewModelProvider(this, new ViewModelFactory(requireActivity().getApplication(), FirebaseAuthHandler.getInstance().getCurrentUserEmail())).get(MyPostsViewModel.class);

        binding.myPostsSwipeRefreshLayout.setOnRefreshListener(this::updatePosts);

        myPostsViewModel.getPostsLoadingStatus().observe(getViewLifecycleOwner(), status -> {
            binding.myPostsSwipeRefreshLayout.setRefreshing(status == LoadingState.LOADING);
        });

        // Set adapter to recycler view
        binding.myPostsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        myPostsViewModel.getPosts().observe(getViewLifecycleOwner(), posts -> {
            adapter.addAll(posts);

            adapter.setOnItemClickListener((pos, postText, postImageBitmap) -> {
                MyPostRowItem postRowItem = adapter.getItemAtPosition(pos);
                Post updatedPost = postRowItem.post;
                FirebaseStorageHandler.getInstance().uploadPostImage(postImageBitmap, updatedPost.getId(), imageUrl -> {
                    if (imageUrl != null) {
                        updatedPost.setText(postText);
                        updatedPost.setPostImageUrl(imageUrl);
                        myPostsViewModel.updatePost(updatedPost);
                    }
                });
            });

            adapter.setChangeImageListener(viewHolder -> {
                this.viewHolder = viewHolder;
                ImageUtils.selectImage(this, galleryResult, cameraResult, permissionResult);
            });
        });

        return binding.getRoot();
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        binding.myPostsRecyclerView.setHasFixedSize(true);

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
            public void onPrepareMenu(@NonNull Menu menu) {
            }
        });
    }

    public void updatePosts() {
        myPostsViewModel.getPosts().observe(getViewLifecycleOwner(), psList -> {
            adapter.clear();
            adapter.addAll(psList);
        });
    }
}
