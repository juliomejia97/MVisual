package com.example.pixelmanipulation;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

public class LandingPage extends AppCompatActivity {

    private TextView text_landing1,text_landing2;
    private ImageView nurse,footbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        text_landing1 = findViewById(R.id.title_landing1);
        text_landing2 = findViewById(R.id.title_landing2);
        nurse= findViewById(R.id.enfermera);
        footbar= findViewById(R.id.footbar);

        text_landing1.setText(HtmlCompat.fromHtml(getString(R.string.test), HtmlCompat.FROM_HTML_MODE_LEGACY));
        text_landing1.animate()
                .alpha(1f)
                .setDuration(2000)
                .setListener(null);
        text_landing2.animate()
                .alpha(1f)
                .setDuration(4000)
                .setListener(null);

        ObjectAnimator animation = ObjectAnimator.ofFloat(nurse, "translationX", 550f);
        animation.setDuration(2500);
        animation.start();

        ObjectAnimator animation2 = ObjectAnimator.ofFloat(footbar, "translationY", -600f);
        animation2.setDuration(2500);
        animation2.start();

    }
}