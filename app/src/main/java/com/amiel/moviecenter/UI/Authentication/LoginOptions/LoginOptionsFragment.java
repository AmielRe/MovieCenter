package com.amiel.moviecenter.UI.Authentication.LoginOptions;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.amiel.moviecenter.UI.Authentication.FirebaseAuthHandler;
import com.amiel.moviecenter.DB.DatabaseRepository;
import com.amiel.moviecenter.DB.Model.User;
import com.amiel.moviecenter.R;
import com.amiel.moviecenter.Utils.ImageUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginOptionsFragment extends Fragment {

    Button signInWithEmail;
    Button signInWithGoogle;
    Button signUp;
    private DatabaseRepository db;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        FirebaseAuthHandler.getInstance().initGoogleAuth(getString(R.string.web_client_id), getActivity());
        db = new DatabaseRepository(getActivity());
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

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

        signInWithEmail.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.signInFragment));

        signUp.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.signUpFragment));

        ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);

                        // ID is 0 because were not setting it, it's used just for retrieval
                        User newUser = new User(account.getDisplayName(), account.getEmail(), ImageUtils.getBytes(((BitmapDrawable)getActivity().getDrawable(R.drawable.default_profile_image)).getBitmap()), 0);
                        db.insertUserTask(newUser);
                        NavController navController = Navigation.findNavController(getActivity(), getView().getId());
                        FirebaseAuthHandler.getInstance().signInWithGoogle(account, getActivity(), navController);
                    } catch (ApiException e) {
                        Log.w("TAG", "Google sign in failed", e);
                    }
                });

        signInWithGoogle.setOnClickListener(v -> {
            Intent signInIntent = FirebaseAuthHandler.getInstance().getSignInIntent();
            startActivityIntent.launch(signInIntent);
        });
    }
}