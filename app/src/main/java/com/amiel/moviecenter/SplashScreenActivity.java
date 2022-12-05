package com.amiel.moviecenter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent i;
        if(FirebaseAuthHandler.getInstance().isUserLoggedIn()){
            Toast.makeText(this, "EXIST", Toast.LENGTH_SHORT).show();
            i = new Intent(SplashScreenActivity.this, MainActivity.class);
        } else {
            Toast.makeText(this, "EXIST", Toast.LENGTH_SHORT).show();
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