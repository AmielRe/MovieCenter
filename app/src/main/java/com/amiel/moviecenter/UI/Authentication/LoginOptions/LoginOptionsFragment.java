package com.amiel.moviecenter.UI.Authentication.LoginOptions;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.amiel.moviecenter.UI.Authentication.FirebaseAuthHandler;
import com.amiel.moviecenter.DB.DatabaseRepository;
import com.amiel.moviecenter.DB.Model.User;
import com.amiel.moviecenter.R;
import com.amiel.moviecenter.Utils.FirebaseStorageHandler;
import com.amiel.moviecenter.Utils.ImageUtils;
import com.amiel.moviecenter.databinding.LoginOptionsFragmentBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.io.IOException;

public class LoginOptionsFragment extends Fragment {

    LoginOptionsFragmentBinding binding;
    private DatabaseRepository db;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        FirebaseAuthHandler.getInstance().initGoogleAuth(getString(R.string.web_client_id), requireActivity());
        db = new DatabaseRepository(requireActivity());
        ((AppCompatActivity) requireActivity()).getSupportActionBar().hide();
        binding = LoginOptionsFragmentBinding.inflate(inflater, parent, false);

        return binding.getRoot();
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        binding.loginSignInWithEmailButton.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.signInFragment));

        binding.loginSignUpButton.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.signUpFragment));

        ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);

                        byte[] userProfileImage = null;
                        try {
                            userProfileImage = ImageUtils.getBytes(ImageUtils.handleSamplingAndRotationBitmap(getActivity(), account.getPhotoUrl()));
                        } catch(Exception ignored) {}

                        User newUser = new User(account.getDisplayName(), account.getEmail(), userProfileImage, account.getId());
                        db.insertUserTask(newUser);
                        if(userProfileImage != null) {
                            FirebaseStorageHandler.getInstance().uploadImage(userProfileImage, account.getId());
                        }

                        NavController navController = Navigation.findNavController(requireActivity(), getView().getId());
                        FirebaseAuthHandler.getInstance().signInWithGoogle(account, requireActivity(), navController);
                    } catch (ApiException e) {
                        Log.w("TAG", "Google sign in failed", e);
                    }
                });

        binding.loginSignInWithGoogleButton.setOnClickListener(v -> {
            Intent signInIntent = FirebaseAuthHandler.getInstance().getSignInIntent();
            startActivityIntent.launch(signInIntent);
        });
    }
}