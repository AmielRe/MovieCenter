package com.amiel.moviecenter.UI.Authentication;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.navigation.NavController;

import com.amiel.moviecenter.UI.Authentication.LoginOptions.LoginOptionsFragmentDirections;
import com.amiel.moviecenter.UI.Authentication.SignUp.SignUpFragmentDirections;
import com.amiel.moviecenter.UI.Authentication.SignIn.SignInFragmentDirections;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class FirebaseAuthHandler {
    private static volatile FirebaseAuthHandler INSTANCE = null;
    private final FirebaseAuth mAuth;
    private GoogleSignInClient mSignInClient;

    private FirebaseAuthHandler() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    public static FirebaseAuthHandler getInstance() {
        if(INSTANCE == null) {
            synchronized (FirebaseAuthHandler.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FirebaseAuthHandler();
                }
            }
        }
        return INSTANCE;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public boolean isUserLoggedIn() {
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        return currentUser != null;
    }

    public void signInWithEmailAndPassword(String email, String password, Activity context, NavController navController) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(context, task -> {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    navController.navigate(SignInFragmentDirections.actionSignInFragmentToMoviesListFragment());
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithEmail:failure", task.getException());
                    Toast.makeText(context, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            });
    }

    public void createUserWithEmailAndPassword(String email, String password, Activity context, NavController navController) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(context, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        navController.navigate(SignUpFragmentDirections.actionSignUpFragmentToMoviesListFragment());
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(context, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void initGoogleAuth(String webClientId, Activity context) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webClientId)
                .requestEmail()
                .build();
        mSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public void signInWithGoogle(GoogleSignInAccount acct, Activity context, NavController navController) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
            .addOnSuccessListener(context, authResult -> {
                navController.navigate(LoginOptionsFragmentDirections.actionLoginOptionsFragmentToMoviesListFragment());
            })
            .addOnFailureListener(context, e -> Toast.makeText(context, "Authentication failed.",
                    Toast.LENGTH_SHORT).show());
    }

    public Intent getSignInIntent() {
        return mSignInClient.getSignInIntent();
    }

    public String getCurrentUserEmail() {
        String email = "";
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            email = user.getEmail();
        }

        return email;
    }

    public void logoutCurrentUser() {
        mAuth.signOut();
    }
}
