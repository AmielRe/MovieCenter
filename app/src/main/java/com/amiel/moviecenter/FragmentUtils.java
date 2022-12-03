package com.amiel.moviecenter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentUtils {

    public static void loadFragment(Fragment fragmentContext, AppCompatActivity activityContext, Fragment fragmentToLoad) {
        FragmentManager fm = fragmentContext != null ? fragmentContext.getParentFragmentManager() : activityContext.getSupportFragmentManager();
        loadFragment(fm, fragmentToLoad);
    }

    private static void loadFragment(FragmentManager fm, Fragment fragment) {
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit(); // save the changes
    }
}
