package com.amiel.moviecenter;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentUtils.loadFragment(null, MainActivity.this, new MoviesListFragment(), R.id.activity_main_frame_layout, null);
    }
}