package com.amiel.moviecenter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentUtils {

    public static void loadFragment(Fragment fragmentContext, FragmentActivity activityContext, Fragment fragmentToLoad, int frameLayoutId, Bundle dataToAdd, boolean addToBackStack) {
        FragmentManager fm = fragmentContext != null ? fragmentContext.getParentFragmentManager() : activityContext.getSupportFragmentManager();
        loadFragment(fm, fragmentToLoad, frameLayoutId, dataToAdd, addToBackStack);
    }

    private static void loadFragment(FragmentManager fm, Fragment fragment, int frameLayoutId, Bundle dataToAdd, Boolean addToBackStack) {
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        if(dataToAdd != null) {
            fragment.setArguments(dataToAdd);
        }

        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(frameLayoutId, fragment);
        if(addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.toString());
        }

        fragmentTransaction.commit(); // save the changes
    }
}
