package com.amiel.moviecenter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class LoginOptionsFragment extends Fragment {

    Button signInWithEmail;
    Button signInWithGoogle;
    Button signUp;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.login_options_fragment, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Button signInWithEmail = (Button) view.findViewById(R.id.login_sign_in_with_email_button);
        Button signInWithGoogle = (Button) view.findViewById(R.id.login_sign_in_with_google_button);
        Button signUp = (Button) view.findViewById(R.id.login_sign_up_button);

        signInWithEmail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentUtils.loadFragment(LoginOptionsFragment.this, null, new SignInFragment());
            }
        });
    }
}