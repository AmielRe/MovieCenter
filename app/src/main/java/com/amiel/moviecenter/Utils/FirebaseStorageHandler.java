package com.amiel.moviecenter.Utils;

import android.graphics.Bitmap;

import com.amiel.moviecenter.DB.GenericListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class FirebaseStorageHandler {

    private static volatile FirebaseStorageHandler INSTANCE = null;
    private final StorageReference storageRef;

    private FirebaseStorageHandler() {
        // Create a storage reference from our app
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    public static FirebaseStorageHandler getInstance() {
        if(INSTANCE == null) {
            synchronized (FirebaseStorageHandler.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FirebaseStorageHandler();
                }
            }
        }
        return INSTANCE;
    }

    private void uploadImage(Bitmap image, String imageName, GenericListener<String> listener) {
        StorageReference imagesRef = storageRef.child("images/" + imageName + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> listener.onComplete(null)).addOnSuccessListener(taskSnapshot -> imagesRef.getDownloadUrl().addOnSuccessListener(uri -> listener.onComplete(uri.toString())));
    }

    public void uploadUserImage(Bitmap image, String imageName, GenericListener<String> listener) {
        uploadImage(image, "users/" + imageName, listener);
    }

    public void uploadPostImage(Bitmap image, String imageName, GenericListener<String> listener) {
        uploadImage(image, "posts/" + imageName, listener);
    }
}