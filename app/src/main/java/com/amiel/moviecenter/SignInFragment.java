package com.amiel.moviecenter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SignInFragment extends Fragment {

    TextInputEditText emailEditText;
    TextInputEditText passwordEditText;
    TextInputLayout emailInputLayout;
    TextInputLayout passwordInputLayout;
    Button signInButton;
    private FirebaseAuth mAuth;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Defines the xml file for the fragment
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
                    mAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("TAG", "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Intent i = new Intent(getActivity(), MainActivity.class);
                                        startActivity(i);
                                        getActivity().finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("TAG", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(SignInFragment.this.getContext(), "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
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
