package com.amiel.moviecenter.DB.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Entity(tableName = User.COLLECTION)
public class User {

    @PrimaryKey
    @NotNull
    @ColumnInfo(name = User.ID)
    public String id;

    @NonNull
    @ColumnInfo(name = USERNAME)
    public String username;

    @NonNull
    @ColumnInfo(name = EMAIL)
    public String email;

    @NonNull
    @ColumnInfo(name = "profileImage")
    public byte[] profileImage;

    public static final String USERNAME = "username";
    public static final String ID = "id";
    public static final String EMAIL = "email";
    public static final String COLLECTION = "Users";

    public User(@NonNull String username, @NonNull String email, byte[] profileImage, String id)
    {
        this.username = username;
        this.email = email;
        this.id = id;
        this.profileImage = profileImage;
    }

    @Ignore
    public User()
    {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
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

    public static User fromJson(Map<String,Object> json){
        String id = (String)json.get(ID);
        String username = (String)json.get(USERNAME);
        String email = (String) json.get(EMAIL);
        return new User(username, email, null, id);
    }

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put(ID, getId());
        json.put(USERNAME, getUsername());
        json.put(EMAIL, getEmail());
        return json;
    }
}
