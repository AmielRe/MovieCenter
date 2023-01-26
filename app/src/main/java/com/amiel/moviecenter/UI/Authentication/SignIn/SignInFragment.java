package com.amiel.moviecenter.UI.Authentication.SignIn;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.amiel.moviecenter.UI.Authentication.FirebaseAuthHandler;
import com.amiel.moviecenter.R;
import com.amiel.moviecenter.Utils.TextValidator;
import com.amiel.moviecenter.databinding.SignInFragmentBinding;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class SignInFragment extends Fragment {

    SignInFragmentBinding binding;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        ((AppCompatActivity) requireActivity()).getSupportActionBar().hide();
        binding = SignInFragmentBinding.inflate(inflater, parent, false);

        return binding.getRoot();
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        binding.signInEmailEdittext.addTextChangedListener(new TextValidator(binding.signInEmailEdittext) {
            @Override public void validate(TextView textView, String text) {
                if(text.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                    binding.signInEmailInputlayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                    binding.signInEmailEdittext.setError(getString(R.string.error_invalid_email_length));
                } else {
                    binding.signInEmailEdittext.setError(null);
                    binding.signInEmailInputlayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                    binding.signInEmailInputlayout.setEndIconDrawable(R.drawable.ic_check_circle_black_24dp);
                }
            }
        });

        binding.signInPasswordEdittext.addTextChangedListener(new TextValidator(binding.signInPasswordEdittext) {
            @Override public void validate(TextView textView, String text) {
                if(text.length() < 6 || text.length() > 20) {
                    binding.signInPasswordInputlayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                    binding.signInPasswordEdittext.setError(getString(R.string.error_invalid_password_length));
                } else {
                    binding.signInPasswordEdittext.setError(null);
                    binding.signInPasswordInputlayout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
                }
            }
        });

        binding.signInSignInButton.setOnClickListener(v -> {
            if(validate()) {
                NavController navController = Navigation.findNavController(requireActivity(), view.getId());
                FirebaseAuthHandler.getInstance().signInWithEmailAndPassword(binding.signInEmailEdittext.getText().toString(), binding.signInPasswordEdittext.getText().toString(), requireActivity(), navController);
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String email = Objects.requireNonNull(binding.signInEmailEdittext.getText()).toString();
        String password = Objects.requireNonNull(binding.signInPasswordEdittext.getText()).toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.signInEmailEdittext.setError(getString(R.string.error_invalid_email_length));
            valid = false;
            binding.signInEmailInputlayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
        } else {
            binding.signInEmailEdittext.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 20) {
            binding.signInPasswordEdittext.setError(getString(R.string.error_invalid_password_length));
            valid = false;
            binding.signInPasswordInputlayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
        } else {
            binding.signInPasswordEdittext.setError(null);
            binding.signInPasswordInputlayout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
        }

        return valid;
    }
}
