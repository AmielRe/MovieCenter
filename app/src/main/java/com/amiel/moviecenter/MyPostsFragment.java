package com.amiel.moviecenter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.amiel.moviecenter.DB.DBManager;
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

    DBManager dbManager;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        dbManager = new DBManager(getActivity());
        dbManager.open();
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.my_posts_fragment, parent, false);
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
        postRating = view.findViewById(R.id.my_post_row_item_post_rating);
        postText = view.findViewById(R.id.my_post_row_item_post_text);
        list = view.findViewById(R.id.my_posts_recycler_view);
        list.setHasFixedSize(true);

        // Set adapter to recycler view
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<Post> userPosts = dbManager.getAllPostsOfUser(FirebaseAuthHandler.getInstance().getCurrentUserEmail());
        List<MyPostRowItem> postsRowItems = new ArrayList<>();
        for(Post currPost : userPosts) {
            MyPostRowItem postRowItem = new MyPostRowItem(currPost.text, currPost.rating, dbManager.getMovieById(currPost.movieID).name);
            postsRowItems.add(postRowItem);
        }

        adapter = new MyPostsRecyclerAdapter(postsRowItems);
        list.setAdapter(adapter);
        
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                // Open edit dialog
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}
