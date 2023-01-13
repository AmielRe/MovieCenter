package com.amiel.moviecenter.UI.Profile;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.amiel.moviecenter.DB.Model.User;
import com.amiel.moviecenter.R;
import com.amiel.moviecenter.UI.Authentication.FirebaseAuthHandler;
import com.amiel.moviecenter.Utils.FirebaseStorageHandler;
import com.amiel.moviecenter.Utils.ImageUtils;
import com.amiel.moviecenter.Utils.PermissionHelper;
import com.amiel.moviecenter.Utils.ViewModelFactory;
import com.amiel.moviecenter.databinding.ProfileFragmentBinding;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class ProfileFragment extends Fragment {

    ProfileFragmentBinding binding;
    ProfileViewModel profileViewModel;
    ActivityResultLauncher<String[]> cameraResult;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        binding = ProfileFragmentBinding.inflate(inflater, parent, false);

        cameraResult = new PermissionHelper().registerForActivityResult(this, isGranted -> {
            // If permission granted
            if(!isGranted.containsValue(false)) {
                cameraResultLauncher.launch(null);
            }
        });

        return binding.getRoot();
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        String email = FirebaseAuthHandler.getInstance().getCurrentUserEmail();
        profileViewModel = new ViewModelProvider(this, new ViewModelFactory(requireActivity().getApplication(), email)).get(ProfileViewModel.class);

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.findItem(R.id.search_bar).setVisible(false);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }

            @Override
            public void onPrepareMenu(@NonNull Menu menu) {}
        });

        profileViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            binding.profileFragmentEmailEditText.setText(email);
            binding.profileFragmentUsernameEditText.setText(user.username);
            binding.profileFragmentUsernameEditText.setSelection(binding.profileFragmentUsernameEditText.getText().length());
            Picasso.get().load(user.getProfileImageUrl()).placeholder(R.drawable.default_profile_image).into(binding.profileFragmentImage);
        });

        binding.profileFragmentImage.setOnClickListener(v -> selectImage());

        binding.profileFragmentSaveButton.setOnClickListener(v -> {
            try {
                User updatedUser = profileViewModel.getUser().getValue();
                byte[] userUpdatedProfileImage = ImageUtils.getBytes(((BitmapDrawable)binding.profileFragmentImage.getDrawable()).getBitmap());
                updatedUser.setUsername(binding.profileFragmentUsernameEditText.getText().toString());
                updatedUser.setProfileImage(userUpdatedProfileImage);
                FirebaseStorageHandler.getInstance().uploadUserImage(((BitmapDrawable)binding.profileFragmentImage.getDrawable()).getBitmap(), updatedUser.getId(), data -> {
                    if(data != null) {
                        updatedUser.setProfileImageUrl(data);
                    }
                    profileViewModel.updateUser(updatedUser);
                    Toast.makeText(requireActivity(), "User details saved!", Toast.LENGTH_SHORT).show();
                });
            } catch(Exception e) {
                Toast.makeText(requireActivity(), "Failed to save details...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Choose profile picture");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo"))
            {
                if(PermissionHelper.isMissingPermissions(requireActivity(), Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET)) {
                    new PermissionHelper().startPermissionRequest(cameraResult, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET);
                } else {
                    cameraResultLauncher.launch(null);
                }
            }
            else if (options[item].equals("Choose from Gallery"))
            {
                galleryResultLauncher.launch("image/*");
            }
            else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    // Launcher for gallery image pick
    private final ActivityResultLauncher<String> galleryResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        try {
                            Bitmap res = ImageUtils.handleSamplingAndRotationBitmap(requireActivity(), result);
                            binding.profileFragmentImage.setImageBitmap(res);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    private final ActivityResultLauncher<Void> cameraResultLauncher = registerForActivityResult(
            new ActivityResultContracts.TakePicturePreview(),
            result -> {
                if(result != null) {
                    binding.profileFragmentImage.setImageBitmap(result);
                }
            }
    );
}
