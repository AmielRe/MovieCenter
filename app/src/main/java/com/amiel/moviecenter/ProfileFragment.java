package com.amiel.moviecenter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ProfileFragment extends Fragment {

    TextInputEditText emailEditText;
    TextInputEditText usernameEditText;
    TextInputLayout emailInputLayout;
    TextInputLayout usernameInputLayout;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.profile_fragment, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        emailInputLayout = view.findViewById(R.id.activity_profile_email_input_layout);
        emailEditText = view.findViewById(R.id.activity_profile_email_edit_text);
        usernameInputLayout = view.findViewById(R.id.activity_profile_username_input_layout);
        usernameEditText = view.findViewById(R.id.activity_profile_username_edit_text);

        emailEditText.setText(FirebaseAuthHandler.getInstance().getCurrentUserEmail());
        usernameEditText.setText("Amiel"); // Temp string
    }
}
