package com.amiel.moviecenter.UI.MyPosts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import java.util.Map;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.amiel.moviecenter.DB.Model.Movie;
import com.amiel.moviecenter.R;
import com.amiel.moviecenter.UI.Authentication.FirebaseAuthHandler;
import com.amiel.moviecenter.DB.Model.Post;
import com.amiel.moviecenter.Utils.ViewModelFactory;
import com.amiel.moviecenter.databinding.MyPostsFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class MyPostsFragment extends Fragment {

    MyPostsRecyclerAdapter adapter;

    MyPostsViewModel myPostsViewModel;
    MyPostsFragmentBinding binding;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        binding = MyPostsFragmentBinding.inflate(inflater, parent, false);
        return binding.getRoot();
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        binding.myPostsRecyclerView.setHasFixedSize(true);
        myPostsViewModel = new ViewModelProvider(this, new ViewModelFactory(requireActivity().getApplication(), FirebaseAuthHandler.getInstance().getCurrentUserEmail())).get(MyPostsViewModel.class);

        // Set adapter to recycler view
        binding.myPostsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        myPostsViewModel.getPosts().observe(getViewLifecycleOwner(), posts -> {
            List<MyPostRowItem> postsRowItems = new ArrayList<>();
            for (Map.Entry<Movie, List<Post>> currEntry : posts.entrySet()) {
                for (Post currPost : currEntry.getValue()) {
                    MyPostRowItem postRowItem = new MyPostRowItem(currPost, currEntry.getKey());
                    postsRowItems.add(postRowItem);
                }
            }

            adapter = new MyPostsRecyclerAdapter(postsRowItems);
            binding.myPostsRecyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener((pos, postText) -> {
                MyPostRowItem postRowItem = adapter.getItemAtPosition(pos);
                Post updatedPost = postRowItem.post;
                updatedPost.setText(postText);
                myPostsViewModel.updatePost(updatedPost);
            });
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
}
