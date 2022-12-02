package com.amiel.moviecenter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        Button signinButton = (Button) findViewById(R.id.login_sign_in_button);

        signinButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Click event works.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}