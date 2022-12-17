package com.amiel.moviecenter;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.amiel.moviecenter.Authentication.FirebaseAuthHandler;

public class MainActivity extends AppCompatActivity {

    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.main_navhost);
        navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this,navController);

        addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu, menu);

                if (FirebaseAuthHandler.getInstance().isUserLoggedIn()) {
                    menu.findItem(R.id.loginOptionsFragment).setVisible(false);
                } else {
                    menu.findItem(R.id.profileFragment).setVisible(false);
                    menu.findItem(R.id.myPostsFragment).setVisible(false);
                    menu.findItem(R.id.menu_logout).setVisible(false);
                }
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

                if(menuItem.getItemId() == R.id.menu_logout) {
                    FirebaseAuthHandler.getInstance().logoutCurrentUser();
                    invalidateOptionsMenu();
                } else if(menuItem.getItemId() == android.R.id.home) {
                    return navController.navigateUp();
                } else {
                    return NavigationUI.onNavDestinationSelected(menuItem, navController);
                }
                return false;
            }

            @Override
            public void onPrepareMenu(@NonNull Menu menu) {
                if (FirebaseAuthHandler.getInstance().isUserLoggedIn()) {
                    menu.findItem(R.id.loginOptionsFragment).setVisible(false);
                } else {
                    menu.findItem(R.id.profileFragment).setVisible(false);
                    menu.findItem(R.id.myPostsFragment).setVisible(false);
                    menu.findItem(R.id.menu_logout).setVisible(false);
                }

                MenuProvider.super.onPrepareMenu(menu);
            }
        });
    }
}