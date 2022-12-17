package com.amiel.moviecenter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amiel.moviecenter.DB.DatabaseRepository;

public class SplashScreenFragment extends Fragment {

    private DatabaseRepository db;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        db = new DatabaseRepository(getActivity());
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        return inflater.inflate(R.layout.fragment_splash, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(getActivity(), view.getId());

        db.getUserByEmail(FirebaseAuthHandler.getInstance().getCurrentUserEmail()).observe(getActivity(), user -> {
            NavDirections directions;
            if(user != null && FirebaseAuthHandler.getInstance().isUserLoggedIn()) {
                directions = SplashScreenFragmentDirections.actionSplashScreenFragmentToMoviesListFragment();
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
        });
    }
}