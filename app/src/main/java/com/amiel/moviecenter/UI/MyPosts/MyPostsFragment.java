package com.amiel.moviecenter.UI.MyPosts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amiel.moviecenter.R;
import com.amiel.moviecenter.UI.Authentication.FirebaseAuthHandler;
import com.amiel.moviecenter.DB.DatabaseRepository;
import com.amiel.moviecenter.DB.Model.Post;
import com.amiel.moviecenter.DB.Model.User;

import java.util.ArrayList;
import java.util.List;

public class MyPostsFragment extends Fragment {

    MyPostsRecyclerAdapter adapter;

    // Recycler View object
    RecyclerView list;

    RatingBar postRating;
    EditText postText;

    DatabaseRepository db;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        db = new DatabaseRepository(getActivity());
        return inflater.inflate(R.layout.my_posts_fragment, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        postRating = view.findViewById(R.id.my_post_row_item_post_rating);
        postText = view.findViewById(R.id.my_post_row_item_post_text);
        list = view.findViewById(R.id.my_posts_recycler_view);
        list.setHasFixedSize(true);

        // Set adapter to recycler view
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        db.getUserByEmail(FirebaseAuthHandler.getInstance().getCurrentUserEmail()).observe(getViewLifecycleOwner(), user -> db.getAllPostsOfUser(user.getId()).observe(getActivity(), posts -> {
            List<MyPostRowItem> postsRowItems = new ArrayList<>();
            for(Post currPost : posts) {
                db.getMovieById(currPost.getMovieID()).observe(getViewLifecycleOwner(), movie -> {
                    MyPostRowItem postRowItem = new MyPostRowItem(currPost.text, currPost.rating, movie.getName());
                    postsRowItems.add(postRowItem);
                    adapter = new MyPostsRecyclerAdapter(postsRowItems);
                    list.setAdapter(adapter);
                });
            }
        }));

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
