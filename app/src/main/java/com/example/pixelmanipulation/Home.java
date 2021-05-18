package com.example.pixelmanipulation;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {

    TextView title_home1,title_home2,title_home3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        title_home1 = findViewById(R.id.title_home1);
        title_home2 = findViewById(R.id.title_home2);
        title_home3 = findViewById(R.id.title_home3);

        title_home1.bringToFront();
        title_home2.bringToFront();
        title_home3.bringToFront();

        title_home1.animate()
                .alpha(1f)
                .setDuration(500)
                .setListener(null);

        title_home2.animate()
                .alpha(1f)
                .setDuration(800)
                .setListener(null);
        title_home3.animate()
                .alpha(1f)
                .setDuration(800)
                .setListener(null);

    }
}