package com.amiel.moviecenter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.amiel.moviecenter.DB.DBManager;
import com.amiel.moviecenter.DB.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new DBManager(this);
        dbManager.open();
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent i;
        if(FirebaseAuthHandler.getInstance().isUserLoggedIn()){
            if(dbManager.isUserExist(FirebaseAuthHandler.getInstance().getCurrentUserEmail())) {
                i = new Intent(SplashScreenActivity.this, MainActivity.class);
            } else {
                i = new Intent(SplashScreenActivity.this, LoginActivity.class);
            }
        } else {
            i = new Intent(SplashScreenActivity.this, LoginActivity.class);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                startActivity(i);
                finish();
            }
        }, 3000);
    }
}