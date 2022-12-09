package com.amiel.moviecenter;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.amiel.moviecenter.DB.DBManager;
import com.amiel.moviecenter.DB.Model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import static com.amiel.moviecenter.FirebaseAuthHandler.RC_SIGN_IN;

public class LoginOptionsFragment extends Fragment {

    Button signInWithEmail;
    Button signInWithGoogle;
    Button signUp;
    private DBManager dbManager;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        FirebaseAuthHandler.getInstance().initGoogleAuth(getString(R.string.web_client_id), getActivity());
        dbManager = new DBManager(getActivity());
        dbManager.open();
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.login_options_fragment, parent, false);
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
        signInWithEmail = (Button) view.findViewById(R.id.login_sign_in_with_email_button);
        signInWithGoogle = (Button) view.findViewById(R.id.login_sign_in_with_google_button);
        signUp = (Button) view.findViewById(R.id.login_sign_up_button);

        signInWithEmail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentUtils.loadFragment(LoginOptionsFragment.this, null, new SignInFragment(), R.id.activity_login_frame_layout, null);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentUtils.loadFragment(LoginOptionsFragment.this, null, new SignUpFragment(), R.id.activity_login_frame_layout, null);
            }
        });

        signInWithGoogle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent signInIntent = FirebaseAuthHandler.getInstance().getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                // ID is 0 because were not setting it, it's used just for retrieval
                User newUser = new User(account.getDisplayName(), account.getEmail(), ImageUtils.getBytes(((BitmapDrawable)getActivity().getDrawable(R.drawable.default_profile_image)).getBitmap()), 0);
                dbManager.insertUser(newUser);
                FirebaseAuthHandler.getInstance().signInWithGoogle(account, getActivity());
            } catch (ApiException e) {
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }
}