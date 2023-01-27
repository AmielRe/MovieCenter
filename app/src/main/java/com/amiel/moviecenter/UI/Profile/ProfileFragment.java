package com.amiel.moviecenter.UI.Profile;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.amiel.moviecenter.DB.Model.User;
import com.amiel.moviecenter.R;
import com.amiel.moviecenter.UI.Authentication.FirebaseAuthHandler;
import com.amiel.moviecenter.Utils.FirebaseStorageHandler;
import com.amiel.moviecenter.Utils.ImageUtils;
import com.amiel.moviecenter.Utils.PermissionUtils;
import com.amiel.moviecenter.Utils.ViewModelFactory;
import com.amiel.moviecenter.databinding.ProfileFragmentBinding;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class ProfileFragment extends Fragment {

    ProfileFragmentBinding binding;
    ProfileViewModel profileViewModel;
    ActivityResultLauncher<String[]> permissionResult;
    ActivityResultLauncher<Void> cameraResult;
    ActivityResultLauncher<String> galleryResult;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        binding = ProfileFragmentBinding.inflate(inflater, parent, false);

        permissionResult = PermissionUtils.registerForActivityResult(this, isGranted -> {
            // If permission granted
            if(!isGranted.containsValue(false)) {
                cameraResult.launch(null);
            }
        });

        cameraResult = ImageUtils.registerForCameraActivityResult(this, data -> {
            if(data != null) {
                binding.profileFragmentImage.setImageBitmap(data);
            }
        });

        galleryResult = ImageUtils.registerForGalleryActivityResult(this, data -> {
            if (data != null) {
                try {
                    Bitmap res = ImageUtils.handleSamplingAndRotationBitmap(requireActivity(), data);
                    binding.profileFragmentImage.setImageBitmap(res);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return binding.getRoot();
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
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

        binding.profileFragmentImage.setOnClickListener(v -> ImageUtils.selectImage(this, galleryResult, cameraResult, permissionResult));

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
}
