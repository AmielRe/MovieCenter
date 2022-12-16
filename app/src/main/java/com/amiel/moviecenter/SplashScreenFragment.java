package com.amiel.moviecenter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.amiel.moviecenter.DB.DBManager;

public class SplashScreenFragment extends Fragment {

    private DBManager dbManager;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        dbManager = new DBManager(getActivity());
        dbManager.open();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        return inflater.inflate(R.layout.fragment_splash, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        dbManager = new DBManager(getActivity());
        dbManager.open();
        NavController navController = Navigation.findNavController(getActivity(), view.getId());

        int i;
        NavDirections directions;
        if(FirebaseAuthHandler.getInstance().isUserLoggedIn()) {
            if (dbManager.isUserExist(FirebaseAuthHandler.getInstance().getCurrentUserEmail())) {
                directions = SplashScreenFragmentDirections.actionSplashScreenFragmentToMoviesListFragment();
            } else {
                directions = SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginOptionsFragment();
            }
        } else {
            directions = SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginOptionsFragment();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                navController.navigate(directions);
            }
        }, 2000);
    }
}