package com.amiel.moviecenter.UI.MyPosts;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.amiel.moviecenter.Utils.PermissionUtils;
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
        myPostsViewModel = new ViewModelProvider(this, new ViewModelFactory(requireActivity().getApplication(), FirebaseAuthHandler.getInstance().getCurrentUserEmail())).get(MyPostsViewModel.class);

        permissionResult = PermissionUtils.registerForActivityResult(this, isGranted -> {
            // If permission granted
            if(!isGranted.containsValue(false)) {
                cameraResult.launch(null);
            }
        });

        cameraResult = ImageUtils.registerForCameraActivityResult(this, data -> {
            if(data != null) {
                Post updatedPost = adapter.getItemAtPosition(binding.myPostsRecyclerView.getChildAdapterPosition(viewHolder.itemView)).post;
                viewHolder.setPostImage(data);
                FirebaseStorageHandler.getInstance().uploadPostImage(data, updatedPost.getId(), imageUrl -> {
                    if (imageUrl != null) {
                        updatedPost.setPostImageUrl(imageUrl);
                        myPostsViewModel.updatePost(updatedPost);
                    }
                });
            }
        });

        galleryResult = ImageUtils.registerForGalleryActivityResult(this, data -> {
            if (data != null) {
                try {
                    Post updatedPost = adapter.getItemAtPosition(binding.myPostsRecyclerView.getChildAdapterPosition(viewHolder.itemView)).post;
                    Bitmap res = ImageUtils.handleSamplingAndRotationBitmap(requireActivity(), data);
                    viewHolder.setPostImage(res);
                    FirebaseStorageHandler.getInstance().uploadPostImage(res, updatedPost.getId(), imageUrl -> {
                        if (imageUrl != null) {
                            updatedPost.setPostImageUrl(imageUrl);
                            myPostsViewModel.updatePost(updatedPost);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return binding.getRoot();
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        binding.myPostsRecyclerView.setHasFixedSize(true);
        binding.myPostsSwipeRefreshLayout.setOnRefreshListener(this::updatePosts);
        adapter = new MyPostsRecyclerAdapter(new ArrayList<>());
        binding.myPostsRecyclerView.setAdapter(adapter);

        myPostsViewModel.getPostsLoadingStatus().observe(getViewLifecycleOwner(), status -> binding.myPostsSwipeRefreshLayout.setRefreshing(status == LoadingState.LOADING));

        // Set adapter to recycler view
        binding.myPostsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        updatePosts();

        adapter.setOnItemClickListener((pos, postText, postImageBitmap) -> {
            MyPostRowItem postRowItem = adapter.getItemAtPosition(pos);
            Post updatedPost = postRowItem.post;
            updatedPost.setText(postText);
            myPostsViewModel.updatePost(updatedPost);
        });

        adapter.setChangeImageListener(viewHolder -> {
            this.viewHolder = viewHolder;
            ImageUtils.selectImage(this, galleryResult, cameraResult, permissionResult);
        });

        adapter.setRemovePostListener(pos -> {
            AlertDialog alertDialog = new AlertDialog.Builder(requireActivity()).create();
            alertDialog.setTitle(getString(R.string.warning_title));
            alertDialog.setMessage(getString(R.string.warning_message));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes),
                    (dialog, which) -> {
                        MyPostRowItem postRowItem = adapter.getItemAtPosition(pos);
                        Post updatedPost = postRowItem.post;
                        updatedPost.setDeleted(true);
                        myPostsViewModel.updatePost(updatedPost);
                        adapter.removeItemAtPos(pos);
                        dialog.dismiss();
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no),
                    (DialogInterface.OnClickListener) (dialog, which) -> dialog.dismiss());
            alertDialog.show();
        });

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
