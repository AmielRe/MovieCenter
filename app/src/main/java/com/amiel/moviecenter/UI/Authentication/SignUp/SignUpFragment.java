package com.amiel.moviecenter.UI.Authentication.SignUp;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.amiel.moviecenter.UI.Authentication.FirebaseAuthHandler;
import com.amiel.moviecenter.DB.DatabaseRepository;
import com.amiel.moviecenter.R;
import com.amiel.moviecenter.Utils.FirebaseStorageHandler;
import com.amiel.moviecenter.Utils.ImageUtils;
import com.amiel.moviecenter.Utils.TextValidator;
import com.amiel.moviecenter.databinding.SignUpFragmentBinding;
import com.google.android.material.textfield.TextInputLayout;

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

    SignUpFragmentBinding binding;
    private DatabaseRepository db;

    private static final String CHECK_EMAIL_BASE_URL = "http://api.eva.pingutil.com/email?email=";

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        db = new DatabaseRepository(requireActivity());
        ((AppCompatActivity) requireActivity()).getSupportActionBar().hide();
        binding = SignUpFragmentBinding.inflate(inflater, parent, false);

        return binding.getRoot();
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        binding.signUpUsernameEdittext.addTextChangedListener(new TextValidator(binding.signUpUsernameEdittext) {
            @Override public void validate(TextView textView, String text) {
                if(text.length() < 3 || text.length() > 15) {
                    binding.signUpUsernameInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                    binding.signUpUsernameEdittext.setError(getString(R.string.error_invalid_username_length));
                } else {
                    binding.signUpUsernameEdittext.setError(null);
                    binding.signUpUsernameInputLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                    binding.signUpUsernameInputLayout.setEndIconDrawable(R.drawable.ic_check_circle_black_24dp);
                }
            }
        });

        binding.signUpEmailEdittext.addTextChangedListener(new TextValidator(binding.signUpEmailEdittext) {
            @Override public void validate(TextView textView, String text) {
                if(text.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                    binding.signUpEmailInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                    binding.signUpEmailEdittext.setError(getString(R.string.error_invalid_email_length));
                } else {
                    binding.signUpEmailEdittext.setError(null);
                    binding.signUpEmailInputLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                    binding.signUpEmailInputLayout.setEndIconDrawable(R.drawable.ic_check_circle_black_24dp);
                }
            }
        });

        binding.signUpPasswordEdittext.addTextChangedListener(new TextValidator(binding.signUpPasswordEdittext) {
            @Override public void validate(TextView textView, String text) {
                if(text.length() < 6 || text.length() > 20) {
                    binding.signUpPasswordInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                    binding.signUpPasswordEdittext.setError(getString(R.string.error_invalid_password_length));
                } else {
                    binding.signUpPasswordEdittext.setError(null);
                    binding.signUpPasswordInputLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                    binding.signUpPasswordInputLayout.setEndIconDrawable(R.drawable.ic_check_circle_black_24dp);
                }

                if(!text.equals(Objects.requireNonNull(binding.signUpPasswordConfirmEdittext.getText()).toString())) {
                    binding.signUpPasswordConfirmInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                    binding.signUpPasswordConfirmEdittext.setError(getString(R.string.error_password_confirm));
                } else {
                    binding.signUpPasswordConfirmEdittext.setError(null);
                    binding.signUpPasswordConfirmInputLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                    binding.signUpPasswordConfirmInputLayout.setEndIconDrawable(R.drawable.ic_check_circle_black_24dp);
                }
            }
        });

        binding.signUpPasswordConfirmEdittext.addTextChangedListener(new TextValidator(binding.signUpPasswordConfirmEdittext) {
            @Override public void validate(TextView textView, String text) {
                if(!text.equals(Objects.requireNonNull(binding.signUpPasswordConfirmEdittext.getText()).toString())) {
                    binding.signUpPasswordConfirmInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                    binding.signUpPasswordConfirmEdittext.setError(getString(R.string.error_password_confirm));
                } else {
                    binding.signUpPasswordConfirmEdittext.setError(null);
                    binding.signUpPasswordConfirmInputLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                    binding.signUpPasswordConfirmInputLayout.setEndIconDrawable(R.drawable.ic_check_circle_black_24dp);
                }
            }
        });

        binding.signUpButtonSignUp.setOnClickListener(v -> {
            if(!validate()) {
                return;
            }

            binding.signUpLoadingProgressBar.setVisibility(View.VISIBLE);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(CHECK_EMAIL_BASE_URL + binding.signUpEmailEdittext.getText().toString())
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    requireActivity().runOnUiThread(() -> {
                        binding.signUpLoadingProgressBar.setVisibility(View.INVISIBLE);
                    });
                    e.printStackTrace();
                    call.cancel();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                    final String myResponse = response.body().string();
                    JSONObject json = new JSONObject();
                    try {
                        json = new JSONObject(myResponse);
                    } catch (JSONException e) {
                        requireActivity().runOnUiThread(() -> {
                            binding.signUpLoadingProgressBar.setVisibility(View.INVISIBLE);
                        });
                        e.printStackTrace();
                    }

                    try {
                        // Check if email is real
                        if(!json.getJSONObject("data").getBoolean("valid_syntax") || !json.getJSONObject("data").getBoolean("deliverable")) {
                            requireActivity().runOnUiThread(() -> {
                                binding.signUpEmailInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                                binding.signUpEmailEdittext.setError(getString(R.string.error_invalid_email));
                                binding.signUpLoadingProgressBar.setVisibility(View.INVISIBLE);
                            });
                            return;
                        }
                        requireActivity().runOnUiThread(() -> {
                            // If we got here, email is not fake
                            FirebaseAuthHandler.getInstance().fetchSignInMethods(binding.signUpEmailEdittext.getText().toString()).addOnCompleteListener(task -> {
                                boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                                if (!isNewUser) {
                                    // Email already exists
                                    binding.signUpEmailEdittext.setError(getString(R.string.error_email_already_in_use));
                                    binding.signUpEmailInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                                    binding.signUpLoadingProgressBar.setVisibility(View.INVISIBLE);
                                    return;
                                }

                                // Email doesn't already exist
                                binding.signUpEmailEdittext.setError(null);

                                NavController navController = Navigation.findNavController(requireActivity(), view.getId());
                                FirebaseAuthHandler.getInstance().createUserWithEmailAndPassword(binding.signUpUsernameEdittext.getText().toString(), binding.signUpEmailEdittext.getText().toString(), binding.signUpPasswordEdittext.getText().toString(), requireActivity(), newUser -> FirebaseStorageHandler.getInstance().uploadUserImage(ImageUtils.getBitmap(newUser.getProfileImage()), newUser.getId(), imageUrl -> {
                                    if (imageUrl != null) {
                                        newUser.setProfileImageUrl(imageUrl);
                                        db.insertUserTask(newUser, data -> {});
                                    }
                                    navController.navigate(SignUpFragmentDirections.actionSignUpFragmentToMoviesListFragment());
                                }));
                            });
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                        requireActivity().runOnUiThread(() -> binding.signUpLoadingProgressBar.setVisibility(View.INVISIBLE));
                    }
                }
            });
        });
    }

    public boolean validate() {
        boolean valid = true;

        String username = Objects.requireNonNull(binding.signUpUsernameEdittext.getText()).toString();
        String email = Objects.requireNonNull(binding.signUpEmailEdittext.getText()).toString();
        String password = Objects.requireNonNull(binding.signUpPasswordEdittext.getText()).toString();
        String passwordConfirm = Objects.requireNonNull(binding.signUpPasswordConfirmEdittext.getText()).toString();

        if (username.isEmpty() || username.length() < 3 || username.length() > 15) {
            binding.signUpUsernameEdittext.setError(getString(R.string.error_invalid_username_length));
            valid = false;
            binding.signUpUsernameInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
        } else {
            binding.signUpUsernameEdittext.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.signUpEmailEdittext.setError(getString(R.string.error_invalid_email_length));
            valid = false;
            binding.signUpEmailInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
        } else {
            binding.signUpEmailEdittext.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 20) {
            binding.signUpPasswordEdittext.setError(getString(R.string.error_invalid_password_length));
            valid = false;
            binding.signUpPasswordInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
        } else {
            binding.signUpPasswordEdittext.setError(null);
        }

        if(passwordConfirm.isEmpty() || !passwordConfirm.equals(password)) {
            binding.signUpPasswordConfirmEdittext.setError(getString(R.string.error_password_confirm));
            valid = false;
            binding.signUpPasswordConfirmInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
        } else {
            binding.signUpPasswordConfirmEdittext.setError(null);
        }

        return valid;
    }
}
