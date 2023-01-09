package com.amiel.moviecenter.Utils;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseStorageHandler {

    final long ONE_MEGABYTE = 1024 * 1024;
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

    public void uploadImage(byte[] image, String imageName) {
        storageRef.child(imageName).putBytes(image);
    }

    public void downloadImage(String imageName, OnSuccessListener<byte[]> listener) {
        StorageReference ref = storageRef.child(imageName);
        ref.getBytes(ONE_MEGABYTE).addOnSuccessListener(listener);
    }
}