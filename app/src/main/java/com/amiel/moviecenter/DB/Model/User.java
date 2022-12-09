package com.amiel.moviecenter.DB.Model;

public class User {

    public long id;
    public String username;
    public String email;
    public byte[] profileImage;

    public User()
    {

    }

    public User(String username, String email, byte[] profileImage, long id)
    {
        this.username = username;
        this.email = email;
        this.profileImage = profileImage;
        this.id = id;
    }
}
