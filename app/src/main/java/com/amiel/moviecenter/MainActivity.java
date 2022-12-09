package com.amiel.moviecenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentUtils.loadFragment(null, MainActivity.this, new MoviesListFragment(), R.id.activity_main_frame_layout, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu with items using MenuInflator
        getMenuInflater().inflate(R.menu.menu, menu);

        if (FirebaseAuthHandler.getInstance().isUserLoggedIn()) {
            menu.findItem(R.id.menu_login).setVisible(false);
        } else {
            menu.findItem(R.id.menu_profile).setVisible(false);
            menu.findItem(R.id.menu_my_posts).setVisible(false);
            menu.findItem(R.id.menu_logout).setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(@NonNull Menu menu) {
        if (FirebaseAuthHandler.getInstance().isUserLoggedIn()) {
            menu.findItem(R.id.menu_login).setVisible(false);
        } else {
            menu.findItem(R.id.menu_profile).setVisible(false);
            menu.findItem(R.id.menu_my_posts).setVisible(false);
            menu.findItem(R.id.menu_logout).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.menu_login:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;

            case R.id.menu_profile:
                FragmentUtils.loadFragment(null, this, new ProfileFragment(), R.id.activity_main_frame_layout, null);
                break;

            case R.id.menu_my_posts:
                // Here will come my_posts fragment
                break;

            case R.id.menu_logout:
                FirebaseAuthHandler.getInstance().logoutCurrentUser();
                invalidateOptionsMenu();

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}