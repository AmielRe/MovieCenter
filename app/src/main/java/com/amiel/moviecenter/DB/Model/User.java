package com.amiel.moviecenter.DB.Model;

public class User {

    public String username;
    public String email;
    public byte[] profileImage;

    public User()
    {

    }

    public User(String username, String email, byte[] profileImage)
    {
        this.username = username;
        this.email = email;
        this.profileImage = profileImage;
    }
}
