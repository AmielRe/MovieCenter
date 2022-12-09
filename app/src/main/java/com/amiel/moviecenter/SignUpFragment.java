package com.amiel.moviecenter;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.amiel.moviecenter.DB.DBManager;
import com.amiel.moviecenter.DB.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.SignInMethodQueryResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SignUpFragment extends Fragment {

    ProgressBar loadingProgressBar;
    TextInputEditText usernameEditText;
    TextInputEditText emailEditText;
    TextInputEditText passwordEditText;
    TextInputEditText passwordConfirmEditText;
    TextInputLayout usernameInputLayout;
    TextInputLayout emailInputLayout;
    TextInputLayout passwordInputLayout;
    TextInputLayout passwordConfirmInputLayout;
    Button signUpButton;
    private DBManager dbManager;

    private static final String BASE_URL = "https://api.eva.pingutil.com/email?email=";

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        dbManager = new DBManager(getActivity());
        dbManager.open();
        return inflater.inflate(R.layout.sign_up_fragment, parent, false);
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
        loadingProgressBar = view.findViewById(R.id.sign_up_loading_progress_bar);
        usernameInputLayout = view.findViewById(R.id.sign_up_username_input_layout);
        usernameEditText = view.findViewById(R.id.sign_up_username_edittext);
        emailInputLayout = view.findViewById(R.id.sign_up_email_input_layout);
        emailEditText = view.findViewById(R.id.sign_up_email_edittext);
        passwordConfirmInputLayout = view.findViewById(R.id.sign_up_password_confirm_input_layout);
        passwordConfirmEditText = view.findViewById(R.id.sign_up_password_confirm_edittext);
        passwordInputLayout = view.findViewById(R.id.sign_up_password_input_layout);
        passwordEditText = view.findViewById(R.id.sign_up_password_edittext);
        signUpButton = view.findViewById(R.id.sign_up_button_sign_up);

        usernameEditText.addTextChangedListener(new TextValidator(usernameEditText) {
            @Override public void validate(TextView textView, String text) {
                if(text.length() < 3 || text.length() > 15) {
                    usernameInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                    usernameEditText.setError(getString(R.string.error_invalid_username_length));
                } else {
                    usernameEditText.setError(null);
                    usernameInputLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                    usernameInputLayout.setEndIconDrawable(R.drawable.ic_check_circle_black_24dp);
                }
            }
        });

        emailEditText.addTextChangedListener(new TextValidator(emailEditText) {
            @Override public void validate(TextView textView, String text) {
                if(text.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                    emailInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                    emailEditText.setError(getString(R.string.error_invalid_email_length));
                } //else if() {
                    // This else if will be used later to verify email is not already taken
                /*}*/ else {
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

                if(!text.equals(Objects.requireNonNull(passwordConfirmEditText.getText()).toString())) {
                    passwordConfirmInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                    passwordConfirmEditText.setError(getString(R.string.error_password_confirm));
                } else {
                    passwordConfirmEditText.setError(null);
                    passwordConfirmInputLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                    passwordConfirmInputLayout.setEndIconDrawable(R.drawable.ic_check_circle_black_24dp);
                }
            }
        });

        passwordConfirmEditText.addTextChangedListener(new TextValidator(passwordConfirmEditText) {
            @Override public void validate(TextView textView, String text) {
                if(!text.equals(Objects.requireNonNull(passwordEditText.getText()).toString())) {
                    passwordConfirmInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                    passwordConfirmEditText.setError(getString(R.string.error_password_confirm));
                } else {
                    passwordConfirmEditText.setError(null);
                    passwordConfirmInputLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                    passwordConfirmInputLayout.setEndIconDrawable(R.drawable.ic_check_circle_black_24dp);
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(validate()) {
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url(BASE_URL + emailEditText.getText().toString())
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            //loadingProgressBar.setVisibility(View.GONE);
                            call.cancel();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            final String myResponse = response.body().string();
                            JSONObject json = new JSONObject();
                            try {
                                json = new JSONObject(myResponse);
                            } catch (JSONException e) {
                                loadingProgressBar.setVisibility(View.INVISIBLE);
                                e.printStackTrace();
                            }

                            try {
                                // Check if email is real
                                if(json.getJSONObject("data").getBoolean("valid_syntax") && json.getJSONObject("data").getBoolean("deliverable")) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // If we got here, email is not fake
                                            FirebaseAuthHandler.getInstance().getmAuth().fetchSignInMethodsForEmail(emailEditText.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                                                            boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                                                            if (isNewUser) {
                                                                // Email doesn't already exist
                                                                emailEditText.setError(null);

                                                                // ID is 0 because were not setting it, it's used just for retrieval
                                                                User newUser = new User(usernameEditText.getText().toString(), emailEditText.getText().toString(), ImageUtils.getBytes(((BitmapDrawable)getActivity().getDrawable(R.drawable.default_profile_image)).getBitmap()), 0);
                                                                dbManager.insertUser(newUser);
                                                                FirebaseAuthHandler.getInstance().createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString(), usernameEditText.getText().toString(), getActivity());
                                                            } else {
                                                                // Email already exists
                                                                emailEditText.setError(getString(R.string.error_email_already_in_use));
                                                                emailInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                                                                loadingProgressBar.setVisibility(View.INVISIBLE);
                                                            }

                                                        }
                                                    });
                                        }
                                    });
                                }
                                else {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            emailInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                                            emailEditText.setError(getString(R.string.error_invalid_email));
                                            loadingProgressBar.setVisibility(View.INVISIBLE);
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                loadingProgressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String username = Objects.requireNonNull(usernameEditText.getText()).toString();
        String email = Objects.requireNonNull(emailEditText.getText()).toString();
        String password = Objects.requireNonNull(passwordEditText.getText()).toString();
        String passwordConfirm = Objects.requireNonNull(passwordConfirmEditText.getText()).toString();

        if (username.isEmpty() || username.length() < 3 || username.length() > 15) {
            usernameEditText.setError(getString(R.string.error_invalid_username_length));
            valid = false;
            usernameInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
        } else {
            usernameEditText.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError(getString(R.string.error_invalid_email_length));
            valid = false;
            emailInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
        } else {
            emailEditText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 20) {
            passwordEditText.setError(getString(R.string.error_invalid_password_length));
            valid = false;
            passwordInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
        } else {
            passwordEditText.setError(null);
        }

        if(passwordConfirm.isEmpty() || !passwordConfirm.equals(password)) {
            passwordConfirmEditText.setError(getString(R.string.error_password_confirm));
            valid = false;
            passwordConfirmInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
        } else {
            passwordConfirmEditText.setError(null);
        }

        return valid;
    }
}
