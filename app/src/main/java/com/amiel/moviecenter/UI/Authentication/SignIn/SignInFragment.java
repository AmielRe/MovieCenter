package com.amiel.moviecenter.UI.Authentication.SignIn;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.amiel.moviecenter.UI.Authentication.FirebaseAuthHandler;
import com.amiel.moviecenter.R;
import com.amiel.moviecenter.Utils.TextValidator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class SignInFragment extends Fragment {

    TextInputEditText emailEditText;
    TextInputEditText passwordEditText;
    TextInputLayout emailInputLayout;
    TextInputLayout passwordInputLayout;
    Button signInButton;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        return inflater.inflate(R.layout.sign_in_fragment, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        emailInputLayout = view.findViewById(R.id.sign_in_email_inputlayout);
        emailEditText = view.findViewById(R.id.sign_in_email_edittext);
        passwordInputLayout = view.findViewById(R.id.sign_in_password_inputlayout);
        passwordEditText = view.findViewById(R.id.sign_in_password_edittext);
        signInButton = view.findViewById(R.id.sign_in_sign_in_button);

        emailEditText.addTextChangedListener(new TextValidator(emailEditText) {
            @Override public void validate(TextView textView, String text) {
                if(text.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                    emailInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                    emailEditText.setError(getString(R.string.error_invalid_email_length));
                } else {
                    emailEditText.setError(null);
                    emailInputLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                    emailInputLayout.setEndIconDrawable(R.drawable.ic_check_circle_black_24dp);
                }
            }
        });

        passwordEditText.addTextChangedListener(new TextValidator(passwordEditText) {
            @Override public void validate(TextView textView, String text) {
                if(text.length() < 6 || text.length() > 20) {
                    passwordInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                    passwordEditText.setError(getString(R.string.error_invalid_password_length));
                } else {
                    passwordEditText.setError(null);
                    passwordInputLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                    passwordInputLayout.setEndIconDrawable(R.drawable.ic_check_circle_black_24dp);
                }
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(validate()) {
                    NavController navController = Navigation.findNavController(getActivity(), view.getId());
                    FirebaseAuthHandler.getInstance().signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString(), getActivity(), navController);
                }
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String email = Objects.requireNonNull(emailEditText.getText()).toString();
        String password = Objects.requireNonNull(passwordEditText.getText()).toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError(getString(R.string.error_invalid_email_length));
            valid = false;
        } else {
            emailEditText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 20) {
            passwordEditText.setError(getString(R.string.error_invalid_password_length));
            valid = false;
        } else {
            passwordEditText.setError(null);
        }

        return valid;
    }
}
