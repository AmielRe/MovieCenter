package com.amiel.moviecenter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginOptionsFragment extends Fragment {

    Button signInWithEmail;
    Button signInWithGoogle;
    Button signUp;
    private FirebaseAuth mAuth;
    GoogleSignInClient mSignInClient;
    private static final int RC_SIGN_IN = 1234;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();
        mSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.login_options_fragment, parent, false);
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
                FragmentUtils.loadFragment(LoginOptionsFragment.this, null, new SignInFragment());
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentUtils.loadFragment(LoginOptionsFragment.this, null, new SignUpFragment());
            }
        });

        signInWithGoogle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent signInIntent = mSignInClient.getSignInIntent();
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
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(getActivity(), authResult -> {
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().finish();
                })
                .addOnFailureListener(getActivity(), e -> Toast.makeText(getActivity(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show());
    }
}