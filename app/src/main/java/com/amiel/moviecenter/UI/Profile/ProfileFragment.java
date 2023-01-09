package com.amiel.moviecenter.UI.Profile;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.amiel.moviecenter.BuildConfig;
import com.amiel.moviecenter.DB.Model.User;
import com.amiel.moviecenter.R;
import com.amiel.moviecenter.UI.Authentication.FirebaseAuthHandler;
import com.amiel.moviecenter.Utils.FirebaseStorageHandler;
import com.amiel.moviecenter.Utils.ImageUtils;
import com.amiel.moviecenter.Utils.PermissionHelper;
import com.amiel.moviecenter.Utils.ViewModelFactory;
import com.amiel.moviecenter.databinding.ProfileFragmentBinding;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static android.app.Activity.RESULT_OK;

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
                cameraResultLauncher.launch(ImageUtils.getCameraIntent(getContext()));
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
            FirebaseStorageHandler.getInstance().downloadImage(user.getId(), bytes -> {
                Bitmap profileBitmap = ImageUtils.getBitmap(bytes);
                if(profileBitmap != null) {
                    binding.profileFragmentImage.setImageBitmap(profileBitmap);
                }
            });
        });

        binding.profileFragmentImage.setOnClickListener(v -> selectImage());

        binding.profileFragmentSaveButton.setOnClickListener(v -> {
            try {
                User updatedUser = profileViewModel.getUser().getValue();
                byte[] userUpdatedProfileImage = ImageUtils.getBytes(((BitmapDrawable)binding.profileFragmentImage.getDrawable()).getBitmap());
                updatedUser.setUsername(binding.profileFragmentUsernameEditText.getText().toString());
                updatedUser.setProfileImage(userUpdatedProfileImage);
                FirebaseStorageHandler.getInstance().uploadImage(userUpdatedProfileImage, updatedUser.getId());
                profileViewModel.updateUser(updatedUser);
                Toast.makeText(requireActivity(), "User details saved!", Toast.LENGTH_SHORT).show();
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
                    cameraResultLauncher.launch(ImageUtils.getCameraIntent(getContext()));
                }
            }
            else if (options[item].equals("Choose from Gallery"))
            {
                galleryResultLauncher.launch(ImageUtils.getGalleryIntent());
            }
            else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    // Launcher for gallery image pick
    private final ActivityResultLauncher<Intent> galleryResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK
                        && result.getData() != null) {
                    Uri selectedImage = result.getData().getData();
                    try {
                        Bitmap res = ImageUtils.handleSamplingAndRotationBitmap(requireActivity(), selectedImage);
                        binding.profileFragmentImage.setImageBitmap(res);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    );

    private final ActivityResultLauncher<Intent> cameraResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK
                        && result.getData() != null) {
                    File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString());
                    for (File temp : f.listFiles()) {
                        if (temp.getName().equals("temp.jpg")) {
                            f = temp;
                            break;
                        }
                    }
                    try {
                        Bitmap res = ImageUtils.handleSamplingAndRotationBitmap(requireActivity(), FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", f));
                        binding.profileFragmentImage.setImageBitmap(res);
                        String path = android.os.Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                                + File.separator
                                + "Phoenix" + File.separator + "default";
                        f.delete();
                        OutputStream outFile = null;
                        File file = new File(path, System.currentTimeMillis() + ".jpg");
                        try {
                            outFile = new FileOutputStream(file);
                            res.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                            outFile.flush();
                            outFile.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
    );
}
