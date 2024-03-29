package com.amiel.moviecenter.DB.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Entity(tableName = User.COLLECTION)
public class User {

    @PrimaryKey
    @NotNull
    @ColumnInfo(name = ID)
    public String id;

    @ColumnInfo(name = USERNAME)
    public String username;

    @ColumnInfo(name = EMAIL)
    public String email;

    @ColumnInfo(name = "profileImage")
    public byte[] profileImage;

    @ColumnInfo(name = PROFILE_IMAGE_URL)
    public String profileImageUrl;

    @ColumnInfo(name = LAST_UPDATED)
    private Long lastUpdated;

    public static final String USERNAME = "username";
    public static final String ID = "user_id";
    public static final String EMAIL = "email";
    public static final String PROFILE_IMAGE_URL = "profileImageUrl";
    public static final String LAST_UPDATED = "userLastUpdated";
    public static final String COLLECTION = "Users";

    public User(@NonNull String username, @NonNull String email, byte[] profileImage, @NonNull String id, String profileImageUrl)
    {
        this.username = username;
        this.email = email;
        this.id = id;
        this.profileImage = profileImage;
        this.profileImageUrl = profileImageUrl;
    }

    @Ignore
    public User()
    {

    }

    @NonNull
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(@NonNull String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public byte[] getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(@NonNull byte[] profileImage) {
        this.profileImage = profileImage;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public static User fromJson(Map<String,Object> json){
        String id = (String)json.get(ID);
        String username = (String)json.get(USERNAME);
        String email = (String) json.get(EMAIL);
        String profileImageUrl = (String) json.get(PROFILE_IMAGE_URL);
        User user = new User(username, email, null, id, profileImageUrl);

        try{
            Timestamp time = (Timestamp) json.get(LAST_UPDATED);
            user.setLastUpdated(time.getSeconds());
        }catch(Exception ignored){}

        return user;
    }

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put(ID, getId());
        json.put(USERNAME, getUsername());
        json.put(EMAIL, getEmail());
        json.put(PROFILE_IMAGE_URL, getProfileImageUrl());
        json.put(LAST_UPDATED, FieldValue.serverTimestamp());
        return json;
    }
}
